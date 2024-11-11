package com.example.feedhope.AppInterface;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.feedhope.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String PREFS_NAME = "LocationPrefs";
    public static final String KEY_LATITUDE = "currentLatitude";
    public static final String KEY_LONGITUDE = "currentLongitude";
    public static final String KEY_LOCATION_NAME = "currentLocationName";

    private double currentLatitude;
    private double currentLongitude;
    private GoogleMap mMap;
    private GoogleMapDB dbHelper;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Geocoder geocoder;
    private LatLng lastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_map);

        dbHelper = new GoogleMapDB(this);
        geocoder = new Geocoder(this, Locale.getDefault());

        fusedLocationClient = new FusedLocationProviderClient(this);
        locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        Log.d("MapsActivity", "Current location: " + location.getLatitude() + ", " + location.getLongitude());
                        updateMapWithCurrentLocation(location);
                    } else {
                        Log.e("MapsActivity", "Location result is null.");
                    }
                }
            }
        };

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        requestLocationPermission();

        findViewById(R.id.btn).setOnClickListener(view -> {
            Intent intent = new Intent(GoogleMapActivity.this, MainActivity.class);
            intent.putExtra("latitude", currentLatitude);
            intent.putExtra("longitude", currentLongitude);
            startActivity(intent);
        });
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            }, 101);
        } else {
            startLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
                enableMyLocation(); // Enable My Location layer if permission is granted
            } else {
                Log.e("MapsActivity", "Location permission denied.");
            }
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    private void retrieveDataAndDisplayMarkers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(GoogleMapDB.TABLE_NAME,
                    new String[]{GoogleMapDB.COLUMN_NAME, GoogleMapDB.COLUMN_LATITUDE, GoogleMapDB.COLUMN_LONGITUDE},
                    null, null, null, null, null);

            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(GoogleMapDB.COLUMN_NAME));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(GoogleMapDB.COLUMN_LATITUDE));
                double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(GoogleMapDB.COLUMN_LONGITUDE));
                LatLng location = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(location).title(name));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    private void addMarker(MarkerOptions markerOptions) {
        if (mMap != null) {
            mMap.addMarker(markerOptions);
        }
    }


    private void updateMapWithCurrentLocation(Location location) {
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        if (lastKnownLocation == null || !lastKnownLocation.equals(currentLocation)) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            mMap.clear();

            String address = getAddressFromLocation(currentLocation);
            if (address != null) {
                if (storeLocationInDatabase(address, location.getLatitude(), location.getLongitude())) {
                    // Add a red marker for the current location
                    mMap.addMarker(new MarkerOptions()
                            .position(currentLocation)
                            .title(address)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))); // Set marker color to red

                    currentLatitude = location.getLatitude();
                    currentLongitude = location.getLongitude();
                    lastKnownLocation = currentLocation;

                    storeCurrentLocationInPreferences(currentLatitude, currentLongitude, address);
                }
            } else {
                Log.e("MapsActivity", "Failed to retrieve address for location.");
            }
        }
    }

    private void clear() {
        if (mMap != null) {
            mMap.clear();
        }
    }


    private void moveCamera(CameraUpdate cameraUpdate) {
        if (mMap != null) {
            mMap.moveCamera(cameraUpdate);
        }
    }


    private void storeCurrentLocationInPreferences(double latitude, double longitude, String locationName) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LOCATION_NAME, locationName); // Store location name
        editor.putFloat(KEY_LATITUDE, (float) latitude);   // Store latitude
        editor.putFloat(KEY_LONGITUDE, (float) longitude); // Store longitude
        editor.apply();
    }

    private String getAddressFromLocation(LatLng latLng) {
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            Log.e("MapsActivity", "Geocoder service failed", e);
        }
        return null;
    }

    private boolean storeLocationInDatabase(String name, double latitude, double longitude) {
        if (!locationExistsInDatabase(latitude, longitude)) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(GoogleMapDB.COLUMN_NAME, name);
            values.put(GoogleMapDB.COLUMN_LATITUDE, latitude);
            values.put(GoogleMapDB.COLUMN_LONGITUDE, longitude);
            db.insert(GoogleMapDB.TABLE_NAME, null, values);
            db.close();
            return true;
        }
        return false;
    }

    private boolean locationExistsInDatabase(double latitude, double longitude) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            String selection = GoogleMapDB.COLUMN_LATITUDE + " = ? AND " + GoogleMapDB.COLUMN_LONGITUDE + " = ?";
            String[] selectionArgs = {String.valueOf(latitude), String.valueOf(longitude)};
            cursor = db.query(GoogleMapDB.TABLE_NAME, null, selection, selectionArgs, null, null, null);
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private void setMyLocationEnabled(boolean enabled) {
        if (mMap != null && (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            mMap.setMyLocationEnabled(enabled);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        retrieveDataAndDisplayMarkers();
        enableMyLocation(); // Call this to enable My Location layer
        startLocationUpdates();
    }
}
