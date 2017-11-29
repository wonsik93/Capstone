package com.example.wonsi;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

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

public class ShowFoodDataActivity extends AppCompatActivity {
    private String barcode;
    private String jsonresult;
    private ArrayList<Object> jsonArray = new ArrayList<>();
    private HttpURLConnection conn;
    private PieChart foodAnalysisChart;
    private PieData foodAnalysisData;
    private PieDataSet foodAnalysisDataSet;
    private boolean isPregnant = false;
    private boolean isSmell = false;
    private boolean isTeeth = false;
    private boolean isDiet = false;

    private TextView tv_foodname;
    private TextView tv_origin;
    private TextView tv_maker;
    private TextView tv_foodweight;
    private TextView tv_prices;
    private TextView tv_whom;
    private TextView tv_agefrom;
    private TextView tv_ageto;
    private TextView tv_breedfrom;
    private TextView tv_breedto;
    private TextView tv_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showfooddata);

        barcode = getIntent().getStringExtra("barcode");
    }

    private void initializeLayout() {
        tv_foodname = (TextView) findViewById(R.id.Barcode_TV_foodname);
        tv_origin = (TextView) findViewById(R.id.Barcode_TV_origin);
        tv_maker = (TextView) findViewById(R.id.Barcode_TV_maker);
        tv_foodweight = (TextView) findViewById(R.id.Barcode_TV_foodweight);
        tv_prices = (TextView) findViewById(R.id.Barcode_TV_price);
        tv_whom = (TextView) findViewById(R.id.Barcode_TV_whom);
        tv_agefrom = (TextView) findViewById(R.id.Barcode_TV_agefrom);
        tv_ageto = (TextView) findViewById(R.id.Barcode_TV_ageto);
        tv_breedfrom = (TextView) findViewById(R.id.Barcode_TV_breedfrom);
        tv_breedto = (TextView) findViewById(R.id.Barcode_TV_breedto);
        tv_score = (TextView) findViewById(R.id.Barcode_TV_score);
        foodAnalysisChart = (PieChart) findViewById(R.id.Barcode_Chart_Bar);
    }

    private void setFoodData() {
        float etc = 0;
        tv_foodname.setText(jsonArray.get(0).toString());
        tv_prices.setText(jsonArray.get(1).toString());
        tv_foodweight.setText(jsonArray.get(2).toString());
        tv_origin.setText(jsonArray.get(3).toString());
        tv_maker.setText(jsonArray.get(4).toString());
        tv_whom.setText(jsonArray.get(5).toString());
        tv_breedfrom.setText(jsonArray.get(6).toString());
        tv_breedto.setText(jsonArray.get(7).toString());
        if (Integer.valueOf(jsonArray.get(8).toString()) == 1) isPregnant = true;
        if (Integer.valueOf(jsonArray.get(9).toString()) == 1) isSmell = true;
        if (Integer.valueOf(jsonArray.get(10).toString()) == 1) isTeeth = true;
        if (Integer.valueOf(jsonArray.get(11).toString()) == 1) isDiet = true;
        tv_agefrom.setText(jsonArray.get(12).toString());
        tv_ageto.setText(jsonArray.get(13).toString());
        tv_score.setText(jsonArray.get(14).toString());

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<String> labelList = new ArrayList<>();
        labelList.add("조단백질");
        labelList.add("조지방");
        labelList.add("무기물질");
        labelList.add("조섬유");
        labelList.add("수분");
        for (int i = 0; i < labelList.size(); i++) {
            entries.add(new PieEntry((float) jsonArray.get(15 + i), labelList.get(i)));
        }
        for (int j = 20; j < 27; j++) {
            etc += (float) jsonArray.get(j);
        }
        entries.add(new PieEntry(etc, "기타"));
    }

    private class GetFoodData extends AsyncTask<String, String, String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(ShowFoodDataActivity.this, "정보를 받아오는 중입니다.", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL("http://18.216.142.72/getBarcode.php");
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
            JSONHandler jsonHandler = new JSONHandler(result);
            jsonArray = jsonHandler.parseJSON("FoodName", "Prices", "FoodWeight", "Origin", "Maker",
                    "Whom", "BreedFrom", "BreedTo", "PregnantFnt", "SmellFnt", "TeethFnt", "DietFnt", "AgeFrom", "AgeTo", "Score",
                    "Protein", "Fat", "Ash", "Fiber", "Moisture", "Calcium", "Phosphrous", "Omega3", "Omega6",
                    "VitaminA", "VitaminD", "VitaminE");

        }
    }
}
