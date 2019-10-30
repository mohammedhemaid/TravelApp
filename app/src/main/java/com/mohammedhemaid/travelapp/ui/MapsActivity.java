package com.mohammedhemaid.travelapp.ui;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mohammedhemaid.travelapp.R;
import com.mohammedhemaid.travelapp.util.PermissionUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

@EActivity(R.layout.activity_maps)
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private static final String MY_LOCATION = "mylocation";
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Marker marker;

    private Boolean mLocationPermissionsGranted = false;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    public static final float DEFAULT_ZOOM = 15f;

    @Extra
    String mLatLng;

    @AfterViews
    public void after() {

        getLocationPermission();
    }

    @Click(R.id.mSave_button)
    public void saveLocation() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("mLatLngText", mLatLng);

        setResult(Activity.RESULT_OK, resultIntent);

        finish();

    }

    private void getDeviceLocation() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionsGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, MY_LOCATION);
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
        }
    }


    private void getLocationPermission() {
        if (!PermissionUtil.hasPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            mLocationPermissionsGranted = true;

            initMap();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;


        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: permission failed");
                        mLocationPermissionsGranted = false;

                        return;
                    }
                }
                Log.d(TAG, "onRequestPermissionsResult: permission granted");
                mLocationPermissionsGranted = true;

                //initialize our map
                initMap();
            }
        }

    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving camera to: lat:" + latLng.latitude + ", lng:" + latLng.latitude);
        mLatLng = latLng.latitude+ "," + latLng.longitude;

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        if (!MY_LOCATION.equals(title)) {

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(title);

            if (marker != null) {
                marker.remove();
            }
            marker = mMap.addMarker(markerOptions);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mLocationPermissionsGranted) {

            if (mLatLng != null && !mLatLng.isEmpty() ) {
                String[] latLng = mLatLng.split(",");
                double latitude = Double.parseDouble(latLng[0]);
                double longitude = Double.parseDouble(latLng[1]);
                moveCamera(new LatLng(latitude, longitude), DEFAULT_ZOOM, null);
            } else {

                getDeviceLocation();
                mMap.setMyLocationEnabled(true);

            }
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                moveCamera(latLng, DEFAULT_ZOOM, null);

            }
        });
    }
}
