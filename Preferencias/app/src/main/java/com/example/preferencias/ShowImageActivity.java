package com.example.preferencias;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ShowImageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        ImageCanvasView imageCanvas = findViewById(R.id.imageCanvas);
        Button btnBack = findViewById(R.id.btnBack);

        String imagePath = getIntent().getStringExtra("image_path");
        if (imagePath != null) {
            imageCanvas.setImagePath(imagePath);
        }

        btnBack.setOnClickListener(v -> finish());
    }
}