package com.example.morphing;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private Bitmap image1, image2, workingBitmap;
    private boolean showingImage1 = true;
    private boolean isProcessing = false;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        // Cargar imágenes en tamaño reducido para mejor rendimiento
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2; // Reduce el tamaño a la mitad

        image1 = BitmapFactory.decodeResource(getResources(), R.drawable.foto, options);
        image2 = BitmapFactory.decodeResource(getResources(), R.drawable.cafe, options);

        // Crear bitmap de trabajo
        workingBitmap = Bitmap.createBitmap(image1.getWidth(), image1.getHeight(), image1.getConfig());
        imageView.setImageBitmap(image1);

        imageView.setOnClickListener(v -> {
            if (!isProcessing) {
                startMorphing();
            }
        });
    }

    private void startMorphing() {
        if (isProcessing || image1 == null || image2 == null) return;
        isProcessing = true;

        new Thread(() -> {
            try {
                // Asegurar mismo tamaño
                if (image1.getWidth() != image2.getWidth() ||
                        image1.getHeight() != image2.getHeight()) {
                    image2 = Bitmap.createScaledBitmap(image2, image1.getWidth(),
                            image1.getHeight(), true);
                }

                // Crear bitmap de trabajo con configuración óptima
                workingBitmap = Bitmap.createBitmap(image1.getWidth(), image1.getHeight(),
                        Bitmap.Config.ARGB_8888);

                // Obtener píxeles
                int width = image1.getWidth();
                int height = image1.getHeight();
                int[] pixels1 = new int[width * height];
                int[] pixels2 = new int[width * height];
                image1.getPixels(pixels1, 0, width, 0, 0, width, height);
                image2.getPixels(pixels2, 0, width, 0, 0, width, height);
                int[] resultPixels = new int[width * height];

                // Animación de morphing en 10 pasos
                for (int step = 0; step <= 10; step++) {
                    float ratio = step / 10f;

                    // Procesar TODOS los píxeles
                    for (int i = 0; i < resultPixels.length; i++) {
                        resultPixels[i] = blendPixels(pixels1[i], pixels2[i], ratio);
                    }

                    // Actualizar TODO el bitmap
                    workingBitmap.setPixels(resultPixels, 0, width, 0, 0, width, height);

                    runOnUiThread(() -> imageView.setImageBitmap(workingBitmap));
                    Thread.sleep(50);
                }

                // Intercambio final
                Bitmap temp = image1;
                image1 = image2;
                image2 = temp;

            } catch (Exception e) {
                Log.e("Morphing", "Error: " + e.getMessage());
            } finally {
                isProcessing = false;
            }
        }).start();
    }

    // Metodo auxiliar para mezclar píxeles
    private int blendPixels(int pixel1, int pixel2, float ratio) {
        float inverseRatio = 1f - ratio;
        return Color.argb(
                (int)(Color.alpha(pixel1) * inverseRatio + Color.alpha(pixel2) * ratio),
                (int)(Color.red(pixel1) * inverseRatio + Color.red(pixel2) * ratio),
                (int)(Color.green(pixel1) * inverseRatio + Color.green(pixel2) * ratio),
                (int)(Color.blue(pixel1) * inverseRatio + Color.blue(pixel2) * ratio)
        );
    }
}