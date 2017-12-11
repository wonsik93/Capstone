package com.example.wonsi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;

import com.example.wonsi.ListViewAdapter.FoodContainListViewAdapter;
import com.example.wonsi.ListViewItemClass.FoodContain;
import com.github.mikephil.charting.charts.HorizontalBarChart;

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
import java.util.List;

/**
 * Created by wonsi on 2017-12-03.
 */

public class ShowFoodContainActivity extends AppCompatActivity {
    private Context mContext;
    private String barcode;
    private HttpURLConnection conn;
    private JSONArray jsonArray;
    private HorizontalBarChart containHorizontalBarChart;
    private ListView listview_foodcontain;
    private TextView tv_foodname;
    private FoodContainListViewAdapter listViewAdapter;
    private List<FoodContain> foodContainList;

    float protein;
    float fat;
    float ash;
    float fiber;
    float moisture;
    float calcium;
    float phosphorus;
    float omega3;
    float omega6;
    float vitamina;
    float vitamind;
    float vitamine;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        barcode = intent.getStringExtra("barcode");
        protein = intent.getFloatExtra("protein",0);
        fat = intent.getFloatExtra("fat",0);
        ash = intent.getFloatExtra("ash",0);
        fiber=intent.getFloatExtra("fiber",0);
        moisture=intent.getFloatExtra("moisture",0);
        calcium = intent.getFloatExtra("calcium",0);
        phosphorus = intent.getFloatExtra("phosphorus",0);
        omega3 = intent.getFloatExtra("omega3",0);
        omega6 = intent.getFloatExtra("omega6",0);
        vitamina = intent.getFloatExtra("vitamina",0);
        vitamind = intent.getFloatExtra("vitamind",0);
        vitamine = intent.getFloatExtra("vitamine",0);
        mContext = this.getBaseContext();

        // 나중에는 Barcode 값을 변경해야함

        setContentView(R.layout.activity_showfoodcontain);
        GetFoodContainData getFoodContainData = new GetFoodContainData();
        getFoodContainData.execute(barcode);

        foodContainList = new ArrayList<>();


        listview_foodcontain = (ListView) findViewById(R.id.Contain_ListView_contains);
        listview_foodcontain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_foodcontain, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(ShowFoodContainActivity.this);
                builder.setTitle("원재료 설명");
                if(foodContainList.get(i).getGrade() == 2) builder.setIcon(R.drawable.containgood);
                if(foodContainList.get(i).getGrade() == 1) builder.setIcon(R.drawable.containnormal);
                if(foodContainList.get(i).getGrade() == 0) builder.setIcon(R.drawable.containbad);
                builder.setView(dialogView);
                TextView tv_note = (TextView) dialogView.findViewById(R.id.Dialog_TV_note);
                TextView tv_information = (TextView) dialogView.findViewById(R.id.Dialog_TV_information);
                tv_note.setText(foodContainList.get(i).getNote());
                tv_information.setText(foodContainList.get(i).getInformation());
                builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

    }
    private class GetFoodContainData extends AsyncTask<String, String, String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(ShowFoodContainActivity.this, "정보를 받아오는 중입니다.", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL("http://18.216.142.72/Android/getFoodContain.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("barcode", params[0]);
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
            try {
                jsonArray = new JSONArray(result);
                setContainListview();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private void setContainListview(){

        for(int i=0; i<jsonArray.length();i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                foodContainList.add(new FoodContain(jsonObject.getInt("IgOrder")
                        ,jsonObject.getInt("C_IngrdtID")
                        ,jsonObject.getString("Note")
                        ,jsonObject.getString("Information")
                        ,jsonObject.getInt("Grade")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        listViewAdapter = new FoodContainListViewAdapter(foodContainList);
        listview_foodcontain.setAdapter(listViewAdapter);

    }
}
