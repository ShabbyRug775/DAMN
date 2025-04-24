package com.example.cuborotatorio;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle b) {

        super.onCreate(b);
        setContentView(R.layout.activity_main); // Usa el layout existente

        // Se obtiene la referencia al contenedor donde se mostrará el cubo
        CuboView cuboView = new CuboView(this);
        setContentView(cuboView); // Reemplaza el contenido de la actividad con la vista del cubo
    }
}