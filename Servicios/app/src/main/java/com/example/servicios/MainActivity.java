package com.example.servicios;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText etMensajeCifrado;
    private TextView tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TraductorService.setMainActivity(this);

        etMensajeCifrado = findViewById(R.id.etMensajeCifrado);
        tvResultado = findViewById(R.id.tvResultado);
        Button btnTraducir = findViewById(R.id.btnTraducir);

        btnTraducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = etMensajeCifrado.getText().toString();
                if (!mensaje.isEmpty()) {
                    traducirMensaje(mensaje);
                } else {
                    tvResultado.setText("Ingrese un texto primero"); // ← Feedback visual
                }
            }
        });
    }

    private void traducirMensaje(String mensaje) {
        Intent intent = new Intent(this, TraductorService.class);
        intent.putExtra("mensaje_cifrado", mensaje);
        startService(intent);
    }

    public void actualizarResultado(String resultado) {
        tvResultado.setText(resultado);
    }
}