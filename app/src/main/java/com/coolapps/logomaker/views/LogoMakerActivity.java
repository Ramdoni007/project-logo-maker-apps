package com.coolapps.logomaker.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.upstream.UdpDataSource;
import com.coolapps.logomaker.BuildConfig;
import com.coolapps.logomaker.R;
import com.coolapps.logomaker.adapter.ColorAdapter;
import com.coolapps.logomaker.fragments.TextPropertiesFragment;
import com.coolapps.logomaker.utilities.AdsUtility;
import com.coolapps.logomaker.utilities.BubbleTextView;
import com.coolapps.logomaker.utilities.ConstantData;
import com.coolapps.logomaker.utilities.FreeCollageDone;
import com.coolapps.logomaker.utilities.Item;
import com.coolapps.logomaker.utilities.LogoEditorView;
import com.coolapps.logomaker.utilities.PhotoEditors;
import com.coolapps.logomaker.utilities.StickerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


public class LogoMakerActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = LogoMakerActivity.class.getSimpleName();
    public static final String EXTRA_IMAGE_PATHS = "extra_image_paths";
    public static final String LINK_PHOTO = "link_photo_es";
    public static final int READ_WRITE_STORAGE = 52;
    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private TextPropertiesFragment mPropertiesBSFragment;
    public static TextView mTxtCurrentTool;
//    private Typeface mWonderFont;
    private PhotoEditors mPhotoEditor;
    private LogoEditorView mPhotoEditorView;
    private static LinearLayout stickerLayout;
    public static int widthScreen;
    public static AppCompatActivity mContext;
    MediaScannerConnection msConn;


    public static StickerView mCurrentView;
    ProgressDialog savingProcessing;
    private String linkSave = BuildConfig.FLAVOR;

    private BubbleTextView mCurrentEditTextView;

    public static String inputText, color;

    private int kindEdit = 0;

    public static RecyclerView imageColor;

    public static ArrayList<View> mViews;

    public static RelativeLayout stickers;

//    private BubbleInputDialog mBubbleInputDialog;

    public static boolean isTextOK = false;
    public  static boolean isCustImageOK = false;

    private static ArrayList<Item> stickerArray;
    private String value ;


//    public static void launch(Context context, ArrayList<String> imagesPath) {
//        Intent starter = new Intent(context, LogoMakerActivity.class);
//        starter.putExtra(EXTRA_IMAGE_PATHS, imagesPath);
//        context.startActivity(starter);
//
//    }


//    public static void launch(Context context, String imagePath) {
//        ArrayList<String> imagePaths = new ArrayList<>();
//        imagePaths.add(imagePath);
//        launch(context, imagePaths);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_edit_image);
        getReadPermission();
        AdsUtility.InterstitialAdmob(this);

        mContext = this;
        initViews();

//        mWonderFont = Typeface.createFromAsset(getAssets(), "beyond _wonderland.ttf");

        mPropertiesBSFragment = new TextPropertiesFragment();


        mPhotoEditor = new PhotoEditors(this, mPhotoEditorView);
    }

    private void initViews() {
        ImageView imgDone;
        ImageView imgText;
        ImageView imgClose;
        ImageView imgSticker;
        ImageView imgBg;

        ImageView imgSave;

        stickerLayout = findViewById(R.id.stickerLayout);
        stickers = findViewById(R.id.stickers);
        mPhotoEditorView = findViewById(R.id.photoEditorView);
        mTxtCurrentTool = findViewById(R.id.txtCurrentTool);
        imageColor = mContext.findViewById(R.id.imageColor);
        stickerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentView != null) {
                    mCurrentView.setInEdit(false);
                }
                if (mCurrentEditTextView != null) {
                    mCurrentEditTextView.setInEdit(false);
                }
            }
        });

        mViews = new ArrayList<>();

//        mBubbleInputDialog = new BubbleInputDialog(this);
//        mBubbleInputDialog.setCompleteCallBack(new BubbleInputDialog.CompleteCallBack() {
//            public void onComplete(View bubbleTextView, String str) {
//                ((BubbleTextView) bubbleTextView).setText(str);
//            }
//        });


        imgText = findViewById(R.id.imgText);
        imgText.setOnClickListener(this);


        imgSticker = findViewById(R.id.imgSticker);
        imgSticker.setOnClickListener(this);

        imgBg = findViewById(R.id.imgBg);
        imgBg.setOnClickListener(this);

        imgDone = findViewById(R.id.imgSave);
        imgDone.setOnClickListener(this);

        showBgColor();

//        imgSave = findViewById(R.id.imgSav);
//        imgSave.setOnClickListener(this);

        imgClose = findViewById(R.id.imgClose);
        imgClose.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imgText:
                mTxtCurrentTool.setText(R.string.label_text);
                imageColor.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(LogoMakerActivity.this, EditTextActivity.class);
                intent.putExtra("id_background_behind_text", 0);
                startActivity(intent);

//                mTxtCurrentTool.setText(R.string.label_text);
//                TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this);
//                textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
//                    @Override
//                    public void onDone(Bitmap bitmap, String inputText, int align, int circle, String color, String font) {
//                        inputText = inputText;
//                        color = color;
//                        mTxtCurrentTool.setText(R.string.label_text);
//                        addStickerView(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/temporary_holder_vintool_name_art.png"));
//                        isTextOK = false;
//
//                    }
//                });



                break;


            case R.id.imgSticker:
                mTxtCurrentTool.setText(R.string.label_sticker);
                imageColor.setVisibility(View.INVISIBLE);
                startActivity(new Intent(LogoMakerActivity.this, StickerTabActivity.class));
                break;

            case R.id.imgBg:
                mTxtCurrentTool.setText(R.string.label_bg);
                showBgColor();
                break;


            case R.id.imgSave:
                if (mCurrentView != null) {
                    mCurrentView.setInEdit(false);
                }
                if (mCurrentEditTextView != null) {
                    mCurrentEditTextView.setInEdit(false);
                }
                showSaveDialog();
                break;

//            case R.id.imgSav:
//                if (mCurrentView != null) {
//                    mCurrentView.setInEdit(false);
//                }
//                if (mCurrentEditTextView != null) {
//                    mCurrentEditTextView.setInEdit(false);
//                }
//                showSaveDialog();
//                break;

            case R.id.imgClose:
                showCloseDialog();
                break;


        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isTextOK) {
            addStickerView(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/temporary_holder_vintool_name_art.png"));
            isTextOK = false;
        }

        if (isTextOK) {
            addCustomStickerView(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/temporary_holder_vintool_name_art.png"));
            isTextOK = false;
        }
    }

    @SuppressLint("MissingPermission")
    private void saveImage() {
        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new SaveThread(getBitmapFromView(stickers), false).execute();

        }
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =stickerLayout.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(-1);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    public class SaveThread extends AsyncTask<Void, Void, Void> {
        Bitmap bitmap;
        private boolean share;

        public SaveThread(Bitmap bitmap, boolean share) {
            this.bitmap = bitmap;
            this.share = share;
            savingProcessing = new ProgressDialog(LogoMakerActivity.this);
            savingProcessing.setMessage("Saving..");
            savingProcessing.show();
        }

        protected Void doInBackground(Void... params) {
            linkSave = savePhoto(this.bitmap);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            savingProcessing.dismiss();
            if (linkSave.equals(BuildConfig.FLAVOR) || !this.share) {
                Toast.makeText(LogoMakerActivity.this, "Save in Logo Maker folder", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LogoMakerActivity.this, FreeCollageDone.class);
                intent.putExtra(LINK_PHOTO, linkSave);
                startActivityForResult(intent, UdpDataSource.DEFAULT_MAX_PACKET_SIZE);
                savingProcessing.dismiss();
                AdsUtility.showIntestitialAds();
                return;
            }
        }
    }

    public String savePhoto(Bitmap bmp) {
        Exception e;
        File imageFileFolder = new File(Environment.getExternalStorageDirectory(), "LogoMakerPro");
        imageFileFolder.mkdir();
        Calendar c = Calendar.getInstance();
        @SuppressLint("WrongConstant") File imageFileName = new File(imageFileFolder, (fromInt(c.get(2)) + fromInt(c.get(5)) + fromInt(c.get(1)) + fromInt(c.get(11)) + fromInt(c.get(12)) + fromInt(c.get(13))) + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(imageFileName);
            try {
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
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
        this.msConn = new MediaScannerConnection(this, new MediaScannerConnection.MediaScannerConnectionClient() {
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case CAMERA_REQUEST:
//                    Bitmap photo = (Bitmap) data.getExtras().get("data");
//                    if (photo == null) {
//                        Toast.makeText(getApplicationContext(), "Null vlaue", Toast.LENGTH_LONG).show();
//                    } else {
//                        mPhotoEditorView.getSource().setImageBitmap(photo);
//                    }
//                    break;
//                case PICK_REQUEST:
//                    try {
//                        Uri uri = data.getData();
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                        if (bitmap == null) {
//                            Toast.makeText(getApplicationContext(), "Null vlaue", Toast.LENGTH_LONG).show();
//                        } else {
//                            mPhotoEditorView.getSource().setImageBitmap(bitmap);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//            }
//        }
//    }

    private void showCloseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you want to Exit ?");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();

    }

    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you want to saving image ?");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveImage();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();

    }

    private void showBgColor() {
//        imageColor = (RecyclerView) mContext.findViewById(R.id.imageColor);
        imageColor.setVisibility(View.VISIBLE);
        imageColor.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        ColorAdapter colorAdapter = new ColorAdapter(ConstantData.bgColor, mContext);
        imageColor.setAdapter(colorAdapter);
        colorAdapter.setOnItemClickListener(new ColorAdapter.OnRecyclerViewItemClickListener() {
            public void onItemClick(View view, String resId) {
                value = resId;
                stickerLayout.setBackgroundColor(Color.parseColor(resId));
            }
        });
    }

    public static void onStickSelect(int position, int ids) {

        stickerArray = new ArrayList<>();
        stickerArray.clear();

        if(ids == 0){

        }
        else if (ids == 1) {
            stickerArray = ConstantData.art;
        } else if (ids == 2) {
            stickerArray = ConstantData.animals;
        } else if (ids == 3) {
            stickerArray = ConstantData.beauty;
        } else if (ids == 4) {
            stickerArray = ConstantData.commun;
        } else if (ids == 5) {
            stickerArray = ConstantData.business;
        } else if (ids == 6) {
            stickerArray = ConstantData.computer;
        } else if (ids == 7) {
            stickerArray = ConstantData.education;
        } else if (ids == 8) {
            stickerArray = ConstantData.enter;
        } else if (ids == 9) {
            stickerArray = ConstantData.events;
        } else if (ids == 10) {
            stickerArray = ConstantData.food;
        } else if (ids == 11) {
            stickerArray = ConstantData.health;
        } else if (ids == 12) {
            stickerArray = ConstantData.heart;
        } else if (ids == 13) {
            stickerArray = ConstantData.kids;
        } else if (ids == 14) {
            stickerArray = ConstantData.pattern;
        } else if (ids == 15) {
            stickerArray = ConstantData.shaps;
        } else if (ids == 16) {
            stickerArray = ConstantData.shop;
        } else if (ids == 17) {
            stickerArray = ConstantData.sports;
        }else if (ids == 18) {
            stickerArray = ConstantData.threed;
        }

        addStickerView(position);

    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getAssets().open(filePath));
        } catch (IOException e) {
        }
        return bitmap;
    }


    public static void addCustomStickerView(Bitmap bitmap) {
        final StickerView stickerView = new StickerView(mContext);
        stickerView.setBitmap(bitmap);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(stickerView);
                stickers.removeView(stickerView);
            }

            @Override
            public void onEdit(StickerView stickerView) {
                mCurrentView.setInEdit(false);
                mCurrentView = stickerView;
                mCurrentView.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView) {
                int position = mViews.indexOf(stickerView);
                if (position == mViews.size() - 1) {
                    return;
                }
                StickerView stickerTemp = (StickerView) mViews.remove(position);
                mViews.add(mViews.size(), stickerTemp);
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        stickers.addView(stickerView, lp);
        mViews.add(stickerView);
        setCurrentEdit(stickerView);
    }

    public static void addStickerView(int position) {
        final StickerView stickerView = new StickerView(mContext);
        stickerView.setImageResource(stickerArray.get(position).getImage());
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(stickerView);
                stickers.removeView(stickerView);
            }

            @Override
            public void onEdit(StickerView stickerView) {
                mCurrentView.setInEdit(false);
                mCurrentView = stickerView;
                mCurrentView.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView) {
                int position = mViews.indexOf(stickerView);
                if (position == mViews.size() - 1) {
                    return;
                }
                StickerView stickerTemp = (StickerView) mViews.remove(position);
                mViews.add(mViews.size(), stickerTemp);
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        stickers.addView(stickerView, lp);
        mViews.add(stickerView);
        setCurrentEdit(stickerView);
    }

    private void addStickerView(Bitmap bitmap) {
        final StickerView stickerView = new StickerView(this);
        stickerView.setBitmap(bitmap);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(stickerView);
                stickers.removeView(stickerView);
            }

            @Override
            public void onEdit(StickerView stickerView) {
//                if (mCurrentEditTextView != null) {
//                    mCurrentEditTextView.setInEdit(false);
//                }
                mCurrentView.setInEdit(false);
                mCurrentView = stickerView;
                mCurrentView.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView) {
                int position = mViews.indexOf(stickerView);
                if (position == mViews.size() - 1) {
                    return;
                }
                StickerView stickerTemp = (StickerView) mViews.remove(position);
                mViews.add(mViews.size(), stickerTemp);
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        stickers.addView(stickerView, lp);
        mViews.add(stickerView);
        setCurrentEdit(stickerView);
    }

    public static void setCurrentEdit(StickerView stickerView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }
//        if (mCurrentEditTextView != null) {
//            mCurrentEditTextView.setInEdit(false);
//        }
        mCurrentView = stickerView;
        stickerView.setInEdit(true);
    }

    public boolean requestPermission(String permission) {
        boolean isGranted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        if (!isGranted) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{permission},
                    READ_WRITE_STORAGE);
        }
        return isGranted;
    }

//    public void isPermissionGranted(boolean isGranted, String permission) {
//
//    }

    public void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case READ_WRITE_STORAGE:
//                isPermissionGranted(grantResults[0] == PackageManager.PERMISSION_GRANTED, permissions[0]);
//                break;
//        }
//    }

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
                    Toast.makeText(LogoMakerActivity.this, "Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Permission Denied
                    Toast.makeText(LogoMakerActivity.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(LogoMakerActivity.this)
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


}
