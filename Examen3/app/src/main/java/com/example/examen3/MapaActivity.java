package com.example.examen3;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private String nombreCiudad;
    private double latitud;
    private double longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        // Obtener datos de la ciudad seleccionada
        nombreCiudad = getIntent().getStringExtra("nombre");
        latitud = getIntent().getDoubleExtra("latitud", 0);
        longitud = getIntent().getDoubleExtra("longitud", 0);

        // Obtener el SupportMapFragment y notificar cuando el mapa esté listo
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Crear objeto LatLng con las coordenadas de la ciudad
        LatLng ubicacionCiudad = new LatLng(latitud, longitud);

        // Añadir marcador en la ubicación y mover la cámara
        mMap.addMarker(new MarkerOptions()
                .position(ubicacionCiudad)
                .title(nombreCiudad));

        // Mover la cámara al marcador con zoom apropiado
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionCiudad, 12));
    }
}