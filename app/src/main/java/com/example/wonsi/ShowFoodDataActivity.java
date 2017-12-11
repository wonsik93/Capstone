package com.example.wonsi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    private String barcode;
    private String jsonresult;
    private JSONArray jsonArray;
    private HttpURLConnection conn;
    private PieChart foodAnalysisChart;
    private PieData foodAnalysisData;
    private PieDataSet foodAnalysisDataSet;
    private boolean isPregnant = false;
    private boolean isSmell = false;
    private boolean isTeeth = false;
    private boolean isDiet = false;
    private GridLayout gridLayout_special;
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
    private TextView tv_reviewUserName;
    private TextView tv_reviewBody;
    private Button btn_review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showfooddata);
        initializeLayout();
        barcode = getIntent().getStringExtra("barcode");
        new ShowFoodDataActivity.GetFoodData().execute(barcode);
        foodAnalysisChart.getDescription().setEnabled(false);
        foodAnalysisChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                Intent intent = new Intent(ShowFoodDataActivity.this, ShowFoodContainActivity.class);
                intent.putExtra("barcode", barcode);
                intent.putExtra("protein", protein);
                intent.putExtra("fat", fat);
                intent.putExtra("ash", ash);
                intent.putExtra("fiber", fiber);
                intent.putExtra("moisture", moisture);
                intent.putExtra("calcium", calcium);
                intent.putExtra("phosphorus", phosphorus);
                intent.putExtra("omega3", omega3);
                intent.putExtra("omega6", omega6);
                intent.putExtra("vitamina", vitamina);
                intent.putExtra("vitamind", vitamind);
                intent.putExtra("vitamine", vitamine);
                startActivity(intent);
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });
    }

    private void initializeLayout() {
        tv_foodname = (TextView) findViewById(R.id.Barcode_TV_foodname);
        tv_origin = (TextView) findViewById(R.id.Barcode_TV_origin);
        tv_maker = (TextView) findViewById(R.id.Barcode_TV_maker);
        tv_foodweight = (TextView) findViewById(R.id.Barcode_TV_foodweight);
        tv_prices = (TextView) findViewById(R.id.Barcode_TV_price);
        //tv_whom = (TextView) findViewById(R.id.Barcode_TV_whom);
        tv_agefrom = (TextView) findViewById(R.id.Barcode_TV_agefrom);
        tv_ageto = (TextView) findViewById(R.id.Barcode_TV_ageto);
        tv_breedfrom = (TextView) findViewById(R.id.Barcode_TV_breedfrom);
        tv_breedto = (TextView) findViewById(R.id.Barcode_TV_breedto);
        tv_score = (TextView) findViewById(R.id.Barcode_TV_score);
        tv_reviewUserName = (TextView) findViewById(R.id.Barcode_TV_reviewUserName);
        tv_reviewBody = (TextView) findViewById(R.id.Barcode_TV_reviewBody);
        foodAnalysisChart = (PieChart) findViewById(R.id.Barcode_Chart_Bar);
        gridLayout_special = (GridLayout) findViewById(R.id.Barcode_GL_special);
        btn_review = (Button) findViewById(R.id.Barcode_BTN_review);
        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setFoodData() {
        float etc = 0;
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            tv_foodname.setText(jsonObject.getString("FoodName"));
            tv_prices.setText(jsonObject.getString("Prices"));
            tv_foodweight.setText(String.valueOf(jsonObject.getDouble("FoodWeight") * 1000) + "g");
            tv_origin.setText(jsonObject.getString("Origin"));
            tv_maker.setText(jsonObject.getString("Maker"));
            //tv_whom.setText(jsonObject.getString("Whom"));
            if (jsonObject.getString("BreedFrom").equals("0")) tv_breedfrom.setText("소형");
            if (jsonObject.getString("BreedFrom").equals("1")) tv_breedfrom.setText("중형");
            if (jsonObject.getString("BreedTo").equals("1")) tv_breedto.setText("중형");
            if (jsonObject.getString("BreedTo").equals("2")) tv_breedto.setText("대형");
            if (jsonObject.getInt("PregnantFnt") == 1) isPregnant = true;
            if (jsonObject.getInt("SmellFnt") == 1) isSmell = true;
            if (jsonObject.getInt("TeethFnt") == 1) isTeeth = true;
            if (jsonObject.getInt("DietFnt") == 1) isDiet = true;

            if (jsonObject.getInt("AgeFrom") > 12) {
                int agefrom = jsonObject.getInt("AgeFrom") / 12;
                tv_agefrom.setText(agefrom + "년");
            } else tv_ageto.setText(jsonObject.getString("AgeFrom") + "개월");

            if (jsonObject.getString("AgeTo").equals("1000")) tv_ageto.setText("무제한");
            else if (jsonObject.getInt("AgeTo") > 12) {
                int ageto = jsonObject.getInt("AgeTo") / 12;
                tv_ageto.setText(ageto + "년");
            } else tv_ageto.setText(jsonObject.getString("AgeTo") + "개월");

            tv_score.setText(jsonObject.getString("Score"));
            tv_reviewUserName.setText(jsonObject.getString("UserName"));
            tv_reviewBody.setText(jsonObject.getString("Body"));
            addSpecial();
            ArrayList<PieEntry> entries = new ArrayList<>();
            ArrayList<String> labelList = new ArrayList<>();
            labelList.add("조단백질");
            labelList.add("조지방");
            labelList.add("무기물질");
            labelList.add("조섬유");
            labelList.add("수분");
            protein = (float) jsonObject.getDouble("Protein");
            fat = (float) jsonObject.getDouble("Fat");
            ash = (float) jsonObject.getDouble("Ash");
            fiber = (float) jsonObject.getDouble("Fiber");
            moisture = (float) jsonObject.getDouble("Moisture");
            calcium = (float) jsonObject.getDouble("Calcium");
            phosphorus = (float) jsonObject.getDouble("Phosphorus");
            omega3 = (float) jsonObject.getDouble("Omega3");
            omega6 = (float) jsonObject.getDouble("Omega6");
            vitamina = (float) jsonObject.getDouble("VitaminA");
            vitamind = (float) jsonObject.getDouble("VitaminD");
            vitamine = (float) jsonObject.getDouble("VitaminE");
            entries.add(new PieEntry((float) jsonObject.getDouble("Protein"), labelList.get(0)));
            entries.add(new PieEntry((float) jsonObject.getDouble("Fat"), labelList.get(1)));
            entries.add(new PieEntry((float) jsonObject.getDouble("Ash"), labelList.get(2)));
            entries.add(new PieEntry((float) jsonObject.getDouble("Fiber"), labelList.get(3)));
            entries.add(new PieEntry((float) jsonObject.getDouble("Moisture"), labelList.get(4)));
            etc = (float) jsonObject.getDouble("Calcium") + (float) jsonObject.getDouble("Phosphorus")
                    + (float) jsonObject.getDouble("Omega3") + (float) jsonObject.getDouble("Omega6")
                    + (float) jsonObject.getDouble("VitaminA") + (float) jsonObject.getDouble("VitaminD")
                    + (float) jsonObject.getDouble("VitaminE");

            entries.add(new PieEntry(etc, "기타"));
            foodAnalysisDataSet = new PieDataSet(entries, "성분표");
            foodAnalysisDataSet.setDrawIcons(false);
            ArrayList<Integer> colors = new ArrayList<Integer>();

            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);

            colors.add(ColorTemplate.getHoloBlue());

            foodAnalysisDataSet.setColors(colors);

            foodAnalysisData = new PieData(foodAnalysisDataSet);
            foodAnalysisData.setValueTextSize(11f);
            foodAnalysisChart.setData(foodAnalysisData);
            foodAnalysisChart.highlightValue(null);
            foodAnalysisChart.invalidate();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void addSpecial() {
        if (isPregnant) {
            TextView textView = new TextView(ShowFoodDataActivity.this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setBackgroundResource(R.drawable.test_edittext);
            textView.setPadding(20, 20, 20, 20);
            textView.setText("임신");
            textView.setTextSize(13);
            gridLayout_special.addView(textView);
        }
        if (isDiet) {
            TextView textView = new TextView(ShowFoodDataActivity.this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setBackgroundResource(R.drawable.test_edittext);
            textView.setPadding(20, 20, 20, 20);
            textView.setText("체중 관리");
            textView.setTextSize(13);
            gridLayout_special.addView(textView);
        }
        if (isTeeth) {
            TextView textView = new TextView(ShowFoodDataActivity.this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setBackgroundResource(R.drawable.test_edittext);
            textView.setPadding(20, 20, 20, 20);
            textView.setText("치아 관리");
            textView.setTextSize(13);
            gridLayout_special.addView(textView);
        }
        if (isSmell) {
            TextView textView = new TextView(ShowFoodDataActivity.this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setBackgroundResource(R.drawable.test_edittext);
            textView.setPadding(20, 20, 20, 20);
            textView.setText("변냄새 관리");
            textView.setTextSize(13);
            gridLayout_special.addView(textView);
        }
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
                URL url = new URL("http://18.216.142.72/Android/getBarcode.php");
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
            } catch (JSONException e) {
                e.printStackTrace();
            }

            setFoodData();

        }
    }
}

