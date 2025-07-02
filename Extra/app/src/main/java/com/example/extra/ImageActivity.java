package com.example.extra;


import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ImageActivity extends AppCompatActivity {

    private ImageView movableImage;
    private float dX, dY; // Desplazamiento inicial del toque

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        movableImage = findViewById(R.id.movable_image);

        movableImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Guardar la posición inicial del toque en relación con la imagen
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // Mover la imagen a la nueva posición del dedo
                        view.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0) // No queremos animación de movimiento, solo un arrastre inmediato
                                .start();
                        break;
                    default:
                        return false;
                }
                return true; // Indica que hemos manejado el evento
            }
        });
    }
}