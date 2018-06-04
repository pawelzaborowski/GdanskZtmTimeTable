package si.uni_lj.student.pz8285.ztmtimetable.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import si.uni_lj.student.pz8285.ztmtimetable.R;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<HashMap<String, String>> stopsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stopsList = (ArrayList<HashMap<String, String>>) this.getIntent().getSerializableExtra("stopsList");
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String name;
        Double lat, lon;

        for (int i = 0; i < stopsList.size(); i++) {
            lat = Double.parseDouble(this.stopsList.get(i).get("stopLat"));
            lon = Double.parseDouble(this.stopsList.get(i).get("stopLon"));
            name = this.stopsList.get(i).get("stopDesc");
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(name));
            Log.i("lat", String.valueOf(lat));
            Log.i("lon", String.valueOf(lon));
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            Intent loc_permission = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(loc_permission);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(54, 18)));
    }
}
