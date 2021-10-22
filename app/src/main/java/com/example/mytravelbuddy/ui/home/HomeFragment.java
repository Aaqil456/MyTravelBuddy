package com.example.mytravelbuddy.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.example.mytravelbuddy.R;
import com.example.mytravelbuddy.databinding.FragmentHomeBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.kit.awareness.Awareness;
import com.huawei.hms.kit.awareness.capture.WeatherStatusResponse;
import com.huawei.hms.kit.awareness.status.WeatherStatus;
import com.huawei.hms.kit.awareness.status.weather.Situation;
import com.huawei.hms.kit.awareness.status.weather.WeatherSituation;
import com.huawei.hms.mlsdk.common.MLApplication;
import com.huawei.hms.mlsdk.common.MLException;
import com.huawei.hms.mlsdk.translate.MLTranslatorFactory;
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslateSetting;
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslator;

// Import the weather capture-related classes.

public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;
    private TextView outputtext,Weathertext;
    private TextInputEditText inputtext1;
    private Button translate,btn_language;
    ImageButton btn_speech;
    private String language;
    int checkedItem;
    public String texttoSpeech;
    public String texttoSpeechLanguage;
    String[]listItems = {"Malay", "Traditional Chinese", "Japanese", "Korean", "Tamil","German","Spanish","Indonesian","Russian","Thai","Vietnamese"};
    String[]languageselected = {"ms", "zh", "ja", "ko", "ta","de","es","id","ru","th","vi"};
    String[]weatherInfoStr;
    private static final String TAG = HomeFragment.class.getSimpleName();



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        //Language Kit
        btn_language=root.findViewById(R.id.btn_language);
        btn_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
                builder.setTitle("Choose item");

                //this will checked the item when user open the dialog
                builder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(root.getContext(), "Position: " + which + " Value: " + listItems[which], Toast.LENGTH_LONG).show();
                        language=languageselected[which];
                        btn_language.setText(listItems[which]);
                        texttoSpeechLanguage=languageselected[which];
                        checkedItem = which;
                    }
                });

                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        //TextToSpeach
        TTS.init (getContext ());
        btn_speech=root.findViewById(R.id.btn_speech);
        btn_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//

                // Set TextToSpeech reading end detection event listener
                TTS.setOnUtteranceProgressListener (new UtteranceProgressListener () {
                    @Override
                    public void onDone (String utteranceId) {
                        Toast.makeText(getContext(), "Speech", Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onError (String utteranceId) {
                        Toast.makeText(getContext(), "There's an Error", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onStart (String utteranceId) {}
                });

                TTS.speak (texttoSpeech, this.hashCode () + "",texttoSpeechLanguage);





            }
        });


        //TranslationMethod
        inputtext1=root.findViewById(R.id.inputtext1);
        outputtext=root.findViewById(R.id.outputtext);
        translate=root.findViewById(R.id.btn_translate);
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateFunction(inputtext1.getText().toString());

            }
        });


//        Weathertext=root.findViewById(R.id.Weathertext);
        getWeather(root.getContext());

        return root;
    }

    private void getWeather(Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Awareness.getCaptureClient(context).getWeatherByDevice()
                    // Callback listener for execution success.
                    .addOnSuccessListener(new OnSuccessListener<WeatherStatusResponse>() {
                        @Override
                        public void onSuccess(WeatherStatusResponse weatherStatusResponse) {
                            WeatherStatus weatherStatus = weatherStatusResponse.getWeatherStatus();
                            WeatherSituation weatherSituation = weatherStatus.getWeatherSituation();
                            Situation situation = weatherSituation.getSituation();

                            weatherInfoStr= new String[]{weatherSituation.getCity().getName(),
                                    String.valueOf(situation.getTemperatureC()),
                                    String.valueOf(situation.getTemperatureF()),
                                    String.valueOf(situation.getWindSpeed()),
                                    situation.getHumidity(),
                                    String.valueOf(situation.getUpdateTime()),
                            };
                            Toast.makeText(getContext(), weatherInfoStr[0], Toast.LENGTH_SHORT).show();
                        }
                    })
                    // Callback listener for execution failure.
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getContext(), "get weather failed", Toast.LENGTH_LONG).show();


                        }
                    });
            return;
        }
        else{

            Weathertext.setText("Permission Not Granted");
            }

    }

    public static int getMessage(int statusCode){


        return statusCode;
    }


    //End Speech Output
    private void translateFunction(final String inputtext) {
        MLApplication.getInstance().setApiKey(getResources().getString(R.string.api_key));

        // Create a text translator using custom parameter settings.
        MLRemoteTranslateSetting setting = new MLRemoteTranslateSetting
                .Factory()
                // Set the source language code. The BCP-47 standard is used for Traditional Chinese, and the ISO 639-1 standard is used for other languages. This parameter is optional. If this parameter is not set, the system automatically detects the language.
                .setSourceLangCode("en")
                // Set the target language code. The BCP-47 standard is used for Traditional Chinese, and the ISO 639-1 standard is used for other languages.
//                .setTargetLangCode(languageselected.toString())
                .setTargetLangCode(language)
                .create();
        MLRemoteTranslator mlRemoteTranslator = MLTranslatorFactory.getInstance().getRemoteTranslator(setting);
        // sourceText: text to be translated, with up to 5000 characters.
        final Task<String> task = mlRemoteTranslator.asyncTranslate(inputtext);
        task.addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String text) {
                outputtext.setText(text);
                texttoSpeech=text;

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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}