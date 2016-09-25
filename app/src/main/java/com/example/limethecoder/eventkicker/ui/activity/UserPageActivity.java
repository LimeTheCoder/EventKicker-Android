package com.example.limethecoder.eventkicker.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.limethecoder.eventkicker.model.EventItem;
import com.example.limethecoder.eventkicker.R;
import com.example.limethecoder.eventkicker.net.ServiceManager;
import com.example.limethecoder.eventkicker.ui.adapter.SmallCardAdapter;
import com.example.limethecoder.eventkicker.model.User;
import com.example.limethecoder.eventkicker.net.ApiResponse;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

import java.util.ArrayList;

public class UserPageActivity extends AppCompatActivity {

    RecyclerView userEventsView;
    RecyclerView userContributedEventsView;
    RecyclerView.LayoutManager eventsLayoutManager;
    int userId;
    User user;
    TextView mail;
    TextView mName;
    RecyclerViewMaterialAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        MaterialViewPager mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        Intent intent = getIntent();
        userId = intent.getIntExtra("id", 1);
        mail = (TextView)findViewById(R.id.mail_profile);
        mName = (TextView) findViewById(R.id.user_name);

        loadUser();

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "https://fs01.androidpit.info/a/63/0e/android-l-wallpapers-630ea6-h900.jpg");
                }

                return null;
            }
        });

        userEventsView = (RecyclerView) findViewById(R.id.myEvents);
        userContributedEventsView = (RecyclerView) findViewById(R.id.contributedEvents);

      eventsLayoutManager = new LinearLayoutManager(this);
      userEventsView.setLayoutManager(eventsLayoutManager);
      ((LinearLayoutManager)eventsLayoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);

        eventsLayoutManager = new LinearLayoutManager(this);
        userContributedEventsView.setLayoutManager(eventsLayoutManager);
        ((LinearLayoutManager)eventsLayoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);

        initializeEventList();
        initializeContributedList();

        Toolbar toolbar = mViewPager.getToolbar();

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(false);
        }

    }

    public void loadUser() {
        ServiceManager.MyApiEndpointInterface apiService = ServiceManager.newService();
        Call<ApiResponse<User>> call = apiService.getUser(userId);

        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(retrofit.Response<ApiResponse<User>> response, Retrofit retrofit) {

                if (response.body().success) {
                    user = response.body().single;
                    mail.setText(user.username);
                    mName.setText(user.name);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
                Log.e("UserData", t.getMessage());
            }
        });
    }


    // Initialize contributed list. Change request from getUserEvents to get contributed event
    public void initializeContributedList() {
        ServiceManager.MyApiEndpointInterface apiService = ServiceManager.newService();
        Call<ApiResponse<EventItem>> call = apiService.getUserEvents(userId);

        call.enqueue(new Callback<ApiResponse<EventItem>>() {
            @Override
            public void onResponse(retrofit.Response<ApiResponse<EventItem>> response, Retrofit retrofit) {

                if (response.body().success) {
                    Toast.makeText(getBaseContext(), "List loaded", Toast.LENGTH_LONG).show();

                    final ArrayList<EventItem> mItems = response.body().multiple;

                    SmallCardAdapter.ViewHolder.ItemClickListener itemClickListener = new
                            SmallCardAdapter.ViewHolder.ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            Intent intent = new Intent(getBaseContext(), EventPageActivity.class);
                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            EventItem event = mItems.get(position - 1);
                            event.setAuthorName(user.name);
                            if(event.getPictureUrl() == null)
                                event.setPictureUrl( "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                            String converted = gson.toJson(event);
                            intent.putExtra("json", converted);
                            startActivity(intent);
                        }
                    };

                    adapter = new RecyclerViewMaterialAdapter(new SmallCardAdapter(mItems, itemClickListener, getBaseContext()));
                    userContributedEventsView.setAdapter(adapter);
                } else
                    Toast.makeText(getBaseContext(), "Error to load list", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
                Log.e("UserPageContEv", t.getMessage());
            }
        });
    }

    public void initializeEventList() {
        ServiceManager.MyApiEndpointInterface apiService = ServiceManager.newService();
        Call<ApiResponse<EventItem>> call = apiService.getUserEvents(userId);

        call.enqueue(new Callback<ApiResponse<EventItem>>() {
            @Override
            public void onResponse(retrofit.Response<ApiResponse<EventItem>> response, Retrofit retrofit) {

                if (response.body().success) {
                    Toast.makeText(getBaseContext(), "List loaded", Toast.LENGTH_LONG).show();

                    final ArrayList<EventItem> items = response.body().multiple;

                    SmallCardAdapter.ViewHolder.ItemClickListener itemClickListener = new
                            SmallCardAdapter.ViewHolder.ItemClickListener() {
                                @Override
                                public void onClick(View view, int position, boolean isLongClick) {
                                    Intent intent = new Intent(getBaseContext(), EventPageActivity.class);
                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    EventItem event = items.get(position - 1);
                                    event.setAuthorName(user.name);
                                    if(event.getPictureUrl() == null)
                                        event.setPictureUrl( "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                                    String converted = gson.toJson(event);
                                    intent.putExtra("json", converted);
                                    startActivity(intent);
                                }
                            };

                    adapter = new RecyclerViewMaterialAdapter(new SmallCardAdapter(items, itemClickListener, getBaseContext()));
                    userEventsView.setAdapter(adapter);
                } else
                    Toast.makeText(getBaseContext(), "Error to load list", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
                Log.e("UserPageEventList", t.getMessage());
            }
        });
    }

}
