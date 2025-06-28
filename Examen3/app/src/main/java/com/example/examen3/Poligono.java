package com.example.examen3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Poligono extends View {
    private List<PointF> vertices = new ArrayList<>();
    private Paint paint;
    private Paint textPaint;
    private Paint pointPaint;

    public Poligono(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4f);
        paint.setAntiAlias(true);

        pointPaint = new Paint();
        pointPaint.setColor(Color.RED);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(24f);
        textPaint.setAntiAlias(true);
    }

    public void setVertices(List<PointF> vertices) {
        this.vertices = new ArrayList<>(vertices);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (vertices.size() < 2) return;

        // Calcular factores de escala y desplazamiento
        float minX = Float.MAX_VALUE, maxX = Float.MIN_VALUE;
        float minY = Float.MAX_VALUE, maxY = Float.MIN_VALUE;

        for (PointF p : vertices) {
            minX = Math.min(minX, p.x);
            maxX = Math.max(maxX, p.x);
            minY = Math.min(minY, p.y);
            maxY = Math.max(maxY, p.y);
        }

        float rangeX = maxX - minX;
        float rangeY = maxY - minY;
        float padding = 50f;

        float scaleX = (getWidth() - 2 * padding) / (rangeX != 0 ? rangeX : 1);
        float scaleY = (getHeight() - 2 * padding) / (rangeY != 0 ? rangeY : 1);
        float scale = Math.min(scaleX, scaleY);

        // Dibujar ejes
        drawAxes(canvas, minX, maxX, minY, maxY, scale, padding);

        // Dibujar polígono
        Path path = new Path();
        PointF first = scalePoint(vertices.get(0), minX, maxX, minY, maxY, scale, padding);
        PointF prev = first;
        path.moveTo(first.x, first.y);

        for (int i = 1; i < vertices.size(); i++) {
            PointF current = scalePoint(vertices.get(i), minX, maxX, minY, maxY, scale, padding);
            path.lineTo(current.x, current.y);

            // Dibujar línea
            canvas.drawLine(prev.x, prev.y, current.x, current.y, paint);
            prev = current;
        }

        // Cerrar polígono
        canvas.drawLine(prev.x, prev.y, first.x, first.y, paint);

        // Dibujar vértices y etiquetas
        for (int i = 0; i < vertices.size(); i++) {
            PointF p = scalePoint(vertices.get(i), minX, maxX, minY, maxY, scale, padding);
            canvas.drawCircle(p.x, p.y, 8, pointPaint);
            canvas.drawText("V" + (i + 1), p.x + 10, p.y - 10, textPaint);
        }
    }

    private PointF scalePoint(PointF original, float minX, float maxX, float minY, float maxY, float scale, float padding) {
        float x = padding + (original.x - minX) * scale;
        float y = getHeight() - padding - (original.y - minY) * scale;
        return new PointF(x, y);
    }

    private void drawAxes(Canvas canvas, float minX, float maxX, float minY, float maxY, float scale, float padding) {
        Paint axisPaint = new Paint();
        axisPaint.setColor(Color.GRAY);
        axisPaint.setStrokeWidth(2f);

        // Eje X
        float originY = getHeight() - padding - (0 - minY) * scale;
        if (originY < padding) originY = padding;
        if (originY > getHeight() - padding) originY = getHeight() - padding;

        canvas.drawLine(padding, originY, getWidth() - padding, originY, axisPaint);

        // Eje Y
        float originX = padding + (0 - minX) * scale;
        if (originX < padding) originX = padding;
        if (originX > getWidth() - padding) originX = getWidth() - padding;

        canvas.drawLine(originX, padding, originX, getHeight() - padding, axisPaint);

        // Flechas
        canvas.drawLine(getWidth() - padding, originY, getWidth() - padding - 15, originY - 10, axisPaint);
        canvas.drawLine(getWidth() - padding, originY, getWidth() - padding - 15, originY + 10, axisPaint);
        canvas.drawLine(originX, padding, originX - 10, padding + 15, axisPaint);
        canvas.drawLine(originX, padding, originX + 10, padding + 15, axisPaint);

        // Etiquetas
        canvas.drawText("X", getWidth() - padding - 20, originY - 15, textPaint);
        canvas.drawText("Y", originX + 15, padding + 20, textPaint);
    }
}