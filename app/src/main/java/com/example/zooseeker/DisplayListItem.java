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

/*This class puts the exhibits into an easy-to-display format and loads the data from the given
 * files into a List format
 */
public class DisplayListItem {
    public String name;

    /*Constructor that sets the DisplayListItem's name to the name passed in
       @param name: name of the displayListItem
     */
    DisplayListItem(String name) {
        this.name = name;
    }

    /*Returns string format of the DisplayListItem
   @return string format of DisplayListItem
    */
    @Override
    public String toString() {
        return "DisplayListItem{" +
                "name='" + name + '\'' +
                '}';
    }

    /*Returns the name of the DisplayListItem
     @return name of DisplayListItem
    */
    public String getName(){
        return this.name;
    }

    /*loads information from example files in form of context and returns a list of DisplayListItems
  If successful, returns list of DisplayListItems, else logs the error and returns an empty list
  @param context = gives information of asset files that need to be loaded
  @param path = name of json file to be opened
   */
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
