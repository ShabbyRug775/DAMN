package com.example.cuborotatorio;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class CuboView extends View {
    private Obj obj;
    private Paint paint;

    public CuboView(Context context) {
        super(context);
        obj = new Obj();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5); // Grosor de los puntos
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.CYAN);
        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;
        int minMaxXY = Math.min(width, height);

        obj.d = obj.rho * minMaxXY / obj.objSize;
        obj.eyeAndScreen();

        // Dibujar la esfera como una nube de puntos
        for (Point2D p : obj.vScr) {
            canvas.drawPoint(iX(p.x), iY(p.y), paint);
        }
    }

    private void drawLine(Canvas canvas, int i, int j) {
        Point2D p = obj.vScr[i];
        Point2D q = obj.vScr[j];
        canvas.drawLine(iX(p.x), iY(p.y), iX(q.x), iY(q.y), paint);
    }

    private int iX(float x) {
        return Math.round(getWidth() / 2 + x);
    }

    private int iY(float y) {
        return Math.round(getHeight() / 2 - y);
    }

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