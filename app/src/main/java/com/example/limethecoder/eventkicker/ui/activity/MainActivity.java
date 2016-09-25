package com.example.limethecoder.eventkicker.ui.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;

import com.example.limethecoder.eventkicker.ui.adapter.MyFragmentPagerAdapter;
import com.example.limethecoder.eventkicker.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.viewpagerindicator.CirclePageIndicator;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final int LOGIN_REQUEST_CODE = 1;
    final int AUTH_REQUEST_CODE = 2;

    static final String TAG = "myLogs";

    int FACEBOOK_LOGIN_STATE = RESULT_CANCELED;

    ViewPager pager;
    PagerAdapter pagerAdapter;
    private CirclePageIndicator mCirclePageIndicator;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        Button loginButton = (Button)findViewById(R.id.loginButton);
        Button signupButton = (Button)findViewById(R.id.signupButton);

        LoginButton loginFbButton = (LoginButton)findViewById(R.id.logFbButton);

        signupButton.setTransformationMethod(null);
        loginButton.setTransformationMethod(null);

        loginFbButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("FB", "Userd id: " + loginResult.getAccessToken().getUserId() + "\nToken: " + loginResult.getAccessToken().getToken());
                FACEBOOK_LOGIN_STATE = RESULT_OK;
            }

            @Override
            public void onCancel() {
                Log.d("FB", "On cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FB", "On error");
            }
        });

        signupButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);

        buttonEffect(loginButton);
        buttonEffect(signupButton);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        //toolbar.setVisibility(View.GONE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.hide();

        pager = (ViewPager)findViewById(R.id.pager);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        mCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mCirclePageIndicator.setViewPager(pager);

        mCirclePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected, position = " + position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.loginButton:
                startActivityForResult(new Intent(this, LoginActivity.class), LOGIN_REQUEST_CODE);
                break;
            case R.id.signupButton:
                startActivityForResult(new Intent(this, AuthActivity.class), AUTH_REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    public static void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && (requestCode == AUTH_REQUEST_CODE || requestCode == LOGIN_REQUEST_CODE
                || FACEBOOK_LOGIN_STATE == RESULT_OK))
            startActivity(new Intent(this, EventListActivity.class));

        if(requestCode == AUTH_REQUEST_CODE && resultCode == RESULT_CANCELED)
            Toast.makeText(getBaseContext(), "Failed connection to server", Toast.LENGTH_LONG).show();

        if(requestCode == AUTH_REQUEST_CODE && resultCode == AuthActivity.RESULT_FAILED)
            Toast.makeText(getBaseContext(), "User already registered", Toast.LENGTH_LONG).show();
    }

}
