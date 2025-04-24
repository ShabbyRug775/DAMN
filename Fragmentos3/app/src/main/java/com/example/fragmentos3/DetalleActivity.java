package com.example.fragmentos3;

import android.os.Bundle;
import android.widget.TextView; // ¡Esta importación faltaba!
import androidx.appcompat.app.AppCompatActivity;

public class DetalleActivity extends AppCompatActivity {
    public static final String EXTRA_TEXTO = "com.example.fragmentos3.EXTRA_TEXTO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        HelechoView helechoView = findViewById(R.id.helechoView);
        TextView tvDetalle = findViewById(R.id.tvDetalleEstudiante); // Ahora reconocerá TextView

        if (getIntent() != null) {
            String textoDetalle = getIntent().getStringExtra(EXTRA_TEXTO);
            if (tvDetalle != null) {
                tvDetalle.setText(textoDetalle);
            }
        }
    }
}