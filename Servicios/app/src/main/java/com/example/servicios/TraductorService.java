package com.example.servicios;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

public class TraductorService extends Service {
    private static MainActivity mainActivity;
    private Handler handler;

    public static void setMainActivity(MainActivity activity) {
        mainActivity = activity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (mainActivity != null && msg.obj != null) {
                    mainActivity.actualizarResultado(msg.obj.toString());
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("mensaje_cifrado")) {
            String mensajeCifrado = intent.getStringExtra("mensaje_cifrado");
            String mensajeDescifrado = descifrarCesar2(mensajeCifrado);

            Message msg = handler.obtainMessage();
            msg.obj = "Texto original: " + mensajeDescifrado;
            handler.sendMessage(msg);
        }
        return START_NOT_STICKY;
    }

    private String descifrarCesar2(String mensajeCifrado) {
        StringBuilder resultado = new StringBuilder();

        for (int i = 0; i < mensajeCifrado.length(); i++) {
            char c = mensajeCifrado.charAt(i);

            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                c = (char)(((c - base - 2 + 26) % 26) + base);
            }

            resultado.append(c);
        }

        return resultado.toString();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}