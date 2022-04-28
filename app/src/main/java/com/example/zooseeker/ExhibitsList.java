package com.example.zooseeker;

import java.util.ArrayList;
import java.util.List;

public class ExhibitsList {
    private List<Exhibit> myList;

    public ExhibitsList() {
        this.myList = new ArrayList<Exhibit>();
    }

    public Boolean addExhibit(Exhibit e) {
        return this.myList.add(e);
    }

    public void clear() {
        this.myList.clear();
    }

    public Exhibit get(int index) {
        return this.myList.get(index);
    }

    public int size() {
        return this.myList.size();
    }

    public Exhibit remove(int index) {
        return this.myList.remove(index);
    }

    public Boolean remove(Exhibit e) {
        return this.myList.remove(e);
    }

    //toImplement sort



}
