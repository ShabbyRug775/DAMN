package com.example.pestaas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelechoView extends View {
    private static final int ITERATIONS = 8000;
    private Bitmap mBitmap;
    private final Paint mPaint = new Paint();
    private final Random mRandom = new Random();
    private boolean mIsDrawing;
    private ExecutorService mExecutor;

    public HelechoView(Context context) {
        super(context);
        init();
    }

    public HelechoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(1f);
        setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0 && !mIsDrawing) {
            startDrawing(w, h);
        }
    }

    private void startDrawing(int width, int height) {
        mIsDrawing = true;
        mExecutor = Executors.newSingleThreadExecutor();
        mExecutor.execute(() -> {
            try {
                // Limitar tamaño máximo para evitar OOM
                int scaledWidth = Math.min(width, 1024);
                int scaledHeight = Math.min(height, 1024);

                mBitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(mBitmap);
                canvas.drawColor(Color.BLACK);

                double x = 0, y = 0;
                for (int i = 0; i < ITERATIONS && !Thread.interrupted(); i++) {
                    double r = mRandom.nextDouble();
                    double nx, ny;

                    if (r < 0.01) {
                        nx = 0;
                        ny = 0.16 * y;
                    } else if (r < 0.86) {
                        nx = 0.85 * x + 0.04 * y;
                        ny = -0.04 * x + 0.85 * y + 1.6;
                    } else if (r < 0.93) {
                        nx = 0.2 * x - 0.26 * y;
                        ny = 0.23 * x + 0.22 * y + 1.6;
                    } else {
                        nx = -0.15 * x + 0.28 * y;
                        ny = 0.26 * x + 0.24 * y + 0.44;
                    }

                    int px = (int)(nx * scaledWidth / 11 + scaledWidth / 2);
                    int py = (int)(scaledHeight - ny * scaledHeight / 11);

                    if (px >= 0 && px < scaledWidth && py >= 0 && py < scaledHeight) {
                        mBitmap.setPixel(px, py, Color.GREEN);
                    }

                    x = nx;
                    y = ny;

                    if (i % 100 == 0) {
                        postInvalidate();
                    }
                }
            } catch (Exception e) {
                Log.e("HelechoView", "Error al dibujar", e);
            } finally {
                post(() -> {
                    mIsDrawing = false;
                    invalidate();
                });
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap != null) {
            Rect src = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
            Rect dst = new Rect(0, 0, getWidth(), getHeight());
            canvas.drawBitmap(mBitmap, src, dst, mPaint);
        } else {
            // Mostrar mensaje mientras carga
            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(40);
            canvas.drawText("Generando helecho...", 50, getHeight()/2, textPaint);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mExecutor != null) {
            mExecutor.shutdownNow();
        }
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
        super.onDetachedFromWindow();
    }
}