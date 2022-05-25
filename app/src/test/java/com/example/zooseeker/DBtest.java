package com.example.zooseeker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DBtest {
    private ExhibitDao dao;
    private ExhibitDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, ExhibitDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.exhibitDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testInsert() {
        String[] tags = new String[] {"alligator", "reptile", "gator"};
        List<String> tagList = Arrays.asList(tags);

        Exhibit exhibit1 = new Exhibit("gators", "Alligators", tagList);
        Exhibit exhibit2 = new Exhibit("gorillas", "gorillas", tagList);

        System.out.println(exhibit1.id);

        long id1 = dao.insert(exhibit1);
        long id2 = dao.insert(exhibit2);

        assertNotEquals(id1, id2);
    }

    @Test
    public void testGet() {
        String[] tags = new String[] {"alligator", "reptile", "gator"};
        List<String> tagList = Arrays.asList(tags);

        Exhibit exhibit1 = new Exhibit("gators", "Alligators", tagList);
        long id = dao.insert(exhibit1);

        Exhibit item = dao.get(id);
        assertEquals(id, item.Uid);
        assertEquals(exhibit1.id, item.id);
        assertEquals(exhibit1.name, item.name);
        for (int i = 0; i < tags.length; i++) {
            assertEquals(exhibit1.tags.get(i), item.tags.get(i));
        }
    }

    @Test
    public void testUpdate() {
        String[] tags = new String[] {"alligator", "reptile", "gator"};
        List<String> tagList = Arrays.asList(tags);

        Exhibit exhibit1 = new Exhibit("gators", "Alligators", tagList);
        long id = dao.insert(exhibit1);

        Exhibit item = dao.get(id);
        item.name = "Photos of Spider-Man";
        int itemsUpdated = dao.update(item);
        assertEquals(1, itemsUpdated);

        item = dao.get(id);
        assertNotNull(item);
        assertEquals("Photos of Spider-Man", item.name);
    }

    @Test
    public void testDelete() {
        String[] tags = new String[] {"alligator", "reptile", "gator"};
        List<String> tagList = Arrays.asList(tags);

        Exhibit exhibit1 = new Exhibit("gators", "Alligators", tagList);
        long id = dao.insert(exhibit1);

        Exhibit item = dao.get(id);
        int itemsDeleted = dao.delete(item);
        assertEquals(1, itemsDeleted);
        assertNull(dao.get(id));

    }
}
