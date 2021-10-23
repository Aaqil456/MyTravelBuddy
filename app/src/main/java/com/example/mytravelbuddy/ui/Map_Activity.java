package com.example.mytravelbuddy.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mytravelbuddy.R;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.HuaweiMapOptions;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.MapsInitializer;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.model.BitmapDescriptorFactory;
import com.huawei.hms.maps.model.Circle;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.content.ContentValues.TAG;

public class Map_Activity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mMapView;
    HuaweiMap hMap;
    private static final LatLng lat=new LatLng(31.294,32.678);
    private Marker mMarker;
    private Circle mCircle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.setApiKey("CwEAAAAA87ygm62CxaYAnCnmLDj8LNdgxZ9Gt+/zTKg5oQDMsbJtWX/zNvzvhQN6gyEZKOWnoEQnrVwrDpT57bq0EZelxL3A9BE=");

        setContentView(R.layout.map_view);
        // If the API level is 23 or higher (Android 6.0 or later), you need to dynamically apply for permissions.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "sdk >= 23 M");
            // Check whether your app has the specified permission and whether the app operation corresponding to the permission is allowed.
            if (ActivityCompat.checkSelfPermission(this,
                    ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Request permissions for your app.
                String[] strings =
                        {ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                // Request permissions.
                ActivityCompat.requestPermissions(this, strings, 1);
            }
        }

        mMapView = findViewById(R.id.mapview_mapviewdemo);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("MapViewBundleKey");
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
        HuaweiMapOptions huaweiMapOptions = new HuaweiMapOptions();
        huaweiMapOptions.liteMode(true);

    }




    //get location
    @RequiresPermission(allOf = {ACCESS_FINE_LOCATION, ACCESS_WIFI_STATE})
    @Override
    public void onMapReady(HuaweiMap map){
         hMap = map;



        // Enable the my-location layer.
        hMap.setMyLocationEnabled(true);
        // Enable the my-location icon.
        hMap.getUiSettings().setMyLocationButtonEnabled(true);
        //allow compass
        hMap.getUiSettings().setCompassEnabled(true);


            MarkerOptions options = new MarkerOptions()
                    .position(new LatLng(48.893478, 2.334595))
                    .title("Hello Huawei Map")
                    .snippet("This is a snippet!")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_arrow_circle_up_24));
            mMarker = hMap.addMarker(options);


    }


    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }




    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



}
