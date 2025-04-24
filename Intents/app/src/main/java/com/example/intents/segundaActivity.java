package com.example.intents;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class segundaActivity extends Activity {
    TextView tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        tvResultado = findViewById(R.id.tvResultado);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            long dividendo = extras.getLong("dividendo");
            long divisor = extras.getLong("divisor");

            String resultado = calcularDivision(dividendo, divisor);
            tvResultado.setText(resultado);
        }
    }

    private String calcularDivision(long dividendo, long divisor) {
        StringBuilder sb = new StringBuilder();
        long cociente = dividendo / divisor;
        long residuo = dividendo % divisor;

        sb.append(" ").append(cociente).append("\n");
        sb.append(divisor).append(" |").append(dividendo).append("\n");
        sb.append("   ").append(divisor * (dividendo / divisor)).append("\n");
        sb.append("   ").append(residuo).append("\n");

        return sb.toString();
    }
}
