package com.example.wonsi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.wonsi.Helper.Constants;
import com.example.wonsi.ListViewAdapter.DogTypeListViewAdapter;
import com.example.wonsi.ListViewItemClass.DogType;

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
 * Created by wonsi on 2017-11-27.
 */

public class GetPetTypeActivity extends AppCompatActivity {
    private JSONArray jsonArray;
    private HttpURLConnection conn;
    private EditText et_searchtype;
    private ListView listview_pettype;
    private DogTypeListViewAdapter listViewAdapter;
    private List<DogType> originList;
    private ArrayList<DogType> copyList;
    private String returnTypeName;
    private String returnTypeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getpettype);
        initializeLayout();
        GetPetType getPetType = new GetPetType();
        getPetType.execute("hello");

    }

    private void initializeLayout() {
        et_searchtype = (EditText) findViewById(R.id.Type_ET_search);
        et_searchtype.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = et_searchtype.getText().toString();
                searchType(text);
            }
        });
        originList = new ArrayList<DogType>();
        listview_pettype = (ListView) findViewById(R.id.Type_LIST_result);
        listview_pettype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int idx, long l) {
                returnTypeName = originList.get(idx).getTypeName();
                returnTypeID = String.valueOf(originList.get(idx).getTypeID());
                AlertDialog.Builder builder = new AlertDialog.Builder(GetPetTypeActivity.this);
                builder.setMessage("견종 선택");
                DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.putExtra("TypeName", returnTypeName);
                        intent.putExtra("TypeID", returnTypeID);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                };
                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                };
                builder.setTitle(originList.get(idx).getTypeName() + "(으)로 선택하시겠습니까?");
                builder.setPositiveButton("확인", okListener);
                builder.setNegativeButton("취소", cancelListener);
                builder.show();
            }
        });


    }

    private void setDogtypeListview() {

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                originList.add(new DogType(jsonObject.getInt("TypeID"), jsonObject.getString("TypeName"), ContextCompat.getDrawable(this, R.drawable.intro)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        copyList = new ArrayList<>();
        copyList.addAll(originList);
        listViewAdapter = new DogTypeListViewAdapter(originList);
        listview_pettype.setAdapter(listViewAdapter);

    }

    private void searchType(String typeText) {
        originList.clear();
        if (typeText.length() == 0) {
            originList.addAll(copyList);
        } else {
            for (int i = 0; i < copyList.size(); i++) {
                if (copyList.get(i).getTypeName().toLowerCase().contains(typeText)) {
                    originList.add(copyList.get(i));
                }
            }
        }
        listViewAdapter.notifyDataSetChanged();
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
                URL url = new URL(Constants.SERVER_GETTYPE_PHP);
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
            try {
                jsonArray = new JSONArray(result);
                setDogtypeListview();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
