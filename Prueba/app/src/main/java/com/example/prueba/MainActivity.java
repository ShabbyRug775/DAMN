package com.example.prueba;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView pantalla;
    private String operador = "";
    private double valor1 = Double.NaN;
    private double valor2;

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

        int[] idsOperadores = {
                R.id.btn_suma, R.id.btn_resta,
                R.id.btn_multi, R.id.btn_divi
        };

        View.OnClickListener operadorClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button boton = (Button) v;
                if (!Double.isNaN(valor1)) {
                    calcular();
                } else {
                    valor1 = Double.parseDouble(pantalla.getText().toString());
                }
                operador = boton.getText().toString();
                pantalla.setText("");
            }
        };

        for (int id : idsOperadores) {
            findViewById(id).setOnClickListener(operadorClick);
        }

        findViewById(R.id.btn_equal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcular();
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

        findViewById(R.id.btn_factorial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularFactorial();
            }
        });

        findViewById(R.id.btn_Mayor_menor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularMayorMenor();
            }
        });
    }

    private void calcular() {
        if (!pantalla.getText().toString().isEmpty()) {
            valor2 = Double.parseDouble(pantalla.getText().toString());

            switch (operador) {
                case "+":
                    valor1 += valor2;
                    break;
                case "-":
                    valor1 -= valor2;
                    break;
                case "X":
                    valor1 *= valor2;
                    break;
                case "/":
                    if (valor2 != 0) {
                        valor1 /= valor2;
                    } else {
                        pantalla.setText("Error");
                        return;
                    }
                    break;
            }

            pantalla.setText(String.valueOf(valor1));
        }
    }

    private void calcularFactorial() {
        if (!pantalla.getText().toString().isEmpty()) {
            int num = Integer.parseInt(pantalla.getText().toString());
            long factorial = 1;

            if (num < 0) {
                pantalla.setText("Error");
                return;
            }

            for (int i = 1; i <= num; i++) {
                factorial *= i;
            }

            pantalla.setText(String.valueOf(factorial));
        }
    }

    private void calcularMayorMenor() {
        if (!pantalla.getText().toString().isEmpty()) {
            String numStr = pantalla.getText().toString();
            char[] numArr = numStr.toCharArray();

            java.util.Arrays.sort(numArr);

            String menor = new String(numArr);
            String mayor = new StringBuilder(menor).reverse().toString();

            int mayorInt = Integer.parseInt(mayor);
            int menorInt = Integer.parseInt(menor);
            int resultado = mayorInt - menorInt;

            pantalla.setText(String.valueOf(resultado));
        }
    }
}
