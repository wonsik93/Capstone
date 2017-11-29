package com.example.wonsi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wonsi on 2017-11-26.
 */

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {
    String userid, userpw;
    EditText editText_ID;
    EditText editText_Password;
    Button button_register;
    HttpURLConnection conn;
    NetworkConnection conncheck = new NetworkConnection(RegisterUserActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (conncheck.checkConnection() == false) {
            conncheck.connectErrorAlert();
        }

        editText_ID = (EditText) findViewById(R.id.Register_ET_ID);
        editText_Password = (EditText) findViewById(R.id.Register_ET_Password);

        button_register = (Button) findViewById(R.id.Register_BTN_register);
        button_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Register_BTN_register:
                userid = editText_ID.getText().toString();
                userpw = editText_Password.getText().toString();
                new RegisterUserActivity.UserRegister().execute(userid, userpw);
                break;

        }
    }

    public void registerSuccessAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원가입");
        builder.setMessage("회원가입이 완료되었습니다...").
                setCancelable(false).
                setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra("id", userid);
                        intent.putExtra("pw", userpw);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private class UserRegister extends AsyncTask<String, String, String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(RegisterUserActivity.this, "회원가입 중 입니다.", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL("http://18.216.142.72/register.php");
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
                    Log.d("Result", result.toString());
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
                registerSuccessAlert();
            } else if (result.equalsIgnoreCase("false")) {
                Toast.makeText(RegisterUserActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
