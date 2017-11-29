package com.example.wonsi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by wonsi on 2017-11-26.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    final static int DO_REGISTER = 1;
    public SharedPreferences settings;
    String userid;
    String userpw;
    Boolean autologin;
    EditText editText_userid;
    EditText editText_userpw;
    Button button_login;
    Button button_register;
    CheckBox cb_autologin;
    ProgressDialog dialog = null;
    List<NameValuePair> params;
    HttpURLConnection conn;
    NetworkConnection conncheck = new NetworkConnection(LoginActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (conncheck.checkConnection() == false) {
            conncheck.connectErrorAlert();
        }

        editText_userid = (EditText) findViewById(R.id.Login_ET_ID);
        editText_userpw = (EditText) findViewById(R.id.Login_ET_password);
        cb_autologin = (CheckBox) findViewById(R.id.Login_CB_saveid);

        button_login = (Button) findViewById(R.id.Login_BTN_login);
        button_register = (Button) findViewById(R.id.Login_BTN_register);
        button_login.setOnClickListener(this);
        button_register.setOnClickListener(this);

        settings = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        autologin = settings.getBoolean("check", false);

        if (autologin) {
            editText_userid.setText(settings.getString("userid", ""));
            editText_userpw.setText(settings.getString("userpw", ""));
            cb_autologin.setChecked(true);
        }

        if (!settings.getString("userid", "").equals("")) editText_userpw.requestFocus();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Login_BTN_login:
                userid = editText_userid.getText().toString();
                userpw = editText_userpw.getText().toString();
                new Userlogin().execute(userid, userpw);
                break;
            case R.id.Login_BTN_register:
                Intent intent = new Intent(this, RegisterUserActivity.class);
                startActivityForResult(intent, DO_REGISTER);
                break;
        }
    }

    protected void onStop() {
        super.onStop();
        if (cb_autologin.isChecked()) {
            settings = getSharedPreferences("settings", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();

            editor.putString("userid", userid);
            editor.putString("userpw", userpw);
            editor.putBoolean("check", true);
            editor.commit();
        } else {
            settings = getSharedPreferences("settings", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DO_REGISTER) {
            if (resultCode == RESULT_OK) {
                String newuserid = data.getStringExtra("id");
                String newuserpw = data.getStringExtra("pw");
                Toast.makeText(LoginActivity.this, newuserid + newuserpw, Toast.LENGTH_SHORT).show();
                new Userlogin().execute(newuserid, newuserpw);
            }
        }
    }

    private class Userlogin extends AsyncTask<String, String, String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(LoginActivity.this, "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL("http://18.216.142.72/login.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("userid", params[0])
                        .appendQueryParameter("userpw", params[1]);
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
            if (result.equalsIgnoreCase("success")) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("userid", userid);
                startActivity(intent);
                finish();
            } else if (result.equalsIgnoreCase("false")) {
                Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
