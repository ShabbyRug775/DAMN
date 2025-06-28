package com.example.aflogical;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private final String MASTER_KEY = "123"; // Cambia esto por tu clave
    private int remainingAttempts = 3;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passwordInput = findViewById(R.id.passwordInput);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String enteredPassword = passwordInput.getText().toString();
            if (checkPassword(enteredPassword)) {
                Toast.makeText(MainActivity.this, "Acceso concedido", Toast.LENGTH_SHORT).show();
                startForensicTools();
            } else {
                remainingAttempts--;
                if (remainingAttempts > 0) {
                    Toast.makeText(MainActivity.this,
                            "Clave incorrecta. Intentos restantes: " + remainingAttempts,
                            Toast.LENGTH_SHORT).show();
                    passwordInput.setText("");
                } else {
                    Toast.makeText(MainActivity.this,
                            "Sistema bloqueado. Contacta al administrador.",
                            Toast.LENGTH_LONG).show();
                    loginButton.setEnabled(false);
                }
            }
        });
    }

    private boolean checkPassword(String enteredPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(enteredPassword.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString().equals(getMasterHash());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getMasterHash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(MASTER_KEY.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void startForensicTools() {
        Intent intent = new Intent(this, ForensicToolsActivity.class);
        startActivity(intent);
        finish();
    }
}