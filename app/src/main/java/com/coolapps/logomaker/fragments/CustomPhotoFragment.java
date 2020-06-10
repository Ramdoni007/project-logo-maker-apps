package com.coolapps.logomaker.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.exoplayer2.upstream.UdpDataSource;
import com.coolapps.logomaker.R;
import com.coolapps.logomaker.utilities.AdsUtility;
import com.coolapps.logomaker.utilities.Item;
import com.coolapps.logomaker.views.LogoMakerActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import yuku.ambilwarna.BuildConfig;

public class CustomPhotoFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    ArrayList<Item> lines;
    private LinearLayout adView;
    private ImageView takepic, galley;
    private static final int REQUEST_CAMERA = 2;
    private static final int SELECT_PICTURE = 1;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    static int width;
    static int height;
    MediaScannerConnection msConn;
    ProgressDialog savingProcessing;
    public static final String LINK_PHOTO = "link_photo_es";
    private String linkSave = BuildConfig.FLAVOR;
    private String selectedImagePath;
    public static Bitmap bitmap;

    public static CustomPhotoFragment newInstance(int paramInt,
                                                  ArrayList<Item> paramArrayList) {
        CustomPhotoFragment localBarishFragment = new CustomPhotoFragment();
        localBarishFragment.lines = paramArrayList;
//        ids = paramInt;
        return localBarishFragment;
    }


    public View onCreateView(LayoutInflater paramLayoutInflater,
                             ViewGroup paramViewGroup, Bundle paramBundle) {

        View localView = paramLayoutInflater.inflate(R.layout.fragment_customphoto,
                paramViewGroup, false);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();

        getReadPermission();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());

        galley = localView.findViewById(R.id.gallery);
        takepic = localView.findViewById(R.id.takepic);
        galley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LogoMakerActivity.onStickSelect(paramAnonymousInt, 0);
                startActivityForResult(Intent.createChooser(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), "Select Picture"), SELECT_PICTURE);
                LogoMakerActivity.isCustImageOK = true;
            }
        });

        takepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LogoMakerActivity.onStickSelect(paramAnonymousInt, 0);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra("output", Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg")));
                startActivityForResult(intent, REQUEST_CAMERA);
                LogoMakerActivity.isCustImageOK = true;
            }
        });
        adView = localView.findViewById(R.id.banneradd);
        AdsUtility.admobBannerCall(this.getActivity(), adView);
//        localListView.setAdapter(new DefaultBGAdapter(getActivity(), lines));

        return localView;
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int height = options.outHeight;
        int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = SELECT_PICTURE;
        if (height > reqHeight) {
            inSampleSize = Math.round(((float) height) / ((float) reqHeight));
        }
        if (width / inSampleSize > reqWidth) {
            inSampleSize = Math.round(((float) width) / ((float) reqWidth));
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public String getPath(Uri uri) {
        String[] projection = new String[SELECT_PICTURE];
        projection[0] = "_data";
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        }
        cursor.moveToFirst();
        String filePath = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
        cursor.close();
        return filePath;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == UdpDataSource.DEFAULT_MAX_PACKET_SIZE && requestCode == UdpDataSource.DEFAULT_MAX_PACKET_SIZE) {
            getActivity().finish();
        }
        if (resultCode == 4000 && requestCode == UdpDataSource.DEFAULT_MAX_PACKET_SIZE) {
            getActivity().setResult(4000);
            getActivity().finish();
        }
        if (resultCode != -1) {
            return;
        }
        Uri selectedImageUri;

        float f;
        if (requestCode == SELECT_PICTURE) {
            selectedImageUri = data.getData();
            Log.d("URI VAL", "selectedImageUri = " + selectedImageUri.toString());
            this.selectedImagePath = getPath(selectedImageUri);
            if (this.selectedImagePath != null) {
                bitmap = BitmapFactory.decodeFile(this.selectedImagePath);
                f = ((float) bitmap.getWidth()) / ((float) bitmap.getHeight());
                if (bitmap.getWidth() > width) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, (int) (((float) width) / f), false);
                }
//                Log.d("Wid", width + BuildConfig.FLAVOR);
//                this.myView.addImage(bitmap);
                new SaveThread(bitmap, false).execute();
//                LogoMakerActivity.addCustomStickerView(bitmap);
//                getActivity().finish();
                System.out.println("local image");
                return;
            }
            System.out.println("picasa image!");
        } else if (requestCode == REQUEST_CAMERA) {

            bitmap = decodeSampledBitmapFromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg").getAbsolutePath(), 500, 500);
            new SaveThread(bitmap, false).execute();
//            LogoMakerActivity.addCustomStickerView(bitmap);
//            getActivity().finish();
        }
    }

    public class SaveThread extends AsyncTask<Void, Void, Void> {
        Bitmap bitmap;
        private boolean share;

        public SaveThread(Bitmap bitmap, boolean share) {
            this.bitmap = bitmap;
            this.share = share;
            savingProcessing = new ProgressDialog(getActivity());
            savingProcessing.setMessage("Saving..");
            savingProcessing.show();
        }

        protected Void doInBackground(Void... params) {
//            linkSave = savePhoto(this.bitmap);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            savingProcessing.dismiss();
            if (linkSave.equals(BuildConfig.FLAVOR) || !this.share) {
                LogoMakerActivity.addCustomStickerView(bitmap);
                getActivity().finish();
                return;
            }
        }
    }


    public String savePhoto(Bitmap bmp) {
        Exception e;
        File imageFileName = new File(Environment.getExternalStorageDirectory(), "/temporary_holder_vintool_name_art.png");
        try {
            FileOutputStream out = new FileOutputStream(imageFileName);
            try {
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                scanPhoto(imageFileName.toString());
                return imageFileName.toString();
            } catch (Exception e2) {
                e = e2;
                FileOutputStream fileOutputStream = out;
                e.printStackTrace();
                return BuildConfig.FLAVOR;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return BuildConfig.FLAVOR;
        }
    }

    public void scanPhoto(final String imageFileName) {
        this.msConn = new MediaScannerConnection(getActivity(), new MediaScannerConnection.MediaScannerConnectionClient() {
            public void onMediaScannerConnected() {
                msConn.scanFile(imageFileName, null);
                Log.i("msClient obj", "connection established");
            }

            public void onScanCompleted(String path, Uri uri) {
                msConn.disconnect();
                Log.i("msClient obj", "scan completed");
            }
        });
        this.msConn.connect();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void getReadPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int hasWriteContactsPermission = getActivity().checkSelfPermission(Manifest.permission.CAMERA);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    showMessageOKCancel("You need to allow access to get Image From gallery",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                                            REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA},
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
                    Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                }).setCancelable(false)
                .create()
                .show();
    }


}
