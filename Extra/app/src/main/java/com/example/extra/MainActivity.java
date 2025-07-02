package com.example.extra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.SecureRandom; // Para generar claves aleatorias seguras

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_PASSWORD = "password";
    private static final String ALPHANUMERIC_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private EditText passwordInputEditText;
    private TextView generatedKeyTextView;
    private Button generateKeyButton;
    private Button validateSaveButton;

    private String savedPassword = null;
    private String currentGeneratedKey = null; // Para almacenar la clave generada temporalmente

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passwordInputEditText = findViewById(R.id.edit_text_password_input);
        generatedKeyTextView = findViewById(R.id.text_view_generated_key);
        generateKeyButton = findViewById(R.id.button_generate_key);
        validateSaveButton = findViewById(R.id.button_validate_save);

        // Cargar la clave guardada (si existe)
        loadPassword();

        // Configurar el estado inicial de los botones
        updateUIState();

        // Listener para el botón "Generar Nueva Clave"
        generateKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentGeneratedKey = generateRandomKey(3);
                generatedKeyTextView.setText(currentGeneratedKey);
                generatedKeyTextView.setVisibility(View.VISIBLE); // Mostrar la clave generada
                passwordInputEditText.setText(""); // Limpiar el campo de entrada
                passwordInputEditText.setHint("Confirme la clave generada"); // Cambiar el hint
                updateUIState(); // Actualizar el estado de los botones
                Toast.makeText(MainActivity.this, "Nueva clave generada. Ingrésala para guardar.", Toast.LENGTH_LONG).show();
            }
        });

        // Listener para el botón "Validar/Guardar Clave"
        validateSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPassword = passwordInputEditText.getText().toString();

                if (enteredPassword.length() != 3) {
                    Toast.makeText(MainActivity.this, "La clave debe ser de 3 caracteres.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currentGeneratedKey != null) {
                    // Si hay una clave generada pendiente de guardar
                    if (enteredPassword.equals(currentGeneratedKey)) {
                        savePassword(currentGeneratedKey);
                        Toast.makeText(MainActivity.this, "Clave guardada exitosamente.", Toast.LENGTH_SHORT).show();
                        currentGeneratedKey = null; // Limpiar la clave generada
                        generatedKeyTextView.setVisibility(View.GONE); // Ocultar la clave generada
                        passwordInputEditText.setText(""); // Limpiar el campo
                        passwordInputEditText.setHint("Ingrese la clave"); // Restaurar el hint
                        updateUIState(); // Actualizar el estado de los botones

                        // NAVEGAR A IMAGEACTIVITY DESPUÉS DE GUARDAR UNA CLAVE GENERADA
                        Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                        startActivity(intent);
                        // finish(); // Opcional: finalizar esta actividad si no quieres que el usuario regrese
                        // usando el botón "atrás" del dispositivo

                    } else {
                        Toast.makeText(MainActivity.this, "La clave ingresada no coincide con la generada.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Si no hay clave generada, estamos en modo de validación normal
                    if (savedPassword == null) {
                        Toast.makeText(MainActivity.this, "No hay clave guardada. Genera una primero.", Toast.LENGTH_SHORT).show();
                    } else if (enteredPassword.equals(savedPassword)) {
                        Toast.makeText(MainActivity.this, "Clave correcta. Acceso concedido.", Toast.LENGTH_SHORT).show();
                        // NAVEGAR A IMAGEACTIVITY DESPUÉS DE VALIDAR UNA CLAVE EXISTENTE
                        Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                        startActivity(intent);
                        // finish(); // Opcional: finalizar esta actividad
                    } else {
                        Toast.makeText(MainActivity.this, "Clave incorrecta. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Listener para el EditText para habilitar/deshabilitar el botón de validar/guardar
        passwordInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateUIState(); // Llama a este método para actualizar el estado del botón
            }
        });
    }

    private void loadPassword() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        savedPassword = settings.getString(KEY_PASSWORD, null);
    }

    private void savePassword(String password) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(KEY_PASSWORD, password);
        editor.apply(); // Usa apply() para guardar de forma asíncrona
        savedPassword = password; // Actualizar la clave en memoria
    }

    // Genera una clave alfanumérica aleatoria de la longitud especificada
    private String generateRandomKey(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUMERIC_CHARS.charAt(random.nextInt(ALPHANUMERIC_CHARS.length())));
        }
        return sb.toString();
    }

    // Actualiza el estado de los botones y hints según la lógica de la aplicación
    private void updateUIState() {
        boolean isPasswordInputValid = passwordInputEditText.getText().length() == 3;

        if (currentGeneratedKey != null) {
            // Estamos en el flujo de guardar una clave generada
            validateSaveButton.setText("Guardar Clave");
            validateSaveButton.setEnabled(isPasswordInputValid);
            generateKeyButton.setEnabled(false); // Deshabilitar generar mientras se guarda
        } else {
            // Estamos en el flujo de validar una clave existente o esperar una nueva generación
            if (savedPassword == null) {
                validateSaveButton.setText("Guardar Clave (1ra vez)"); // Opcional: indicar que es la primera vez
                validateSaveButton.setEnabled(isPasswordInputValid);
            } else {
                validateSaveButton.setText("Validar Clave");
                validateSaveButton.setEnabled(isPasswordInputValid);
            }
            generateKeyButton.setEnabled(true); // Habilitar generar
            generatedKeyTextView.setVisibility(View.GONE); // Asegurarse de que la clave generada esté oculta
            passwordInputEditText.setHint("Ingrese la clave"); // Restaurar el hint
        }
    }
}