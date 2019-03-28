package com.example.weather.saver;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaveData {
    private Context mContext;

    public SaveData(Context context) {
        this.mContext = context;
    }

    public void saveData(List<String> date_list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String i : date_list) {
            stringBuilder.append(i);
            stringBuilder.append(",");
        }
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("city_name", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cities", stringBuilder.toString());
        editor.commit();
    }

    public List<String> loadData() {
        List<String> items = new ArrayList<>();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("city_name", Context.MODE_PRIVATE);
        String wordsString = sharedPreferences.getString("cities", "");

        if (!wordsString.equals("")) {
            String[] itemsWords = wordsString.split(",");
            items.addAll(Arrays.asList(itemsWords));
        }
        return items;
    }
}
