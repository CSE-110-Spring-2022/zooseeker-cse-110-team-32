package com.example.zooseeker;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

/*
    An example converter class used from stackOverflow, I posted the link in the resource tab
 */
public class TagConverter {
    @TypeConverter
    public List<String> storedStringToLanguages(String value) {
        List<String> tags = Arrays.asList(value.split("\\s*,\\s*"));
        return tags;
    }

    @TypeConverter
    public String languagesToStoredString(List<String> tags) {
        String result = "";

        for (String tag : tags)
            result += tag + ",";

        return result;
    }
}
