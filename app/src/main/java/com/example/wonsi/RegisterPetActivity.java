package com.example.wonsi;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * Created by wonsi on 2017-11-26.
 */

public class RegisterPetActivity extends AppCompatActivity implements View.OnClickListener {
    final static int REQUEST_PETTYPE = 1;
    int ageYearorMonth = 1;
    private ImageView iv_petImage;
    private ImageButton btn_addPetImage;
    private Button btn_petagebymonth;
    private Button btn_petagebyyear;
    private Button btn_registerPetData;
    private EditText et_petType;
    private EditText et_petName;
    private EditText et_petAge;
    private EditText et_petWeight;
    private Spinner spinner_petSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerpet);
        initializeLayout();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Pet_BTN_picture:
                Intent getPetTypeIntent = new Intent();
                startActivityForResult(getPetTypeIntent, REQUEST_PETTYPE);
                break;
            case R.id.Pet_BTN_agemonth:
                ageYearorMonth = 1;
                btn_petagebymonth.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_click)));
                btn_petagebyyear.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_back)));
                break;
            case R.id.Pet_BTN_ageyear:
                ageYearorMonth = 12;
                btn_petagebymonth.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_back)));
                btn_petagebyyear.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_click)));
                break;
            case R.id.Pet_BTN_registerpet:
                break;
            case R.id.Pet_ET_pettype:

                break;
        }
    }

    private void initializeLayout() {
        iv_petImage = (ImageView) findViewById(R.id.Pet_IV_petimage);
        btn_addPetImage = (ImageButton) findViewById(R.id.Pet_BTN_picture);
        btn_petagebymonth = (Button) findViewById(R.id.Pet_BTN_agemonth);
        btn_petagebyyear = (Button) findViewById(R.id.Pet_BTN_ageyear);
        btn_registerPetData = (Button) findViewById(R.id.Pet_BTN_registerpet);
        et_petType = (EditText) findViewById(R.id.Pet_ET_pettype);
        et_petName = (EditText) findViewById(R.id.Pet_ET_petname);
        et_petAge = (EditText) findViewById(R.id.Pet_ET_age);
        et_petWeight = (EditText) findViewById(R.id.Pet_ET_weight);
        spinner_petSex = (Spinner) findViewById(R.id.Pet_SPIN_sex);
    }
}

