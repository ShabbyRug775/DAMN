package com.example.lienzo2;

import android.content.Context;
import android.graphics.*;  // Canvas, Color, Paint, Path;
import android.view.*;      // MotionEvent, View;

// Clase personalizada que extiende de View para dibujar en la pantalla
class MiLienzo extends View {
    // Variables para almacenar las coordenadas x, y del toque
    float x = 50, y = 50;
    // Variable para almacenar el estado del toque (abajo o mover)
    String s = "";
    // Objeto Path para almacenar el trazo dibujado
    Path pa = new Path();

    // Constructor de la clase MiLienzo
    public MiLienzo(Context c) {
        super(c);
    }

    // Metodo que se llama para dibujar en el lienzo
    @Override
    protected void onDraw(Canvas c) {
        // Establece el color de fondo del lienzo (gris claro)
        c.drawColor(Color.rgb(200, 200, 200));

        // Crea un objeto Paint para definir el estilo de dibujo
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);  // Estilo de trazo (sin relleno)
        p.setStrokeWidth(6);             // Grosor del trazo
        p.setColor(Color.BLACK);         // Color del trazo (negro)

        // Si el estado es "abajo", mueve el punto inicial del trazo
        if (s == "abajo") pa.moveTo(x, y);
        // Si el estado es "mover", añade una línea al trazo
        if (s == "mover") pa.lineTo(x, y);

        // Dibuja el trazo almacenado en el objeto Path
        c.drawPath(pa, p);

        // Dibuja una línea horizontal en la mitad de la pantalla
        c.drawLine(0, this.getHeight() / 2, this.getWidth(), this.getHeight() / 2, p);
    }

    // Metodo que se llama cuando se detecta un evento táctil en la pantalla
    @Override
    public boolean onTouchEvent(MotionEvent me) {
        // Obtiene las coordenadas x, y del toque
        x = me.getX();
        y = me.getY();

        // Si el evento es ACTION_DOWN (toque inicial), establece el estado a "abajo"
        if (me.getAction() == MotionEvent.ACTION_DOWN) s = "abajo";
        // Si el evento es ACTION_MOVE (arrastre), establece el estado a "mover"
        if (me.getAction() == MotionEvent.ACTION_MOVE) s = "mover";

        // Fuerza la redibujar el lienzo llamando a onDraw()
        invalidate();

        // Retorna true para indicar que el evento táctil fue manejado
        return true;
    }
}