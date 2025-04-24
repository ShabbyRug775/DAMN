package com.example.listas;

import android.app.Activity;
import android.os.Bundle;
import java.util.*;

import android.util.Log;
import android.widget.ListView;

public class MainActivity extends Activity {
    
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        final ListView lv = (ListView) findViewById(R.id.xlv1);
        final NuevaEntradaAdapter nea = new NuevaEntradaAdapter(this, R.layout.nueva_entrada_lista);
        lv.setAdapter(nea);

        // Agrega las entradas al adaptador
        for (final NuevaEntrada i : getEntradas()) {
            nea.add(i);
        }

        Log.d("MainActivity", "ListView y Adapter configurados correctamente");
    }

    private List<NuevaEntrada> getEntradas() {
        final List<NuevaEntrada> datos = new ArrayList<NuevaEntrada>();

        // Genera entradas de prueba
        for (int i = 1; i < 31; i++) {
            datos.add(new NuevaEntrada(
                    "Datos de Entrada " + i,
                    "Alejandro ESCOM " + i,
                    new GregorianCalendar(2016, Calendar.DECEMBER, i).getTime(), // Cambié 12 por Calendar.DECEMBER
                    i % 2 == 0 ? R.drawable.icon_1 : R.drawable.icon_2 // Corrección en la concatenación
            ));
        }
        return datos;
    }
}
