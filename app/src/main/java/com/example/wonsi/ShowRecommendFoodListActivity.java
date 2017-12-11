package com.example.wonsi;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wonsi.Helper.Constants;
import com.example.wonsi.ListViewAdapter.FoodListViewAdapter;
import com.example.wonsi.ListViewItemClass.Food;

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

/**
 * Created by wonsi on 2017-12-09.
 */

public class ShowRecommendFoodListActivity extends AppCompatActivity {
    String username;
    String petname;
    private TextView tv_petinfo;
    private ListView lv_foodlist;
    private TextView tv_username;
    private HttpURLConnection conn;
    private JSONArray jsonArray;
    private ArrayList<Food> foodArrayList = new ArrayList<>();
    private FoodListViewAdapter foodListViewAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showrecommendfoodlist);
        Intent intent = getIntent();
        username = intent.getStringExtra("userid");
        petname = intent.getStringExtra("petname");
        tv_username = (TextView) findViewById(R.id.FoodList_TV_username);
        tv_username.setText(username +"님의 " + petname + "의");
        lv_foodlist = (ListView) findViewById(R.id.FoodList_LV_foodlist);
        lv_foodlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String barcode = foodArrayList.get(i).getBarcode();
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowRecommendFoodListActivity.this);
                builder.setMessage("세부 정보 확인");
                DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(ShowRecommendFoodListActivity.this, ShowFoodDataActivity.class);
                        intent.putExtra("barcode", barcode);
                        startActivity(intent);
                    }
                };
                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                };
                builder.setPositiveButton("확인", okListener);
                builder.setNegativeButton("취소", cancelListener);
                builder.show();
            }
        });
        GetRecommendFood getRecommendFood = new GetRecommendFood();
        getRecommendFood.execute(username, petname);


    }
    private class GetRecommendFood extends AsyncTask<String, String, String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(ShowRecommendFoodListActivity.this, "추천사료를 받아오고 있습니다.", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(Constants.SERVER_GETSUITABLEFOOD_PHP);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("P_UserID", params[0])
                        .appendQueryParameter("PetName", params[1]);
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
                Log.d("Result : ", result);
                jsonArray = new JSONArray(result);
                setRecommendFoodListview();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }
    private void setRecommendFoodListview(){

        for(int i=0; i<jsonArray.length();i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                foodArrayList.add(new Food(jsonObject.getString("Barcode"),android.R.drawable.ic_dialog_alert,jsonObject.getString("FoodName"),"후기가 존재하지 않습니다."));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        foodListViewAdapter = new FoodListViewAdapter(foodArrayList);
        lv_foodlist.setAdapter(foodListViewAdapter);
        foodListViewAdapter.notifyDataSetChanged();

    }
}
