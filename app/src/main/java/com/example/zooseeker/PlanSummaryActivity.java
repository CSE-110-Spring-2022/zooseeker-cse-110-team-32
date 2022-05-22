package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PlanSummaryActivity extends AppCompatActivity {

    public static PlanList planList;
    public static PlanList getPlan(){
        return planList;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_summary);
        PlanList plan = SearchActivity.getPlan();
        planList = plan;

    }
}