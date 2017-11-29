package com.example.wonsi;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;

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
 * Created by wonsi on 2017-11-27.
 */

public class GetPetTypeActivity extends AppCompatActivity {
    private String petTypeJSONData;
    private ArrayList<Object> petTypeArray = new ArrayList<>();
    private HttpURLConnection conn;
    private EditText et_searchtype;
    private ListView listview_pettype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getpettype);

    }

    private void initializeLayout() {
        et_searchtype = (EditText) findViewById(R.id.Type_ET_search);
        listview_pettype = (ListView) findViewById(R.id.Type_LIST_result);
    }

    private class GetPetType extends AsyncTask<String, String, String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(GetPetTypeActivity.this, "애완동물 정보를 받아오는 중입니다...", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL("http://18.216.142.72/getType.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("userid", params[0]);
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
            petTypeArray = jsonHandler.parseJSON("TypeID", "TypeName");

        }
    }
}
