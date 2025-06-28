package com.example.afinaguitarra;

import android.app.Activity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends Activity implements OnClickListener {
    static final String msg = "Reproduciendo ";
    MediaPlayer mp;
    Button jbnE1, jbnA1, jbnD1, jbnG1, jbnB1, jbnE2, jbns1;
    TextView jtv;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        // Inicializar el TextView primero
        jtv = (TextView) findViewById(R.id.xtv);

        // Luego inicializar los botones
        jbnE1 = (Button) findViewById(R.id.E1); jbnE1.setOnClickListener(this);
        jbnA1 = (Button) findViewById(R.id.A1); jbnA1.setOnClickListener(this);
        jbnD1 = (Button) findViewById(R.id.D1); jbnD1.setOnClickListener(this);
        jbnG1 = (Button) findViewById(R.id.G1); jbnG1.setOnClickListener(this);
        jbnB1 = (Button) findViewById(R.id.B1); jbnB1.setOnClickListener(this);
        jbnE2 = (Button) findViewById(R.id.E2); jbnE2.setOnClickListener(this);
        jbns1 = (Button) findViewById(R.id.xbns1); jbns1.setOnClickListener(this);
    }

    public void onClick(View v) {
        // Detener y liberar cualquier reproducción anterior
        if (mp != null) {
            mp.stop();
            mp.release();
        }

        if (v.getId() == R.id.E1) {
            mp = MediaPlayer.create(this, R.raw.cuerda_e1);
            jtv.setText(msg + "Cuerda E (6ta)");
        } else if (v.getId() == R.id.A1) {
            mp = MediaPlayer.create(this, R.raw.cuerda_a1);
            jtv.setText(msg + "Cuerda A (5ta)");
        } else if (v.getId() == R.id.D1) {
            mp = MediaPlayer.create(this, R.raw.cuerda_d1);
            jtv.setText(msg + "Cuerda D (4ta)");
        } else if (v.getId() == R.id.G1) {
            mp = MediaPlayer.create(this, R.raw.cuerda_g1);
            jtv.setText(msg + "Cuerda G (3ra)");
        } else if (v.getId() == R.id.B1) {
            mp = MediaPlayer.create(this, R.raw.cuerda_b1);
            jtv.setText(msg + "Cuerda B (2da)");
        } else if (v.getId() == R.id.E2) {
            mp = MediaPlayer.create(this, R.raw.cuerda_e2);
            jtv.setText(msg + "Cuerda E (1ra)");
        } else if (v.getId() == R.id.xbns1) {
            finish();
        }

        // Reproducir el sonido
        if (mp != null) {
            mp.start();
        }
    }

    @Override
    public void onPause(){
        try{
            super.onPause();
            if (mp != null) {
                mp.pause();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }
}