package com.example.archivos;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends Activity {
    EditText jetNombre;
    TextView txtMD5, txtResultado;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        jetNombre = findViewById(R.id.xetNombre);
        txtMD5 = findViewById(R.id.txtMD5);
        txtResultado = findViewById(R.id.txtResultado);
    }

    public void guardar(View v) {
        String nombre = jetNombre.getText().toString().trim();

        if(nombre.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa tu nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Leer el himno del archivo existente
            InputStream is = getResources().openRawResource(R.raw.himno); // O usar openFileInput si está en almacenamiento interno
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder himnoBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                himnoBuilder.append(line).append("\n");
            }
            reader.close();

            String himno = himnoBuilder.toString();
            String contenidoCompleto = "Nombre: " + nombre + "\nHimno: " + himno;

            // Mostrar el contenido combinado
            txtResultado.setText(contenidoCompleto);

            // Generar y mostrar MD5
            String md5 = generarMD5(contenidoCompleto);
            txtMD5.setText("MD5: " + md5);

            // Guardar el resultado combinado en un nuevo archivo
            FileOutputStream fos = openFileOutput("usuario_himno.txt", MODE_PRIVATE);
            fos.write(contenidoCompleto.getBytes());
            fos.close();

            Toast.makeText(this, "MD5 generado correctamente", Toast.LENGTH_SHORT).show();

        } catch (IOException ex) {
            ex.printStackTrace();
            Toast.makeText(this, "Error al leer el himno", Toast.LENGTH_SHORT).show();
        }
    }

    private String generarMD5(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes());
            byte[] messageDigest = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String h = Integer.toHexString(0xFF & b);
                if (h.length() == 1)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "Error al generar MD5";
        }
    }
}