package com.example.limethecoder.eventkicker.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.limethecoder.eventkicker.R;
import com.example.limethecoder.eventkicker.ui.adapter.ViewPagerAdapter;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventListActivity extends AppCompatActivity {

    @BindView(R.id.materialViewPager)
    MaterialViewPager mViewPager;
    public final int EVENT_REQUEST = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        ButterKnife.bind(this);

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "https://fs01.androidpit.info/a/63/0e/android-l-wallpapers-630ea6-h900.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }

                return null;
            }
        });

        CharSequence titles[] = new CharSequence[4];
        titles[0] = "Sport";
        titles[1] = "Music";
        titles[2] = "Cinema";
        titles[3] = "Technology";
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, 4);

        ViewPager viewPager = mViewPager.getViewPager();
        viewPager.setAdapter(adapter);
        mViewPager.getPagerTitleStrip().setViewPager(this.mViewPager.getViewPager());

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

        Drawable myFabSrc = ContextCompat.getDrawable(this, android.R.drawable.ic_input_add);
        Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
        willBeWhite.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(willBeWhite);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getBaseContext(), EventCreatorActivity.class), EVENT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EVENT_REQUEST && resultCode == RESULT_OK)
            Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_LONG).show();

        if(requestCode == EVENT_REQUEST && resultCode == RESULT_CANCELED)
            Toast.makeText(getBaseContext(), "Failed connection to server", Toast.LENGTH_LONG).show();

        if(requestCode == EVENT_REQUEST && resultCode == AuthActivity.RESULT_FAILED)
            Toast.makeText(getBaseContext(), "User already registered", Toast.LENGTH_LONG).show();
    }

}
