package com.example.wondernumber;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView pantalla;
    private String operador = "";
    private double valor1 = Double.NaN;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        pantalla = findViewById(R.id.et_pantalla);
        asignarEventos();
    }

    private void asignarEventos() {
        int[] idsNumeros = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3,
                R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7,
                R.id.btn_8, R.id.btn_9
        };

        View.OnClickListener numeroClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button boton = (Button) v;
                pantalla.append(boton.getText().toString());
            }
        };

        for (int id : idsNumeros) {
            findViewById(id).setOnClickListener(numeroClick);
        }


        View.OnClickListener operadorClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button boton = (Button) v;
                if (!Double.isNaN(valor1)) {
                    pantalla.setText("");
                } else {
                    valor1 = Double.parseDouble(pantalla.getText().toString());
                }
                operador = boton.getText().toString();
                pantalla.setText("");
            }
        };

        findViewById(R.id.btn_equal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operador = "";
            }
        });

        findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pantalla.setText("");
                valor1 = Double.NaN;
                operador = "";
            }
        });

        findViewById(R.id.btn_WN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularWN();
            }
        });

        findViewById(R.id.btn_FP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularFP();
            }
        });
    }

    private void calcularWN() {
        if (!pantalla.getText().toString().isEmpty()) {
            try {
                int num = Integer.parseInt(pantalla.getText().toString());
                long wonderN = wondernumber(num); // Llamar con el número completo
                pantalla.setText(String.valueOf(wonderN));
            } catch (NumberFormatException e) {
                pantalla.setText("Número inválido.");
            }
        }
    }

    private int wondernumber(int n) {
        // Verifica que sea un numero natural
        if (n <= 0) {
            throw new IllegalArgumentException(n + " no es un número natural.");
        }
        // Variable que se manda a la pantalla
        int resultado;
        // Verifica si es par
        if (n % 2 == 0) {
            resultado = n / 2;
        } else { // Es impar
            resultado = (n * 3) + 1;
        }
        // Me regresa el resultado obtenido
        return resultado;
    }

    private void calcularFP() {
        if (!pantalla.getText().toString().isEmpty()) {
            int num = Integer.parseInt(pantalla.getText().toString());

            if (esPrimo(num)) {
                pantalla.setText(num + " es primo");
            } else if (esFibonacci(num)) {
                pantalla.setText(num + " es Fibonacci");
            } else {
                pantalla.setText(num + " no es primo ni Fibonacci");
            }
        }
    }

    private boolean esPrimo(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    private boolean esFibonacci(int n) {
        int a = 0, b = 1, temp;
        while (b < n) {
            temp = a + b;
            a = b;
            b = temp;
        }
        return b == n || n == 0;
    }

}
