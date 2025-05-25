package com.example.lampvibrasincronia;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    private boolean encendida = false;
    private Camera camera;
    private Button button;
    private Vibrator vibrator;
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.LED);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Verificar si el dispositivo tiene cámara con flash
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            Toast.makeText(this, "Este dispositivo no tiene flash", Toast.LENGTH_LONG).show();
            button.setEnabled(false);
            return;
        }

        // Verificar y solicitar permiso de cámara
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            initializeCamera();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFlashlight();
                vibrateMorse("SOS"); // Vibra en código Morse
            }
        });
    }

    private void initializeCamera() {
        try {
            releaseCamera(); // Libera primero si estaba abierta
            camera = Camera.open();

            // Configuración inicial obligatoria
            Parameters params = camera.getParameters();
            params.setFocusMode(Parameters.FOCUS_MODE_INFINITY);
            camera.setParameters(params);
        } catch (Exception e) {
            Log.e("Camera", "Error inicializando cámara: " + e.getMessage());
            camera = null;
        }
    }

    private void toggleFlashlight() {
        if (camera == null) {
            initializeCamera(); // Intenta reinicializar si es null
            if (camera == null) return;
        }

        try {
            Parameters params = camera.getParameters();
            if (encendida) {
                params.setFlashMode(Parameters.FLASH_MODE_OFF);
                camera.setParameters(params);
                camera.stopPreview();
                encendida = false;
                Log.i("Flash", "Linterna apagada");
            } else {
                // Configuración previa para evitar el error
                params.setFlashMode(Parameters.FLASH_MODE_TORCH);
                params.setFocusMode(Parameters.FOCUS_MODE_INFINITY); // Importante!
                camera.setParameters(params);
                camera.startPreview();
                encendida = true;
                Log.i("Flash", "Linterna encendida");
            }
        } catch (RuntimeException e) {
            Log.e("Flash Error", "Error al cambiar parámetros: " + e.getMessage());
            releaseCamera();
            Toast.makeText(this, "Error al controlar el flash", Toast.LENGTH_SHORT).show();
        }
    }

    private void vibrateMorse(String message) {
        if (vibrator == null || !vibrator.hasVibrator()) {
            Toast.makeText(this, "Este dispositivo no tiene vibrador", Toast.LENGTH_SHORT).show();
            return;
        }

        long[] pattern = MorseCodeConverter.pattern(message);
        vibrator.vibrate(pattern, -1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeCamera();
            } else {
                Toast.makeText(this, "Se necesita permiso de la cámara para usar el flash", Toast.LENGTH_LONG).show();
                button.setEnabled(false);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
            encendida = false;
        }
    }

    // AÑADE ESTA CLASE INTERNA AL FINAL DEL ARCHIVO
    private static class MorseCodeConverter {
        private static final long SPEED_BASE = 100;
        public static final long DOT = SPEED_BASE;
        public static final long DASH = SPEED_BASE * 3;
        public static final long GAP = SPEED_BASE;
        public static final long LETTER_GAP = SPEED_BASE * 3;
        public static final long WORD_GAP = SPEED_BASE * 7;

        public static long[] pattern(String message) {
            // Implementación básica para SOS (... --- ...)
            return new long[] {
                    0, // Inicio inmediato
                    DOT, GAP, DOT, GAP, DOT, // S (...)
                    LETTER_GAP,
                    DASH, GAP, DASH, GAP, DASH, // O (---)
                    LETTER_GAP,
                    DOT, GAP, DOT, GAP, DOT // S (...)
            };
        }
    }
}