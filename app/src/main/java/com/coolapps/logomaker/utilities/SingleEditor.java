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

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.BuildConfig;

public class SingleEditor extends AppCompatActivity {
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
    int color = -5491902;
    private ImageView frame;
    private boolean isFrame = true;
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
                SingleEditor.this.frame.setImageBitmap(SingleEditor.getBitmapFromAsset(SingleEditor.this, "Frame/f1_" + v.getId() + ".png"));
            }
        };

        public int getCount() {
            return 20;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem, null);
            ImageView image = retval.findViewById(R.id.imageFrame);
            image.setImageBitmap(SingleEditor.getBitmapFromAsset(SingleEditor.this, "ThumbFrameSmall/background_thumb_" + position + ".png"));
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
            SingleEditor.this.savingProcessing = new ProgressDialog(SingleEditor.this);
            SingleEditor.this.savingProcessing.setMessage("Saving..");
            SingleEditor.this.savingProcessing.show();
        }

        protected Void doInBackground(Void... params) {
            SingleEditor.this.linkSave = SingleEditor.this.savePhoto(this.bitmap);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            SingleEditor.this.savingProcessing.dismiss();
            if (SingleEditor.this.linkSave.equals(BuildConfig.FLAVOR) || !this.share) {
                Toast.makeText(SingleEditor.this, "Save complete to LogoMaker folder", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SingleEditor.this, FreeCollageDone.class);
                intent.putExtra(SingleEditor.LINK_PHOTO, SingleEditor.this.linkSave);
                SingleEditor.this.startActivityForResult(intent, UdpDataSource.DEFAULT_MAX_PACKET_SIZE);
                return;
            }
            Intent sharingIntent = new Intent("android.intent.action.SEND");
            sharingIntent.setType("image/*");
            sharingIntent.putExtra("android.intent.extra.SUBJECT", "Subject Here");
            sharingIntent.putExtra("android.intent.extra.TEXT", "Here is the share content body");
            sharingIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(SingleEditor.this.linkSave)));
            SingleEditor.this.startActivity(Intent.createChooser(sharingIntent, "Share via"));
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
        this.rootLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        int type = getIntent().getIntExtra("number_frame", 0);
        this.frame = new ImageView(this);
        this.frame.setImageBitmap(getBitmapFromAsset(this, "Background/background_0.png"));
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        this.LayoutSave = new LinearLayout(this);
        LayoutParams layoutParamsB1 = new LayoutParams(width, (width * 16) / 12);
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
        LayoutParams layoutParamsView = new LayoutParams(width, (width * 16) / 12);
        this.layoutView.setX(0.0f);
        this.layoutView.setY(0.0f);
        this.layoutView.setLayoutParams(layoutParamsView);
        this.layoutView.setBackgroundColor(Color.parseColor("#98cfaf"));
        this.layoutCustom = findViewById(R.id.layout_view);
        this.layoutCustom.setY((float) (height / 10));
        this.layoutCustom.setX((float) (((int) (((double) width) - (0.95d * ((double) width)))) / REQUEST_CAMERA));
        this.layoutCustom.getLayoutParams().width = (int) (0.95d * ((double) width));
        this.layoutCustom.getLayoutParams().height = (int) (((0.95d * ((double) width)) * 16.0d) / 12.0d);
//        this.frame.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                if (SingleEditor.mCurrentView != null) {
//                    SingleEditor.mCurrentView.setInEdit(false);
//                }
//                if (SingleEditor.mCurrentEditTextView != null) {
//                    SingleEditor.mCurrentEditTextView.setInEdit(false);
//                }
//            }
//        });
//        this.layoutCustom.addView(this.frame);
//        this.layoutButton1 = new Button(this);
//        LayoutParams lPB1 = new LayoutParams(((height / 12) * 400) / 237, height / 12);
//        this.layoutButton3 = new Button(this);
//        this.layoutButton3.setX((float) (width / REQUEST_CAMERA));
//        this.layoutButton3.setY(0.0f);
//        this.layoutButton3.setLayoutParams(lPB1);
//        this.layoutButton3.setBackgroundResource(R.drawable.btn3);
//        this.layoutButton3.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                if (SingleEditor.mCurrentView != null) {
//                    SingleEditor.mCurrentView.setInEdit(false);
//                }
//                if (SingleEditor.mCurrentEditTextView != null) {
//                    SingleEditor.mCurrentEditTextView.setInEdit(false);
//                }
//                new SaveThread(SingleEditor.getBitmapFromView(SingleEditor.this.layoutCustom), false).execute(new Void[0]);
//            }
//        });
//        this.layoutButton4 = new Button(this);
//        this.layoutButton4.setX((float) ((width * SELECT_PICTURE_MORE) / SELECT_PICTURE_BACKGROUND));
//        this.layoutButton4.setY(0.0f);
//        this.layoutButton4.setLayoutParams(lPB1);
//        this.layoutButton4.setBackgroundResource(R.drawable.btn4);
//        this.layoutButton4.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                if (SingleEditor.mCurrentView != null) {
//                    SingleEditor.mCurrentView.setInEdit(false);
//                }
//                if (SingleEditor.mCurrentEditTextView != null) {
//                    SingleEditor.mCurrentEditTextView.setInEdit(false);
//                }
//                new SaveThread(SingleEditor.getBitmapFromView(SingleEditor.this.layoutCustom), true).execute(new Void[0]);
//            }
//        });
//        this.layoutButtonRotateLeft = new Button(this);
//        LayoutParams lPB2 = new LayoutParams(width / SELECT_PICTURE_BACKGROUND, height / 13);
//        this.layoutButtonRotateLeft.setX((float) ((width * SELECT_PICTURE) / SELECT_PICTURE_BACKGROUND));
//        this.layoutButtonRotateLeft.setY((float) (height - (height / 12)));
//        this.layoutButtonRotateLeft.setLayoutParams(lPB2);
//        this.layoutButtonRotateLeft.setBackgroundResource(R.drawable.btn9);
//        this.layoutButtonRotateLeft.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                SingleEditor.this.startActivity(new Intent(SingleEditor.this, ListSticker.class));
//            }
//        });
//        this.layoutButtonRotateRight = new Button(this);
//        this.layoutButtonRotateRight.setX((float) ((width * REQUEST_CAMERA) / SELECT_PICTURE_BACKGROUND));
//        this.layoutButtonRotateRight.setY((float) (height - (height / 12)));
//        this.layoutButtonRotateRight.setLayoutParams(lPB2);
//        this.layoutButtonRotateRight.setBackgroundResource(R.drawable.btn15);
//        this.layoutButtonRotateRight.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                SingleEditor.this.startActivityForResult(Intent.createChooser(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), "Select Picture"), SingleEditor.SELECT_PICTURE_MORE);
//            }
//        });
//        this.layoutButtonRotateRight90 = new Button(this);
//        this.layoutButtonRotateRight90.setX((float) ((width * SELECT_PICTURE_MORE) / SELECT_PICTURE_BACKGROUND));
//        this.layoutButtonRotateRight90.setY((float) (height - (height / 12)));
//        this.layoutButtonRotateRight90.setLayoutParams(lPB2);
//        this.layoutButtonRotateRight90.setBackgroundResource(R.drawable.btn_select_camera);
//        this.layoutButtonRotateRight90.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
////                SingleEditor.this.startActivity(new Intent(SingleEditor.this, ListBubble.class));
//            }
//        });
        HorizontalListView listview = findViewById(R.id.listview);
        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                SingleEditor.this.frame.setImageBitmap(SingleEditor.getBitmapFromAsset(SingleEditor.this, "Background/background_" + position + ".png"));
            }
        });
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = ((float) displayMetrics.heightPixels) / displayMetrics.density;
        float dpWidth = ((float) displayMetrics.widthPixels) / displayMetrics.density;
        listview.setAdapter(this.mAdapter);
        listview.setBackgroundColor(Color.parseColor("#ffffff"));
        listview.setY(((float) (height - (height / 13))) - (60.0f * (((float) height) / dpHeight)));
//        this.btn1 = new Button(this);
//        this.btn1.setX((float) ((width * 0) / SELECT_PICTURE_BACKGROUND));
//        this.btn1.setY(((float) (height - (height / 13))) - (75.0f * (((float) height) / dpHeight)));
//        this.btn1.setLayoutParams(lPB2);
//        this.btn1.setBackgroundResource(R.drawable.btn8);
//        this.btn1.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                SingleEditor.this.myView.rotateBitmap90(-1);
//            }
//        });
//        this.btn2 = new Button(this);
//        this.btn2.setX((float) ((width * SELECT_PICTURE) / SELECT_PICTURE_BACKGROUND));
//        this.btn2.setY(((float) (height - (height / 13))) - (75.0f * (((float) height) / dpHeight)));
//        this.btn2.setLayoutParams(lPB2);
//        this.btn2.setBackgroundResource(R.drawable.btn5);
//        this.btn2.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                SingleEditor.this.myView.rotateBitmap(-1);
//            }
//        });
//        this.btn3 = new Button(this);
//        this.btn3.setX((float) ((width * REQUEST_CAMERA) / SELECT_PICTURE_BACKGROUND));
//        this.btn3.setY(((float) (height - (height / 13))) - (75.0f * (((float) height) / dpHeight)));
//        this.btn3.setLayoutParams(lPB2);
//        this.btn3.setBackgroundResource(R.drawable.btn6);
//        this.btn3.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                SingleEditor.this.myView.rotateBitmap(SingleEditor.SELECT_PICTURE);
//            }
//        });
//        this.btn4 = new Button(this);
//        this.btn4.setX((float) ((width * SELECT_PICTURE_MORE) / SELECT_PICTURE_BACKGROUND));
//        this.btn4.setY(((float) (height - (height / 13))) - (75.0f * (((float) height) / dpHeight)));
//        this.btn4.setLayoutParams(lPB2);
//        this.btn4.setBackgroundResource(R.drawable.btn7);
//        this.btn4.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                SingleEditor.this.myView.rotateBitmap90(SingleEditor.SELECT_PICTURE);
//            }
//        });
//        this.layoutButtonRotateLeft90 = new Button(this);
//        this.layoutButtonRotateLeft90.setX((float) ((width * 0) / SELECT_PICTURE_BACKGROUND));
//        this.layoutButtonRotateLeft90.setY((float) (height - (height / 12)));
//        this.layoutButtonRotateLeft90.setLayoutParams(lPB2);
//        this.layoutButtonRotateLeft90.setBackgroundResource(R.drawable.btn14);
//        this.layoutButtonRotateLeft90.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                SingleEditor.this.openDialog(false);
//            }
//        });
//        this.rootLayout.addView(this.layoutButtonRotateLeft);
//        this.rootLayout.addView(this.layoutButtonRotateRight90);
//        this.rootLayout.addView(this.layoutButton3);
//        this.layoutButtonRotateLeft.setVisibility(View.GONE);
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
//                SingleEditor.this.startActivityForResult(Intent.createChooser(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), "Select Picture"), SingleEditor.SELECT_PICTURE_BACKGROUND);
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
//                SingleEditor.this.startActivityForResult(intent, SingleEditor.REQUEST_CAMERA);
//            }
//        });
//        LayoutParams layoutParams = new LayoutParams(width, height);
//        this.layoutSelect = new LinearLayout(this);
//        this.layoutSelect.setBackgroundColor(Color.parseColor("#171717"));
//        this.layoutSelect.setLayoutParams(layoutParams);
//        this.layoutSelect.addView(this.layoutButton1);
//        this.layoutSelect.addView(this.layoutButton2);
//        this.rootLayout.addView(this.layoutSelect);
    }

    void openDialog(boolean supportsAlpha) {
        new AmbilWarnaDialog(this, this.color, supportsAlpha, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onOk(AmbilWarnaDialog dialog, int color) {
                SingleEditor.this.color = color;
                Object[] objArr = new Object[SingleEditor.SELECT_PICTURE];
                objArr[0] = Integer.valueOf(color);
                String c = String.format("#%08x", objArr);
                SingleEditor.this.frame.setImageDrawable(null);
                SingleEditor.this.frame.setBackgroundColor(Color.parseColor(c));
            }

            public void onCancel(AmbilWarnaDialog dialog) {
            }
        }).show();
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
                SingleEditor.this.mViews.remove(stickerView);
                SingleEditor.this.layoutCustom.removeView(stickerView);
            }

            public void onEdit(StickerView stickerView) {
                if (SingleEditor.mCurrentEditTextView != null) {
                    SingleEditor.mCurrentEditTextView.setInEdit(false);
                }
                SingleEditor.mCurrentView.setInEdit(false);
                SingleEditor.mCurrentView = stickerView;
                SingleEditor.mCurrentView.setInEdit(true);
            }

            public void onTop(StickerView stickerView) {
                int position = SingleEditor.this.mViews.indexOf(stickerView);
                if (position != SingleEditor.this.mViews.size() - 1) {
                    SingleEditor.this.mViews.add(SingleEditor.this.mViews.size(), SingleEditor.this.mViews.remove(position));
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
                SingleEditor.this.mViews.remove(bubbleTextView);
                SingleEditor.this.layoutCustom.removeView(bubbleTextView);
            }

            public void onEdit(BubbleTextView bubbleTextView) {
                if (SingleEditor.mCurrentView != null) {
                    SingleEditor.mCurrentView.setInEdit(false);
                }
                SingleEditor.mCurrentEditTextView.setInEdit(false);
                SingleEditor.mCurrentEditTextView = bubbleTextView;
                SingleEditor.mCurrentEditTextView.setInEdit(true);
            }

            public void onClick(BubbleTextView bubbleTextView) {
//                SingleEditor.this.mBubbleInputDialog.setBubbleTextView(bubbleTextView);
//                SingleEditor.this.mBubbleInputDialog.show();
            }

            public void onTop(BubbleTextView bubbleTextView) {
                int position = SingleEditor.this.mViews.indexOf(bubbleTextView);
                if (position != SingleEditor.this.mViews.size() - 1) {
                    SingleEditor.this.mViews.add(SingleEditor.this.mViews.size(), SingleEditor.this.mViews.remove(position));
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
                SingleEditor.this.mViews.remove(stickerView);
                SingleEditor.this.layoutCustom.removeView(stickerView);
            }

            public void onEdit(StickerView stickerView) {
                if (SingleEditor.mCurrentEditTextView != null) {
                    SingleEditor.mCurrentEditTextView.setInEdit(false);
                }
                SingleEditor.mCurrentView.setInEdit(false);
                SingleEditor.mCurrentView = stickerView;
                SingleEditor.mCurrentView.setInEdit(true);
            }

            public void onTop(StickerView stickerView) {
                int position = SingleEditor.this.mViews.indexOf(stickerView);
                if (position != SingleEditor.this.mViews.size() - 1) {
                    SingleEditor.this.mViews.add(SingleEditor.this.mViews.size(), SingleEditor.this.mViews.remove(position));
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
        File imageFileFolder = new File(Environment.getExternalStorageDirectory(), "BabyFrames");
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
                SingleEditor.this.msConn.scanFile(imageFileName, null);
                Log.i("msClient obj", "connection established");
            }

            public void onScanCompleted(String path, Uri uri) {
                SingleEditor.this.msConn.disconnect();
                Log.i("msClient obj", "scan completed");
            }
        });
        this.msConn.connect();
    }

    public Bitmap addWhiteBorder(Bitmap bmp, int borderSize) {
        bmp = Bitmap.createScaledBitmap(bmp, 500, (bmp.getHeight() * 500) / bmp.getWidth(), false);
        if (bmp.getWidth() < bmp.getHeight()) {
            bmp = Bitmap.createScaledBitmap(bmp, 500, (bmp.getHeight() * 500) / bmp.getWidth(), false);
        } else if (bmp.getWidth() > bmp.getHeight()) {
            bmp = Bitmap.createScaledBitmap(bmp, (bmp.getWidth() * 500) / bmp.getHeight(), 500, false);
        }
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + (borderSize * REQUEST_CAMERA), bmp.getHeight() + (borderSize * REQUEST_CAMERA), bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(-1);
        canvas.drawBitmap(bmp, (float) borderSize, (float) borderSize, null);
        Log.d("bmp", bmp.getWidth() + BuildConfig.FLAVOR);
        return bmpWithBorder;
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
                this.layoutSelect.setVisibility(View.GONE);
                this.frame.setImageBitmap(bitmap);
                System.out.println("local image");
                return;
            }
            System.out.println("picasa image!");
        } else if (requestCode == REQUEST_CAMERA) {
            addStickerView(addWhiteBorder(decodeSampledBitmapFromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg").getAbsolutePath(), 1000, 700), 20));
        } else if (requestCode == SELECT_PICTURE_MORE) {
            selectedImageUri = data.getData();
            Log.d("URI VAL", "selectedImageUri = " + selectedImageUri.toString());
            this.selectedImagePath = getPath(selectedImageUri);
            if (this.selectedImagePath != null) {
                bitmap = BitmapFactory.decodeFile(this.selectedImagePath);
                f = ((float) bitmap.getWidth()) / ((float) bitmap.getHeight());
                bitmap = Bitmap.createScaledBitmap(bitmap, 900, 1200, false);
                this.layoutSelect.setVisibility(View.GONE);
                LayoutParams lImage = new LayoutParams(width, width * (bitmap.getHeight() / bitmap.getWidth()));
                this.layoutCustom.setLayoutParams(lImage);
                this.frame.setLayoutParams(lImage);
                this.frame.setImageBitmap(bitmap);
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
                this.layoutCustom.getLayoutParams().width = bitmap.getWidth();
                this.layoutCustom.getLayoutParams().height = bitmap.getHeight();
                this.frame.setImageBitmap(bitmap);
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

    public Bitmap fastblur(Bitmap sentBitmap, float scale, int radius) {

        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int[] r = new int[wh];
        int[] g = new int[wh];
        int[] b = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int[] vmin = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int[] dv = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    public static void verifyStoragePermissions(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, SELECT_PICTURE);
        }
    }
}
