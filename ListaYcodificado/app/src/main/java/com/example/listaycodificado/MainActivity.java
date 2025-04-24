package com.example.listaycodificado;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etTexto;
    private Button btnCodificar, btnDescodificar;
    private TextView tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        etTexto = findViewById(R.id.etTexto);
        btnCodificar = findViewById(R.id.btnCodificar);
        btnDescodificar = findViewById(R.id.btnDescodificar);
        tvResultado = findViewById(R.id.tvResultado);

        // Configurar botón para codificar
        btnCodificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = etTexto.getText().toString().trim();
                if (texto.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor, ingresa un texto", Toast.LENGTH_SHORT).show();
                } else {
                    String textoCodificado = rot13(texto);
                    tvResultado.setText("Resultado: " + textoCodificado);
                }
            }
        });

        // Configurar botón para descodificar
        btnDescodificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = etTexto.getText().toString().trim();
                if (texto.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor, ingresa un texto", Toast.LENGTH_SHORT).show();
                } else {
                    String textoDescodificado = rot13(texto);
                    tvResultado.setText("Resultado: " + textoDescodificado);
                }
            }
        });
    }

    // Método para aplicar ROT13
    private String rot13(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                // Rotación para letras minúsculas
                c = (char) ('a' + (c - 'a' + 13) % 26);
            } else if (c >= 'A' && c <= 'Z') {
                // Rotación para letras mayúsculas
                c = (char) ('A' + (c - 'A' + 13) % 26);
            }
            result.append(c);
        }
        return result.toString();
    }
}