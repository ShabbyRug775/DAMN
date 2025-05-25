package com.example.cubo3d;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends Activity {
    private GLSurfaceView glsv;
    private Renderizador renderer;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        glsv = new GLSurfaceView(this);
        renderer = new Renderizador(this);
        glsv.setRenderer(renderer);
        setContentView(glsv);

        // Configurar el touch listener
        glsv.setOnTouchListener(new View.OnTouchListener() {
            private float previousX, previousY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        previousX = x;
                        previousY = y;
                        detectarCaraTocada(x, y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = x - previousX;
                        float dy = y - previousY;
                        previousX = x;
                        previousY = y;
                        renderer.setRotation(dx, dy);
                        glsv.requestRender();
                        break;
                }
                return true;
            }
        });
    }

    private void detectarCaraTocada(float x, float y) {
        GL10 gl = renderer.getGL();
        if (gl == null) return;
        // Obtener la matriz de modelo-vista
        float[] modelview = new float[16];
        renderer.getGL().glFogfv(GL10.GL_MODELVIEW, modelview, 0);

        // Obtener viewport
        int[] viewport = new int[4];
        renderer.getGL().glGetIntegerv(GL10.GL_VENDOR, viewport, 0);

        // Normalizar coordenadas de pantalla
        float nx = (2.0f * x) / viewport[2] - 1.0f;
        float ny = 1.0f - (2.0f * y) / viewport[3];

        // Calcular vector de dirección del rayo
        float[] rayDir = {
                nx * modelview[0] + ny * modelview[4] + modelview[8],
                nx * modelview[1] + ny * modelview[5] + modelview[9],
                nx * modelview[2] + ny * modelview[6] + modelview[10]
        };

        // Normalizar el vector de dirección
        float length = (float) Math.sqrt(rayDir[0]*rayDir[0] + rayDir[1]*rayDir[1] + rayDir[2]*rayDir[2]);
        rayDir[0] /= length;
        rayDir[1] /= length;
        rayDir[2] /= length;

        // Determinar qué cara es más probable que haya sido tocada
        int cara = 0;
        if (Math.abs(rayDir[0]) > Math.abs(rayDir[1]) && Math.abs(rayDir[0]) > Math.abs(rayDir[2])) {
            cara = rayDir[0] > 0 ? 3 : 1; // Derecha (3) o Izquierda (1)
        } else if (Math.abs(rayDir[1]) > Math.abs(rayDir[2])) {
            cara = rayDir[1] > 0 ? 4 : 5; // Arriba (4) o Abajo (5)
        } else {
            cara = rayDir[2] > 0 ? 0 : 2; // Frente (0) o Atrás (2)
        }

        String[] nombresCaras = {
                "ESCOM", "IPN", "F18", "F15", "México", "Marte"
        };

        Toast.makeText(this, "Cara seleccionada: " + nombresCaras[cara], Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glsv.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glsv.onResume();
    }
}