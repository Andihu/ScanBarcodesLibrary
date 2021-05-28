package com.microfountain.scanbarcordes.barcodescanner;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;


import com.google.mlkit.vision.barcode.Barcode;
import com.microfountain.scanbarcordes.R;

import java.util.List;

public class CustomScannerActivity extends CameraXLivePreviewActivity {

    private static final String TAG = "CustomScannerActivity";
    private ScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = findViewById(R.id.scanner_view);
        getLifecycle().addObserver(scannerView);
    }

    @Override
    public int getLayout() {
        return R.layout.custom_sacnner_layout;
    }

    @Override
    protected void onScannerSuccess(@NonNull List<Barcode> barcodes) {
        if (barcodes.isEmpty()) {
            Log.v(TAG, "No barcode has been detected");
        } else {

            for (Barcode barcode : barcodes) {
                Log.e(TAG, "onScannerSuccess: " + barcode.getDisplayValue());

            }

            scannerView.stop();
        }
    }

    @Override
    protected void onScannerFailure(@NonNull Exception e) {

    }
}
