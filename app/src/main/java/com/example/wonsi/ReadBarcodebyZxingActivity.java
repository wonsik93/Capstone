package com.example.wonsi;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.journeyapps.barcodescanner.CaptureActivity;

/**
 * Created by wonsi on 2017-11-26.
 */

public class ReadBarcodebyZxingActivity extends CaptureActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView title = new TextView(this);
        title.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
        title.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        title.setPadding(150, 100, 100, 100);
        title.setTextColor(Color.parseColor("#FF7200"));
        title.setTextSize(30);
        title.setText("바코드를 입력해주세요.");

        this.addContentView(title, layoutParams);
    }

}
