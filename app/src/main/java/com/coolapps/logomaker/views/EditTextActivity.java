package com.coolapps.logomaker.views;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.coolapps.logomaker.R;
import com.coolapps.logomaker.utilities.BubbleTextView;
import com.coolapps.logomaker.utilities.ConstantData;
import com.coolapps.logomaker.utilities.CustomView;
import com.coolapps.logomaker.utilities.HorizontalListView;
import com.coolapps.logomaker.utilities.StickerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.BuildConfig;

public class EditTextActivity extends AppCompatActivity implements OnClickListener {
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
    private boolean grCentral = true;
    private boolean grLeft = false;
    private boolean grRight = false;
    private ImageView img;
    SeekBar seekBar;
    private boolean isFrame = true;
    private Button layoutButton1;
    private Button layoutButton2;
    private Button layoutButton3;
    private Button layoutButton4;
    private FrameLayout layoutCustom;
    private LinearLayout layoutEditMyText;
    private LinearLayout layoutView;
    ImageView btnColor, btnShadow, btnGravity, btnDone;
    private String linkSave = BuildConfig.FLAVOR;


    private BaseAdapter mAdapter = new BaseAdapter() {

        public int getCount() {
            return 19;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem, null);
            TextView image = retval.findViewById(R.id.imageFrame);
            image.setText("FONT");
            image.setTypeface(Typeface.createFromAsset(getAssets(), ConstantData.fonts[position]));
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
    private TextView myText;
    private CustomView myView;
    private RelativeLayout rootLayout;
    ProgressDialog savingProcessing;
    private String selectedImagePath;
    private int txtColorShadow = -16777216;
    private int txtDx = 5;
    private int txtDy = 5;
    private int txtRadius = SELECT_PICTURE_MORE;
    private TextView txtShadow;
    private TextView txtSize;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_gravity:
                AlertDialog alertDialog = new Builder(EditTextActivity.this).create();
                alertDialog.setTitle("Text gravity");
                alertDialog.setButton(-1, "RIGHT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditTextActivity.this.myText.setGravity(5);
                    }
                });
                alertDialog.setButton(-2, "CENTER", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditTextActivity.this.myText.setGravity(17);
                    }
                });
                alertDialog.setButton(-3, "LEFT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditTextActivity.this.myText.setGravity(EditTextActivity.SELECT_PICTURE_MORE);
                    }
                });
                alertDialog.show();
                break;
            case R.id.btn_shadow:
                EditTextActivity.this.openDialogShadow(true);
                break;
            case R.id.btn_color:
                EditTextActivity.this.openDialog(false);
                break;
            case R.id.btn_done:
                new SaveThread(EditTextActivity.getBitmapFromView(EditTextActivity.this.layoutCustom), false).execute();
                LogoMakerActivity.isTextOK = true;
                break;
        }
    }

    public class SaveThread extends AsyncTask<Void, Void, Void> {
        Bitmap bitmap;
        private boolean share;

        public SaveThread(Bitmap bitmap, boolean share) {
            this.bitmap = bitmap;
            this.share = share;
            EditTextActivity.this.savingProcessing = new ProgressDialog(EditTextActivity.this);
            EditTextActivity.this.savingProcessing.setMessage("Saving..");
            EditTextActivity.this.savingProcessing.show();
        }

        protected Void doInBackground(Void... params) {
            EditTextActivity.this.linkSave = EditTextActivity.this.savePhoto(this.bitmap);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            EditTextActivity.this.savingProcessing.dismiss();
            if (EditTextActivity.this.linkSave.equals(BuildConfig.FLAVOR) || !this.share) {
                EditTextActivity.this.finish();
                return;
            }
            Intent sharingIntent = new Intent("android.intent.action.SEND");
            sharingIntent.setType("image/*");
            sharingIntent.putExtra("android.intent.extra.SUBJECT", "Subject Here");
            sharingIntent.putExtra("android.intent.extra.TEXT", "Here is the share content body");
            sharingIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(EditTextActivity.this.linkSave)));
            EditTextActivity.this.startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(SELECT_PICTURE);
        getWindow().setFlags(1024, 1024);
        this.mainLayout = new RelativeLayout(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtext);
        this.mContentRootView = findViewById(R.id.rl_content_root);
        verifyStoragePermissions(this);
        this.rootLayout = findViewById(R.id.root_layout);
//        this.rootLayout.setBackgroundColor(Color.parseColor("#e5e5e5"));
        int type = getIntent().getIntExtra("number_frame", 0);
        this.frame = new ImageView(this);
        this.frame.setImageBitmap(getBitmapFromAsset(this, "Background/background_1.png"));
        Display display = getWindowManager().getDefaultDisplay();


        btnColor = findViewById(R.id.btn_color);
        btnDone = findViewById(R.id.btn_done);
        btnGravity = findViewById(R.id.btn_gravity);
        btnShadow = findViewById(R.id.btn_shadow);

        btnShadow.setOnClickListener(this);
        btnGravity.setOnClickListener(this);
        btnColor.setOnClickListener(this);
        btnDone.setOnClickListener(this);


        width = display.getWidth();
        height = display.getHeight();
//        this.LayoutSave = new LinearLayout(this);
//        LayoutParams layoutParamsB1 = new LayoutParams(width, (width * 16) / 16);
//        this.LayoutSave.setX(0.0f);
//        this.LayoutSave.setY((float) (height / 5));
//        this.LayoutSave.setLayoutParams(layoutParamsB1);
//        this.LayoutSave.setBackgroundColor(0);

        this.myView = findViewById(R.id.bk1);
        this.layoutView = new LinearLayout(this);
        LayoutParams layoutParamsView = new LayoutParams(width, (width * 16) / 16);
        this.layoutView.setX(0.0f);
        this.layoutView.setY(0.0f);
        this.layoutView.setLayoutParams(layoutParamsView);
//        this.layoutView.setBackgroundColor(Color.parseColor("#98cfaf"));
        this.layoutCustom = findViewById(R.id.layout_view);
        layoutCustom.setPadding(10,0, 10,0);
        this.layoutCustom.setY(0.0f);
        this.layoutCustom.setX(0.0f);
        this.layoutCustom.getLayoutParams().width = width;
        this.layoutCustom.getLayoutParams().height = (width * SELECT_PICTURE_MORE) / SELECT_PICTURE_BACKGROUND;
        this.layoutCustom.setBackgroundColor(0);
        this.frame.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (EditTextActivity.mCurrentView != null) {
                    EditTextActivity.mCurrentView.setInEdit(false);
                }
                if (EditTextActivity.mCurrentEditTextView != null) {
                    EditTextActivity.mCurrentEditTextView.setInEdit(false);
                }
            }
        });
//        this.img = new ImageView(this);
//        this.img.setScaleType(ScaleType.FIT_XY);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(width, width);
//        this.img.setY((float) ((-(width - ((width * SELECT_PICTURE_MORE) / SELECT_PICTURE_BACKGROUND))) / REQUEST_CAMERA));
//        this.img.setX(0.0f);
//        this.img.setLayoutParams(layoutParams);
//        this.img.setImageBitmap(getBitmapFromAsset(this, "Background/background_" + getIntent().getIntExtra("id_background_behind_text", 0) + ".jpg"));
        this.txtSize = new TextView(this);
        this.txtSize.setTextColor(Color.parseColor("#ffffff"));
        this.txtSize.setText("Size");
        this.txtSize.setY((float) ((int) (0.74d * ((double) height))));
        this.txtSize.setX((float) ((int) (0.54d * ((double) width))));
        this.rootLayout.addView(this.txtSize);
        seekBar = new SeekBar(this);

        seekBar.setMax(200);
        seekBar.setProgress(70);
        this.rootLayout.addView(seekBar);
        layoutParams = new RelativeLayout.LayoutParams(new ViewGroup.LayoutParams((width * 48) / 100, height / 20));
        seekBar.setY((float) ((int) (0.75d * ((double) height))));
        seekBar.setX((float) ((int) (0.5d * ((double) width))));
        seekBar.setLayoutParams(layoutParams);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                EditTextActivity.this.myText.setTextSize((float) progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBar = new SeekBar(this);
        seekBar.setMax(20);
        seekBar.setProgress(SELECT_PICTURE_MORE);
        this.rootLayout.addView(seekBar);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(new ViewGroup.LayoutParams((width * 48) / 100, height / 20));
        seekBar.setY((float) ((int) (0.75d * ((double) height))));
        seekBar.setLayoutParams(layoutParams2);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                EditTextActivity.this.txtRadius = progress;
                EditTextActivity.this.myText.setShadowLayer((float) EditTextActivity.this.txtRadius, (float) EditTextActivity.this.txtDx, (float) EditTextActivity.this.txtDy, EditTextActivity.this.txtColorShadow);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBar = new SeekBar(this);
        seekBar.setMax(30);
        seekBar.setProgress(17);
        this.rootLayout.addView(seekBar);
        layoutParams = new RelativeLayout.LayoutParams(new ViewGroup.LayoutParams((width * 48) / 100, height / 20));
        seekBar.setY((float) ((int) (0.68d * ((double) height))));
        seekBar.setLayoutParams(layoutParams);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                EditTextActivity.this.txtDx = progress - 15;
                EditTextActivity.this.myText.setShadowLayer((float) EditTextActivity.this.txtRadius, (float) EditTextActivity.this.txtDx, (float) EditTextActivity.this.txtDy, EditTextActivity.this.txtColorShadow);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBar = new SeekBar(this);
        seekBar.setMax(30);
        seekBar.setProgress(17);
        this.rootLayout.addView(seekBar);
        layoutParams = new RelativeLayout.LayoutParams(new ViewGroup.LayoutParams((width * 48) / 100, height / 20));
        seekBar.setX((float) ((int) (0.5d * ((double) width))));
        seekBar.setY((float) ((int) (0.68d * ((double) height))));
        seekBar.setLayoutParams(layoutParams);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                EditTextActivity.this.txtDy = progress - 15;
                EditTextActivity.this.myText.setShadowLayer((float) EditTextActivity.this.txtRadius, (float) EditTextActivity.this.txtDx, (float) EditTextActivity.this.txtDy, EditTextActivity.this.txtColorShadow);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.myText = new TextView(this);
        this.myText.setText("Your name");
        this.myText.setTextSize(70.0f);
        this.myText.setTextColor(-1);
        this.myText.setGravity(17);
        this.myText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
        this.myText.setTypeface(Typeface.createFromAsset(getAssets(), "font14.ttf"));
        this.myText.setShadowLayer(0.0f, 5.0f, 5.0f, -16777216);
        this.myText.setLayoutParams(new LayoutParams(width, (width * SELECT_PICTURE_MORE) / SELECT_PICTURE_BACKGROUND));
        this.myText.setGravity(17);
        this.layoutCustom.addView(this.myText);
//        this.layoutButton1 = new Button(this);
//        LayoutParams lPB1 = new LayoutParams(((height / 12) * 400) / 237, height / 12);
//        this.layoutButton1.setX((float) (((width / SELECT_PICTURE_BACKGROUND) - (((height / 12) * 400) / 237)) / REQUEST_CAMERA));
//        this.layoutButton1.setY(0.0f);
//        this.layoutButton1.setLayoutParams(lPB1);
//        this.layoutButton1.setBackgroundResource(R.drawable.btn1);
//        this.layoutButton1.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                EditTextActivity.this.startActivityForResult(Intent.createChooser(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), "Select Picture"), EditTextActivity.SELECT_PICTURE_BACKGROUND);
//            }
//        });
//        this.layoutButton2 = new Button(this);
//        this.layoutButton2.setX((float) ((width / SELECT_PICTURE_BACKGROUND) + (((width / SELECT_PICTURE_BACKGROUND) - (((height / 12) * 400) / 237)) / REQUEST_CAMERA)));
//        this.layoutButton2.setY(0.0f);
//        this.layoutButton2.setLayoutParams(lPB1);
//        this.layoutButton2.setBackgroundResource(R.drawable.btn2);
//        this.layoutButton2.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent.putExtra("output", Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg")));
//                EditTextActivity.this.startActivityForResult(intent, EditTextActivity.REQUEST_CAMERA);
//            }
//        });
//        this.layoutButton3 = new Button(this);
//        this.layoutButton3.setX((float) ((width / REQUEST_CAMERA) + (((width / SELECT_PICTURE_BACKGROUND) - (((height / 12) * 400) / 237)) / REQUEST_CAMERA)));
//        this.layoutButton3.setY(0.0f);
//        this.layoutButton3.setLayoutParams(lPB1);
//        this.layoutButton3.setBackgroundResource(R.drawable.btn3);
//        this.layoutButton3.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                if (EditTextActivity.mCurrentView != null) {
//                    EditTextActivity.mCurrentView.setInEdit(false);
//                }
//                if (EditTextActivity.mCurrentEditTextView != null) {
//                    EditTextActivity.mCurrentEditTextView.setInEdit(false);
//                }
//                new SaveThread(EditTextActivity.getBitmapFromView(EditTextActivity.this.layoutCustom), false).execute(new Void[0]);
//                Editor editor = PreferenceManager.getDefaultSharedPreferences(EditTextActivity.this).edit();
//                editor.putInt("com.vt.na.bubble_id.true", EditTextActivity.SELECT_PICTURE);
//                editor.apply();
//            }
//        });
//        this.layoutButton4 = new Button(this);
//        this.layoutButton4.setX((float) (((width * SELECT_PICTURE_MORE) / SELECT_PICTURE_BACKGROUND) + (((width / SELECT_PICTURE_BACKGROUND) - (((height / 12) * 400) / 237)) / REQUEST_CAMERA)));
//        this.layoutButton4.setY(0.0f);
//        this.layoutButton4.setLayoutParams(lPB1);
//        this.layoutButton4.setBackgroundResource(R.drawable.btn4);
//        this.layoutButton4.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                if (EditTextActivity.mCurrentView != null) {
//                    EditTextActivity.mCurrentView.setInEdit(false);
//                }
//                if (EditTextActivity.mCurrentEditTextView != null) {
//                    EditTextActivity.mCurrentEditTextView.setInEdit(false);
//                }
//                new SaveThread(EditTextActivity.getBitmapFromView(EditTextActivity.this.layoutCustom), true).execute(new Void[0]);
//            }
//        });
//        LayoutParams lPB2 = new LayoutParams(((height / 13) * 400) / 218, height / 13);


        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = ((float) displayMetrics.heightPixels) / displayMetrics.density;
        float dpWidth = ((float) displayMetrics.widthPixels) / displayMetrics.density;

        HorizontalListView listview = findViewById(R.id.listview);

        listview.setAdapter(this.mAdapter);
        listview.setY(((float) (height - (height / 10))) - (200.0f * (((float) height) / dpHeight)));
        listview.setPadding(15,0, 10,0);
        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                int pos =  position+1;
                Typeface type = Typeface.createFromAsset(EditTextActivity.this.getAssets(), "font" + pos + ".ttf");
                EditTextActivity.this.myText.setTypeface(type);

            }
        });
        this.layoutEditMyText = new LinearLayout(this);
        LayoutParams layoutEditMyTextLP = new LayoutParams(width, height / 10);
        this.layoutEditMyText.setPadding(5,0, 5,0);
        this.layoutEditMyText.setX(0.0f);
        this.layoutEditMyText.setY((float) ((width * SELECT_PICTURE_MORE) / SELECT_PICTURE_BACKGROUND));
        this.layoutEditMyText.setLayoutParams(layoutEditMyTextLP);
        this.layoutEditMyText.setBackgroundColor(Color.parseColor("#b1aeae"));
        final EditText editText = new EditText(this);
        editText.setPadding(10,0, 10,0);
        editText.setLayoutParams(layoutEditMyTextLP);
        editText.setBackgroundResource(R.drawable.edit_text);
        editText.setHint("Enter your text here");
        editText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EditTextActivity.this.myText.setText(editText.getText());
            }

            public void afterTextChanged(Editable s) {
            }
        });
        this.layoutEditMyText.addView(editText);
//        this.btn1 = new Button(this);
//        this.btn1.setX((float) (((width * 0) / SELECT_PICTURE_BACKGROUND) + ((((width * SELECT_PICTURE) / SELECT_PICTURE_BACKGROUND) - (((height / 13) * 400) / 218)) / REQUEST_CAMERA)));
//        this.btn1.setY(((float) (height - (height / 13))) - (75.0f * (((float) height) / dpHeight)));
//        this.btn1.setLayoutParams(lPB2);
//        this.btn1.setBackgroundResource(R.drawable.btn8);
//        this.btn1.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                EditTextActivity.this.myView.rotateBitmap90(-1);
//            }
//        });
//        this.btn2 = new Button(this);
//        this.btn2.setX((float) (((width * SELECT_PICTURE) / SELECT_PICTURE_BACKGROUND) + ((((width * SELECT_PICTURE) / SELECT_PICTURE_BACKGROUND) - (((height / 13) * 400) / 218)) / REQUEST_CAMERA)));
//        this.btn2.setY(((float) (height - (height / 13))) - (75.0f * (((float) height) / dpHeight)));
//        this.btn2.setLayoutParams(lPB2);
//        this.btn2.setBackgroundResource(R.drawable.btn5);
//        this.btn2.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                EditTextActivity.this.myView.rotateBitmap(-1);
//            }
//        });
//        this.btn3 = new Button(this);
//        this.btn3.setX((float) (((width * REQUEST_CAMERA) / SELECT_PICTURE_BACKGROUND) + ((((width * SELECT_PICTURE) / SELECT_PICTURE_BACKGROUND) - (((height / 13) * 400) / 218)) / REQUEST_CAMERA)));
//        this.btn3.setY(((float) (height - (height / 13))) - (75.0f * (((float) height) / dpHeight)));
//        this.btn3.setLayoutParams(lPB2);
//        this.btn3.setBackgroundResource(R.drawable.btn6);
//        this.btn3.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                EditTextActivity.this.myView.rotateBitmap(EditTextActivity.SELECT_PICTURE);
//            }
//        });
//        this.btn4 = new Button(this);
//        this.btn4.setX((float) (((width * SELECT_PICTURE_MORE) / SELECT_PICTURE_BACKGROUND) + ((((width * SELECT_PICTURE) / SELECT_PICTURE_BACKGROUND) - (((height / 13) * 400) / 218)) / REQUEST_CAMERA)));
//        this.btn4.setY(((float) (height - (height / 13))) - (75.0f * (((float) height) / dpHeight)));
//        this.btn4.setLayoutParams(lPB2);
//        this.btn4.setBackgroundResource(R.drawable.btn7);
//        this.btn4.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                EditTextActivity.this.myView.rotateBitmap90(EditTextActivity.SELECT_PICTURE);
//            }
//        });

//        this.btn1.setVisibility(View.GONE);
//        this.btn2.setVisibility(View.GONE);
//        this.btn3.setVisibility(View.GONE);
//        this.btn4.setVisibility(View.GONE);
//        this.rootLayout.addView(this.btn1);
//        this.rootLayout.addView(this.btn2);
//        this.rootLayout.addView(this.btn3);
//        this.rootLayout.addView(this.btn4);
//        this.rootLayout.addView(this.img);
        this.rootLayout.addView(this.layoutEditMyText);
        this.rootLayout.removeView(this.layoutCustom);
        this.rootLayout.addView(this.layoutCustom);
        this.mViews = new ArrayList();
//        this.mBubbleInputDialog = new BubbleInputDialog(this);
//        this.mBubbleInputDialog.setCompleteCallBack(new BubbleInputDialog.CompleteCallBack() {
//            public void onComplete(View bubbleTextView, String str) {
//                ((BubbleTextView) bubbleTextView).setText(str);
//            }
//        });
        this.frame.setBackgroundColor(Color.rgb(100, 100, 50));
    }


    void openDialog(boolean supportsAlpha) {
        new AmbilWarnaDialog(this, this.color, supportsAlpha, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onOk(AmbilWarnaDialog dialog, int color) {
                EditTextActivity.this.color = color;
                Object[] objArr = new Object[EditTextActivity.SELECT_PICTURE];
                objArr[0] = Integer.valueOf(color);
                EditTextActivity.this.myText.setTextColor(Color.parseColor(String.format("#%08x", objArr)));
            }

            public void onCancel(AmbilWarnaDialog dialog) {
            }
        }).show();
    }

    void openDialogShadow(boolean supportsAlpha) {
        new AmbilWarnaDialog(this, this.color, supportsAlpha, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onOk(AmbilWarnaDialog dialog, int color) {
                EditTextActivity.this.color = color;
                Object[] objArr = new Object[EditTextActivity.SELECT_PICTURE];
                objArr[0] = Integer.valueOf(color);
                EditTextActivity.this.txtColorShadow = Color.parseColor(String.format("#%08x", objArr));
                EditTextActivity.this.myText.setShadowLayer((float) EditTextActivity.this.txtRadius, (float) EditTextActivity.this.txtDx, (float) EditTextActivity.this.txtDy, EditTextActivity.this.txtColorShadow);
            }

            public void onCancel(AmbilWarnaDialog dialog) {
            }
        }).show();
    }

    public void onResume() {
        super.onResume();
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

    protected void onDestroy() {
        super.onDestroy();
        System.gc();
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
        File imageFileName = new File(Environment.getExternalStorageDirectory(), "/temporary_holder_vintool_name_art.png");
        try {
            FileOutputStream out = new FileOutputStream(imageFileName);
            try {
                bmp.compress(CompressFormat.PNG, 100, out);
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
                EditTextActivity.this.msConn.scanFile(imageFileName, null);
                Log.i("msClient obj", "connection established");
            }

            public void onScanCompleted(String path, Uri uri) {
                EditTextActivity.this.msConn.disconnect();
                Log.i("msClient obj", "scan completed");
            }
        });
        this.msConn.connect();
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
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

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
