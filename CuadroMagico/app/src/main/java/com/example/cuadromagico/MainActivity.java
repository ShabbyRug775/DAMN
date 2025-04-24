package com.example.cuadromagico;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // InputEditText
    private EditText panta;
    // Grill para generar los cuadros
    private GridLayout gridLayout;
    // Variable que se recibe
    private int n = 0;

    @Override
    protected void onCreate(Bundle b) {

        super.onCreate(b);

        // XML a buscar
        setContentView(R.layout.activity_main);

        // Se busca el InputEditText con el id de id_matriz
        panta = findViewById(R.id.id_matriz);
        // Se busca el botón envíar
        Button btnEnviar = findViewById(R.id.enviar);
        // Se busca el grill a ocupar
        gridLayout = findViewById(R.id.gridLayout);

        // Acciones para el botón envíar
        btnEnviar.setOnClickListener(v -> {

            // Convierte a string lo que recibe de la pantalla
            String recibir = panta.getText().toString().trim();

            // Revisa si está vacío
            if (!recibir.isEmpty()) {
                try {
                    // Recibe el valor numerico de la pantalla
                    n = Integer.parseInt(recibir);
                    // Verifica si es impar positivo
                    if (n % 2 == 0 || n < 1) {
                        Log.e("CuadroMágico", "Número inválido. Debe ser impar y positivo.");
                        return;
                    }
                    // Se crea el cuadro magico
                    generarCuadroMagico(n);

                } catch (NumberFormatException e) {
                    Log.e("CuadroMágico", "Error: No es un número válido.");
                }
            } else {
                Log.w("CuadroMágico", "Advertencia: Campo vacío.");
            }
        });
    }

    // Metodo para generar el cuadro magico
    private void generarCuadroMagico(int n) {

        gridLayout.removeAllViews();    // Limpiar la grilla anterior
        gridLayout.setColumnCount(n);   // Asigna columnas
        gridLayout.setRowCount(n);      // Asigna filas

        int[][] cuadroMagico = new int[n][n];   // Arreglo del cuadro magico

        // Algoritmo de Siam para cuadrados mágicos impares
        int fila = 0;
        int columna = n / 2;

        // Recorre la matriz
        for (int num = 1; num <= n * n; num++) {

            // Recorre las filas y columnas
            cuadroMagico[fila][columna] = num;

            int nuevaFila = (fila - 1 + n) % n; // Subir una fila (cíclico)
            int nuevaColumna = (columna + 1) % n; // Mover una columna a la derecha (cíclico)

            // Verifica si la fila o columna está vacía
            if (cuadroMagico[nuevaFila][nuevaColumna] != 0) {
                fila = (fila + 1) % n; // Si está ocupado, bajar una fila
            } else {
                fila = nuevaFila;
                columna = nuevaColumna;
            }
        }

        // Agregar elementos al GridLayout
        for (int i = 0; i < n; i++) {

            for (int j = 0; j < n; j++) {

                TextView textView = new TextView(this);
                textView.setText(String.valueOf(cuadroMagico[i][j]));
                textView.setPadding(16, 16, 16, 16);
                textView.setBackgroundResource(android.R.color.darker_gray);
                textView.setTextSize(18);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 150;
                params.height = 150;
                textView.setLayoutParams(params);

                gridLayout.addView(textView);
            }
        }

        Log.d("CuadroMágico", "Generado con éxito.");
    }
}