package com.example.mytravelbuddy;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.common.ResolvableApiException;
import com.huawei.hms.location.FusedLocationProviderClient;
import com.huawei.hms.location.Geofence;
import com.huawei.hms.location.GeofenceService;
import com.huawei.hms.location.LocationCallback;
import com.huawei.hms.location.LocationRequest;
import com.huawei.hms.location.LocationResult;
import com.huawei.hms.location.LocationServices;
import com.huawei.hms.location.LocationSettingsRequest;
import com.huawei.hms.location.LocationSettingsResponse;
import com.huawei.hms.location.LocationSettingsStates;
import com.huawei.hms.location.LocationSettingsStatusCodes;
import com.huawei.hms.location.SettingsClient;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class LocationBroadcastReceiver extends AppCompatActivity {
// Dynamically apply for required permissions if the API level is 28 or lower.

    // Define a FusedLocationProviderClient object.
    private FusedLocationProviderClient fusedLocationProviderClient;
    // Define a location request object.
    private LocationRequest mLocationRequest;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.i(TAG, "android sdk <= 28 Q");
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] strings =
                        {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                ActivityCompat.requestPermissions(this, strings, 1);
            }
        } else {
            // Dynamically apply for the android.permission.ACCESS_BACKGROUND_LOCATION permission in addition to the preceding permissions if the API level is higher than 28.
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    "android.permission.ACCESS_BACKGROUND_LOCATION") != PackageManager.PERMISSION_GRANTED) {
                String[] strings = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        "android.permission.ACCESS_BACKGROUND_LOCATION"};
                ActivityCompat.requestPermissions(this, strings, 2);
            }
        }
        //to obtain SettingsClient instance
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        mLocationRequest = new LocationRequest();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check the device location settings.
        settingsClient.checkLocationSettings(locationSettingsRequest)
                // Define the listener for success in calling the API for checking device location settings.
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        LocationSettingsStates locationSettingsStates =
                                locationSettingsResponse.getLocationSettingsStates();
                        StringBuilder stringBuilder = new StringBuilder();
                        // Checks whether the location function is enabled.
                        stringBuilder.append(",\nisLocationUsable=")
                                .append(locationSettingsStates.isLocationUsable());
                        // Checks whether HMS Core (APK) is available.
                        stringBuilder.append(",\nisHMSLocationUsable=")
                                .append(locationSettingsStates.isHMSLocationUsable());
                        Log.i(TAG, "checkLocationSetting onComplete:" + stringBuilder.toString());
                    }
                })
                // Define callback for failure in checking the device location settings.
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Processing when the device is a Huawei device and has HMS Core (APK) installed, but its settings do not meet the location requirements.
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    // Call startResolutionForResult to display a popup asking the user to enable related permission.
                                    rae.startResolutionForResult(LocationBroadcastReceiver.this, 0);
                                } catch (IntentSender.SendIntentException sie) {
                                    // TODO
                                }
                                break;
                        }
                    }
                });

        //parameters for continuously requesting device locations
        LocationRequest mLocationRequest = new LocationRequest();
        // Set the location update interval (unit: ms).
        mLocationRequest.setInterval(10000);
        // Set the location type.
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationCallback mLocationCallback;
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    // Process the location callback result.
                }
            }
        };

        fusedLocationProviderClient
                .requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Processing when the API call is successful.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Processing when the API call fails.
                    }
                });

        // Note: When requesting location updates is stopped, mLocationCallback must be the same object as LocationCallback in the requestLocationUpdates method.
        fusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
                // Define callback for success in stopping requesting location updates.
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // TODO
                    }
                })
                // Define callback for failure in stopping requesting location updates.
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // TODO
                    }
                });

        // Obtain the last known location.
        Task<Location> task = fusedLocationProviderClient.getLastLocation()
                // Define callback for success in obtaining the last known location.
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            return;
                        }
                        // Define logic for processing the Location object upon success.
                        // TODO
                    }
                })
                // Define callback for failure in obtaining the last known location.
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // TODO
                    }
                });

        // Set the mock mode. Pass true to enable it; pass false to disable it.
        boolean mockFlag = true;
        fusedLocationProviderClient.setMockMode(mockFlag)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "setMockMode onSuccess");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.i(TAG, "setMockMode onFailure:" + e.getMessage());
                    }
                });

        // Set a mock location.
        final Location mockLocation = new Location(LocationManager.GPS_PROVIDER);
        mockLocation.setLongitude(118.76);
        mockLocation.setLatitude(31.98);
        fusedLocationProviderClient.setMockLocation(mockLocation)
                // Define callback for success in setting the mock location information.
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "setMockLocation onSuccess");
                    }
                })
                // Define callback for failure in setting the mock location information.
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.i(TAG, "setMockLocation onFailure:" + e.getMessage());
                    }
                });

        // Set a mock location.
         final Location mock1Location = new Location(LocationManager.GPS_PROVIDER);
        mock1Location.setLongitude(118.76);
        mock1Location.setLatitude(31.98);
        fusedLocationProviderClient.setMockLocation(mock1Location)
                // Define callback for success in setting the mock location information.
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "setMockLocation onSuccess");
                    }
                })
                // Define callback for failure in setting the mock location information.
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.i(TAG, "setMockLocation onFailure:" + e.getMessage());
                    }
                });
    }
}
