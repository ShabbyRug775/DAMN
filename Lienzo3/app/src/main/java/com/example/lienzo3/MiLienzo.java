package com.example.lienzo3;

import android.content.Context;
import android.graphics.*;
import android.view.*;

public class MiLienzo extends View {
    float x = 50, y = 50;
    String s = "";
    Path paPintar = new Path(); // Path para pintar
    Path paBorrar = new Path(); // Path para borrar temporal
    boolean modoBorrar = false;
    boolean borradoTemporal = false; // Estado para el borrado temporal
    int[] colores = {Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW}; // Lista de colores
    int indiceColor = 0; // Índice del color actual
    Paint pincel = new Paint(); // Pincel para pintar

    public MiLienzo(Context c) {
        super(c);
        configurarPincel(); // Configurar el pincel inicial
    }

    private void configurarPincel() {
        pincel.setStyle(Paint.Style.STROKE);
        pincel.setStrokeWidth(6);
        pincel.setColor(colores[indiceColor]); // Color inicial
    }

    @Override
    protected void onDraw(Canvas c) {
        c.drawColor(Color.rgb(210, 250, 230)); // Fondo

        // Dibujar el Path de pintar
        c.drawPath(paPintar, pincel);

        // Dibujar el Path de borrar temporal
        Paint pBorrar = new Paint();
        pBorrar.setStyle(Paint.Style.STROKE);
        pBorrar.setStrokeWidth(6);
        pBorrar.setColor(Color.rgb(210, 250, 230)); // Color para borrar (mismo que el fondo)
        c.drawPath(paBorrar, pBorrar);

        // Dibujar la línea horizontal
        pBorrar.setColor(Color.BLACK);
        c.drawLine(0, this.getHeight() / 2, this.getWidth(), this.getHeight() / 2, pBorrar);
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        x = me.getX();
        y = me.getY();

        if (me.getAction() == MotionEvent.ACTION_DOWN) s = "abajo";
        if (me.getAction() == MotionEvent.ACTION_MOVE) s = "mover";

        if (borradoTemporal) {
            // Modo borrar temporal: usar paBorrar
            if (s.equals("abajo")) paBorrar.moveTo(x, y);
            if (s.equals("mover")) paBorrar.lineTo(x, y);
        } else if (modoBorrar) {
            // Modo borrar: usar paBorrar
            if (s.equals("abajo")) paBorrar.moveTo(x, y);
            if (s.equals("mover")) paBorrar.lineTo(x, y);
        } else {
            // Modo pintar: usar paPintar
            if (s.equals("abajo")) paPintar.moveTo(x, y);
            if (s.equals("mover")) paPintar.lineTo(x, y);
        }

        invalidate();
        return true;
    }

    public void setModoBorrar(boolean modoBorrar) {
        this.modoBorrar = modoBorrar;
        invalidate(); // Redibujar la vista
    }

    public void setBorradoTemporal(boolean borradoTemporal) {
        this.borradoTemporal = borradoTemporal;
        invalidate(); // Redibujar la vista
    }

    public void cambiarColorPincel() {
        indiceColor = (indiceColor + 1) % colores.length; // Ciclar entre los colores
        pincel.setColor(colores[indiceColor]); // Actualizar el color del pincel
        invalidate(); // Redibujar la vista
    }

    // Método para reiniciar los Paths
    public void reiniciarPaths() {
        paPintar.reset();
        paBorrar.reset();
        invalidate(); // Redibujar la vista
    }
}