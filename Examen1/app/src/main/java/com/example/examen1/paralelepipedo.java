package com.example.examen1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class paralelepipedo extends View{

    private Objeto obj;
    private Paint paint;

    public paralelepipedo(Context context) {
        super(context);
        obj = new Objeto();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5); // Grosor de los puntos
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.rgb(210, 255, 220));
        int width = getWidth();
        int height = getHeight();
        int minMaxXY = Math.min(width, height);

        obj.d = obj.rho * minMaxXY / obj.objSize;
        obj.eyeAndScreen();

        // Dibujar las 12 aristas del paralelepípedo
        // Cara inferior (0-1-2-3-0)
        drawLine(canvas, 0, 1);
        drawLine(canvas, 1, 2);
        drawLine(canvas, 2, 3);
        drawLine(canvas, 3, 0);

        // Cara superior (4-5-6-7-4)
        drawLine(canvas, 4, 5);
        drawLine(canvas, 5, 6);
        drawLine(canvas, 6, 7);
        drawLine(canvas, 7, 4);

        // Aristas verticales
        drawLine(canvas, 0, 4);
        drawLine(canvas, 1, 5);
        drawLine(canvas, 2, 6);
        drawLine(canvas, 3, 7);
    }

    private void drawLine(Canvas canvas, int i, int j) {
        modelo2D p = obj.vScr[i];
        modelo2D q = obj.vScr[j];
        canvas.drawLine(iX(p.x), iY(p.y), iX(q.x), iY(q.y), paint);
    }

    private int iX(float x) {
        return Math.round(getWidth() / 2 + x);
    }

    private int iY(float y) { return Math.round(getHeight() / 2 - y); }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                obj.theta = (float) getWidth() / event.getX();
                obj.phi = (float) getHeight() / event.getY();
                obj.rho = (obj.phi / obj.theta) * getHeight();
                invalidate(); // Vuelve a dibujar la vista
                break;
        }
        return true;
    }

}
