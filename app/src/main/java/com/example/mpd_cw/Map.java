package com.example.mpd_cw;
//<!--Greg MacPherson S1509595-->
//
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends AppCompatActivity implements OnMapReadyCallback {
    String geo = "";
    String[] lats;
    int zoom;
    LatLng location;
    Map googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        geo = intent.getExtras().getString("geo");
        if (geo != null && !geo.isEmpty()) {
            lats = geo.split(" ");
            zoom = 14;
            Log.e("MyTag", "--");
            Log.d("MyTag", "--");
            Log.d("MyTag", "lats[0] " + lats[0]);
            Log.d("MyTag", "lats[1] " + lats[1]);
            Log.d("MyTag", "--");
        } else {
            Log.e("MyTag", "--");
            lats = new String[]{"55.953251", "-3.188267"};
            zoom = 6;
        }

        location = new LatLng(Double.parseDouble(lats[0]), Double.parseDouble(lats[1]));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (geo != null && !geo.isEmpty()) {
            googleMap.addMarker(new MarkerOptions().position(location)
                .title("Roadworks"));
        }
            googleMap.setTrafficEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        googleMap.setMapType(4);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,zoom));

    }
}








