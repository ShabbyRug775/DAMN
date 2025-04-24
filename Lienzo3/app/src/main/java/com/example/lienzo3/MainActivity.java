package com.example.lienzo3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    private MiLienzo lienzo;
    private Button botonModo;
    private Button botonPintarBorrarTemporal; // Botón combinado
    private Button botonCambiarColor; // Botón para cambiar el color del pincel
    private boolean modoBorrar = false;
    private boolean borradoTemporal = false; // Estado para el borrado temporal

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        // Crear un RelativeLayout como contenedor principal
        RelativeLayout relativeLayout = new RelativeLayout(this);

        // Crear MiLienzo y agregarlo al RelativeLayout
        lienzo = new MiLienzo(this);
        RelativeLayout.LayoutParams paramsLienzo = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        relativeLayout.addView(lienzo, paramsLienzo);

        // Crear el botón de modo y agregarlo al RelativeLayout
        botonModo = new Button(this);
        botonModo.setText("Borrar"); // Texto inicial del botón

        RelativeLayout.LayoutParams paramsBotonModo = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        paramsBotonModo.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM); // Posicionar en la parte inferior
        paramsBotonModo.addRule(RelativeLayout.CENTER_HORIZONTAL);   // Centrar horizontalmente
        paramsBotonModo.bottomMargin = 50; // Margen inferior

        relativeLayout.addView(botonModo, paramsBotonModo);

        // Crear el botón combinado de pintar/borrar temporal y agregarlo al RelativeLayout
        botonPintarBorrarTemporal = new Button(this);
        botonPintarBorrarTemporal.setText("Borrar Temporal"); // Texto inicial

        RelativeLayout.LayoutParams paramsBotonPintarBorrarTemporal = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        paramsBotonPintarBorrarTemporal.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM); // Posicionar en la parte inferior
        paramsBotonPintarBorrarTemporal.addRule(RelativeLayout.CENTER_HORIZONTAL);   // Centrar horizontalmente
        paramsBotonPintarBorrarTemporal.bottomMargin = 150; // Margen inferior mayor para separar los botones

        relativeLayout.addView(botonPintarBorrarTemporal, paramsBotonPintarBorrarTemporal);

        // Crear el botón para cambiar el color del pincel y agregarlo al RelativeLayout
        botonCambiarColor = new Button(this);
        botonCambiarColor.setText("Cambiar Color");

        RelativeLayout.LayoutParams paramsBotonCambiarColor = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        paramsBotonCambiarColor.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM); // Posicionar en la parte inferior
        paramsBotonCambiarColor.addRule(RelativeLayout.CENTER_HORIZONTAL);   // Centrar horizontalmente
        paramsBotonCambiarColor.bottomMargin = 250; // Margen inferior mayor para separar los botones

        relativeLayout.addView(botonCambiarColor, paramsBotonCambiarColor);

        // Configurar el clic del botón de modo
        botonModo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modoBorrar = !modoBorrar; // Cambiar el modo
                lienzo.setModoBorrar(modoBorrar); // Actualizar el modo en MiLienzo

                if (modoBorrar) {
                    botonModo.setText("Pintar"); // Cambiar texto del botón
                } else {
                    botonModo.setText("Borrar");
                }

                // Reiniciar los Paths al cambiar de modo
                lienzo.reiniciarPaths();
            }
        });

        // Configurar el clic del botón combinado de pintar/borrar temporal
        botonPintarBorrarTemporal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borradoTemporal = !borradoTemporal; // Alternar entre borrado temporal y pintar
                lienzo.setBorradoTemporal(borradoTemporal); // Actualizar el estado en MiLienzo

                if (borradoTemporal) {
                    botonPintarBorrarTemporal.setText("Pintar"); // Cambiar texto del botón
                } else {
                    botonPintarBorrarTemporal.setText("Borrar Temporal");
                }
            }
        });

        // Configurar el clic del botón para cambiar el color del pincel
        botonCambiarColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lienzo.cambiarColorPincel(); // Cambiar el color del pincel
            }
        });

        // Establecer el RelativeLayout como la vista principal
        setContentView(relativeLayout);
    }
}