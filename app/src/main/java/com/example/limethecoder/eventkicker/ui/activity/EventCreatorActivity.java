package com.example.limethecoder.eventkicker.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.limethecoder.eventkicker.model.EventItem;
import com.example.limethecoder.eventkicker.R;
import com.example.limethecoder.eventkicker.net.ServiceManager;
import com.example.limethecoder.eventkicker.net.ApiResponse;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

import java.util.Calendar;

public class EventCreatorActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {


    @BindView(R.id.time_textview)
    TextView timeTextView;
    @BindView(R.id.date_textview)
    TextView dateTextView;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.nameEvent)
    EditText eventName;

    String enteredDate;
    String enteredTime;
    String requestDateStr;

    public static final int RESULT_FAILED = 77;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creator);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.time_button)
    public void onTimeSubmit(View v) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                EventCreatorActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );

        tpd.enableSeconds(true);
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("TimePicker", "Dialog was cancelled");
            }
        });
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    @OnClick(R.id.date_button)
    public void onDateSubmit(View v) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                EventCreatorActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.showYearPickerFirst(true);
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onResume() {
        super.onResume();
        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");

        if(tpd != null) tpd.setOnTimeSetListener(this);
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = ""+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        enteredDate = "" + year + monthOfYear + dayOfMonth;
        dateTextView.setText(date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        String time = ""+hourString+" h "+minuteString+"m "+secondString+"s";
        enteredTime = hourString + minuteString + secondString;
        timeTextView.setText(time);
    }

    @OnClick(R.id.event_creator_submit_button)
    public void submitForm(View v) {
        final ProgressDialog progressDialog = new ProgressDialog(EventCreatorActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Submiting...");
        progressDialog.show();
        String name = eventName.getText().toString();
        String desc = description.getText().toString();
        String pol = "+0200";

        requestDateStr = enteredDate + "T" + enteredTime + pol;

        Calendar c = Calendar.getInstance();
        int second = c.get(Calendar.SECOND);
        int min = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR_OF_DAY);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        String hourString = hour < 10 ? "0"+hour : ""+hour;
        String minuteString = min < 10 ? "0"+min : ""+min;
        String secondString = second < 10 ? "0"+second : ""+second;

        String currTime = hourString + minuteString + secondString;
        String currDate = "" +year +(++month) +day;

        String finalCurrDate = currDate + "T" + currTime + pol;

        EventItem event = new EventItem();
        event.setAuthorId(5);
        event.setTimeScheduled(finalCurrDate);
        event.setName(name);
        event.setDescription(desc);

        ServiceManager.MyApiEndpointInterface apiService = ServiceManager.newService();
        Call<ApiResponse<EventItem>> call = apiService.createEvent(event);
        call.enqueue(new Callback<ApiResponse<EventItem>>() {
            @Override
            public void onResponse(retrofit.Response<ApiResponse<EventItem>> response, Retrofit retrofit) {

                if(response.body().success)
                    setResult(RESULT_OK);
                else
                    setResult(RESULT_FAILED);
            }

            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
                Log.e("PostEvent", t.getMessage());
                setResult(RESULT_CANCELED);
            }
        });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        finish();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }
}
