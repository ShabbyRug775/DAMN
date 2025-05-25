package com.example.morphing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Lienzo extends View {
    private Paint p;
    private Bitmap image1, image2;
    private Bitmap resultado;
    private boolean intercambiando = false;
    private int progreso = 0;

    public Lienzo(Context c) {
        super(c);
        p = new Paint();
        image1 = BitmapFactory.decodeResource(getResources(), R.drawable.foto);
        image2 = BitmapFactory.decodeResource(getResources(), R.drawable.cafe);
        image2 = Bitmap.createScaledBitmap(image2, image1.getWidth(), image1.getHeight(), true);
        resultado = Bitmap.createBitmap(image1.getWidth(), image1.getHeight(), image1.getConfig());
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        if (!intercambiando) {
            // Mostrar imágenes lado a lado
            c.drawBitmap(image1, 50, 50, p);
            c.drawBitmap(image2, 50 + image1.getWidth() + 20, 50, p);
        } else {
            // Mostrar progreso del intercambio
            c.drawBitmap(resultado, getWidth()/2 - resultado.getWidth()/2,
                    getHeight()/2 - resultado.getHeight()/2, p);
        }
    }

    public void iniciarIntercambio() {
        if (intercambiando) return;

        new Thread(() -> {
            intercambiando = true;

            for (int x = 0; x < image1.getWidth(); x++) {
                for (int y = 0; y < image1.getHeight(); y++) {
                    int pixel1 = image1.getPixel(x, y);
                    int pixel2 = image2.getPixel(x, y);

                    // Intercambiar píxeles (50% de cada imagen)
                    int nuevoPixel = mezclarPixeles(pixel1, pixel2, progreso);
                    resultado.setPixel(x, y, nuevoPixel);
                }

                progreso = (int)(100 * (float)x / image1.getWidth());
                postInvalidate(); // Redibujar
                try { Thread.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
            }

            // Al finalizar, intercambiar las referencias
            Bitmap temp = image1;
            image1 = image2;
            image2 = temp;
            intercambiando = false;
            progreso = 0;
            postInvalidate();
        }).start();
    }

    private int mezclarPixeles(int pixel1, int pixel2, int porcentaje) {
        float ratio = porcentaje / 100f;
        return Color.argb(
                (int)(Color.alpha(pixel1) * (1-ratio) + Color.alpha(pixel2) * ratio),
                (int)(Color.red(pixel1) * (1-ratio) + Color.red(pixel2) * ratio),
                (int)(Color.green(pixel1) * (1-ratio) + Color.green(pixel2) * ratio),
                (int)(Color.blue(pixel1) * (1-ratio) + Color.blue(pixel2) * ratio)
        );
    }
}