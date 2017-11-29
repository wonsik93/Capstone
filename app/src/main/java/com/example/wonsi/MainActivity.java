package com.example.wonsi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_barcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_barcode = (Button) findViewById(R.id.Main_BTN_readbarcode);
        btn_barcode.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            Intent intent = new Intent(MainActivity.this, ShowFoodDataActivity.class);
            intent.putExtra("barcode", result.getContents());
            startActivity(intent);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Main_BTN_registerpet:

                break;
            case R.id.Main_BTN_readbarcode:
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setCaptureActivity(ReadBarcodebyZxingActivity.class);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.initiateScan();
                break;
            case R.id.Main_BTN_recommendfood:
                break;
            case R.id.Main_BTN_community:
                break;

        }

    }
}
