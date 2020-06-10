package com.coolapps.logomaker.utilities;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Images.Media;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.exoplayer2.upstream.UdpDataSource;
import com.coolapps.logomaker.R;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import yuku.ambilwarna.BuildConfig;

public class PhotoEditor extends AppCompatActivity {
    public static final String LINK_PHOTO = "link_photo_es";
    private static String[] PERMISSIONS_STORAGE = null;
    private static final int REQUEST_CAMERA = 2;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int SELECT_PICTURE = 1;
    private static final int SELECT_PICTURE_BACKGROUND = 4;
    private static final int SELECT_PICTURE_MORE = 3;
    private static String[] dataObjects = new String[]{"demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png"};
    static int height;
    public static BubbleTextView mCurrentEditTextView;
    public static StickerView mCurrentView;
    static int width;
    private LinearLayout LayoutSave;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private ImageView frame;
//    private InterstitialAd interstitialAd;
    private boolean isFrame = true;
    boolean isRatioSquare = true;
    private Button layoutBtnCamera;
    private Button layoutBtnGallery;
    private Button layoutButton1;
    private Button layoutButton2;
    private Button layoutButton3;
    private Button layoutButton4;
    private Button layoutButtonRotateLeft;
    private Button layoutButtonRotateLeft90;
    private Button layoutButtonRotateRight;
    private Button layoutButtonRotateRight90;
    private FrameLayout layoutCustom;
    private LinearLayout layoutSelect;
    private LinearLayout layoutView;
    private String linkSave = BuildConfig.FLAVOR;
    private BaseAdapter mAdapter = new BaseAdapter() {
        private OnClickListener mOnButtonClicked = new OnClickListener() {
            public void onClick(View v) {
                int id = v.getId();
            }
        };

        public int getCount() {
            return 33;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem_square, null);
            ImageView image = retval.findViewById(R.id.imageFrame);
            image.setImageBitmap(PhotoEditor.getBitmapFromAsset(PhotoEditor.this, "ThumbFrameSmall/t1_" + position + ".png"));
            image.setId(position);
            return retval;
        }
    };
//    private BubbleInputDialog mBubbleInputDialog;
    private RelativeLayout mContentRootView;
    private File mFileTemp;
    private ArrayList<View> mViews;
    private RelativeLayout mainLayout;
    MediaScannerConnection msConn;
    private CustomView myView;
    private FrameLayout rootLayout;
    ProgressDialog savingProcessing;
    private String selectedImagePath;

    public class SaveThread extends AsyncTask<Void, Void, Void> {
        Bitmap bitmap;
        private boolean share;

        public SaveThread(Bitmap bitmap, boolean share) {
            this.bitmap = bitmap;
            this.share = share;
            PhotoEditor.this.savingProcessing = new ProgressDialog(PhotoEditor.this);
            PhotoEditor.this.savingProcessing.setMessage("Saving..");
            PhotoEditor.this.savingProcessing.show();
        }

        protected Void doInBackground(Void... params) {
            PhotoEditor.this.linkSave = PhotoEditor.this.savePhoto(this.bitmap);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            PhotoEditor.this.savingProcessing.dismiss();
            if (PhotoEditor.this.linkSave.equals(BuildConfig.FLAVOR) || !this.share) {
                Toast.makeText(PhotoEditor.this, "Save complete to LogoMaker folder", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PhotoEditor.this, OneFrameDone.class);
                intent.putExtra(PhotoEditor.LINK_PHOTO, PhotoEditor.this.linkSave);
                PhotoEditor.this.startActivityForResult(intent, UdpDataSource.DEFAULT_MAX_PACKET_SIZE);
                return;
            }
            Intent sharingIntent = new Intent("android.intent.action.SEND");
            sharingIntent.setType("image/*");
            sharingIntent.putExtra("android.intent.extra.SUBJECT", "Subject Here");
            sharingIntent.putExtra("android.intent.extra.TEXT", "Here is the share content body");
            sharingIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(PhotoEditor.this.linkSave)));
            PhotoEditor.this.startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(SELECT_PICTURE);
        getWindow().setFlags(1024, 1024);
        this.mainLayout = new RelativeLayout(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        this.mContentRootView = findViewById(R.id.rl_content_root);
        verifyStoragePermissions(this);
        this.rootLayout = findViewById(R.id.root_layout_single);
        this.rootLayout.setBackgroundColor(Color.parseColor("#e4e4e4"));
        int type = getIntent().getIntExtra("number_frame", 0);
        this.frame = new ImageView(this);
        this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_1.png"));
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        this.LayoutSave = new LinearLayout(this);
        LayoutParams layoutParamsB1 = new LayoutParams(width, width);
        this.LayoutSave.setX(0.0f);
        this.LayoutSave.setY((float) (height / 5));
        this.LayoutSave.setLayoutParams(layoutParamsB1);
        this.LayoutSave.setBackgroundColor(Color.parseColor("#ffffff"));
//        switch (type) {
//            case 1:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_1.png"));
//                break;
//            case 2:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_1.png"));
//                break;
//            case 3:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_2.png"));
//                break;
//            case 4:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_3.png"));
//                break;
//            case 5:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_4.png"));
//                break;
//            case 6:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_5.png"));
//                break;
//            case 7:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_6.png"));
//                break;
//            case 8:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_7.png"));
//                break;
//            case 9:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_8.png"));
//                break;
//            case 10:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_9.png"));
//                break;
//            case 11:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_10.png"));
//                break;
//            case 12:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_11.png"));
//                break;
//            case 13:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_12.png"));
//                break;
//            case 14:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_13.png"));
//                break;
//            case 15:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_14.png"));
//                break;
//            case 16:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_15.png"));
//                break;
//            case 17:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_16.png"));
//                break;
//            case 18:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_17.png"));
//                break;
//            case 19:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_18.png"));
//                break;
//            case 20:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_19.png"));
//                break;
//            case 21:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_20.png"));
//                break;
//            case 22:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_21.png"));
//                break;
//            case 23:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_22.png"));
//                break;
//            case 24:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_23.png"));
//                break;
//            case 25:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_24.png"));
//                break;
//            case 26:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_25.png"));
//                break;
//            case 27:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_26.png"));
//                break;
//            case 28:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_27.png"));
//                break;
//            case 29:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_28.png"));
//                break;
//            case 30:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_29.png"));
//                break;
//            case 31:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_30.png"));
//                break;
//            case 32:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_31.png"));
//                break;
//            case 33:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_32.png"));
//                break;
//            case 34:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_33.png"));
//                break;
//            case 35:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_34.png"));
//                break;
//            case 36:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_35.png"));
//                break;
//            case 37:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_36.png"));
//                break;
//            case 38:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_37.png"));
//                break;
//            case 39:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_38.png"));
//                break;
//            case 40:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_39.png"));
//                break;
//            case 41:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_40.png"));
//                break;
//            case 42:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_41.png"));
//                break;
//            case 43:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_42.png"));
//                break;
//            case 44:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_43.png"));
//                break;
//            case 45:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_44.png"));
//                break;
//            case 46:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_45.png"));
//                break;
//            case 47:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_46.png"));
//                break;
//            case 48:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_47.png"));
//                break;
//            case 49:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_48.png"));
//                break;
//            case 50:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_49.png"));
//                break;
//            case 51:
//                this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_50.png"));
//                break;
//        }
        this.myView = findViewById(R.id.bk1);
        this.layoutView = new LinearLayout(this);
        LayoutParams layoutParamsView = new LayoutParams(width, width);
        this.layoutView.setX(0.0f);
        this.layoutView.setY(0.0f);
        this.layoutView.setLayoutParams(layoutParamsView);
        this.layoutView.setBackgroundColor(Color.parseColor("#98cfaf"));
        this.layoutCustom = findViewById(R.id.layout_view);
        this.layoutCustom.setY((float) (height / 6));
        this.layoutCustom.setX(0.0f);
        this.layoutCustom.getLayoutParams().width = width;
        this.layoutCustom.getLayoutParams().height = width;
//        this.layoutButton1 = new Button(this);
//        LayoutParams lPB1 = new LayoutParams(((height / 12) * 400) / 237, height / 12);
//        this.layoutBtnGallery = new Button(this);
//        this.layoutBtnGallery.setX((float) (((width / SELECT_PICTURE_BACKGROUND) - (((height / 12) * 400) / 237)) / REQUEST_CAMERA));
//        this.layoutBtnGallery.setY(0.0f);
//        this.layoutBtnGallery.setLayoutParams(lPB1);
//        this.layoutBtnGallery.setBackgroundResource(R.drawable.btn1);
//        this.layoutBtnGallery.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                PhotoEditor.this.loadImage();
//            }
//        });
//        this.layoutBtnCamera = new Button(this);
//        this.layoutBtnCamera.setX((float) ((width / SELECT_PICTURE_BACKGROUND) + (((width / SELECT_PICTURE_BACKGROUND) - (((height / 12) * 400) / 237)) / REQUEST_CAMERA)));
//        this.layoutBtnCamera.setY(0.0f);
//        this.layoutBtnCamera.setLayoutParams(lPB1);
//        this.layoutBtnCamera.setBackgroundResource(R.drawable.btn2);
//        this.layoutBtnCamera.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent.putExtra("output", Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg")));
//                PhotoEditor.this.startActivityForResult(intent, PhotoEditor.REQUEST_CAMERA);
//            }
//        });
//        this.layoutButton3 = new Button(this);
//        this.layoutButton3.setX((float) ((width / REQUEST_CAMERA) + (((width / SELECT_PICTURE_BACKGROUND) - (((height / 12) * 400) / 237)) / REQUEST_CAMERA)));
//        this.layoutButton3.setY(0.0f);
//        this.layoutButton3.setLayoutParams(lPB1);
//        this.layoutButton3.setBackgroundResource(R.drawable.btn3);
//        this.layoutButton3.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                if (PhotoEditor.mCurrentView != null) {
//                    PhotoEditor.mCurrentView.setInEdit(false);
//                }
//                if (PhotoEditor.mCurrentEditTextView != null) {
//                    PhotoEditor.mCurrentEditTextView.setInEdit(false);
//                }
//                new SaveThread(PhotoEditor.getBitmapFromView(PhotoEditor.this.layoutCustom), false).execute(new Void[0]);
//            }
//        });
//        this.layoutButton4 = new Button(this);
//        this.layoutButton4.setX((float) (((width * SELECT_PICTURE_MORE) / SELECT_PICTURE_BACKGROUND) + (((width / SELECT_PICTURE_BACKGROUND) - (((height / 12) * 400) / 237)) / REQUEST_CAMERA)));
//        this.layoutButton4.setY(0.0f);
//        this.layoutButton4.setLayoutParams(lPB1);
//        this.layoutButton4.setBackgroundResource(R.drawable.btn4);
//        this.layoutButton4.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                if (PhotoEditor.mCurrentView != null) {
//                    PhotoEditor.mCurrentView.setInEdit(false);
//                }
//                if (PhotoEditor.mCurrentEditTextView != null) {
//                    PhotoEditor.mCurrentEditTextView.setInEdit(false);
//                }
//                new SaveThread(PhotoEditor.getBitmapFromView(PhotoEditor.this.layoutCustom), true).execute(new Void[0]);
//            }
//        });
//        this.layoutButtonRotateLeft = new Button(this);
//        LayoutParams lPB2 = new LayoutParams(((height / 13) * 400) / 218, height / 13);
//        this.layoutButtonRotateLeft.setX((float) (((width * SELECT_PICTURE) / SELECT_PICTURE_BACKGROUND) + ((((width * SELECT_PICTURE) / SELECT_PICTURE_BACKGROUND) - (((height / 13) * 400) / 218)) / REQUEST_CAMERA)));
//        this.layoutButtonRotateLeft.setY((float) (height - (height / 12)));
//        this.layoutButtonRotateLeft.setLayoutParams(lPB2);
//        this.layoutButtonRotateLeft.setBackgroundResource(R.drawable.btn9);
//        this.layoutButtonRotateLeft.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                PhotoEditor.this.startActivity(new Intent(PhotoEditor.this, ListSticker.class));
//            }
//        });
//        this.layoutButtonRotateRight = new Button(this);
//        this.layoutButtonRotateRight.setX((float) (((width * REQUEST_CAMERA) / SELECT_PICTURE_BACKGROUND) + ((((width * SELECT_PICTURE) / SELECT_PICTURE_BACKGROUND) - (((height / 13) * 400) / 218)) / REQUEST_CAMERA)));
//        this.layoutButtonRotateRight.setY((float) (height - (height / 12)));
//        this.layoutButtonRotateRight.setLayoutParams(lPB2);
//        this.layoutButtonRotateRight.setBackgroundResource(R.drawable.btn17);
//        this.layoutButtonRotateRight.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                boolean z = true;
//                if (PhotoEditor.this.isRatioSquare) {
//                    PhotoEditor.this.layoutButtonRotateRight.setBackgroundResource(R.drawable.btn16);
//                    PhotoEditor.this.layoutCustom.setY((float) (PhotoEditor.height / 12));
//                    PhotoEditor.this.layoutCustom.setX((float) ((PhotoEditor.width - ((int) (((double) PhotoEditor.width) * 0.9d))) / PhotoEditor.REQUEST_CAMERA));
//                    PhotoEditor.this.layoutCustom.getLayoutParams().width = (int) (((double) PhotoEditor.width) * 0.9d);
//                    PhotoEditor.this.layoutCustom.getLayoutParams().height = (((int) (((double) PhotoEditor.width) * 0.9d)) * 16) / 12;
//                    PhotoEditor.this.layoutButtonRotateRight.setVisibility(View.GONE);
//                    PhotoEditor.this.layoutButtonRotateRight.setVisibility(View.VISIBLE);
//                } else {
//                    PhotoEditor.this.layoutButtonRotateRight.setBackgroundResource(R.drawable.btn17);
//                    PhotoEditor.this.layoutCustom.setX(0.0f);
//                    PhotoEditor.this.layoutCustom.setY((float) (PhotoEditor.height / 6));
//                    PhotoEditor.this.layoutCustom.getLayoutParams().width = PhotoEditor.width;
//                    PhotoEditor.this.layoutCustom.getLayoutParams().height = PhotoEditor.width;
//                    PhotoEditor.this.layoutButtonRotateRight.setVisibility(View.GONE);
//                    PhotoEditor.this.layoutButtonRotateRight.setVisibility(View.VISIBLE);
//                }
//                PhotoEditor photoEditor = PhotoEditor.this;
//                if (PhotoEditor.this.isRatioSquare) {
//                    z = false;
//                }
//                photoEditor.isRatioSquare = z;
//            }
//        });
//        this.layoutButtonRotateRight90 = new Button(this);
//        this.layoutButtonRotateRight90.setX((float) (((width * SELECT_PICTURE_MORE) / SELECT_PICTURE_BACKGROUND) + ((((width * SELECT_PICTURE) / SELECT_PICTURE_BACKGROUND) - (((height / 13) * 400) / 218)) / REQUEST_CAMERA)));
//        this.layoutButtonRotateRight90.setY((float) (height - (height / 12)));
//        this.layoutButtonRotateRight90.setLayoutParams(lPB2);
//        this.layoutButtonRotateRight90.setBackgroundResource(R.drawable.btn11);
//        this.layoutButtonRotateRight90.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
////                PhotoEditor.this.startActivity(new Intent(PhotoEditor.this, ListBubble.class));
//            }
//        });
        HorizontalListView listview = findViewById(R.id.listview);
        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PhotoEditor.this.frame.setImageBitmap(PhotoEditor.getBitmapFromAsset(PhotoEditor.this, "Frame/f1_" + position + ".png"));
            }
        });
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = ((float) displayMetrics.heightPixels) / displayMetrics.density;
        float dpWidth = ((float) displayMetrics.widthPixels) / displayMetrics.density;
        listview.setAdapter(this.mAdapter);
        listview.setBackgroundColor(Color.parseColor("#ffffff"));
        listview.setY(((float) (height - (height / 13))) - (75.0f * (((float) height) / dpHeight)));
        listview.setVisibility(View.GONE);
//        this.btn1 = new Button(this);
//        this.btn1.setX((float) (((width * 0) / SELECT_PICTURE_BACKGROUND) + ((((width * SELECT_PICTURE) / SELECT_PICTURE_BACKGROUND) - (((height / 13) * 400) / 218)) / REQUEST_CAMERA)));
//        this.btn1.setY(((float) (height - (height / 13))) - (75.0f * (((float) height) / dpHeight)));
//        this.btn1.setLayoutParams(lPB2);
//        this.btn1.setBackgroundResource(R.drawable.btn8);
//        this.btn1.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                PhotoEditor.this.myView.rotateBitmap90(-1);
//            }
//        });
//        this.btn2 = new Button(this);
//        this.btn2.setX((float) (((width * SELECT_PICTURE) / SELECT_PICTURE_BACKGROUND) + ((((width * SELECT_PICTURE) / SELECT_PICTURE_BACKGROUND) - (((height / 13) * 400) / 218)) / REQUEST_CAMERA)));
//        this.btn2.setY(((float) (height - (height / 13))) - (75.0f * (((float) height) / dpHeight)));
//        this.btn2.setLayoutParams(lPB2);
//        this.btn2.setBackgroundResource(R.drawable.btn5);
//        this.btn2.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                PhotoEditor.this.myView.rotateBitmap(-1);
//            }
//        });
//        this.btn3 = new Button(this);
//        this.btn3.setX((float) (((width * REQUEST_CAMERA) / SELECT_PICTURE_BACKGROUND) + ((((width * SELECT_PICTURE) / SELECT_PICTURE_BACKGROUND) - (((height / 13) * 400) / 218)) / REQUEST_CAMERA)));
//        this.btn3.setY(((float) (height - (height / 13))) - (75.0f * (((float) height) / dpHeight)));
//        this.btn3.setLayoutParams(lPB2);
//        this.btn3.setBackgroundResource(R.drawable.btn6);
//        this.btn3.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                PhotoEditor.this.myView.rotateBitmap(PhotoEditor.SELECT_PICTURE);
//            }
//        });
//        this.btn4 = new Button(this);
//        this.btn4.setX((float) (((width * SELECT_PICTURE_MORE) / SELECT_PICTURE_BACKGROUND) + ((((width * SELECT_PICTURE) / SELECT_PICTURE_BACKGROUND) - (((height / 13) * 400) / 218)) / REQUEST_CAMERA)));
//        this.btn4.setY(((float) (height - (height / 13))) - (75.0f * (((float) height) / dpHeight)));
//        this.btn4.setLayoutParams(lPB2);
//        this.btn4.setBackgroundResource(R.drawable.btn7);
//        this.btn4.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                PhotoEditor.this.myView.rotateBitmap90(PhotoEditor.SELECT_PICTURE);
//            }
//        });
//        this.layoutButtonRotateLeft90 = new Button(this);
//        this.layoutButtonRotateLeft90.setX((float) (((width * 0) / SELECT_PICTURE_BACKGROUND) + ((((width * SELECT_PICTURE) / SELECT_PICTURE_BACKGROUND) - (((height / 13) * 400) / 218)) / REQUEST_CAMERA)));
//        this.layoutButtonRotateLeft90.setY((float) (height - (height / 12)));
//        this.layoutButtonRotateLeft90.setLayoutParams(lPB2);
//        this.layoutButtonRotateLeft90.setBackgroundColor(Color.parseColor("#ffffff"));
//        this.layoutButtonRotateLeft90.setBackgroundResource(R.drawable.btn12);
//        this.layoutButtonRotateLeft90.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                boolean z = true;
//                if (PhotoEditor.this.isFrame) {
//                    PhotoEditor.this.btn1.setVisibility(View.VISIBLE);
//                    PhotoEditor.this.btn2.setVisibility(View.VISIBLE);
//                    PhotoEditor.this.btn3.setVisibility(View.VISIBLE);
//                    PhotoEditor.this.btn4.setVisibility(View.VISIBLE);
//                } else {
//                    PhotoEditor.this.btn1.setVisibility(View.GONE);
//                    PhotoEditor.this.btn2.setVisibility(View.GONE);
//                    PhotoEditor.this.btn3.setVisibility(View.GONE);
//                    PhotoEditor.this.btn4.setVisibility(View.GONE);
//                }
//                PhotoEditor photoEditor = PhotoEditor.this;
//                if (PhotoEditor.this.isFrame) {
//                    z = false;
//                }
//                photoEditor.isFrame = z;
//            }
//        });
//        this.btn1.setVisibility(View.GONE);
//        this.btn2.setVisibility(View.GONE);
//        this.btn3.setVisibility(View.GONE);
//        this.btn4.setVisibility(View.GONE);
//        this.rootLayout.addView(this.layoutBtnCamera);
//        this.rootLayout.addView(this.layoutBtnGallery);
//        this.rootLayout.addView(this.layoutButton3);
//        this.rootLayout.addView(this.layoutButton4);
//        this.rootLayout.addView(this.layoutButtonRotateLeft);
//        this.rootLayout.addView(this.layoutButtonRotateRight);
//        this.rootLayout.addView(this.layoutButtonRotateLeft90);
//        this.rootLayout.addView(this.layoutButtonRotateRight90);
//        this.rootLayout.addView(this.btn1);
//        this.rootLayout.addView(this.btn2);
//        this.rootLayout.addView(this.btn3);
//        this.rootLayout.addView(this.btn4);
//        this.layoutBtnGallery.setVisibility(View.GONE);
//        this.layoutBtnCamera.setVisibility(View.GONE);
//        this.layoutButton3.setVisibility(View.GONE);
//        this.layoutButton4.setVisibility(View.GONE);
//        this.layoutButtonRotateLeft.setVisibility(View.GONE);
//        this.layoutButtonRotateRight.setVisibility(View.GONE);
//        this.layoutButtonRotateLeft90.setVisibility(View.GONE);
//        this.layoutButtonRotateRight90.setVisibility(View.GONE);
//        this.mViews = new ArrayList();
//        this.mBubbleInputDialog = new BubbleInputDialog(this);
//        this.mBubbleInputDialog.setCompleteCallBack(new BubbleInputDialog.CompleteCallBack() {
//            public void onComplete(View bubbleTextView, String str) {
//                ((BubbleTextView) bubbleTextView).setText(str);
//            }
//        });
//        LayoutParams lSelect = new LayoutParams(width / SELECT_PICTURE_MORE, height / 10);
//        this.layoutButton1.setX((float) ((width / REQUEST_CAMERA) - (width / 6)));
//        this.layoutButton1.setY((((float) (height / REQUEST_CAMERA)) - (10.0f * (((float) height) / dpHeight))) - ((float) (height / 10)));
//        this.layoutButton1.setLayoutParams(lSelect);
//        this.layoutButton1.setBackgroundResource(R.drawable.btn_select_gallery);
//        this.layoutButton1.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                PhotoEditor.this.startActivityForResult(Intent.createChooser(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), "Select Picture"), PhotoEditor.SELECT_PICTURE_BACKGROUND);
//            }
//        });
//        this.layoutButton2 = new Button(this);
//        this.layoutButton2.setX(0.0f);
//        this.layoutButton2.setY(((float) (height / REQUEST_CAMERA)) + (10.0f * (((float) height) / dpHeight)));
//        this.layoutButton2.setLayoutParams(lSelect);
//        this.layoutButton2.setBackgroundResource(R.drawable.btn_select_camera);
//        this.layoutButton2.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent.putExtra("output", Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg")));
//                PhotoEditor.this.startActivityForResult(intent, PhotoEditor.REQUEST_CAMERA);
//            }
//        });
//        LayoutParams layoutParams = new LayoutParams(width, height);
//        this.layoutSelect = new LinearLayout(this);
//        this.layoutSelect.setBackgroundColor(Color.parseColor("#e4e4e4"));
//        this.layoutSelect.setLayoutParams(layoutParams);
//        this.layoutSelect.addView(this.layoutButton1);
//        this.layoutSelect.addView(this.layoutButton2);
//        this.rootLayout.addView(this.layoutSelect);
    }


    public void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int name = preferences.getInt("com.vt.na.sticker_id", -1);
        if (preferences.getInt("com.vt.na.sticker_id.true", -1) == SELECT_PICTURE) {
            addStickerView(getBitmapFromAsset(this, "Sticker/sticker_" + name + ".png"));
        }
        Editor editor = preferences.edit();
        editor.putInt("com.vt.na.sticker_id.true", -1);
        editor.apply();
        SharedPreferences preferencesBubble = PreferenceManager.getDefaultSharedPreferences(this);
        int nameBubble = preferences.getInt("com.vt.na.bubble_id", -1);
        int name_trueBubble = preferences.getInt("com.vt.na.bubble_id.true", -1);
        int nameFont = preferences.getInt("com.vt.na.font_id", -1);
        if (name_trueBubble == SELECT_PICTURE) {
            if (nameFont == 0 || nameFont == SELECT_PICTURE || nameFont == SELECT_PICTURE_MORE || nameFont == SELECT_PICTURE_BACKGROUND || nameFont == 5 || nameFont == 7 || nameFont == 8 || nameFont == 9 || nameFont == 11 || nameFont == 12 || nameFont == 15 || nameFont == 18) {
                addBubble(getBitmapFromAsset(this, "Bubble/bubble_" + nameBubble + ".png"), Typeface.createFromAsset(getAssets(), "fonts/font_ori_" + nameFont + ".ttf"));
            } else {
                addBubble(getBitmapFromAsset(this, "Bubble/bubble_" + nameBubble + ".png"), Typeface.createFromAsset(getAssets(), "fonts/font_ori_" + nameFont + ".otf"));
            }
        }
        Editor editorBubble = preferences.edit();
        editorBubble.putInt("com.vt.na.bubble_id.true", -1);
        editorBubble.apply();
    }

    private void addStickerView(Bitmap b) {
        final StickerView stickerView = new StickerView(this);
        stickerView.setImageBitmap(b);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            public void onDeleteClick() {
                PhotoEditor.this.mViews.remove(stickerView);
                PhotoEditor.this.layoutCustom.removeView(stickerView);
            }

            public void onEdit(StickerView stickerView) {
                if (PhotoEditor.mCurrentEditTextView != null) {
                    PhotoEditor.mCurrentEditTextView.setInEdit(false);
                }
                PhotoEditor.mCurrentView.setInEdit(false);
                PhotoEditor.mCurrentView = stickerView;
                PhotoEditor.mCurrentView.setInEdit(true);
            }

            public void onTop(StickerView stickerView) {
                int position = PhotoEditor.this.mViews.indexOf(stickerView);
                if (position != PhotoEditor.this.mViews.size() - 1) {
                    PhotoEditor.this.mViews.add(PhotoEditor.this.mViews.size(), PhotoEditor.this.mViews.remove(position));
                }
            }
        });
        this.layoutCustom.addView(stickerView, new RelativeLayout.LayoutParams(-1, -1));
        this.mViews.add(stickerView);
        setCurrentEdit(stickerView);
    }

    private void setCurrentEdit(StickerView stickerView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }
        if (mCurrentEditTextView != null) {
            mCurrentEditTextView.setInEdit(false);
        }
        mCurrentView = stickerView;
        stickerView.setInEdit(true);
    }

    private void setCurrentEdit(BubbleTextView bubbleTextView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }
        if (mCurrentEditTextView != null) {
            mCurrentEditTextView.setInEdit(false);
        }
        mCurrentEditTextView = bubbleTextView;
        mCurrentEditTextView.setInEdit(true);
    }

    private void addBubble(Bitmap b, Typeface type) {
        final BubbleTextView bubbleTextView = new BubbleTextView(this, -1, 0);
        bubbleTextView.setFont(type);
        bubbleTextView.setImageBitmap(b);
        bubbleTextView.setOperationListener(new BubbleTextView.OperationListener() {
            public void onDeleteClick() {
                PhotoEditor.this.mViews.remove(bubbleTextView);
                PhotoEditor.this.layoutCustom.removeView(bubbleTextView);
            }

            public void onEdit(BubbleTextView bubbleTextView) {
                if (PhotoEditor.mCurrentView != null) {
                    PhotoEditor.mCurrentView.setInEdit(false);
                }
                PhotoEditor.mCurrentEditTextView.setInEdit(false);
                PhotoEditor.mCurrentEditTextView = bubbleTextView;
                PhotoEditor.mCurrentEditTextView.setInEdit(true);
            }

            public void onClick(BubbleTextView bubbleTextView) {
//                PhotoEditor.this.mBubbleInputDialog.setBubbleTextView(bubbleTextView);
//                PhotoEditor.this.mBubbleInputDialog.show();
            }

            public void onTop(BubbleTextView bubbleTextView) {
                int position = PhotoEditor.this.mViews.indexOf(bubbleTextView);
                if (position != PhotoEditor.this.mViews.size() - 1) {
                    PhotoEditor.this.mViews.add(PhotoEditor.this.mViews.size(), PhotoEditor.this.mViews.remove(position));
                }
            }
        });
        this.layoutCustom.addView(bubbleTextView, new RelativeLayout.LayoutParams(-1, -1));
        this.mViews.add(bubbleTextView);
        setCurrentEdit(bubbleTextView);
    }

    private void addStickerView() {
        final StickerView stickerView = new StickerView(this);
//        stickerView.setImageResource(R.drawable.button1);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            public void onDeleteClick() {
                PhotoEditor.this.mViews.remove(stickerView);
                PhotoEditor.this.layoutCustom.removeView(stickerView);
            }

            public void onEdit(StickerView stickerView) {
                if (PhotoEditor.mCurrentEditTextView != null) {
                    PhotoEditor.mCurrentEditTextView.setInEdit(false);
                }
                PhotoEditor.mCurrentView.setInEdit(false);
                PhotoEditor.mCurrentView = stickerView;
                PhotoEditor.mCurrentView.setInEdit(true);
            }

            public void onTop(StickerView stickerView) {
                int position = PhotoEditor.this.mViews.indexOf(stickerView);
                if (position != PhotoEditor.this.mViews.size() - 1) {
                    PhotoEditor.this.mViews.add(PhotoEditor.this.mViews.size(), PhotoEditor.this.mViews.remove(position));
                }
            }
        });
        this.layoutCustom.addView(stickerView, new RelativeLayout.LayoutParams(-1, -1));
        this.mViews.add(stickerView);
        setCurrentEdit(stickerView);
    }

    static {
        String[] strArr = new String[REQUEST_CAMERA];
        strArr[0] = "android.permission.READ_EXTERNAL_STORAGE";
        strArr[SELECT_PICTURE] = "android.permission.WRITE_EXTERNAL_STORAGE";
        PERMISSIONS_STORAGE = strArr;
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getAssets().open(filePath));
        } catch (IOException e) {
        }
        return bitmap;
    }

    public void loadImage() {
        startActivityForResult(Intent.createChooser(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), "Select Picture"), SELECT_PICTURE);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        try {
            destination.createNewFile();
            FileOutputStream fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        this.myView.addImage(thumbnail);
    }

    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i += SELECT_PICTURE) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(-1);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    public String savePhoto(Bitmap bmp) {
        Exception e;
        File imageFileFolder = new File(Environment.getExternalStorageDirectory(), "suitFrames");
        imageFileFolder.mkdir();
        Calendar c = Calendar.getInstance();
        @SuppressLint("WrongConstant") File imageFileName = new File(imageFileFolder, (fromInt(c.get(REQUEST_CAMERA)) + fromInt(c.get(5)) + fromInt(c.get(SELECT_PICTURE)) + fromInt(c.get(11)) + fromInt(c.get(12)) + fromInt(c.get(13))) + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(imageFileName);
            try {
                bmp.compress(CompressFormat.JPEG, 100, out);
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

    public String fromInt(int val) {
        return String.valueOf(val);
    }

    public void scanPhoto(final String imageFileName) {
        this.msConn = new MediaScannerConnection(this, new MediaScannerConnectionClient() {
            public void onMediaScannerConnected() {
                PhotoEditor.this.msConn.scanFile(imageFileName, null);
                Log.i("msClient obj", "connection established");
            }

            public void onScanCompleted(String path, Uri uri) {
                PhotoEditor.this.msConn.disconnect();
                Log.i("msClient obj", "scan completed");
            }
        });
        this.msConn.connect();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == UdpDataSource.DEFAULT_MAX_PACKET_SIZE && requestCode == UdpDataSource.DEFAULT_MAX_PACKET_SIZE) {
            finish();
        }
        if (resultCode == 4000 && requestCode == UdpDataSource.DEFAULT_MAX_PACKET_SIZE) {
            setResult(4000);
            finish();
        }
        if (resultCode != -1) {
            return;
        }
        Uri selectedImageUri;
        Bitmap bitmap;
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
                Log.d("Wid", width + BuildConfig.FLAVOR);
                this.myView.addImage(bitmap);
                System.out.println("local image");
                return;
            }
            System.out.println("picasa image!");
        } else if (requestCode == REQUEST_CAMERA) {
            this.layoutSelect.setVisibility(View.GONE);
            this.layoutBtnGallery.setVisibility(View.VISIBLE);
            this.layoutBtnCamera.setVisibility(View.VISIBLE);
            this.layoutButton3.setVisibility(View.VISIBLE);
            this.layoutButton4.setVisibility(View.VISIBLE);
            this.layoutButtonRotateLeft.setVisibility(View.VISIBLE);
            this.layoutButtonRotateRight.setVisibility(View.VISIBLE);
            this.layoutButtonRotateLeft90.setVisibility(View.VISIBLE);
            this.layoutButtonRotateRight90.setVisibility(View.VISIBLE);
            this.myView.addImage(decodeSampledBitmapFromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg").getAbsolutePath(), 1000, 700));
        } else if (requestCode == SELECT_PICTURE_MORE) {
            selectedImageUri = data.getData();
            Log.d("URI VAL", "selectedImageUri = " + selectedImageUri.toString());
            this.selectedImagePath = getPath(selectedImageUri);
            if (this.selectedImagePath != null) {
                bitmap = BitmapFactory.decodeFile(this.selectedImagePath);
                f = ((float) bitmap.getWidth()) / ((float) bitmap.getHeight());
                if (bitmap.getWidth() > width) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, (int) (((float) width) / f), false);
                }
                addStickerView(bitmap);
                System.out.println("local image");
                return;
            }
            System.out.println("picasa image!");
        } else if (requestCode == SELECT_PICTURE_BACKGROUND) {
            selectedImageUri = data.getData();
            Log.d("URI VAL", "selectedImageUri = " + selectedImageUri.toString());
            this.selectedImagePath = getPath(selectedImageUri);
            if (this.selectedImagePath != null) {
                bitmap = BitmapFactory.decodeFile(this.selectedImagePath);
                f = ((float) bitmap.getWidth()) / ((float) bitmap.getHeight());
                if (bitmap.getWidth() > width) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, (int) (((float) width) / f), false);
                }
                this.layoutSelect.setVisibility(View.GONE);
                this.layoutBtnGallery.setVisibility(View.VISIBLE);
                this.layoutBtnCamera.setVisibility(View.VISIBLE);
                this.layoutButton3.setVisibility(View.VISIBLE);
                this.layoutButton4.setVisibility(View.VISIBLE);
                this.layoutButtonRotateLeft.setVisibility(View.VISIBLE);
                this.layoutButtonRotateRight.setVisibility(View.VISIBLE);
                this.layoutButtonRotateLeft90.setVisibility(View.VISIBLE);
                this.layoutButtonRotateRight90.setVisibility(View.VISIBLE);
                this.myView.addImage(bitmap);
                System.out.println("local image");
                return;
            }
            System.out.println("picasa image!");
        }
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int height = options.outHeight;
        int width = options.outWidth;
        options.inPreferredConfig = Config.RGB_565;
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
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        }
        cursor.moveToFirst();
        String filePath = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
        cursor.close();
        return filePath;
    }

    public static void verifyStoragePermissions(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, SELECT_PICTURE);
        }
    }
}
