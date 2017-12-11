package com.example.wonsi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.example.wonsi.Helper.CircularNetworkImageView;
import com.example.wonsi.Helper.Constants;
import com.example.wonsi.Helper.CustomVolleyRequest;
import com.example.wonsi.ListViewItemClass.PetData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static int REQUEST_SHOWFOOD = 0;
    private final static int REQUEST_SHOWFOODFIT = 1;
    private String username;
    private String petname;
    private TextView tv_hellouser;
    private Button btn_controlpet;
    private Button btn_recommendfood;
    private Button btn_community;
    private Button btn_barcode;
    private LinearLayout ll_mypet;
    private ImageLoader imageLoader;
    private HttpURLConnection conn;
    private JSONArray jsonArray;
    private ArrayList<PetData> petdataList = new ArrayList<>();
    private ArrayList<String> petnameList = new ArrayList<>();
    private ArrayAdapter<String> adspin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adspin = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, petnameList);

        // 이전 Activity에서 사용자의 id 정보를 가져온다.
        Intent intent = getIntent();
        username = intent.getStringExtra("userid");

        // Layout을 초기화한다.
        initLayout();
        tv_hellouser.setText("안녕하세요 " + username + "님!");

    }

    private void initLayout() {
        ll_mypet = (LinearLayout) findViewById(R.id.Main_ScrollView_mypet);
        tv_hellouser = (TextView) findViewById(R.id.Main_TV_hellouser);
        btn_controlpet = (Button) findViewById(R.id.Main_BTN_registerpet);
        btn_barcode = (Button) findViewById(R.id.Main_BTN_readbarcode);
        btn_community = (Button) findViewById(R.id.Main_BTN_community);
        btn_recommendfood = (Button) findViewById(R.id.Main_BTN_recommendfood);
        btn_controlpet.setOnClickListener(this);
        btn_barcode.setOnClickListener(this);
        btn_community.setOnClickListener(this);
        btn_recommendfood.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SHOWFOOD) {
            if (resultCode == Activity.RESULT_OK) {
                String barcode = data.getStringExtra("SCAN_RESULT");
                //IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                Intent intent = new Intent(MainActivity.this, ShowFoodDataActivity.class);
                intent.putExtra("barcode", barcode);
                startActivity(intent);
            }
        } else if (requestCode == REQUEST_SHOWFOODFIT) {
            if (resultCode == Activity.RESULT_OK) {
                String barcode = data.getStringExtra("SCAN_RESULT");
                //IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                Intent intent = new Intent(MainActivity.this, ShowFoodFitActivity.class);
                intent.putExtra("userid", username);
                intent.putExtra("petname", petname);
                intent.putExtra("barcode", barcode);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Main_BTN_registerpet:
                final LayoutInflater inflater2 = getLayoutInflater();
                final DialogInterface PopupSpiciesDialog = null;
                final View selectSpiciesView = inflater2.inflate(R.layout.dialog_dogorpet, null);
                final AlertDialog.Builder selectSpiciesBuilder = new AlertDialog.Builder(MainActivity.this);
                final Intent registerPetIntent = new Intent(MainActivity.this, RegisterPetActivity.class);
                selectSpiciesBuilder.setView(selectSpiciesView);
                Button btn_dog = (Button) selectSpiciesView.findViewById(R.id.DogorCat_BTN_dog);
                Button btn_cat = (Button) selectSpiciesView.findViewById(R.id.DogorCat_BTN_cat);
                final AlertDialog selectSpiciesDialog = selectSpiciesBuilder.create();
                btn_dog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        registerPetIntent.putExtra("Species", "0");
                        registerPetIntent.putExtra("username",username);
                        startActivity(registerPetIntent);
                        selectSpiciesDialog.dismiss();
                    }
                });
                btn_cat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        registerPetIntent.putExtra("Species", "1");
                        registerPetIntent.putExtra("username",username);
                        startActivity(registerPetIntent);
                        selectSpiciesDialog.dismiss();
                    }
                });
                selectSpiciesDialog.setCanceledOnTouchOutside(true);
                selectSpiciesDialog.show();
                break;
            case R.id.Main_BTN_readbarcode:
                Intent readBarcodeIntent = new Intent("com.google.zxing.client.android.SCAN");
                readBarcodeIntent.putExtra("SCAN_MODE", "ALL");
                startActivityForResult(readBarcodeIntent, REQUEST_SHOWFOOD);
                break;
            case R.id.Main_BTN_recommendfood:
                final LayoutInflater inflater = getLayoutInflater();
                final View recommendFoodView = inflater.inflate(R.layout.dialog_recommend, null);
                final AlertDialog.Builder recommendFoodBuilder = new AlertDialog.Builder(MainActivity.this);
                recommendFoodBuilder.setView(recommendFoodView);
                Spinner spin_petname = (Spinner) recommendFoodView.findViewById(R.id.Recommend_Spin_petname);
                Button btn_fit = (Button) recommendFoodView.findViewById(R.id.Recommend_BTN_fit);
                Button btn_food = (Button) recommendFoodView.findViewById(R.id.Recommend_BTN_food);
                final AlertDialog recommendFoodDialog = recommendFoodBuilder.create();

                spin_petname.setAdapter(adspin);
                spin_petname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        petname = petdataList.get(i).getPetName();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                btn_fit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent readBarcodeForRecomIntent = new Intent("com.google.zxing.client.android.SCAN");
                        readBarcodeForRecomIntent.putExtra("SCAN_MODE", "ALL");
                        startActivityForResult(readBarcodeForRecomIntent, REQUEST_SHOWFOODFIT);
                        recommendFoodDialog.dismiss();
                    }
                });
                btn_food.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent showRecommendFoodListIntent = new Intent(MainActivity.this, ShowRecommendFoodListActivity.class);
                        showRecommendFoodListIntent.putExtra("userid", username);
                        showRecommendFoodListIntent.putExtra("petname", petname);
                        startActivity(showRecommendFoodListIntent);
                        recommendFoodDialog.dismiss();
                    }
                });
                recommendFoodDialog.setCanceledOnTouchOutside(true);
                recommendFoodDialog.show();
                break;
            case R.id.Main_BTN_community:
                Toast.makeText(MainActivity.this, "미구현 기능입니다!", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    private void loadImage() {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                final JSONObject jsonObject = jsonArray.getJSONObject(i);
                petdataList.add(new PetData(jsonObject.getString("PetName")
                        , jsonObject.getString("Breed")
                        , jsonObject.getString("PetAge")
                        , jsonObject.getString("Weight")
                        , jsonObject.getString("Species")
                        , jsonObject.getString("P_TypeID")
                        , jsonObject.getString("TypeName")
                        , jsonObject.getString("Pregnant")
                        , jsonObject.getString("StoolSmell")
                        , jsonObject.getString("Teeth")
                        , jsonObject.getString("Diet")
                        , jsonObject.getString("image")));
                adspin.add(jsonObject.getString("PetName"));
                adspin.notifyDataSetChanged();
                String url = jsonObject.getString("image");
                CircularNetworkImageView circularNetworkImageView = new CircularNetworkImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(400, 400);
                params.setMargins(0, 20, 30, 0);
                circularNetworkImageView.setLayoutParams(params);

                if (url.equals("")) {
                    return;
                }
                imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext()).getImageLoader();
                imageLoader.get(url, ImageLoader.getImageListener(circularNetworkImageView, R.drawable.intro, android.R.drawable.ic_dialog_alert));
                //networkImageView.setImageUrl(url,imageLoader);
                circularNetworkImageView.setImageUrl(url, imageLoader);
                circularNetworkImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {

                            Intent showPetDataIntent = new Intent(MainActivity.this, ShowPetDataActivity.class);
                            showPetDataIntent.putExtra("PetName", jsonObject.getString("PetName"));
                            showPetDataIntent.putExtra("Breed", jsonObject.getString("Breed"));
                            showPetDataIntent.putExtra("PetAge", jsonObject.getString("PetAge"));
                            showPetDataIntent.putExtra("Weight", jsonObject.getString("Weight"));
                            showPetDataIntent.putExtra("Species", jsonObject.getString("Species"));
                            showPetDataIntent.putExtra("P_TypeID", jsonObject.getString("P_TypeID"));
                            showPetDataIntent.putExtra("TypeName", jsonObject.getString("TypeName"));
                            showPetDataIntent.putExtra("Pregnant", jsonObject.getString("Pregnant"));
                            showPetDataIntent.putExtra("StoolSmell", jsonObject.getString("StoolSmell"));
                            showPetDataIntent.putExtra("Teeth", jsonObject.getString("Teeth"));
                            showPetDataIntent.putExtra("Diet", jsonObject.getString("Diet"));
                            showPetDataIntent.putExtra("image", jsonObject.getString("image"));
                            startActivity(showPetDataIntent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                ll_mypet.addView(circularNetworkImageView);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adspin.clear();
        ll_mypet.removeAllViews();
        GetMyPet getMyPet = new GetMyPet();
        getMyPet.execute(username);
    }

    private class GetMyPet extends AsyncTask<String, String, String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(MainActivity.this, "사용자 정보를 받아오는 중입니다.", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(Constants.SERVER_GETUSERPET_PHP);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

                int responce_code = conn.getResponseCode();
                if (responce_code == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return (result.toString());
                } else {
                    return ("???");
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
            }
            return "false";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loading.dismiss();
            try {
                jsonArray = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loadImage();
        }
    }


}


