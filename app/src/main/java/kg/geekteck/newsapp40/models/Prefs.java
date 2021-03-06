package kg.geekteck.newsapp40.models;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.Serializable;

import kg.geekteck.newsapp40.R;

public class Prefs {
    private final SharedPreferences preferences;

    public Prefs(Context context) {
        this.preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }
    public void saveBoardState(){
        preferences.edit().putBoolean("board_state", true).apply();
    }
    public boolean isBoardShown(){
        return preferences.getBoolean("board_state", false);
    }
    /*public void putValues(String image, String field){
        preferences.edit().putString("image_resource", image).putString("value_of_field", field).apply();
    }*/
    public void putImage(String image){
        preferences.edit().putString("image_resource", image).apply();
    }
    public void putText(String text){
        preferences.edit().putString("value_of_field", text).apply();
    }
    public String getImage(){
        Uri path = Uri.parse("android.resource://kg.geekteck.newsapp40/" + R.drawable._image_rick);
        String imgPath = path.toString();
        return preferences.getString("image_resource", imgPath);
    }
    public String getValue(){
        return preferences.getString("value_of_field", "");
    }
    public void clean(){
        SharedPreferences.Editor et = preferences.edit();
        et.remove("image_resource").apply();
        et.remove("value_of_field").apply();
    }
}
