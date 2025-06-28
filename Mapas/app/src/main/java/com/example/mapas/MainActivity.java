package com.example.mapas;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private Button btnSatellite, btnCentrar, btnAnimar, btnMover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar botones
        btnSatellite = findViewById(R.id.btnSatellite);
        btnCentrar = findViewById(R.id.btnCentrar);
        btnAnimar = findViewById(R.id.btnAnimar);
        btnMover = findViewById(R.id.btnMover);

        // Configurar mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Configurar listeners de botones
        configurarBotones();
    }

    private void configurarBotones() {
        // Botón de vista satélite
        btnSatellite.setOnClickListener(v -> {
            if (mMap != null) {
                if (mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    btnSatellite.setText("Vista Satélite");
                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    btnSatellite.setText("Vista Normal");
                }
            }
        });

        // Botón para centrar en Sevilla
        btnCentrar.setOnClickListener(v -> {
            if (mMap != null) {
                LatLng sevilla = new LatLng(37.40, -5.99);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sevilla, 10));
                agregarMarcadorPersonalizado(sevilla);
            }
        });

        // Botón para animar zoom
        btnAnimar.setOnClickListener(v -> {
            if (mMap != null) {
                LatLng sevilla = new LatLng(37.40, -5.99);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sevilla, 10));
            }
        });

        // Botón para mover mapa (simulación de scrollBy)
        btnMover.setOnClickListener(v -> {
            if (mMap != null) {
                LatLng current = mMap.getCameraPosition().target;
                LatLng newPosition = new LatLng(current.latitude + 0.001, current.longitude + 0.001);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(newPosition));
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Configuración inicial del mapa
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapClickListener(this);

        // Añadir marcador inicial
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marcador en Sídney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));

        // Añadir overlay personalizado
        agregarOverlayPersonalizado();
    }

    private void agregarOverlayPersonalizado() {
        LatLng sevilla = new LatLng(37.40, -5.99);

        // Círculo azul
        mMap.addCircle(new CircleOptions()
                .center(sevilla)
                .radius(100) // metros
                .strokeColor(Color.BLUE)
                .strokeWidth(5));

        // Marcador personalizado
        BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        mMap.addMarker(new MarkerOptions()
                .position(sevilla)
                .title("Sevilla")
                .icon(icon));
    }

    private void agregarMarcadorPersonalizado(LatLng position) {
        mMap.addMarker(new MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)) // Marcador rojo
                .title("Marcador predeterminado")); // Opcional: añade un título
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // Mostrar coordenadas con Toast
        String msg = "Lat: " + latLng.latitude + " - Lon: " + latLng.longitude;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        // Añadir marcador en la posición clickeada
        mMap.addMarker(new MarkerOptions().position(latLng).title("Nuevo marcador"));
    }
}