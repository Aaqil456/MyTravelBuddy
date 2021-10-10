package com.example.mytravelbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mytravelbuddy.ui.OcrDetectorProcessor;
import com.example.mytravelbuddy.ui.home.HomeFragment;
import com.example.mytravelbuddy.ui.map.Map_search;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.huawei.hms.mlplugin.asr.MLAsrCaptureActivity;
import com.huawei.hms.mlplugin.asr.MLAsrCaptureConstants;
import com.huawei.hms.mlsdk.asr.MLAsrConstants;
import com.huawei.hms.mlsdk.asr.MLAsrRecognizer;
import com.huawei.hms.mlsdk.common.LensEngine;
import com.huawei.hms.mlsdk.common.MLApplication;
import com.huawei.hms.mlsdk.text.MLTextAnalyzer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton camera;
    TextView textView3;
    MLTextAnalyzer analyzer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        MLApplication.getInstance().setApiKey(getResources().getString(R.string.api_key));

        //loading the default fragment
        loadFragment(new HomeFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setBackground(null);
        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fragment = new HomeFragment();
                        break;

                    case R.id.navigation_map:
                        fragment = new Map_search();
                        break;

                }

                return loadFragment(fragment);
            }
        });


        //for mic
        camera=findViewById(R.id.camera);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCameraStream();


            }
        });


    }

    public void onCameraStream(){
        MLTextAnalyzer analyzer = new MLTextAnalyzer.Factory(MainActivity.this).create();

        analyzer.setTransactor(new OcrDetectorProcessor());
        LensEngine lensEngine = new LensEngine.Creator(getApplicationContext(),analyzer)
                .setLensType(LensEngine.BACK_LENS)
                .applyDisplayDimension(1440, 1080)
                .applyFps(30.0f)
                .enableAutomaticFocus(true)
                .create();



        SurfaceView mSurfaceView = findViewById(R.id.surface_view);
        try {
            lensEngine.run(mSurfaceView.getHolder());
        } catch (IOException e) {
            // Exception handling logic.
        }

        if (analyzer != null) {
            try {
                analyzer.stop();
            } catch (IOException e) {
                // Exception handling.
            }
        }
        if (lensEngine != null) {
            lensEngine.release();
        }

    }



    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


}