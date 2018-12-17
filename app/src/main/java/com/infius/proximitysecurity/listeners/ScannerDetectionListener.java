package com.infius.proximitysecurity.listeners;

import android.util.SparseArray;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.text.TextBlock;

/**
 * Created by kshemendra on 07/09/16.
 */
public interface ScannerDetectionListener {
    void onBarcodeDetected(Barcode item);
    void onTextDetected(SparseArray<TextBlock> items);
}
