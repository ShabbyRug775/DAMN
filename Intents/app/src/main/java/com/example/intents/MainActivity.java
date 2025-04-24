package com.example.intents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    EditText etDividendo, etDivisor;
    Button btnCalcular;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDividendo = findViewById(R.id.etDividendo);
        etDivisor = findViewById(R.id.etDivisor);
        btnCalcular = findViewById(R.id.btnCalcular);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    long dividendo = Long.parseLong(etDividendo.getText().toString());
                    long divisor = Long.parseLong(etDivisor.getText().toString());

                    if (divisor == 0) {
                        Toast.makeText(MainActivity.this, "El divisor no puede ser 0", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    intent = new Intent(MainActivity.this, segundaActivity.class);
                    intent.putExtra("dividendo", dividendo);
                    intent.putExtra("divisor", divisor);
                    startActivity(intent);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Ingrese valores válidos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

