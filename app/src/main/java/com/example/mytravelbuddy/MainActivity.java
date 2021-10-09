package com.example.mytravelbuddy;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mytravelbuddy.ui.Map_Activity;
import com.example.mytravelbuddy.ui.home.HomeFragment;
import com.example.mytravelbuddy.ui.map.Map_search;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.huawei.hms.mlplugin.asr.MLAsrCaptureActivity;
import com.huawei.hms.mlplugin.asr.MLAsrCaptureConstants;
import com.huawei.hms.mlsdk.asr.MLAsrConstants;
import com.huawei.hms.mlsdk.asr.MLAsrRecognizer;
import com.huawei.hms.mlsdk.common.MLApplication;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton mic;
    TextView textView3;
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
        mic=findViewById(R.id.mic);
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // context: app context.
                MLAsrRecognizer mSpeechRecognizer = MLAsrRecognizer.createAsrRecognizer(MainActivity.this);
                asrPickUpUi();
            }
        });


    }
    private void asrPickUpUi(){
        // Use Intent for recognition settings.
        Intent intent = new Intent(this, MLAsrCaptureActivity.class)
                // Set the language that can be recognized to English. If this parameter is not set, English is recognized by default. Example: "zh-CN": Chinese; "en-US": English; "fr-FR": French; "es-ES": Spanish; "de-DE": German; "it-IT": Italian; "ar": Arabic.
                .putExtra(MLAsrCaptureConstants.LANGUAGE, "en-US")
// Set whether to display the recognition result on the speech pickup UI. MLAsrCaptureConstants.FEATURE_ALLINONE: no; MLAsrCaptureConstants.FEATURE_WORDFLUX: yes.
                .putExtra(MLAsrCaptureConstants.FEATURE, MLAsrCaptureConstants.FEATURE_WORDFLUX)
                // Set the application scenario. MLAsrConstants.SCENES_SHOPPING indicates shopping, which is supported only for Chinese. Under this scenario, recognition for the name of Huawei products has been optimized.
                .putExtra(MLAsrConstants.SCENES, MLAsrConstants.SCENES_SHOPPING);
                 startActivityIfNeeded(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String text = "Hello";
        // REQUEST_CODE_ASR: request code between the current activity and speech pickup UI activity defined in step 3.
        if (requestCode == 100) {
            switch (resultCode) {
        // MLAsrCaptureConstants.ASR_SUCCESS: Recognition is successful.
                case MLAsrCaptureConstants.ASR_SUCCESS:
                    if (data != null) {
                        Bundle bundle = data.getExtras();
        // Obtain the text information recognized from speech.
                        if (bundle.containsKey(MLAsrCaptureConstants.ASR_RESULT)) {
                            text = bundle.getString(MLAsrCaptureConstants.ASR_RESULT);
                            textView3.setText(text);
        // Process the recognized text information.
                        }
                    }
                    break;
        // MLAsrCaptureConstants.ASR_FAILURE: Recognition fails.
                case MLAsrCaptureConstants.ASR_FAILURE:
        // Processing logic for recognition failure.
                    if(data != null) {
                        Bundle bundle = data.getExtras();
        // Check whether a result code is contained.
                        if(bundle.containsKey(MLAsrCaptureConstants.ASR_ERROR_CODE)) {
                            int errorCode = bundle.getInt(MLAsrCaptureConstants.ASR_ERROR_CODE);
        // Perform troubleshooting based on the result code.
                        }
        // Check whether error information is contained.
                        if(bundle.containsKey(MLAsrCaptureConstants.ASR_ERROR_MESSAGE)){
                            String errorMsg = bundle.getString(MLAsrCaptureConstants.ASR_ERROR_MESSAGE);
        // Perform troubleshooting based on the error information.
                        }
        // Check whether a sub-result code is contained.
                        if(bundle.containsKey(MLAsrCaptureConstants.ASR_SUB_ERROR_CODE)) {
                            int subErrorCode = bundle.getInt(MLAsrCaptureConstants.ASR_SUB_ERROR_CODE);
        // Process the sub-result code.
                        }
                    }
                default:
                    break;
            }
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


    //mic


}