package com.example.wonsi;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.wonsi.Helper.CustomVolleyRequest;

/**
 * Created by wonsi on 2017-12-03.
 */

public class ShowPetDataActivity extends AppCompatActivity implements View.OnClickListener {
    int ageYearorMonth = 1;
    int maleOrFemale = 0;
    private ArrayAdapter<CharSequence> adspin;
    private NetworkImageView iv_petImage;
    private ImageButton btn_addPetImage;
    private Button btn_petagebymonth;
    private Button btn_petagebyyear;
    private Button btn_registerPetData;
    private EditText et_petType;
    private EditText et_petName;
    private EditText et_petAge;
    private EditText et_petWeight;
    private Spinner spinner_breed;
    private ImageLoader imageLoader;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpetdata);

        initLayout();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initLayout() {
        iv_petImage = (NetworkImageView) findViewById(R.id.Pet_IV_petimage);
        btn_addPetImage = (ImageButton) findViewById(R.id.Pet_BTN_picture);
        btn_petagebymonth = (Button) findViewById(R.id.Pet_BTN_agemonth);
        btn_petagebyyear = (Button) findViewById(R.id.Pet_BTN_ageyear);
        btn_registerPetData = (Button) findViewById(R.id.Pet_BTN_registerpet);
        btn_addPetImage.setOnClickListener(this);
        btn_petagebymonth.setOnClickListener(this);
        btn_petagebyyear.setOnClickListener(this);
        btn_registerPetData.setOnClickListener(this);
        et_petType = (EditText) findViewById(R.id.Pet_ET_pettype);
        et_petType.setOnClickListener(this);
        et_petName = (EditText) findViewById(R.id.Pet_ET_petname);
        et_petAge = (EditText) findViewById(R.id.Pet_ET_age);
        et_petWeight = (EditText) findViewById(R.id.Pet_ET_weight);
        spinner_breed = (Spinner) findViewById(R.id.Pet_SPIN_sex);
        adspin = ArrayAdapter.createFromResource(this, R.array.breed, android.R.layout.simple_spinner_item);
        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_breed.setAdapter(adspin);
        spinner_breed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                maleOrFemale = (int) adspin.getItemId(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Intent intent = getIntent();
        et_petName.setText(intent.getStringExtra("PetName"));
        if (Integer.valueOf(intent.getStringExtra("PetAge")) > 12) {
            int year = Integer.valueOf(intent.getStringExtra("PetAge")) / 12;
            et_petAge.setText(String.valueOf(year));
            btn_petagebymonth.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_white)));
            btn_petagebymonth.setTextColor(getResources().getColor(R.color.color_button_back));
            btn_petagebyyear.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_button_back)));
            btn_petagebyyear.setTextColor(getResources().getColor(R.color.color_white));
        } else {
            et_petAge.setText(intent.getStringExtra("PetAge"));
        }

        et_petWeight.setText(intent.getStringExtra("Weight"));
        et_petType.setText(intent.getStringExtra("TypeName"));
        imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext()).getImageLoader();
        imageLoader.get(intent.getStringExtra("image"), ImageLoader.getImageListener(iv_petImage, R.drawable.intro, android.R.drawable.ic_dialog_alert));
        iv_petImage.setImageUrl(intent.getStringExtra("image"), imageLoader);
        spinner_breed.setSelection(Integer.valueOf(intent.getStringExtra("Species")) - 1);
    }

    @Override
    public void onClick(View view) {

    }
}
