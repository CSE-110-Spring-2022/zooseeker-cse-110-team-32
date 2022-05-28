package com.example.zooseeker;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class SearchClassUnitTest {
    @Before
    public void createContext(){

    }

    @Test
    public void mapSize(){
        Context context = ApplicationProvider.getApplicationContext();
        Map<String, ZooData.VertexInfo> completeMap = ZooData.loadVertexInfoJSON(context);
        assertEquals(27, completeMap.size());
    }

    @Test
    public void arrayCollectionSize(){
        Context context = ApplicationProvider.getApplicationContext();
        Map<String, ZooData.VertexInfo> completeMap = ZooData.loadVertexInfoJSON(context);
        Collection<ZooData.VertexInfo> valuesCollection = completeMap.values();
        ZooData.VertexInfo[] values = new ZooData.VertexInfo[7];
        valuesCollection.toArray(values);
        assertEquals(7, values.length);
    }

    @Test
    public void sizeResults(){
        Context context = ApplicationProvider.getApplicationContext();
        Search searcher = new Search(context);
        ArrayList<ZooData.VertexInfo> results = searcher.getResults("Crocodiles");
        assertEquals(1, results.size());
    }

    @Test
    public void crocodilesResults(){
        Context context = ApplicationProvider.getApplicationContext();
        Search searcher = new Search(context);
        ArrayList<ZooData.VertexInfo> results = searcher.getResults("Crocodiles");
        assertEquals("Crocodiles", results.get(0).name);
    }

    @Test
    public void gateResults(){
        Context context = ApplicationProvider.getApplicationContext();
        Search searcher = new Search(context);
        ArrayList<ZooData.VertexInfo> results = searcher.getResults("Gate");
        assertEquals(0, results.size());
    }

    @Test
    public void multipleExhibits(){
        Context context = ApplicationProvider.getApplicationContext();
        Search searcher = new Search(context);
        ArrayList<ZooData.VertexInfo> results = searcher.getResults("e");
        assertEquals(12, results.size());
    }

    @Test
    public void testCapitalization(){
        Context context = ApplicationProvider.getApplicationContext();
        Search searcher = new Search(context);
        assertEquals(searcher.getResults("Gorillas"), searcher.getResults(("gorillas")));
    }

    @Test
    public void testSearchTags(){
        Context context = ApplicationProvider.getApplicationContext();
        Search searcher = new Search(context);
        ArrayList<ZooData.VertexInfo> apeResults = searcher.getResults("ape");
        assertEquals(2, searcher.getResults(("ape")).size());
        ArrayList<ZooData.VertexInfo> results = searcher.getResults("mammal");
        assertEquals(5, results.size());
    }
}
