package com.example.mytravelbuddy.ui;

import android.util.SparseArray;

import com.huawei.hms.mlsdk.common.MLAnalyzer;
import com.huawei.hms.mlsdk.text.MLText;

public class OcrDetectorProcessor implements MLAnalyzer.MLTransactor<MLText.Block> {
    @Override
    public void transactResult(MLAnalyzer.Result<MLText.Block> results) {
        SparseArray<MLText.Block> items = results.getAnalyseList();
        // Determine detection result processing as required. Note that only the detection results are processed.
        // Other detection-related APIs provided by ML Kit cannot be called.
    }
    @Override
    public void destroy() {
        // Callback method used to release resources when the detection ends.
    }

}