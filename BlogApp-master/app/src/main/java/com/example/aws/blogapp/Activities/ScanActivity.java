package com.example.aws.blogapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.widget.Toast;

import com.example.aws.blogapp.R;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class ScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {
    BarcodeReader barcodeReader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        // get the barcode reader instance
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);
    }
    @Override
    public void onScanned(Barcode barcode) {
        // playing barcode reader beep sound
        barcodeReader.playBeep();
        // ticket details activity by passing barcode
        Intent intent = new Intent(ScanActivity.this, SecondActivity.class);
        intent.putExtra("code", barcode.rawValue);
        System.out.println("(((((((((((((((())))))))))))))))" + barcode.rawValue);
        startActivity(intent);
    }
    @Override
    public void onScannedMultiple(List<Barcode> list) {
    }
    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
    }
    @Override
    public void onScanError(String s) {
        Toast.makeText(getApplicationContext(), "Error occurred while scanning " + s, Toast.LENGTH_SHORT).show();
    }
}
