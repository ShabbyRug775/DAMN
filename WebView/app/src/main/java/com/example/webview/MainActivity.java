package com.example.webview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.*;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends Activity implements OnClickListener, OnItemSelectedListener {
    Button jbn1, jbn2, jbn3, jbn4;
    WebSettings ws;
    WebView wv;
    EditText jet;
    Spinner spinner;
    String s = "https://www.google.com/";

    // HTML de los códigos proporcionados
    final String PLANTA_HTML = "<!DOCTYPE html><html><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'><title>Planta</title><style> canvas { border: 1px solid black; } </style></head><body><canvas id='canvas' width='600' height='600'></canvas><script>const canvas = document.getElementById('canvas');const ctx = canvas.getContext('2d');function pintaPlanta() {let x = 0, y = 0;ctx.fillStyle = 'green';ctx.translate(300, 600);ctx.scale(50, 50);for (let i = 0; i < 100000; i++) {const rand = Math.random(); let nextX, nextY;if (rand < 0.01) {nextX = 0; nextY = 0.16*y;} else if (rand < 0.86) {nextX = 0.85*x + 0.04*y;nextY = -0.04*x + 0.85*y + 1.6;} else if (rand < 0.93) {nextX = 0.2*x - 0.26*y;nextY = 0.23*x + 0.22*y + 1.6;} else {nextX = -0.15*x + 0.28*y;nextY = 0.26*x + 0.24*y + 0.44;}x = nextX; y = nextY;ctx.fillRect(x, -y, 0.02, 0.02);}}pintaPlanta();</script></body></html>";

    final String ALFOMBRA_HTML = "<!DOCTYPE html><html><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'><title>Alfombra</title><style> canvas { border: 1px solid black; } </style></head><body><canvas id='canvas' width='600' height='600'></canvas><script>const canvas = document.getElementById('canvas');const ctx = canvas.getContext('2d');function pintaAlfombra(x, y, tam, prof) {if (prof===0) {ctx.fillRect(x, y, tam, tam);} else {const nuevo = tam/3;for (let i=0; i<3; i++) {for (let j=0; j<3; j++) {if (i===1 && j===1) {continue; }pintaAlfombra(x + i*nuevo, y + j*nuevo, nuevo, prof-1);}}}}ctx.fillStyle = 'indigo';pintaAlfombra(0, 0, 600, 4);</script></body></html>";

    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        // Configurar Spinner
        spinner = findViewById(R.id.xspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.paginas_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Configurar botones y WebView
        jbn1 = findViewById(R.id.xbn1);
        jbn1.setOnClickListener(this);
        jbn2 = findViewById(R.id.xbn2);
        jbn2.setOnClickListener(this);
        jbn3 = findViewById(R.id.xbn3);
        jbn3.setOnClickListener(this);
        jbn4 = findViewById(R.id.xbn4);
        jbn4.setOnClickListener(this);

        jet = findViewById(R.id.xet);
        wv = findViewById(R.id.xwv);
        wv.setWebViewClient(new Cliente());

        ws = wv.getSettings();
        ws.setBuiltInZoomControls(true);
        ws.setJavaScriptEnabled(true);
        ws.setUseWideViewPort(true);
        ws.setLoadWithOverviewMode(true);
    }

    class Cliente extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        public void onPageFinished(WebView view, String url) {
            jet.setText(url);
        }
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.xbn1) {
            wv.goBack();
        } else if (id == R.id.xbn2) {
            wv.loadUrl(s);
        } else if (id == R.id.xbn3) {
            wv.goForward();
        } else if (id == R.id.xbn4) {
            wv.loadUrl("https://" + jet.getText().toString());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                wv.loadDataWithBaseURL(null, PLANTA_HTML, "text/html", "UTF-8", null);
                break;
            case 1:
                wv.loadDataWithBaseURL(null, ALFOMBRA_HTML, "text/html", "UTF-8", null);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // No hacer nada
    }
}