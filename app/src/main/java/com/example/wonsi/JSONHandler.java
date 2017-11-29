package com.example.wonsi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wonsi on 2017-11-26.
 */

public class JSONHandler {
    String jsondata;

    public JSONHandler(String jsondata) {
        this.jsondata = jsondata;
    }

    public ArrayList parseJSON(String... strings) {
        try {
            ArrayList<Object> arrayList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsondata);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                for (String s : strings) {
                    arrayList.add(jsonObject.get(s));
                }
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
