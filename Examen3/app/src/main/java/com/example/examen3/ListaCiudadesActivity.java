package com.example.examen3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class ListaCiudadesActivity extends AppCompatActivity {
    private ListView listView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ciudades);

        databaseHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listViewCiudades);

        cargarCiudades();
        configurarClicCiudad();
    }

    private void cargarCiudades() {
        List<Ciudad> ciudades = databaseHelper.obtenerTodasLasCiudades();
        ArrayAdapter<Ciudad> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, ciudades);
        listView.setAdapter(adapter);
    }

    private void configurarClicCiudad() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ciudad ciudadSeleccionada = (Ciudad) parent.getItemAtPosition(position);
                abrirMapaConCiudad(ciudadSeleccionada);
            }
        });
    }

    private void abrirMapaConCiudad(Ciudad ciudad) {
        Intent intent = new Intent(this, MapaActivity.class);
        intent.putExtra("nombre", ciudad.getNombre());
        intent.putExtra("latitud", ciudad.getLatitud());
        intent.putExtra("longitud", ciudad.getLongitud());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}