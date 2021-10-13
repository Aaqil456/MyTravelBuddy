package com.example.mytravelbuddy.ui;

import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mytravelbuddy.MainActivity;
import com.example.mytravelbuddy.R;
import com.huawei.hms.mlsdk.common.LensEngine;
import com.huawei.hms.mlsdk.text.MLTextAnalyzer;

import java.io.IOException;

public class camera_translate extends AppCompatActivity{
    SurfaceView mSurfaceView;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.camera);
    mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
    onCameraStream();
    }
    public void onCameraStream(){

        MLTextAnalyzer analyzer = new MLTextAnalyzer.Factory(camera_translate.this).create();

        analyzer.setTransactor(new OcrDetectorProcessor());
        LensEngine lensEngine = new LensEngine.Creator(getApplicationContext(),analyzer)
                .setLensType(LensEngine.BACK_LENS)
                .applyDisplayDimension(1440, 1080)
                .applyFps(30.0f)
                .enableAutomaticFocus(true)
                .create();

        try {
            lensEngine.run(mSurfaceView.getHolder());
            Toast.makeText(this, "Camera Is Running", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

        if (analyzer != null) {
            try {
                analyzer.stop();
                Toast.makeText(this, "Analyzer Stop", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                // Exception handling.
            }
        }
        if (lensEngine != null) {
            Toast.makeText(this, "Lens Stop", Toast.LENGTH_SHORT).show();
            lensEngine.release();
        }

    }



}
