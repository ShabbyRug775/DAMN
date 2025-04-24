package com.example.fragmentos3;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements FragmentListado.GruposListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentListado fragmentListado = (FragmentListado) getSupportFragmentManager()
                .findFragmentById(R.id.FrgListado);

        if (fragmentListado != null) {
            fragmentListado.setGruposListener(this);
        }
    }

    @Override
    public void onGrupoSeleccionado(Grupo grupo) {
        if (grupo == null) return;

        boolean dualPane = getSupportFragmentManager()
                .findFragmentById(R.id.FrgDetalle) != null;

        if (dualPane) {
            // Modo tablet - mostrar en el fragmento existente
            FragmentDetalle fragmentDetalle = (FragmentDetalle) getSupportFragmentManager()
                    .findFragmentById(R.id.FrgDetalle);
            if (fragmentDetalle != null) {
                fragmentDetalle.mostrarDetalle(grupo.getTexto());
            }
        } else {
            // Modo teléfono - iniciar nueva actividad
            Intent intent = new Intent(this, DetalleActivity.class);
            intent.putExtra(DetalleActivity.EXTRA_TEXTO, grupo.getTexto());
            startActivity(intent);
        }
    }
}