package com.example.preferencias;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import java.io.File;

public class ImageCanvasView extends View {
    private Bitmap bitmap;
    private Paint paint;

    public ImageCanvasView(Context context) {
        super(context);
        init();
    }

    public ImageCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageCanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
    }

    public void setImagePath(String imagePath) {
        if (imagePath != null) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                invalidate();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) {
            // Center the image on canvas
            float x = (getWidth() - bitmap.getWidth()) / 2f;
            float y = (getHeight() - bitmap.getHeight()) / 2f;
            canvas.drawBitmap(bitmap, x, y, paint);
        }
    }
}