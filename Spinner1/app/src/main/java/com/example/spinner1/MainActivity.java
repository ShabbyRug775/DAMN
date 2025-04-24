package com.example.spinner1;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private Spinner jsp1;
    private Button jbn1;
    private EditText jet1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        initViews();

        // Configurar listeners
        setupListeners();

        // Cargar datos iniciales en el Spinner
        cargaSpinner();
    }

    private void initViews() {
        jsp1 = findViewById(R.id.xsp1);
        jbn1 = findViewById(R.id.xbn1);
        jet1 = findViewById(R.id.xet1);
    }

    private void setupListeners() {
        jsp1.setOnItemSelectedListener(this);

        jbn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarElemento();
            }
        });
    }

    private void agregarElemento() {
        String texto = jet1.getText().toString().trim();

        if (texto.isEmpty()) {
            Toast.makeText(this, "Por favor escriba un elemento", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHandler db = new DatabaseHandler(this);
        db.insertLabel(texto);
        jet1.setText("");

        // Ocultar teclado virtual
        ocultarTeclado();

        // Actualizar Spinner
        cargaSpinner();
    }

    private void ocultarTeclado() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void cargaSpinner() {
        DatabaseHandler db = new DatabaseHandler(this);
        List<String> etiquetas = db.getAllLabels();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                etiquetas
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jsp1.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String seleccion = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, "Selección: " + seleccion, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // No se requiere acción cuando no hay selección
    }
}