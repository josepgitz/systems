package ke.co.qkut.qkut.datastore;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class LocalDatabase {
    private static String AUTH_TOKEN="TOKEN";
    public  static String NOT_LOGGED_IN="NO_TOKEN";
    public  static  String getToken(AppCompatActivity appCompatActivity){
        SharedPreferences sharedPreferences=appCompatActivity.getPreferences(Context.MODE_PRIVATE) ;
        return sharedPreferences.getString(AUTH_TOKEN,NOT_LOGGED_IN);

    }
    public  static  void   setToken(AppCompatActivity appCompatActivity,String token ){

        SharedPreferences sharedPreferences=appCompatActivity.getPreferences(Context.MODE_PRIVATE) ;
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(AUTH_TOKEN,token);
        editor.apply();
    }


    public static String getToken(Context context) {
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(AUTH_TOKEN,NOT_LOGGED_IN);
    }
}
