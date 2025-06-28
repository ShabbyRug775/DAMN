package com.example.examen3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegMapaActivity extends AppCompatActivity {
    private EditText etNomCiudad, etLatitud, etLongitud;
    private Button btnIrRegMapa;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_mapa);

        // Inicializar la base de datos
        databaseHelper = new DatabaseHelper(this);

        // Vincular vistas
        etNomCiudad = findViewById(R.id.etNomCiudad);
        etLatitud = findViewById(R.id.etLatitud);
        etLongitud = findViewById(R.id.etLongitud);
        btnIrRegMapa = findViewById(R.id.btnIrRegMapa);
        Button btnIrListaCiudades = findViewById(R.id.btnIrListaCiudades);

        // Configurar el botón
        btnIrRegMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarCiudad();
            }
        });

        btnIrListaCiudades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verListaCiudades(v);
            }
        });
    }

    private void registrarCiudad() {
        String nombre = etNomCiudad.getText().toString().trim();
        String strLatitud = etLatitud.getText().toString().trim();
        String strLongitud = etLongitud.getText().toString().trim();

        if (nombre.isEmpty() || strLatitud.isEmpty() || strLongitud.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double latitud = Double.parseDouble(strLatitud);
            double longitud = Double.parseDouble(strLongitud);

            // Validar rangos de latitud y longitud
            if (latitud < -90 || latitud > 90) {
                Toast.makeText(this, "La latitud debe estar entre -90 y 90", Toast.LENGTH_SHORT).show();
                return;
            }
            if (longitud < -180 || longitud > 180) {
                Toast.makeText(this, "La longitud debe estar entre -180 y 180", Toast.LENGTH_SHORT).show();
                return;
            }

            // Guardar en la base de datos
            long id = databaseHelper.agregarCiudad(nombre, latitud, longitud);

            if (id != -1) {
                Toast.makeText(this, "Ciudad registrada exitosamente", Toast.LENGTH_SHORT).show();
                limpiarCampos();
            } else {
                Toast.makeText(this, "Error al registrar la ciudad", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingrese valores numéricos válidos para latitud y longitud", Toast.LENGTH_SHORT).show();
        }
    }

    public void verListaCiudades(View view) {
        Intent intent = new Intent(this, ListaCiudadesActivity.class);
        startActivity(intent);
    }

    private void limpiarCampos() {
        etNomCiudad.setText("");
        etLatitud.setText("");
        etLongitud.setText("");
        etNomCiudad.requestFocus();
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}