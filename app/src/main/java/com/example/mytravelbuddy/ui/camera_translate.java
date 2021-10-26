package com.example.mytravelbuddy.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mytravelbuddy.MainActivity;
import com.example.mytravelbuddy.R;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.common.LensEngine;
import com.huawei.hms.mlsdk.common.MLAnalyzer;
import com.huawei.hms.mlsdk.common.MLApplication;
import com.huawei.hms.mlsdk.common.MLException;
import com.huawei.hms.mlsdk.text.MLLocalTextSetting;
import com.huawei.hms.mlsdk.text.MLText;
import com.huawei.hms.mlsdk.text.MLTextAnalyzer;
import com.huawei.hms.mlsdk.translate.MLTranslatorFactory;
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslateSetting;
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class camera_translate extends AppCompatActivity {

    LensEngine mLensEngine;
    MLTextAnalyzer analyzer;
    private int lensType = LensEngine.BACK_LENS;
    private LensEnginePreview mPreview;
    private TextView tv,tvFrom,tvTo,tvcamerastatus;
    int languagesFrom;
    ImageButton pause;
    String LanguageSelectedFrom="ko";
    String[]languageselected = {"ko", "zh", "ja", "ms", "ta","de","es","id","ru","th","vi"};
    public String LanguageFrom = "ko";
    public String Status="pause";
    List<String> list = new ArrayList<String>();
    Spinner btnlanguagefrom,btnlanguageto;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        mPreview = findViewById(R.id.lensengine_preview);
        tv = (TextView) findViewById(R.id.textView);
        tvFrom=findViewById(R.id.tvFrom);
        tvTo=findViewById(R.id.tvTo);
        tvcamerastatus=findViewById(R.id.tvcamerastatus);
        pause=findViewById(R.id.pause_btn);

        //Initialization
        analyzer = new MLTextAnalyzer.Factory(camera_translate.this).setLocalOCRMode(MLLocalTextSetting.OCR_DETECT_MODE).setLanguage(LanguageSelectedFrom).create();
        analyzer.setTransactor(new OcrDetectorProcessor());
        createLensEngine();
        startLensEngine();


        btnlanguagefrom=findViewById(R.id.btnlanguagefrom);
        //Array list of animals to display in the spinner
        list.add("Korean");
        list.add("Traditional Chinese");
        list.add("Japanese");
        list.add("Malay");
        list.add("Tamil");
        list.add("German");
        list.add("Spanish");
        list.add("Indonesian");
        list.add("Thai");
        list.add("Vietnamese");
        //create an ArrayAdapter from the String Array
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        //set the view for the Drop down list
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the ArrayAdapter to the spinner
        btnlanguagefrom.setAdapter(dataAdapter);
        btnlanguagefrom.setPrompt("Select a Language!");
        //attach the listener to the spinner
        btnlanguagefrom.setOnItemSelectedListener(new MyOnItemSelectedListener());

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (Status){
                    case "pause":
                        onPause();
                        pause.setImageResource(R.drawable.ic_baseline_pause_circle_24);
                        Toast.makeText(camera_translate.this, "The Preview Is Play ", Toast.LENGTH_SHORT).show();
                        tvcamerastatus.setText("The Camera Is Pause");
                        Status="play";

                        break;
                    case "play":
                        startLensEngine();
                        pause.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                        Toast.makeText(camera_translate.this, "The Preview Is Pause ", Toast.LENGTH_SHORT).show();
                        tvcamerastatus.setText("The Camera Is Play");
                        Status="pause";

                        break;

            }}
        });
    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(@NonNull AdapterView<?> parent, View view, int pos, long id) {

            String selectedItem = parent.getItemAtPosition(pos).toString();

            //check which spinner triggered the listener
            switch (parent.getId()) {
                //Language From spinner
                case R.id.btnlanguagefrom:
                    //make sure the country was already selected during the onCreate

                        languagesFrom=list.indexOf(selectedItem);
                        LanguageSelectedFrom=languageselected[languagesFrom];
                        Toast.makeText(parent.getContext(), "Language From: " + LanguageSelectedFrom, Toast.LENGTH_SHORT).show();
                        startLensEngine();
                        LanguageFrom = selectedItem;

            }

        }

        public void onNothingSelected(AdapterView<?> parent) {
            Toast.makeText(parent.getContext(), "Nothing" + LanguageSelectedFrom, Toast.LENGTH_SHORT).show();
        }

    }

    private void createLensEngine() {
        Context context = this.getApplicationContext();
        mLensEngine = new LensEngine.Creator(context, this.analyzer)
                .setLensType(this.lensType)
                .applyDisplayDimension(1600, 1024)
                .applyFps(25.0f)
                .enableAutomaticFocus(true)
                .create();
    }

    private void startLensEngine() {
        //Translation Kit
        // Method 2: Use the customized parameter MLLocalTextSetting to configure the text analyzer on the device.
        MLLocalTextSetting setting = new MLLocalTextSetting.Factory()
                .setOCRMode(MLLocalTextSetting.OCR_DETECT_MODE)
                // Specify languages that can be recognized.
                .setLanguage(LanguageSelectedFrom)
                .create();
        analyzer = MLAnalyzerFactory.getInstance().getLocalTextAnalyzer(setting);
        Toast.makeText(this, "Language CURRENTLY: " + LanguageSelectedFrom, Toast.LENGTH_SHORT).show();

        if (this.mLensEngine != null) {
            try {
                this.mPreview.start(this.mLensEngine);
            } catch (IOException e) {
                this.mLensEngine.release();
                this.mLensEngine = null;
            }
        }
    }


    public class OcrDetectorProcessor implements MLAnalyzer.MLTransactor<MLText.Block> {

        @Override
        public void transactResult(MLAnalyzer.Result<MLText.Block> results) {

            SparseArray<MLText.Block> blocks = results.getAnalyseList();


            for (int i = 0; i < blocks.size(); i++) {
                List<MLText.TextLine> lines = blocks.get(i).getContents();
                for (int j = 0; j < lines.size(); j++) {
                    List<MLText.Word> elements = lines.get(j).getContents();
                    for (int k = 0; k < elements.size(); k++) {
                        tvFrom.setText( elements.get(k).getStringValue());
                        translateFunction(elements.get(k).getStringValue());

                    }
                }
            }


        }

        @Override
        public void destroy() {

        }

    }
    //End Speech Output
    private void translateFunction(final String inputtext) {
        MLApplication.getInstance().setApiKey(getResources().getString(R.string.api_key));

        // Create a text translator using custom parameter settings.
        MLRemoteTranslateSetting setting = new MLRemoteTranslateSetting
                .Factory()
                // Set the source language code. The BCP-47 standard is used for Traditional Chinese, and the ISO 639-1 standard is used for other languages. This parameter is optional. If this parameter is not set, the system automatically detects the language.
                .setSourceLangCode(LanguageSelectedFrom)
                // Set the target language code. The BCP-47 standard is used for Traditional Chinese, and the ISO 639-1 standard is used for other languages.
//                .setTargetLangCode(languageselected.toString())
                .setTargetLangCode("en")
                .create();
        MLRemoteTranslator mlRemoteTranslator = MLTranslatorFactory.getInstance().getRemoteTranslator(setting);
        // sourceText: text to be translated, with up to 5000 characters.
        final Task<String> task = mlRemoteTranslator.asyncTranslate(inputtext);
        task.addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String text) {
                tvTo.setText(text);


                Log.d(inputtext+"translated: ",text);
                // Processing logic for recognition success.
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // Processing logic for recognition failure.
                try {
                    MLException mlException = (MLException)e;
                    // Obtain the result code. You can process the result code and customize respective messages displayed to users.
                    int errorCode = mlException.getErrCode();
                    // Obtain the error information. You can quickly locate the fault based on the result code.
                    String errorMessage = mlException.getMessage();
                } catch (Exception error) {
                    if (mlRemoteTranslator!= null) {
                        mlRemoteTranslator.stop();
                    }
                    // Handle the conversion error.
                }
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        this.mPreview.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mLensEngine != null) {
            this.mLensEngine.release();
        }
        if (this.analyzer != null) {
            try {
                this.analyzer.stop();
            } catch (IOException e) {
            }
        }
    }
}
