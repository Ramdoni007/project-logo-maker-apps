package com.coolapps.logomaker.utilities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coolapps.logomaker.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import yuku.ambilwarna.BuildConfig;

public class OneFrameDone extends AppCompatActivity {
    private static String[] PERMISSIONS_STORAGE = null;
    private static final int REQUEST_CAMERA = 2;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int SELECT_PICTURE = 1;
    private static final int SELECT_PICTURE_MORE = 3;
    private static String[] dataObjects = new String[]{"demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png"};
    static int height;
    public static BubbleTextView mCurrentEditTextView;
    public static StickerView mCurrentView;
    static int width;
    private LinearLayout LayoutSave;
//    private AdView adView;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
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
    private LinearLayout layoutView;
    private BaseAdapter mAdapter = new BaseAdapter() {
        private OnClickListener mOnButtonClicked = new OnClickListener() {
            public void onClick(View v) {
                OneFrameDone.this.frame.setImageBitmap(OneFrameDone.getBitmapFromAsset(OneFrameDone.this, "Frame/f1_" + v.getId() + ".png"));
            }
        };

        public int getCount() {
            return 50;
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
            image.setImageBitmap(OneFrameDone.getBitmapFromAsset(OneFrameDone.this, "ThumbFrameSmall/t1_" + position + ".png"));
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
    private RelativeLayout rootLayout;
    ProgressDialog savingProcessing;
    private String selectedImagePath;
    private TextView txtDone;

    public class SaveThread extends AsyncTask<Void, Void, Void> {
        Bitmap bitmap;
        private String linkSave = BuildConfig.FLAVOR;
        private boolean share;

        public SaveThread(Bitmap bitmap, boolean share) {
            this.bitmap = bitmap;
            this.share = share;
            OneFrameDone.this.savingProcessing = new ProgressDialog(OneFrameDone.this);
            OneFrameDone.this.savingProcessing.setMessage("Saving..");
            OneFrameDone.this.savingProcessing.show();
        }

        protected Void doInBackground(Void... params) {
            this.linkSave = OneFrameDone.this.savePhoto(this.bitmap);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            OneFrameDone.this.savingProcessing.dismiss();
            if (this.linkSave.equals(BuildConfig.FLAVOR) || !this.share) {
                Toast.makeText(OneFrameDone.this, "Save complete to LogoMaker folder", Toast.LENGTH_LONG).show();
                return;
            }
            Intent sharingIntent = new Intent("android.intent.action.SEND");
            sharingIntent.setType("image/*");
            sharingIntent.putExtra("android.intent.extra.SUBJECT", "Subject Here");
            sharingIntent.putExtra("android.intent.extra.TEXT", "Here is the share content body");
            sharingIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(this.linkSave)));
            OneFrameDone.this.startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(SELECT_PICTURE);
        getWindow().setFlags(1024, 1024);
        this.mainLayout = new RelativeLayout(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_frame_done);
        this.mContentRootView = findViewById(R.id.rl_content_root);
        verifyStoragePermissions(this);
        this.rootLayout = findViewById(R.id.root_layout);
        this.rootLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        Intent in = getIntent();
        int type = in.getIntExtra("number_frame", 0);
        this.frame = new ImageView(this);
        this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_1.png"));
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        this.LayoutSave = new LinearLayout(this);
        LayoutParams layoutParams = new ActionBar.LayoutParams(width, width);
        this.LayoutSave.setX(0.0f);
        this.LayoutSave.setY((float) (height / 5));
        this.LayoutSave.setLayoutParams(layoutParams);
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
        layoutParams = new ActionBar.LayoutParams(width, width);
        this.layoutView.setX(0.0f);
        this.layoutView.setY(0.0f);
        this.layoutView.setLayoutParams(layoutParams);
        this.layoutView.setBackgroundColor(Color.parseColor("#98cfaf"));
        this.layoutCustom = findViewById(R.id.layout_view);
        this.layoutCustom.setY((float) (height / 5));
        this.layoutCustom.setX(0.0f);
        this.layoutCustom.getLayoutParams().width = width;
        this.layoutCustom.getLayoutParams().height = width;
        this.layoutCustom.addView(this.frame);
        String link = in.getStringExtra(SingleEditor.LINK_PHOTO);
        Options options = new Options();
        options.inPreferredConfig = Config.ARGB_8888;
//        this.frame.setImageBitmap(BitmapFactory.decodeFile(link, options));
//        ScaleAnimation fade_in = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, SELECT_PICTURE, 0.5f, SELECT_PICTURE, 0.5f);
//        fade_in.setDuration(500);
//        fade_in.setFillAfter(true);
//        fade_in.setAnimationListener(new AnimationListener() {
//            public void onAnimationStart(Animation animation) {
//            }
//
//            public void onAnimationEnd(Animation animation) {
//                ScaleAnimation fade_out = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f, OneFrameDone.SELECT_PICTURE, 0.5f, OneFrameDone.SELECT_PICTURE, 0.5f);
//                fade_out.setDuration(500);
//                fade_out.setFillAfter(true);
//                OneFrameDone.this.frame.startAnimation(fade_out);
//            }
//
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });
//        this.frame.startAnimation(fade_in);
//        this.layoutButton1 = new Button(this);
//        layoutParams = new ActionBar.LayoutParams(width / 4, height / 12);
//        this.layoutButton1.setX(0.0f);
//        this.layoutButton1.setY(0.0f);
//        this.layoutButton1.setLayoutParams(layoutParams);
//        this.layoutButton1.setBackgroundResource(R.drawable.btn1);
//        this.layoutButton1.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                OneFrameDone.this.loadImage();
//            }
//        });
//        this.layoutButton2 = new Button(this);
//        this.layoutButton2.setX((float) (width / 4));
//        this.layoutButton2.setY(0.0f);
//        this.layoutButton2.setLayoutParams(layoutParams);
//        this.layoutButton2.setBackgroundResource(R.drawable.btn2);
//        this.layoutButton2.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent.putExtra("output", Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg")));
//                OneFrameDone.this.startActivityForResult(intent, OneFrameDone.REQUEST_CAMERA);
//            }
//        });
//        this.layoutButton3 = new Button(this);
//        this.layoutButton3.setX((float) (width / REQUEST_CAMERA));
//        this.layoutButton3.setY(0.0f);
//        this.layoutButton3.setLayoutParams(layoutParams);
//        this.layoutButton3.setBackgroundResource(R.drawable.btn3);
//        this.layoutButton3.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                if (OneFrameDone.mCurrentView != null) {
//                    OneFrameDone.mCurrentView.setInEdit(false);
//                }
//                if (OneFrameDone.mCurrentEditTextView != null) {
//                    OneFrameDone.mCurrentEditTextView.setInEdit(false);
//                }
//                new SaveThread(OneFrameDone.getBitmapFromView(OneFrameDone.this.layoutCustom), false).execute(new Void[0]);
//            }
//        });
//        this.layoutButton4 = new Button(this);
//        this.layoutButton4.setX((float) ((width * SELECT_PICTURE_MORE) / 4));
//        this.layoutButton4.setY(0.0f);
//        this.layoutButton4.setLayoutParams(layoutParams);
//        this.layoutButton4.setBackgroundResource(R.drawable.btn4);
//        this.layoutButton4.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                if (OneFrameDone.mCurrentView != null) {
//                    OneFrameDone.mCurrentView.setInEdit(false);
//                }
//                if (OneFrameDone.mCurrentEditTextView != null) {
//                    OneFrameDone.mCurrentEditTextView.setInEdit(false);
//                }
//                new SaveThread(OneFrameDone.getBitmapFromView(OneFrameDone.this.layoutCustom), true).execute(new Void[0]);
//            }
//        });
//        this.layoutButtonRotateLeft = new Button(this);
//        layoutParams = new ActionBar.LayoutParams(((height / 13) * 400) / 218, height / 13);
//        this.layoutButtonRotateLeft.setX((float) (((width * SELECT_PICTURE) / 4) + (((width / 4) - (((height / 13) * 400) / 218)) / REQUEST_CAMERA)));
//        this.layoutButtonRotateLeft.setY((float) (height - (height / 12)));
//        this.layoutButtonRotateLeft.setLayoutParams(layoutParams);
//        this.layoutButtonRotateLeft.setBackgroundResource(R.drawable.btn_rate);
//        this.layoutButtonRotateLeft.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                Intent i = new Intent("android.intent.action.VIEW");
//                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.vt.na"));
//                OneFrameDone.this.startActivity(i);
//            }
//        });
//        this.layoutButtonRotateRight = new Button(this);
//        this.layoutButtonRotateRight.setX((float) (((width * REQUEST_CAMERA) / 4) + (((width / 4) - (((height / 13) * 400) / 218)) / REQUEST_CAMERA)));
//        this.layoutButtonRotateRight.setY((float) (height - (height / 12)));
//        this.layoutButtonRotateRight.setLayoutParams(layoutParams);
//        this.layoutButtonRotateRight.setBackgroundResource(R.drawable.btn_folder);
//        this.layoutButtonRotateRight.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                OneFrameDone.this.setResult(4000);
//                OneFrameDone.this.finish();
//            }
//        });
//        this.layoutButtonRotateRight90 = new Button(this);
//        this.layoutButtonRotateRight90.setX((float) (((width * SELECT_PICTURE_MORE) / 4) + (((width / 4) - (((height / 13) * 400) / 218)) / REQUEST_CAMERA)));
//        this.layoutButtonRotateRight90.setY((float) (height - (height / 12)));
//        this.layoutButtonRotateRight90.setLayoutParams(layoutParams);
//        this.layoutButtonRotateRight90.setBackgroundResource(R.drawable.btn_new);
//        this.layoutButtonRotateRight90.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                OneFrameDone.this.setResult(UdpDataSource.DEFAULT_MAX_PACKET_SIZE);
//                OneFrameDone.this.finish();
//            }
//        });
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = ((float) displayMetrics.heightPixels) / displayMetrics.density;
        float dpWidth = ((float) displayMetrics.widthPixels) / displayMetrics.density;
//        this.btn1 = new Button(this);
//        this.btn1.setX((float) (((width * 0) / 4) + (((width / 4) - (((height / 13) * 400) / 218)) / REQUEST_CAMERA)));
//        this.btn1.setY(((float) (height - (height / 13))) - (75.0f * (((float) height) / dpHeight)));
//        this.btn1.setLayoutParams(layoutParams);
//        this.btn1.setBackgroundResource(R.drawable.btn_show);
//        this.btn1.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                OneFrameDone.this.myView.rotateBitmap90(-1);
//            }
//        });
//        this.btn2 = new Button(this);
//        this.btn2.setX((float) ((width * SELECT_PICTURE) / 4));
//        this.btn2.setY(((float) (height - (height / 13))) - (75.0f * (((float) height) / dpHeight)));
//        this.btn2.setLayoutParams(layoutParams);
//        this.btn2.setBackgroundResource(R.drawable.btn5);
//        this.btn2.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                OneFrameDone.this.myView.rotateBitmap(-1);
//            }
//        });
//        this.btn3 = new Button(this);
//        this.btn3.setX((float) ((width * REQUEST_CAMERA) / 4));
//        this.btn3.setY(((float) (height - (height / 13))) - (75.0f * (((float) height) / dpHeight)));
//        this.btn3.setLayoutParams(layoutParams);
//        this.btn3.setBackgroundResource(R.drawable.btn6);
//        this.btn3.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                OneFrameDone.this.myView.rotateBitmap(OneFrameDone.SELECT_PICTURE);
//            }
//        });
//        this.btn4 = new Button(this);
//        this.btn4.setX((float) ((width * SELECT_PICTURE_MORE) / 4));
//        this.btn4.setY(((float) (height - (height / 13))) - (75.0f * (((float) height) / dpHeight)));
//        this.btn4.setLayoutParams(layoutParams);
//        this.btn4.setBackgroundResource(R.drawable.btn7);
//        this.btn4.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                OneFrameDone.this.myView.rotateBitmap90(OneFrameDone.SELECT_PICTURE);
//            }
//        });
//        this.layoutButtonRotateLeft90 = new Button(this);
//        this.layoutButtonRotateLeft90.setX((float) (((width * 0) / 4) + (((width / 4) - (((height / 13) * 400) / 218)) / REQUEST_CAMERA)));
//        this.layoutButtonRotateLeft90.setY((float) (height - (height / 12)));
//        this.layoutButtonRotateLeft90.setLayoutParams(layoutParams);
//        this.layoutButtonRotateLeft90.setBackgroundResource(R.drawable.btn_back);
//        this.layoutButtonRotateLeft90.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                OneFrameDone.this.finish();
//            }
//        });
//        this.btn1.setVisibility(View.GONE);
//        this.btn2.setVisibility(View.GONE);
//        this.btn3.setVisibility(View.GONE);
//        this.btn4.setVisibility(View.GONE);
//        this.txtDone = new TextView(this);
//        this.txtDone.setText("Save Successful");
//        this.txtDone.setTextColor(Color.parseColor("#3b3b3b"));
//        this.txtDone.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_ori_2.otf"));
//        this.txtDone.setTextSize(30.0f);
//        this.txtDone.measure(0, 0);
//        this.txtDone.setX((float) ((width / REQUEST_CAMERA) - (this.txtDone.getMeasuredWidth() / REQUEST_CAMERA)));
//        this.txtDone.setY((float) (height / 9));
//        this.rootLayout.addView(this.layoutButtonRotateLeft);
//        this.rootLayout.addView(this.layoutButtonRotateRight);
//        this.rootLayout.addView(this.layoutButtonRotateLeft90);
//        this.rootLayout.addView(this.layoutButtonRotateRight90);
//        this.rootLayout.addView(this.btn1);
//        this.rootLayout.addView(this.btn2);
//        this.rootLayout.addView(this.btn3);
//        this.rootLayout.addView(this.btn4);
//        this.rootLayout.addView(this.txtDone);
//        this.mViews = new ArrayList();
//        this.mBubbleInputDialog = new BubbleInputDialog(this);
//        this.mBubbleInputDialog.setCompleteCallBack(new BubbleInputDialog.CompleteCallBack() {
//            public void onComplete(View bubbleTextView, String str) {
//                ((BubbleTextView) bubbleTextView).setText(str);
//            }
//        });
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
            if (nameFont == 0 || nameFont == SELECT_PICTURE || nameFont == SELECT_PICTURE_MORE || nameFont == 4 || nameFont == 5 || nameFont == 7 || nameFont == 8 || nameFont == 9 || nameFont == 11 || nameFont == 12 || nameFont == 15 || nameFont == 18) {
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
                OneFrameDone.this.mViews.remove(stickerView);
                OneFrameDone.this.layoutCustom.removeView(stickerView);
            }

            public void onEdit(StickerView stickerView) {
                if (OneFrameDone.mCurrentEditTextView != null) {
                    OneFrameDone.mCurrentEditTextView.setInEdit(false);
                }
                OneFrameDone.mCurrentView.setInEdit(false);
                OneFrameDone.mCurrentView = stickerView;
                OneFrameDone.mCurrentView.setInEdit(true);
            }

            public void onTop(StickerView stickerView) {
                int position = OneFrameDone.this.mViews.indexOf(stickerView);
                if (position != OneFrameDone.this.mViews.size() - 1) {
                    OneFrameDone.this.mViews.add(OneFrameDone.this.mViews.size(), OneFrameDone.this.mViews.remove(position));
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
                OneFrameDone.this.mViews.remove(bubbleTextView);
                OneFrameDone.this.layoutCustom.removeView(bubbleTextView);
            }

            public void onEdit(BubbleTextView bubbleTextView) {
                if (OneFrameDone.mCurrentView != null) {
                    OneFrameDone.mCurrentView.setInEdit(false);
                }
                OneFrameDone.mCurrentEditTextView.setInEdit(false);
                OneFrameDone.mCurrentEditTextView = bubbleTextView;
                OneFrameDone.mCurrentEditTextView.setInEdit(true);
            }

            public void onClick(BubbleTextView bubbleTextView) {
//                OneFrameDone.this.mBubbleInputDialog.setBubbleTextView(bubbleTextView);
//                OneFrameDone.this.mBubbleInputDialog.show();
            }

            public void onTop(BubbleTextView bubbleTextView) {
                int position = OneFrameDone.this.mViews.indexOf(bubbleTextView);
                if (position != OneFrameDone.this.mViews.size() - 1) {
                    OneFrameDone.this.mViews.add(OneFrameDone.this.mViews.size(), OneFrameDone.this.mViews.remove(position));
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
                OneFrameDone.this.mViews.remove(stickerView);
                OneFrameDone.this.layoutCustom.removeView(stickerView);
            }

            public void onEdit(StickerView stickerView) {
                if (OneFrameDone.mCurrentEditTextView != null) {
                    OneFrameDone.mCurrentEditTextView.setInEdit(false);
                }
                OneFrameDone.mCurrentView.setInEdit(false);
                OneFrameDone.mCurrentView = stickerView;
                OneFrameDone.mCurrentView.setInEdit(true);
            }

            public void onTop(StickerView stickerView) {
                int position = OneFrameDone.this.mViews.indexOf(stickerView);
                if (position != OneFrameDone.this.mViews.size() - 1) {
                    OneFrameDone.this.mViews.add(OneFrameDone.this.mViews.size(), OneFrameDone.this.mViews.remove(position));
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
                OneFrameDone.this.msConn.scanFile(imageFileName, null);
                Log.i("msClient obj", "connection established");
            }

            public void onScanCompleted(String path, Uri uri) {
                OneFrameDone.this.msConn.disconnect();
                Log.i("msClient obj", "scan completed");
            }
        });
        this.msConn.connect();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                this.myView.addImage(bitmap);
                System.out.println("local image");
                return;
            }
            System.out.println("picasa image!");
        } else if (requestCode == REQUEST_CAMERA) {
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
