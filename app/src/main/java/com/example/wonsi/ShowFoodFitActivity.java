package com.example.wonsi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wonsi.ListViewAdapter.FitListViewAdapter;
import com.example.wonsi.ListViewItemClass.Fit;

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

/**
 * Created by wonsi on 2017-11-26.
 */

public class ShowFoodFitActivity extends AppCompatActivity implements View.OnClickListener {
    String check1 = "사료 성분 확인 이상없습니다.";
    String check2 = "사용 용도 확인 이상없습니다.";
    String check3 = "사용 조건 확인 이상없습니다.";
    String check4 = "사용 나이 확인 이상없습니다.";
    String check5 = "애견 크기 확인 이상없습니다.";
    String check6 = "치아 관리용 확인 이상없습니다.";
    String check7 = "임신 전용 확인 이상없습니다.";
    String check8 = "체중 관리용 확인 이상없습니다.";
    String check9 = "변냄새 관리용 확인 이상없습니다.";
    int fit1 = 1;
    int fit2 = 1;
    int fit3 = 1;
    int fit4 = 1;
    int fit5 = 1;
    int fit6 = 1;
    int fit7 = 1;
    int fit8 = 1;
    int fit9 = 1;
    private String barcode;
    private String username;
    private String petname;
    private JSONObject jsonObject;
    private Button btn_fooddetail;
    private TextView tv_foodname;
    private ListView lv_fit;
    private HttpURLConnection conn;
    private ArrayList<Fit> fitArrayList = new ArrayList<>();
    private FitListViewAdapter fitListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodfitable);
        initializeLayout();
        barcode = getIntent().getStringExtra("barcode");
        username = getIntent().getStringExtra("userid");
        petname = getIntent().getStringExtra("petname");
        CheckFoodFit checkFoodFit = new CheckFoodFit();
        checkFoodFit.execute(username, petname, barcode);
    }

    private void initializeLayout() {
        lv_fit = (ListView) findViewById(R.id.Fitable_LV_fit);
        tv_foodname = (TextView) findViewById(R.id.Fitable_TV_foodname);
        btn_fooddetail = (Button) findViewById(R.id.Fitable_BTN_more);
        btn_fooddetail.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Fitable_BTN_more:
                //IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                Intent intent = new Intent(ShowFoodFitActivity.this, ShowFoodDataActivity.class);
                intent.putExtra("barcode", barcode);
                startActivity(intent);
        }
    }

    private void checkFit() {
        try {
            tv_foodname.setText(jsonObject.getString("foodName"));
            if (!(jsonObject.getBoolean("check1"))) {

                check1 = "사료 성분 확인 이상 발생!";
                fit1 = 0;
            }

            if (!(jsonObject.getBoolean("check2"))) {
                check2 = "사용 용도 확인 이상 발생!";
                fit2 = 0;
            }
            if (!(jsonObject.getBoolean("check3"))) {
                check3 = "사용 조건 확인 이상 발생!";
                fit3 = 0;
            }
            if (!(jsonObject.getBoolean("check4"))) {
                check4 = "사용 나이 확인 이상 발생!";
                fit4 = 0;
            }
            if (!(jsonObject.getBoolean("check5"))) {
                check5 = "애견 크기 확인 이상 발생!";
                fit5 = 0;
            }
            if (!(jsonObject.getBoolean("check6"))) {
                check6 = "치아 관리용 확인 이상 발생!";
                fit6 = 0;
            }
            if (!(jsonObject.getBoolean("check7"))) {
                check7 = "임신 전용 확인 이상 발생!";
                fit7 = 0;
            }
            if (!(jsonObject.getBoolean("check8"))) {
                check8 = "체중 관리용 확인 이상 발생!";
                fit8 = 0;
            }
            if (!(jsonObject.getBoolean("check9"))) {
                check9 = "변냄새 관리용 확인 이상 발생!";
                fit9 = 0;
            }
            fitArrayList.add(new Fit(fit1, check1));
            fitArrayList.add(new Fit(fit2, check2));
            fitArrayList.add(new Fit(fit3, check3));
            fitArrayList.add(new Fit(fit4, check4));
            fitArrayList.add(new Fit(fit5, check5));
            fitArrayList.add(new Fit(fit6, check6));
            fitArrayList.add(new Fit(fit7, check7));
            fitArrayList.add(new Fit(fit8, check8));
            fitArrayList.add(new Fit(fit9, check9));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        fitListViewAdapter = new FitListViewAdapter(fitArrayList);
        lv_fit.setAdapter(fitListViewAdapter);
    }

    private class CheckFoodFit extends AsyncTask<String, String, String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(ShowFoodFitActivity.this, "정보를 받아오는 중입니다.", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL("http://18.216.142.72/isItSuitableForPet.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("P_UserID", params[0])
                        .appendQueryParameter("PetName", params[1])
                        .appendQueryParameter("Barcode", params[2]);
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
                    Log.d("JSON Result", result.toString());
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
            Log.d("Result : ", result);
            try {
                jsonObject = new JSONObject(result);
                //jsonArray = new JSONArray(result);
                checkFit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}

