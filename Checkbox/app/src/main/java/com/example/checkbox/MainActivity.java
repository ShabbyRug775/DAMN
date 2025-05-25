package com.example.checkbox;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {
    private CheckBox jchb;
    private TextView tv;
    private Button bn;
    private String s = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jchb = findViewById(R.id.xchotro);
        tv = findViewById(R.id.xetotro);
        bn = findViewById(R.id.xbn);

        // Configurar el listener del CheckBox
        jchb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Mostrar u ocultar el TextView según el estado del CheckBox
                tv.setVisibility(isChecked ? View.VISIBLE : View.GONE);

                // Si el CheckBox no está marcado, limpiar el TextView
                if (!isChecked) {
                    tv.setText("");
                }
            }
        });

        // Configurar el listener del botón
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcion(v);
            }
        });
    }

    public void opcion(View v) {
        // Agregar el texto del CheckBox a la cadena
        s += jchb.getText().toString() + " ";

        // Mostrar Toast con la información
        String textoTV = tv.getText().toString();
        if (!textoTV.isEmpty()) {
            Toast.makeText(this, "Otro: " + textoTV, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Seleccionado: " + jchb.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }
}