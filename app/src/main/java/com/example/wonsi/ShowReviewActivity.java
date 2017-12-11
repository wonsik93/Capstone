package com.example.wonsi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by wonsi on 2017-12-11.
 */

public class ShowReviewActivity extends AppCompatActivity {
    private TextView tv_foodname;
    private ListView lv_foodreview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showreview);
    }
    private void initLayout(){
        tv_foodname = (TextView) findViewById(R.id.ShowReview_TV_foodname);
        lv_foodreview = (ListView) findViewById(R.id.ShowReview_LV_review);
    }
}
