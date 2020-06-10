package com.coolapps.logomaker.utilities;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coolapps.logomaker.R;

import java.io.File;
import java.util.ArrayList;

import yuku.ambilwarna.BuildConfig;

public class FreeCollageDone extends AppCompatActivity implements OnClickListener {
    private static String[] PERMISSIONS_STORAGE = null;
    private static final int REQUEST_CAMERA = 2;
    private static final int SELECT_PICTURE = 1;
    private static final int SELECT_PICTURE_MORE = 3;
    private String linkSave = BuildConfig.FLAVOR;
    private static String[] dataObjects = new String[]{"demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png", "demo1.png"};
    static int height;
    public static BubbleTextView mCurrentEditTextView;
    public static StickerView mCurrentView;
    static int width;
    private LinearLayout LayoutSave;
    private ImageView frame;
    private FrameLayout layoutCustom;
    private LinearLayout layoutView;
    ImageView btn_rate, btn_share, btn_back;

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
    String link;
    private LinearLayout adView;
    private Uri shareuri = null;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                FreeCollageDone.this.finish();
                break;
            case R.id.btn_share:
//                Intent sharingIntent = new Intent("android.intent.action.SEND");
//                sharingIntent.setType("image/*");
//                sharingIntent.putExtra("android.intent.extra.SUBJECT", "Subject Here");
//                sharingIntent.putExtra("android.intent.extra.TEXT", "Here is the share content body");
//                sharingIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(link)));
//                startActivity(Intent.createChooser(sharingIntent, "Share via"));

                File file = new File(link);

                try {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.putExtra("sms_body", "Image");
                    intent.setType("image/*");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        shareuri = FileProvider.getUriForFile(FreeCollageDone.this, "com.coolapps.logomaker.provider", file);
                        intent.putExtra("android.intent.extra.SUBJECT", "Image");
                        intent.putExtra("android.intent.extra.STREAM", shareuri);
                        startActivity(Intent.createChooser(intent,
                                "Share image by..."));
                    } else {
                        intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(link)));
                        startActivity(Intent.createChooser(intent,
                                "Share image by..."));
                    }
                } catch (ActivityNotFoundException anfe) {
                    Toast.makeText(FreeCollageDone.this, "No activity found to open this attachment.", Toast.LENGTH_LONG).show();
                }


                break;
            case R.id.btn_rate:
                Intent ii = new Intent("android.intent.action.VIEW");
                ii.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.coolapps.logomaker"));
                FreeCollageDone.this.startActivity(ii);
                break;
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(SELECT_PICTURE);
        getWindow().setFlags(1024, 1024);
        this.mainLayout = new RelativeLayout(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_frame_done);

        adView = findViewById(R.id.adView);
        if (!NetworkUtils.isNetworkAvailable(this)) {

            adView.setVisibility(View.GONE);

        } else {


            adView.setVisibility(View.VISIBLE);
            AdsUtility.admobBannerCall(this, adView);

        }

        this.mContentRootView = findViewById(R.id.rl_content_root);
//        verifyStoragePermissions(this);
        this.rootLayout = findViewById(R.id.root_layout);
//        this.rootLayout.setBackgroundColor(Color.parseColor("#e5e5e5"));
        Intent in = getIntent();
        int type = in.getIntExtra("number_frame", 0);
        this.frame = new ImageView(this);
//        this.frame.setImageBitmap(getBitmapFromAsset(this, "Frame/f1_1.png"));
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();


        // Buttons.........
        btn_back = findViewById(R.id.btn_back);
        btn_rate = findViewById(R.id.btn_rate);
        btn_share = findViewById(R.id.btn_share);
        btn_rate.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        btn_back.setOnClickListener(this);


        this.LayoutSave = new LinearLayout(this);
        LayoutParams layoutParams = new ActionBar.LayoutParams(width, width);
        this.LayoutSave.setX(0.0f);
        this.LayoutSave.setY((float) (height / 5));
        this.LayoutSave.setLayoutParams(layoutParams);
//        this.LayoutSave.setBackgroundColor(Color.parseColor("#e5e5e5"));
        this.myView = findViewById(R.id.bk1);
        this.layoutView = new LinearLayout(this);
        layoutParams = new ActionBar.LayoutParams(width, width);
        this.layoutView.setX(0.0f);
        this.layoutView.setY(0.0f);
        this.layoutView.setLayoutParams(layoutParams);
//        this.layoutView.setBackgroundColor(Color.parseColor("#98cfaf"));
        this.layoutCustom = findViewById(R.id.layout_view);
        this.layoutCustom.setY((float) (height / 12));
        this.layoutCustom.setX((float) ((width - ((int) (((double) width) * 0.93d))) / REQUEST_CAMERA));
        this.layoutCustom.getLayoutParams().width = (int) (((double) width) * 0.93d);
        this.layoutCustom.getLayoutParams().height = (int) (((((double) width) * 0.93d) * 16.0d) / 12.0d);
        this.layoutCustom.addView(this.frame);
        link = in.getStringExtra(SingleEditor.LINK_PHOTO);
        Options options = new Options();
        options.inPreferredConfig = Config.ARGB_8888;
        this.frame.setImageBitmap(BitmapFactory.decodeFile(link, options));
        this.frame.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
        layoutParams = new ActionBar.LayoutParams(((height / 13) * 400) / 218, height / 13);

        this.txtDone = new TextView(this);
        this.txtDone.setText("Successfully Saved");
        this.txtDone.setTextColor(Color.parseColor("#ffffff"));
        this.txtDone.setTypeface(Typeface.createFromAsset(getAssets(), "font9.ttf"));
        this.txtDone.setTextSize(30.0f);
        this.txtDone.measure(0, 0);
        this.txtDone.setX((float) ((width / REQUEST_CAMERA) - (this.txtDone.getMeasuredWidth() / REQUEST_CAMERA)));
        this.txtDone.setY((float) (height / 60));

        this.rootLayout.addView(this.txtDone);


    }


    public void onResume() {
        super.onResume();

    }


}
