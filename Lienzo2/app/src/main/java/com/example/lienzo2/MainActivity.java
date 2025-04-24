package com.example.lienzo2;

import android.os.*;
import android.app.*;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        MiLienzo ml=new MiLienzo(this);
        setContentView(ml);
    }
}