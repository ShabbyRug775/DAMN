package com.example.listviewaves;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private ListView lv;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado);

        // Obtener el SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Obtener la lista de sensores disponibles
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        // Crear una lista de sensores para mostrar en el ListView
        ArrayList<ListaEntrada> al = new ArrayList<>();

        // Mostrar solo los primeros 3 sensores (o menos si no hay suficientes)
        int maxSensors = Math.min(3, sensorList.size());
        for (int i = 0; i < maxSensors; i++) {
            Sensor sensor = sensorList.get(i);
            al.add(new ListaEntrada(
                    R.drawable.ic_sensor, // Icono genérico para sensores
                    sensor.getName(),     // Nombre del sensor
                    "Tipo: " + sensor.getType() + ", Fabricante: " + sensor.getVendor()
            ));
        }

        // Configurar el ListView
        lv = findViewById(R.id.ListView_listado);
        lv.setAdapter(new ListaAdapter(this, R.layout.activity_main, al) {
            @Override
            public void onEntrada(Object o, View v) {
                if (o != null) {
                    TextView texto_superior_entrada = v.findViewById(R.id.textView_superior);
                    if (texto_superior_entrada != null)
                        texto_superior_entrada.setText(((ListaEntrada) o).get_textoEncima());

                    TextView texto_inferior_entrada = v.findViewById(R.id.textView_inferior);
                    if (texto_inferior_entrada != null)
                        texto_inferior_entrada.setText(((ListaEntrada) o).get_textoDebajo());

                    ImageView imagen_entrada = v.findViewById(R.id.imageView_imagen);
                    if (imagen_entrada != null)
                        imagen_entrada.setImageResource(((ListaEntrada) o).get_idImagen());
                }
            }
        });

        // Manejar clics en los elementos del ListView
        lv.setOnItemClickListener((av, view, i, l) -> {
            ListaEntrada le = (ListaEntrada) av.getItemAtPosition(i);
            Toast.makeText(MainActivity.this, "Sensor seleccionado: " + le.get_textoEncima(), Toast.LENGTH_SHORT).show();
        });
    }
}