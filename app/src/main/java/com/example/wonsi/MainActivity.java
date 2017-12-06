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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.wonsi.Helper.CircularNetworkImageView;
import com.example.wonsi.Helper.Constants;
import com.example.wonsi.Helper.CustomVolleyRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String username;
    private TextView tv_hellouser;
    private Button btn_controlpet;
    private Button btn_recommendfood;
    private Button btn_community;
    private Button btn_barcode;
    private LinearLayout ll_mypet;
    private ImageLoader imageLoader;
    private HttpURLConnection conn;
    private JSONArray jsonArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        username = intent.getStringExtra("userid");
        initializeLayout();
        tv_hellouser.setText("안녕하세요 " + username + "님!");
        //GetMyPet getMyPet = new GetMyPet();
        //getMyPet.execute(username);

    }
    private void initializeLayout(){
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
                Intent intent = new Intent(MainActivity.this, RegisterPetActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
                break;
            case R.id.Main_BTN_readbarcode:
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setCaptureActivity(ReadBarcodebyZxingActivity.class);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.initiateScan();
                break;
            case R.id.Main_BTN_recommendfood:
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_recommend, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("사료 추천 선택");
                builder.setView(dialogView);
                Button btn_fit = (Button) dialogView.findViewById(R.id.Recommend_BTN_fit);
                btn_fit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                Button btn_food= (Button) dialogView.findViewById(R.id.Recommend_BTN_food);
                btn_food.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                break;
            case R.id.Main_BTN_community:
                break;

        }

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
    private void loadImage(){
        for(int i=0;i<jsonArray.length();i++){
            try {
                final JSONObject jsonObject = jsonArray.getJSONObject(i);
                String url = jsonObject.getString("image");
                CircularNetworkImageView circularNetworkImageView = new CircularNetworkImageView(this);

                NetworkImageView networkImageView = new NetworkImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(400,400);
                params.setMargins(0,20,30,0);
                //networkImageView.setLayoutParams(params);
                circularNetworkImageView.setLayoutParams(params);

                if(url.equals("")){
                    return;
                }
                imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext()).getImageLoader();
                imageLoader.get(url,ImageLoader.getImageListener(circularNetworkImageView,R.drawable.intro,android.R.drawable.ic_dialog_alert));
                //networkImageView.setImageUrl(url,imageLoader);
                circularNetworkImageView.setImageUrl(url, imageLoader);
                circularNetworkImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {
                            Intent intent = new Intent(MainActivity.this,ShowPetDataActivity.class);
                            intent.putExtra("PetName",jsonObject.getString("PetName"));
                            intent.putExtra("Breed",jsonObject.getString("Breed"));
                            intent.putExtra("PetAge",jsonObject.getString("PetAge"));
                            intent.putExtra("Weight",jsonObject.getString("Weight"));
                            intent.putExtra("Species",jsonObject.getString("Species"));
                            intent.putExtra("P_TypeID",jsonObject.getString("P_TypeID"));
                            intent.putExtra("TypeName",jsonObject.getString("TypeName"));
                            intent.putExtra("Pregnant",jsonObject.getString("Pregnant"));
                            intent.putExtra("StoolSmell",jsonObject.getString("StoolSmell"));
                            intent.putExtra("Teeth",jsonObject.getString("Teeth"));
                            intent.putExtra("Diet",jsonObject.getString("Diet"));
                            intent.putExtra("image",jsonObject.getString("image"));
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                ll_mypet.addView(networkImageView);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        ll_mypet.removeAllViews();
        GetMyPet getMyPet = new GetMyPet();
        getMyPet.execute(username);
    }

}


