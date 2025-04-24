package com.example.pestaas;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class MainActivity extends Activity {
    private TabHost th;
    private TabSpec ts;
    private Resources r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        r = getResources();
        th = findViewById(android.R.id.tabhost);
        th.setup();

        // Tab 1
        ts = th.newTabSpec("mitab1");
        ts.setContent(R.id.xtab1);
        ts.setIndicator("TAB1", r.getDrawable(android.R.drawable.ic_btn_speak_now));
        th.addTab(ts);

        // Tab 2
        ts = th.newTabSpec("mitab2");
        ts.setContent(R.id.xtab2);
        ts.setIndicator("TAB2", r.getDrawable(android.R.drawable.ic_dialog_map));
        th.addTab(ts);

        // Tab 3 (Helecho)
        ts = th.newTabSpec("mitab3");
        ts.setContent(R.id.xtab3);
        ts.setIndicator("FRACTAL", r.getDrawable(android.R.drawable.ic_dialog_info));
        th.addTab(ts);

        th.setCurrentTab(0);

        th.setOnTabChangedListener(tabId -> {
            Toast.makeText(getApplicationContext(),
                    "Pestaña seleccionada: " + tabId,
                    Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onDestroy() {
        if (th != null) {
            th.setOnTabChangedListener(null);
        }
        super.onDestroy();
    }
}