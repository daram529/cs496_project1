package com.example.q.imageslider;

/**
 * Created by q on 2016-12-28.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class fullScreenActivity extends Activity{

    private Utils utils;
    private fullScreenAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);

        viewPager = (ViewPager) findViewById(R.id.pager);
        utils = new Utils(getApplicationContext());
        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        adapter = new fullScreenAdapter(fullScreenActivity.this,
                utils.getFilePaths());
        viewPager.setAdapter(adapter);
        // displaying selected image first
        viewPager.setCurrentItem(position);
    }
}
