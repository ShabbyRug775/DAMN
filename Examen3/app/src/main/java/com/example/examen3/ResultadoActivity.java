package com.example.examen3;


import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ResultadoActivity extends AppCompatActivity {
    private Poligono polygonView;
    private TextView tvArea;
    private TextView tvVertices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        polygonView = findViewById(R.id.polygonView);
        tvArea = findViewById(R.id.tvArea);
        tvVertices = findViewById(R.id.tvVertices);

        // Obtener datos de la intención
        Intent intent = getIntent();
        double area = intent.getDoubleExtra("area", 0);
        ArrayList<PointF> vertices = intent.getParcelableArrayListExtra("vertices");

        // Mostrar resultados
        tvArea.setText(String.format(Locale.getDefault(), "Área: %.2f", area));

        StringBuilder verticesText = new StringBuilder("Vértices:\n");
        for (int i = 0; i < vertices.size(); i++) {
            PointF vertex = vertices.get(i);
            verticesText.append(String.format(Locale.getDefault(), "V%d (%.1f, %.1f)\n",
                    i + 1, vertex.x, vertex.y));
        }
        tvVertices.setText(verticesText.toString());

        // Configurar la vista del polígono
        polygonView.setVertices(vertices);

    }
    public void volver(View view) {
        finish(); // Cierra la actividad actual y regresa a la anterior
    }
}