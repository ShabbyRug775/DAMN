package com.example.canvaspaint;

import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Lienzo l = new Lienzo(this);
        setContentView(l);
    }
}