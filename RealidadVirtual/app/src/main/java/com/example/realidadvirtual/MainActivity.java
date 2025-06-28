package com.example.realidadvirtual;

import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;

public class MainActivity extends GvrActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GvrView gvrView = new GvrView(this);
        gvrView.setRenderer(new CuboRenderer(this));
        setContentView(gvrView);
    }
}