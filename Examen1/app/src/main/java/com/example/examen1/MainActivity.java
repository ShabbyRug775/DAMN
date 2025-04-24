package com.example.examen1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {

        super.onCreate(b);
        setContentView(R.layout.activity_main); // Usa el layout existente

        // Se obtiene la referencia al contenedor donde se mostrará el cubo
        paralelepipedo paraView = new paralelepipedo(this);
        setContentView(paraView); // Reemplaza el contenido de la actividad con la vista del paralepipedo
    }
}