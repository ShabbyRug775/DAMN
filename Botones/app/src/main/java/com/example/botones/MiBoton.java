package com.example.botones;

import android.app.*;
import android.os.*;

import android.view.View;
import android.view.View.*; // OnClickListener                      // 1
import android.widget.*;    // Button


public class MiBoton extends Activity implements OnClickListener{   // 2
    Button jbn;
    TextView jtv;

    protected void onCreate(Bundle b) {

        super.onCreate(b);
        setContentView(R.layout.activity_main);
        jbn = (Button)findViewById(R.id.xbn1);
        jtv = (TextView)findViewById(R.id.xtv);
        jbn.setOnClickListener(this);                               // 3
    }

    public void onClick(View v){                                    // 4
        jtv.setText("Boton digitado");                              // 5
    }
}
