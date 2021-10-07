//package com.example.mytravelbuddy.ui;
//
//import android.os.Bundle;
//import android.util.Pair;
//import com.huawei.hms.mlsdk.tts.MLTtsConfig;
//import com.huawei.hms.mlsdk.tts.MLTtsAudioFragment;
//import com.huawei.hms.mlsdk.tts.MLTtsCallback;
//import com.huawei.hms.mlsdk.tts.MLTtsConstants;
//import com.huawei.hms.mlsdk.tts.MLTtsEngine;
//import com.huawei.hms.mlsdk.tts.MLTtsError;
//import com.huawei.hms.mlsdk.tts.MLTtsWarn;
//
//
//public class MLTtsConfig {
//    public MLTtsConfig(){
//
//        // 1. Create parameters. These parameters are optional. If you do not set them, default values are used.
//        MLTtsConfig mlTtsConfig = new MLTtsConfig()
//                .setVolume(1.5f)
//                .setSpeed(1.5f)
//                .setLanguage(MLTtsConstants.TTS_ZH_HANS)
//                .setPerson(MLTtsConstants.TTS_SPEAKER_FEMALE_ZH)
//                .setSynthesizeMode(MLTtsConstants.TTS_ONLINE_MODE);
//
//// 2. View custom parameter values.
//        mlTtsConfig.getLanguage();// Obtain the language. If null is returned, the value is not customized.
//        mlTtsConfig.getPerson();// Obtain the speaker name. If null is returned, the value is not customized.
//        mlTtsConfig.getSpeed();// Obtain the speed. If 0 is returned, the value is not customized.
//        mlTtsConfig.getVolume();// Obtain the volume. If 0 is returned, the value is not customized.
//        mlTtsConfig.getSynthesizeMode();// Obtain the TTS mode. If null is returned, the TTS mode is not customized.
//
//// 3. Update configurations.
//        // a. Use the previous configurations if there is no custom configuration. Use default configurations if no previous configuration exists.
//        // b. Use current custom configurations if there are any.
//
//// 4. Update mode:
//// a. During initialization.
//        MLTtsEngine mlTtsEngine =  = new MLTtsEngine(mlTtsConfig);
//// b. During the process.
//        mlTtsEngine.updateConfig(mlTtsConfig);
//
//    }
//    public void speech (){
//        // The currently used voice code is Chinese, and the speaker code is female.
//          String Language = "en-US";
//          String TTS_SPEAKER_MALE_ZH = "en-US-st-1";
//        // Use customized parameter settings to create a TTS engine.
//        MLTtsConfig mlTtsConfig = (MLTtsConfig) new MLTtsConfig()
//                // Set the text converted from speech to Chinese.
//                .setLanguage(Language)
//                // Set the Chinese timbre.
//                .setPerson(MLTtsConstants.TTS_SPEAKER_FEMALE_EN)
//                // Set the speech speed. The range is (0, 5.0]. 1.0 indicates a normal speed.
//                .setSpeed(1.0f)
//                // Set the volume. The range is (0, 2). 1.0 indicates a normal volume.
//                .setVolume(1.0f);
//
//        MLTtsEngine mlTtsEngine = new MLTtsEngine(mlTtsConfig);
//// Set the volume of the built-in player, in dBs. The value is in the range of [0, 100].
//        mlTtsEngine.setPlayerVolume(20);
//// Update the configuration when the engine is running.
//        mlTtsEngine.updateConfig(mlTtsConfig);
//
//
//        MLTtsCallback callback = new MLTtsCallback() {
//            @Override
//            public void onError(String taskId, MLTtsError err) {
//// Processing logic for TTS failure.
//            }
//            @Override
//            public void onWarn(String taskId, MLTtsWarn warn) {
//// Alarm handling without affecting service logic.
//            }
//            @Override
//// Return the mapping between the currently played segment and text. start: start position of the audio segment in the input text; end (excluded): end position of the audio segment in the input text.
//            public void onRangeStart(String taskId, int start, int end) {
//// Process the mapping between the currently played segment and text.
//            }
//            @Override
//            // taskId: ID of an audio synthesis task corresponding to the audio.
//            // audioFragment: audio data.
//            // offset: offset of the audio segment to be transmitted in the queue. One audio synthesis task corresponds to an audio synthesis queue.
//            // range: text area where the audio segment to be transmitted is located; range.first (included): start position; range.second (excluded): end position.
//            public void onAudioAvailable(String taskId, MLTtsAudioFragment audioFragment, int offset, Pair<Integer, Integer> range, Bundle bundle){
//// Audio stream callback API, which is used to return the synthesized audio data to the app.
//            }
//            @Override
//            public void onEvent(String taskId, int eventId, Bundle bundle) {
//                // Callback method of a TTS event. eventId indicates the event name.
//                switch (eventId) {
//                    case MLTtsConstants.EVENT_PLAY_START:
//                        // Called when playback starts.
//                        break;
//                    case MLTtsConstants.EVENT_PLAY_STOP:
//                        // Called when playback stops.
//                        boolean isInterrupted = bundle.getBoolean(MLTtsConstants.EVENT_PLAY_STOP_INTERRUPTED);
//                        break;
//                    case MLTtsConstants.EVENT_PLAY_RESUME:
//                        // Called when playback resumes.
//                        break;
//                    case MLTtsConstants.EVENT_PLAY_PAUSE:
//                        // Called when playback pauses.
//                        break;
//
//                    // Pay attention to the following callback events when you focus on only synthesized audio data but do not use the internal player for playback:
//                    case MLTtsConstants.EVENT_SYNTHESIS_START:
//                        // Called when TTS starts.
//                        break;
//                    case MLTtsConstants.EVENT_SYNTHESIS_END:
//                        // Called when TTS ends.
//                        break;
//                    case MLTtsConstants.EVENT_SYNTHESIS_COMPLETE:
//                        // TTS is complete. All synthesized audio streams are passed to the app.
//                        isInterrupted = bundle.getBoolean(MLTtsConstants.EVENT_SYNTHESIS_INTERRUPTED);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        };
//
//
//        mlTtsEngine.setTtsCallback(callback);
///**
// *First parameter sourceText: text information to be synthesized. The value can contain a maximum of 500 characters.
// * Second parameter indicating the synthesis mode: The format is configA | configB | configC.
// *configA:
// *    MLTtsEngine.QUEUE_APPEND: After a TTS task is generated, the task is processed as follows: If playback is going on, the task is added to the queue for execution in sequence; if playback pauses, the playback is resumed and the task is added to the queue for execution in sequence; if there is no playback, the TTS task is executed immediately.
// *    MLTtsEngine.QUEUE_FLUSH: The ongoing TTS task and playback are stopped immediately, all TTS tasks in the queue are cleared, and the current TTS task is executed immediately and played.
// *configB:
// *    MLTtsEngine.OPEN_STREAM: The synthesized audio data is output through onAudioAvailable.
// *configC:
// *    MLTtsEngine.EXTERNAL_PLAYBACK: external playback mode. The player provided by the SDK is shielded. You need to process the audio output by the onAudioAvailable callback API. In this case, the playback-related APIs in the callback APIs become invalid, and only the callback APIs related to audio synthesis can be listened.
// */
//// Use the built-in player of the SDK to play speech in queuing mode.
//        String id = mlTtsEngine.speak("Hello and Welcome", MLTtsEngine.QUEUE_APPEND);
//// In queuing mode, the synthesized audio stream is output through onAudioAvailable, and the built-in player of the SDK is used to play the speech.
//// String id = mlTtsEngine.speak(sourceText, MLTtsEngine.QUEUE_APPEND | MLTtsEngine.OPEN_STREAM);
//// In queuing mode, the synthesized audio stream is output through onAudioAvailable, and the audio stream is not played, but controlled by you.
//// String id = mlTtsEngine.speak(sourceText, MLTtsEngine.QUEUE_APPEND | MLTtsEngine.OPEN_STREAM | MLTtsEngine.EXTERNAL_PLAYBACK);
//
//
//
//        if (mlTtsEngine!= null) {
//            mlTtsEngine.shutdown();
//        }
//
//    }
//
//}
