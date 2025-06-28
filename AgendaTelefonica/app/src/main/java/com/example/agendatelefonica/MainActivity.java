package com.example.agendatelefonica;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private EditText jetcel, jetweb;
    private ImageButton jibcel, jibweb, jibcam;
    private static final int CALL_PHONE_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        jetcel = findViewById(R.id.xetcel);
        jetweb = findViewById(R.id.xetweb);
        jibcel = findViewById(R.id.xibcel);
        jibweb = findViewById(R.id.xibweb);
        jibcam = findViewById(R.id.xibcam);

        // Configurar listeners
        jibcel.setOnClickListener(v -> handleCallButtonClick());
        jibweb.setOnClickListener(v -> openWebsite());
        jibcam.setOnClickListener(v -> checkCameraPermission());
    }

    private void handleCallButtonClick() {
        String phoneNumber = jetcel.getText().toString().trim();

        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Ingrese un número telefónico", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            makePhoneCall(phoneNumber);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PHONE_REQUEST_CODE);
        }
    }

    private void makePhoneCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        }
    }

    private void openWebsite() {
        String url = jetweb.getText().toString().trim();

        if (url.isEmpty()) {
            Toast.makeText(this, "Ingrese una dirección web", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(webIntent);
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(cameraIntent);
        } else {
            Toast.makeText(this, "No se encontró una aplicación de cámara", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length == 0) return;

        switch (requestCode) {
            case CALL_PHONE_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    handleCallButtonClick();
                } else {
                    Toast.makeText(this, "Permiso denegado para realizar llamadas", Toast.LENGTH_LONG).show();
                }
                break;

            case CAMERA_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Permiso denegado para usar la cámara", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}