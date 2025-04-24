package com.example.canvaspaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

public class Lienzo extends View {
    Paint p;
    int x, y;

    public Lienzo(Context c) {
        super(c);
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c); // Canvas pinta atributos

        p = new Paint(); // Paint asigna atributos
        x = c.getWidth(); // Ancho del canvas
        y = c.getHeight(); // Alto del canvas

        p.setColor(Color.WHITE); // Fondo blanco
        c.drawPaint(p);

        p.setColor(Color.BLACK); // Texto negro
        p.setTextSize(20);

        // Dibujar texto con alineación izquierda
        p.setTextAlign(Paint.Align.LEFT); // Modificación a Align.LEFT
        c.drawText("x0=" + x / 2 + ", y0=" + y / 2, x / 2 + 20, y / 2 - 20, p);

        p.setColor(Color.rgb(0, 0, 255)); // Ejes azules
        c.drawLine(x / 2, 0, x / 2, y, p); // Eje Y
        c.drawLine(0, y / 2, x, y / 2, p); // Eje X

        // Dibujar texto con alineación usando valueOf
        p.setTextAlign(Paint.Align.valueOf("RIGHT")); // Modificación a valueOf
        p.setTypeface(Typeface.SERIF);
        c.drawText("Eje X", x - 5, y / 2 - 10, p);

        // Dibujar texto con alineación centrada
        p.setTextAlign(Paint.Align.CENTER);
        p.setTypeface(Typeface.DEFAULT_BOLD); // Fuente DEFAULT_BOLD
        c.drawText("Eje Y", x / 2 + 30, 20, p);

        // Dibujar figuras en cada cuadrante
        p.setColor(Color.argb(100, 200, 100, 100));

        // Cuadrante 1: drawOval
        c.drawOval(x / 2 + 50, 50, x - 50, y / 2 - 50, p);

        // Cuadrante 2: drawRect
        c.drawRect(50, 50, x / 2 - 50, y / 2 - 50, p);

        // Cuadrante 3: drawRoundRect
        c.drawRoundRect(50, y / 2 + 50, x / 2 - 50, y - 50, 20, 20, p);

        // Cuadrante 4: drawArc
        c.drawArc(x / 2 + 50, y / 2 + 50, x - 50, y - 50, 0, 90, true, p);
    }
}