package com.example.zooseeker;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class DisplayListItem {
    public String name;

    DisplayListItem(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DisplayListItem{" +
                "name='" + name + '\'' +
                '}';
    }

    public static List<DisplayListItem> loadJSON(Context context, String path) {
        try  {
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new  TypeToken<List<DisplayListItem>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
