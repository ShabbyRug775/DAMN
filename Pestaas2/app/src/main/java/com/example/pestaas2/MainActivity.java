package com.example.pestaas2;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTabHost;

public class MainActivity extends FragmentActivity {
    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = findViewById(R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.tabcontent);

        tabHost.addTab(
                tabHost.newTabSpec("tab1").setIndicator("Pestaña 1"),
                Tab1.class, null
        );
        tabHost.addTab(
                tabHost.newTabSpec("tab2").setIndicator("Pestaña 2"),
                Tab2.class, null
        );
        tabHost.addTab(
                tabHost.newTabSpec("tab3").setIndicator("Pestaña 3"),
                Tab3.class, null
        );
    }
}