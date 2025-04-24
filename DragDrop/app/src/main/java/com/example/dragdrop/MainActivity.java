package com.example.dragdrop;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DragView(this)); // Usamos la vista personalizada
    }

    // Clase interna para la vista personalizada
    class DragView extends View {
        Paint paint = new Paint();
        Paint linePaint = new Paint();
        float circle1X = 200, circle1Y = 200, circle2X = 500, circle2Y = 500, radius = 50;
        boolean isDrawingLine = false;
        boolean isLinePermanent = false; // Indica si la línea debe ser permanente
        float touchX = -1, touchY = -1; // Coordenadas del toque actual

        public DragView(Context context) {
            super(context);
            paint.setColor(Color.GREEN); // Color de los círculos
            linePaint.setColor(Color.BLUE); // Color de la línea
            linePaint.setStrokeWidth(5); // Grosor de la línea
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(Color.WHITE); // Fondo blanco

            // Dibuja los dos círculos (estáticos)
            canvas.drawCircle(circle1X, circle1Y, radius, paint);
            canvas.drawCircle(circle2X, circle2Y, radius, paint);

            // Dibuja la línea dinámica si el usuario está arrastrando
            if (isDrawingLine) {
                canvas.drawLine(circle1X, circle1Y, touchX, touchY, linePaint);
            }

            // Dibuja la línea permanente si está activa
            if (isLinePermanent) {
                canvas.drawLine(circle1X, circle1Y, circle2X, circle2Y, linePaint);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Verifica si el toque inicial está dentro del primer círculo
                    if (isInsideCircle(x, y, circle1X, circle1Y, radius)) {
                        isDrawingLine = true;
                        touchX = circle1X;
                        touchY = circle1Y;
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (isDrawingLine) {
                        // Actualiza las coordenadas del toque actual
                        touchX = x;
                        touchY = y;
                        invalidate(); // Redibuja la vista
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    if (isDrawingLine) {
                        // Verifica si el toque final está dentro del segundo círculo
                        if (isInsideCircle(x, y, circle2X, circle2Y, radius)) {
                            isLinePermanent = true; // Activa la línea permanente
                        }
                        isDrawingLine = false; // Deja de dibujar la línea dinámica
                        invalidate(); // Redibuja la vista
                    }
                    break;
            }
            return true;
        }

        // Método para verificar si un punto está dentro de un círculo
        private boolean isInsideCircle(float x, float y, float circleX, float circleY, float radius) {
            return Math.sqrt(Math.pow(x - circleX, 2) + Math.pow(y - circleY, 2)) <= radius;
        }
    }
}