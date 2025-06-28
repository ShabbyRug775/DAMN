package com.example.examen3;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.*;

import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText etVertexCount;
    private TableLayout tableVertices;
    private Button btnCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etVertexCount = findViewById(R.id.etVertexCount);
        tableVertices = findViewById(R.id.tableVertices);
        btnCalculate = findViewById(R.id.btnCalculate);

        Button btnGenerate = findViewById(R.id.btnGenerate);
        btnGenerate.setOnClickListener(v -> generateVertexTable());
        btnCalculate.setOnClickListener(v -> calculateArea());

        Button btnIrRegMapa = findViewById(R.id.btnIrRegMapa);
        btnIrRegMapa.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegMapaActivity.class);
            startActivity(intent);
        });

    }

    private void generateVertexTable() {
        tableVertices.removeAllViews();

        try {
            int vertexCount = Integer.parseInt(etVertexCount.getText().toString());
            if (vertexCount < 3) {
                Toast.makeText(this, "Se necesitan al menos 3 vértices", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear tabla para ingresar coordenadas
            for (int i = 0; i < vertexCount; i++) {
                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                // Etiqueta del vértice
                TextView tvLabel = new TextView(this);
                tvLabel.setText("Vértice " + (i + 1) + ":");
                row.addView(tvLabel);

                // Campo X
                EditText etX = new EditText(this);
                etX.setHint("X" + (i + 1));
                etX.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                row.addView(etX);

                // Campo Y
                EditText etY = new EditText(this);
                etY.setHint("Y" + (i + 1));
                etY.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                row.addView(etY);

                tableVertices.addView(row);
            }

            btnCalculate.setEnabled(true);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingrese un número válido", Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateArea() {
        int vertexCount = Integer.parseInt(etVertexCount.getText().toString());
        List<PointF> vertices = new ArrayList<>();

        // Obtener los vértices ingresados
        for (int i = 0; i < vertexCount; i++) {
            TableRow row = (TableRow) tableVertices.getChildAt(i);
            EditText etX = (EditText) row.getChildAt(1);
            EditText etY = (EditText) row.getChildAt(2);

            try {
                float x = Float.parseFloat(etX.getText().toString());
                float y = Float.parseFloat(etY.getText().toString());
                vertices.add(new PointF(x, y));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Ingrese valores válidos para todos los vértices", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Calcular área usando el método del shoelace
        double area = calculatePolygonArea(vertices);

        // Pasar a la pantalla de resultados
        Intent intent = new Intent(this, ResultadoActivity.class);
        intent.putExtra("vertices", new ArrayList<>(vertices));
        intent.putExtra("area", area);
        startActivity(intent);
    }

    private double calculatePolygonArea(List<PointF> vertices) {
        double sum = 0;
        int n = vertices.size();

        for (int i = 0; i < n; i++) {
            PointF current = vertices.get(i);
            PointF next = vertices.get((i + 1) % n);
            sum += (current.x * next.y) - (next.x * current.y);
        }

        return Math.abs(sum) / 2.0;
    }
}