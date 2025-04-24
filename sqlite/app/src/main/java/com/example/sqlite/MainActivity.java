package com.example.sqlite;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {
    EditText jetN, jetP;
    Button jbnA, jbnL, jbnE, jbnU;
    TextView jtvL;
    SQLiteDatabase sqld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jetN = findViewById(R.id.xetN);
        jetP = findViewById(R.id.xetP);
        jbnA = findViewById(R.id.xbnA);
        jbnL = findViewById(R.id.xbnL);
        jbnE = findViewById(R.id.xbnE);
        jbnU = findViewById(R.id.xbnU);
        jtvL = findViewById(R.id.xtvL);

        DbmsSQLiteHelper dsqlh = new DbmsSQLiteHelper(this, "DBUsuarios", null, 1);
        sqld = dsqlh.getWritableDatabase();

        jbnA.setOnClickListener(v -> agregarUsuario());
        jbnL.setOnClickListener(v -> consultarUsuarios());
        jbnE.setOnClickListener(v -> eliminarUsuario());
        jbnU.setOnClickListener(v -> actualizarUsuario());
    }

    private void agregarUsuario() {
        String nombre = jetN.getText().toString();
        String password = jetP.getText().toString();

        if (nombre.isEmpty() || password.isEmpty()) {
            mostrarMensaje("Nombre y contraseña son requeridos");
            return;
        }

        String passwordCifrada = cifrarCesar13(password);

        ContentValues cv = new ContentValues();
        cv.put("nombre", nombre);
        cv.put("password", passwordCifrada);

        long result = sqld.insert("Usuarios", null, cv);
        if (result != -1) {
            mostrarMensaje("Usuario agregado");
            limpiarCampos();
        } else {
            mostrarMensaje("Error al agregar usuario");
        }
    }

    private void consultarUsuarios() {
        Cursor c = sqld.rawQuery("SELECT nombre, password FROM Usuarios", null);
        jtvL.setText("");

        if (c.moveToFirst()) {
            do {
                String nombre = c.getString(0);
                String passwordCifrada = c.getString(1);
                String passwordDescifrada = descifrarCesar13(passwordCifrada);

                jtvL.append("Usuario: " + nombre + "\n" +
                        "Contraseña (cifrada): " + passwordCifrada + "\n" +
                        "Contraseña (original): " + passwordDescifrada + "\n\n");
            } while (c.moveToNext());
        } else {
            jtvL.setText("No hay usuarios registrados");
        }
        c.close();
    }

    private void eliminarUsuario() {
        String nombre = jetN.getText().toString();

        if (nombre.isEmpty()) {
            mostrarMensaje("Ingrese el nombre del usuario a eliminar");
            return;
        }

        int count = sqld.delete("Usuarios", "nombre=?", new String[]{nombre});
        if (count > 0) {
            mostrarMensaje("Usuario eliminado");
            limpiarCampos();
        } else {
            mostrarMensaje("No se encontró el usuario");
        }
    }

    private void actualizarUsuario() {
        String nombre = jetN.getText().toString();
        String password = jetP.getText().toString();

        if (nombre.isEmpty() || password.isEmpty()) {
            mostrarMensaje("Nombre y nueva contraseña son requeridos");
            return;
        }

        String passwordCifrada = cifrarCesar13(password);
        ContentValues cv = new ContentValues();
        cv.put("password", passwordCifrada);

        int count = sqld.update("Usuarios", cv, "nombre=?", new String[]{nombre});
        if (count > 0) {
            mostrarMensaje("Contraseña actualizada");
            limpiarCampos();
        } else {
            mostrarMensaje("No se encontró el usuario");
        }
    }

    // Método para cifrar con Cesar13
    private String cifrarCesar13(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if       (c >= 'a' && c <= 'm') c += 13;
            else if  (c >= 'A' && c <= 'M') c += 13;
            else if  (c >= 'n' && c <= 'z') c -= 13;
            else if  (c >= 'N' && c <= 'Z') c -= 13;
            result.append(c);
        }
        return result.toString();
    }

    // Método para descifrar con Cesar13 (es el mismo que cifrar)
    private String descifrarCesar13(String input) {
        return cifrarCesar13(input); // Cesar13 es reversible con el mismo método
    }

    private void limpiarCampos() {
        jetN.setText("");
        jetP.setText("");
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sqld != null) {
            sqld.close();
        }
    }
}