package com.example.preferencias;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MostrarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar);

        TextView tvCoordenadas = findViewById(R.id.tvCoordenadas);
        Button btnRegresar = findViewById(R.id.btnRegresar);

        Intent intent = getIntent();
        String latitud = intent.getStringExtra("latitud");
        String longitud = intent.getStringExtra("longitud");

        String mensaje = "Coordenadas de CDMX:\nLatitud: " + latitud + "\nLongitud: " + longitud;
        tvCoordenadas.setText(mensaje);

        btnRegresar.setOnClickListener(v -> finish());
    }
}