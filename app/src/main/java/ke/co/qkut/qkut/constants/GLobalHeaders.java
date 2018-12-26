package ke.co.qkut.qkut.constants;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import ke.co.qkut.qkut.datastore.LocalDatabase;

public class GLobalHeaders {
    public  static  JSONObject  getGlobalHeaders(AppCompatActivity appCompatActivity){
       JSONObject headers = new  JSONObject();
        try {
            headers.put("Content-Type", "application/json");
            headers.put("Authorization","Bearer " +LocalDatabase.getToken(appCompatActivity));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return headers;
    }
    public  static  JSONObject  getGlobalHeaders(Context context){
        JSONObject headers = new  JSONObject();
        try {
            headers.put("Content-Type", "application/json");
            headers.put("Authorization","Bearer " +LocalDatabase.getToken(context));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return headers;
    }
}
