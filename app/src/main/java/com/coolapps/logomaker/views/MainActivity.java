package com.coolapps.logomaker.views;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.coolapps.logomaker.R;
import com.coolapps.logomaker.utilities.AdsUtility;
import com.coolapps.logomaker.utilities.NetworkUtils;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {

//    private static final int ACTION_OPEN_DOCUMENT_REQUEST_CODE = 2;
//    private static final int ACTION_PICK_REQUEST_CODE = 1;
//    private static final int PERMISSIONS_REQUEST_CODE = 3;
//    private static final int REQUEST_CODE_OPEN_SETTINGS = 6;
    private RelativeLayout startlogo, mylogos;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private LinearLayout adView;
//    private Snackbar mPermissionSnackbar;
//    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        adView = findViewById(R.id.adView);
        if (!NetworkUtils.isNetworkAvailable(this)) {

            adView.setVisibility(View.GONE);

        } else {


            adView.setVisibility(View.VISIBLE);
            AdsUtility.admobLargBannerCall(this, adView);

        }

        AdsUtility.InterstitialAdmob(this);

        initialized();

    }

    public void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initialized() {
        getReadPermission();

//        requestAllPermissions();

        startlogo = findViewById(R.id.startlogo);
        startlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LogoMakerActivity.class));
                AdsUtility.showIntestitialAds();
            }
        });
        mylogos = findViewById(R.id.mylogos);
        mylogos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyWorkActivity.class));
                AdsUtility.showIntestitialAds();
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void getReadPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showMessageOKCancel("You need to allow access to get Image From gallery",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }


        } else {
        }


    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false)
                .create()
                .show();
    }


//    private void showProgressDialog(String str) {
//        if (this.mProgressDialog == null) {
//            this.mProgressDialog = new ProgressDialog(this, PERMISSIONS_REQUEST_CODE);
//            this.mProgressDialog.setTitle(getString(R.string.please_wait_dialog_title));
//            this.mProgressDialog.setMessage(str);
//            this.mProgressDialog.setCancelable(false);
//            this.mProgressDialog.show();
//        }
//    }
//
//
//    private void hideProgressDialog() {
//        if (this.mProgressDialog != null) {
//            try {
//                this.mProgressDialog.dismiss();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            this.mProgressDialog = null;
//        }
//    }
//
//    private boolean hasPermissionCamera() {
//        return ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0;
//    }
//
//    private boolean hasPermissionWriteExternalStorage() {
//        return ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
//    }
//
//    @TargetApi(Build.VERSION_CODES.M)
//    private void requestAllPermissions() {
//        String[] strArr;
//        if (hasPermissionCamera() || hasPermissionWriteExternalStorage()) {
//            if (!hasPermissionCamera() || hasPermissionWriteExternalStorage()) {
//                if (!hasPermissionCamera() && hasPermissionWriteExternalStorage()) {
//                    if (shouldShowRequestPermissionRationale("android.permission.CAMERA")) {
//                        showPermissionNotGrantedSnackbar("android.permission.CAMERA");
//                        return;
//                    }
//                    strArr = new String[ACTION_PICK_REQUEST_CODE];
//                    strArr[0] = "android.permission.CAMERA";
//                    requestPermissions(strArr, PERMISSIONS_REQUEST_CODE);
//                }
//            } else if (shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE")) {
//                showPermissionNotGrantedSnackbar("android.permission.WRITE_EXTERNAL_STORAGE");
//            } else {
//                strArr = new String[ACTION_PICK_REQUEST_CODE];
//                strArr[0] = "android.permission.WRITE_EXTERNAL_STORAGE";
//                requestPermissions(strArr, PERMISSIONS_REQUEST_CODE);
//            }
//        } else if (shouldShowRequestPermissionRationale("android.permission.CAMERA")) {
//            showPermissionNotGrantedSnackbar("android.permission.CAMERA");
//        } else if (shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE")) {
//            showPermissionNotGrantedSnackbar("android.permission.WRITE_EXTERNAL_STORAGE");
//        } else {
//            strArr = new String[ACTION_OPEN_DOCUMENT_REQUEST_CODE];
//            strArr[0] = "android.permission.CAMERA";
//            strArr[ACTION_PICK_REQUEST_CODE] = "android.permission.WRITE_EXTERNAL_STORAGE";
//            requestPermissions(strArr, PERMISSIONS_REQUEST_CODE);
//        }
//    }
//
//
//    private void hidePermissionNotGrantedSnackbar() {
//        if (this.mPermissionSnackbar != null) {
//            this.mPermissionSnackbar.dismiss();
//            this.mPermissionSnackbar = null;
//        }
//    }
//
//    private void showPermissionNotGrantedSnackbar(String str) {
//        int i = 0;
//        if (str.equals("android.permission.CAMERA")) {
//            i = R.string.permission_camera_not_granted_dialog_message;
//        } else if (str.equals("android.permission.WRITE_EXTERNAL_STORAGE")) {
//            i = R.string.permission_write_external_storage_not_granted_dialog_message;
//        }
//        if (this.mPermissionSnackbar == null) {
//            this.mPermissionSnackbar = Snackbar.make(findViewById(R.id.snackbar), i, -2).setAction(getString(R.string.permission).toUpperCase(), new View.OnClickListener() {
//                public void onClick(View view) {
//                    openAppPermissionsSettings();
//                }
//            });
//        }
//        this.mPermissionSnackbar.setText(i);
//        if (!this.mPermissionSnackbar.isShown()) {
//            this.mPermissionSnackbar.show();
//        }
//    }
//
//    private void openAppPermissionsSettings() {
//        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
//        intent.setData(Uri.parse("package:" + getPackageName()));
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, REQUEST_CODE_OPEN_SETTINGS);
//        }
//    }
//
//    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
//        switch (i) {
//            case PERMISSIONS_REQUEST_CODE /*3*/:
//                if (iArr.length > 0) {
//                    hidePermissionNotGrantedSnackbar();
//                    int length = strArr.length;
//                    for (int i2 = 0; i2 < length; i2 += ACTION_PICK_REQUEST_CODE) {
//                        Integer valueOf;
//                        if (iArr[i2] == 0) {
//                            valueOf = Integer.valueOf(ACTION_PICK_REQUEST_CODE);
//                        } else {
//                            valueOf = null;
//                        }
//                        if (valueOf == null) {
//                            showPermissionNotGrantedSnackbar(strArr[i2]);
//                        }
//                    }
//                    return;
//                } else if (!hasPermissionCamera()) {
//                    showPermissionNotGrantedSnackbar("android.permission.CAMERA");
//                    return;
//                } else if (!hasPermissionWriteExternalStorage()) {
//                    showPermissionNotGrantedSnackbar("android.permission.WRITE_EXTERNAL_STORAGE");
//                    return;
//                } else {
//                    return;
//                }
//            default:
//                return;
//        }
//    }


}
