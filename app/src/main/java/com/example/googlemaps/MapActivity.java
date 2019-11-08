package com.example.googlemaps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;


public class MapActivity extends AppCompatActivity
{
    SupportMapFragment fragment;
    FusedLocationProviderClient client;
    Location lastlocation;
    double latitude,longitude;
    boolean flag=false;
    GoogleMap gmap;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        fragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        client= LocationServices.getFusedLocationProviderClient(this);
        checkmapspermission();
    }

    private void checkmapspermission()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else
        {
            Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
            flag=true;
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location!=null)
                    {
                        lastlocation=location;
                        latitude=lastlocation.getLatitude();
                        longitude=lastlocation.getLongitude();
                        initMap();
                    }
                }
            });
        }

    }

    private void initMap() {
        Toast.makeText(this, "Intialize Map", Toast.LENGTH_SHORT).show();
        fragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gmap=googleMap;
                if (flag)
                {
                    LatLng mylatlng=new LatLng(latitude,longitude);
                    gmap.moveCamera(CameraUpdateFactory.newLatLng(mylatlng));
                    gmap.setMyLocationEnabled(true);
                    MarkerOptions markerOptions=new MarkerOptions();
                    markerOptions.position(mylatlng);
                    markerOptions.title("you are at");
                    gmap.addMarker(markerOptions);
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1)
        {
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                checkmapspermission();
            }
            else
            {
                Toast.makeText(this, "Location Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
