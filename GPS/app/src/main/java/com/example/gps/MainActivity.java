package com.example.gps;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.maps.android.data.kml.KmlLayer;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        try {
            // Carga el archivo KML (asegúrate de tener location_data.kml en res/raw)
            KmlLayer kmlLayer = new KmlLayer(googleMap, R.raw.location_data, this);
            kmlLayer.addLayerToMap();

            // Marcador de ejemplo
            googleMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(-34, 151))
                            .title("Marcador de ejemplo")
            );

        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }


    // Métodos del ciclo de vida del MapView
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}