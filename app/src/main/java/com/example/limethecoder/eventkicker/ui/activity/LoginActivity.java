package com.example.limethecoder.eventkicker.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.limethecoder.eventkicker.R;
import com.example.limethecoder.eventkicker.net.ServiceManager;
import com.example.limethecoder.eventkicker.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.email)
    AutoCompleteTextView email;
    @BindView(R.id.password)
    EditText password;

    private User user;

    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        mHandler = new Handler();
    }

    public boolean checkData() {
        boolean is_correct = true;

        String emailData = email.getText().toString();
        String passwordData = password.getText().toString();

        if (emailData.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailData).matches()) {
            email.setError("Invalid email adress");
            is_correct = false;
        } else {
            email.setError(null);
        }

        if (passwordData.isEmpty() || passwordData.length() < 6) {
            password.setError("Password must have at least 6 characters");
            is_correct = false;
        } else {
            password.setError(null);
        }

        return is_correct;
    }

    @OnClick(R.id.email_sign_in_button)
    public void signIn(View v) {
        if (!checkData()) {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String emailData = email.getText().toString();
        final String passwordData = password.getText().toString();


      ServiceManager.MyApiEndpointInterface apiService = ServiceManager.newService(emailData, passwordData);
      Call<User> call = apiService.login();

      call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(retrofit.Response<User> response, Retrofit retrofit) {

                if(response.code() == 200) {
                    if (response.body() != null) {
                        user = response.body();
                      PreferenceManager.getDefaultSharedPreferences
                          (LoginActivity.this).edit().putInt("userId", user
                          .id).putString("username", user.username).putString
                          ("userPassword", passwordData).commit();
                        Toast.makeText(getBaseContext(), "Welcome " + user.name, Toast.LENGTH_LONG).show();
                        setResult(RESULT_OK);
                    } else {
                        Toast.makeText(getBaseContext(), "Something wrong", Toast.LENGTH_LONG).show();
                        setResult(RESULT_CANCELED);
                    }
                }
                else {
                    Toast.makeText(getBaseContext(), "Authentication failed", Toast.LENGTH_LONG).show();
                    setResult(RESULT_CANCELED);
                }
              progressDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
                Toast.makeText(getBaseContext(), "Authentication failed", Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED);
            }
        });


        mHandler.postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        finish();
                    }
                }, 3000);
    }
}