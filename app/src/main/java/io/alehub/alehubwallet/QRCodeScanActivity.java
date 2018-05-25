package io.alehub.alehubwallet;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import io.alehub.alehubwallet.view.ToolbarFont;

/**
 * Created by dima on 13.03.18.
 */

public class QRCodeScanActivity extends LangBaseActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private QRCodeReaderView qrCodeView;
    private ToolbarFont toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_scan_activity);
        toolbar = (ToolbarFont) findViewById(R.id.toolbar);
        qrCodeView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        initReader();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Intent intent = new Intent();
        intent.putExtra("data", text);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void initReader() {
        if (Build.VERSION.SDK_INT < 23) {
            initReaderNext();
        } else {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                initReaderNext();
            } else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CAMERA};
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 9);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean permissionGranted = false;
        switch (requestCode) {
            case 9:
                permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (permissionGranted) {
            initReaderNext();
        }
    }

    private void initReaderNext() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            qrCodeView.setOnQRCodeReadListener(this);
            qrCodeView.setQRDecodingEnabled(true);
            qrCodeView.setAutofocusInterval(2000L);
            qrCodeView.setFrontCamera();
            qrCodeView.setBackCamera();

            qrCodeView.startCamera();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (qrCodeView != null) {
            //initReader();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            qrCodeView.stopCamera();
        } catch (Exception e) {
        }
    }
}
