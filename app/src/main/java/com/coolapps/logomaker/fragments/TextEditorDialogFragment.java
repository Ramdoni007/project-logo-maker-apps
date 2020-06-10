package com.coolapps.logomaker.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.coolapps.logomaker.BuildConfig;
import com.coolapps.logomaker.R;
import com.coolapps.logomaker.adapter.ColorAdapter;
import com.coolapps.logomaker.adapter.FontAdapter;
import com.coolapps.logomaker.utilities.ConstantData;
import com.coolapps.logomaker.views.LogoMakerActivity;

import java.io.File;
import java.io.FileOutputStream;

import me.grantland.widget.AutofitTextView;


public class TextEditorDialogFragment extends DialogFragment implements View.OnClickListener{


    private String[] listfont;

    public static final String TAG = TextEditorDialogFragment.class.getSimpleName();
    public static final String EXTRA_INPUT_TEXT = "extra_input_text";
    public static final String EXTRA_COLOR_CODE = "extra_color_code";
    private RecyclerView rvtext;
    private AutofitTextView  afltext;
    private EditText edtext;
    private InputMethodManager mInputMethodManager;
    private ImageView changeText, changeColor, changeFont, ivclose, ivdone;
    private   ImageView ivalign, ivcircle;
    int align = 0;
    int circle = 0;

    public static final String LINK_PHOTO = "link_photo_es";
    private String linkSave = yuku.ambilwarna.BuildConfig.FLAVOR;
    ProgressDialog savingProcessing;
    MediaScannerConnection msConn;

    private TextEditor mTextEditor;


    private String textsample, colorsample, fontsample;




    public interface TextEditor {
        void onDone(Bitmap bitmap, String inputText, int align, int circle, String color, String font);
    }


    //Show dialog with provide text and text color
    public static TextEditorDialogFragment show(@NonNull AppCompatActivity appCompatActivity,
                                                @NonNull String inputText,
                                                @ColorInt int colorCode) {
        Bundle args = new Bundle();
        args.putString(EXTRA_INPUT_TEXT, inputText);
        args.putInt(EXTRA_COLOR_CODE, R.color.white);
        TextEditorDialogFragment fragment = new TextEditorDialogFragment();
        fragment.setArguments(args);
        fragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return fragment;
    }

    //Show dialog with default text input as empty and text color white
    public static TextEditorDialogFragment show(@NonNull AppCompatActivity appCompatActivity) {
        return show(appCompatActivity,
                "", ContextCompat.getColor(appCompatActivity, R.color.black));
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        //Make dialog full screen with transparent background
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_text_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        rvtext = view.findViewById(R.id.rvtext);
        afltext = view.findViewById(R.id.afltext);
        edtext = view.findViewById(R.id.edtext);

        edtext.setText(LogoMakerActivity.inputText);

        changeText = view.findViewById(R.id.ivchangetext);
        changeColor = view.findViewById(R.id.ivchangecolor);
        changeFont = view.findViewById(R.id.ivchangefont);
        ivalign = view.findViewById(R.id.ivalign);
        ivalign.setTag(Integer.valueOf(1));
        ivcircle = view.findViewById(R.id.ivcircle);
        ivcircle.setTag(Integer.valueOf(0));
        ivclose = view.findViewById(R.id.imgClose);
        ivdone = view.findViewById(R.id.imgSave);

        edtext.setOnClickListener(this);
        changeText.setOnClickListener(this);
        changeColor.setOnClickListener(this);
        changeFont.setOnClickListener(this);
        ivalign.setOnClickListener(this);
        ivcircle.setOnClickListener(this);
        ivclose.setOnClickListener(this);
        ivdone.setOnClickListener(this);

        edtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                afltext.setText(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    //Callback to listener if user is done with text editing
    public void setOnTextEditorListener(TextEditor textEditor) {
        mTextEditor = textEditor;
    }


    private void loadFont() {

            listfont = ConstantData.fonts;

        if (listfont != null) {

            FontAdapter fontAdapter = new FontAdapter(listfont, getActivity());
            this.rvtext.setAdapter(fontAdapter);
            fontAdapter.setOnItemClickListener(new FontAdapter.OnRecyclerViewItemClickListener() {
                public void onItemClick(View view, String resId) {
                    loadSampleText(BuildConfig.FLAVOR, resId, BuildConfig.FLAVOR);
                }
            });
        }
    }

    private void setCircleText() {
        if (ivcircle.getTag().equals(Integer.valueOf(0))) {
            this.ivcircle.setImageResource(R.drawable.iccirclepressed);
            this.ivcircle.setTag(Integer.valueOf(1));
            circle = 1;
//            this.afltext.setShadowLayer(1.6f, BubbleTextView.MAX_SCALE_SIZE, BubbleTextView.MAX_SCALE_SIZE, -1);
        } else if (this.ivcircle.getTag().equals(Integer.valueOf(1))) {
            this.ivcircle.setTag(Integer.valueOf(0));
            this.ivcircle.setImageResource(R.drawable.iccircle);
            circle = 0;
            this.afltext.setShadowLayer(0.0f, 0.0f, 0.0f, -1);
        }
    }


    private void setAlignText() {
        if (this.ivalign.getTag().equals(Integer.valueOf(1))) {
            this.afltext.setGravity(3);
            this.ivalign.setImageResource(R.drawable.icalignleft);
            this.ivalign.setTag(Integer.valueOf(2));
            align = 2;
        } else if (this.ivalign.getTag().equals(Integer.valueOf(2))) {
            this.afltext.setGravity(5);
            this.ivalign.setImageResource(R.drawable.icalignright);
            this.ivalign.setTag(Integer.valueOf(3));
            align = 3;
        } else if (this.ivalign.getTag().equals(Integer.valueOf(3))) {
            this.afltext.setGravity(17);
            this.ivalign.setImageResource(R.drawable.iccentertextalignment);
            this.ivalign.setTag(Integer.valueOf(1));
            align = 1;
        }
    }



    private void loadSampleText(String color, String font, String text) {
        if (!text.equals(BuildConfig.FLAVOR)) {
            this.afltext.setText(text);
            this.edtext.setText(text);
            this.textsample = text;
        }
        if (!color.equals(BuildConfig.FLAVOR)) {
            this.afltext.setTextColor(Color.parseColor(color));
            this.colorsample = color;
        }
        if (!font.equals(BuildConfig.FLAVOR)) {
            this.afltext.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), font));
            this.fontsample = font;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.edtext:
                rvtext.setVisibility(View.INVISIBLE);
                edtext.setVisibility(View.VISIBLE);
                edtext.requestFocus();
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(edtext, 1);

                break;
            case R.id.ivchangetext:
                rvtext.setVisibility(View.INVISIBLE);
                edtext.setVisibility(View.VISIBLE);
                edtext.requestFocus();
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(edtext, 1);

                break;

            case R.id.ivchangecolor:
                rvtext.setVisibility(View.VISIBLE);
                edtext.setVisibility(View.INVISIBLE);
                closeKeyboard();
                rvtext.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
                ColorAdapter colorAdapter = new ColorAdapter(ConstantData.listColor, getActivity());
                rvtext.setAdapter(colorAdapter);
                colorAdapter.setOnItemClickListener(new ColorAdapter.OnRecyclerViewItemClickListener() {
                    public void onItemClick(View view, String resId) {
                        loadSampleText(resId, BuildConfig.FLAVOR, BuildConfig.FLAVOR);
                    }
                });
                break;

            case R.id.ivchangefont:

                rvtext.setVisibility(View.VISIBLE);
                edtext.setVisibility(View.INVISIBLE);
                closeKeyboard();
                rvtext.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
                loadFont();

                break;

            case R.id.ivalign:
                setAlignText();
                break;

            case R.id.ivcircle:
                setCircleText();
                break;

            case R.id.imgClose:
                getActivity().finish();
                break;

            case R.id.imgSave:
                closeKeyboard();
                this.textsample = this.afltext.getText().toString();
                if (!this.textsample.equals(BuildConfig.FLAVOR)) {
                    this.afltext.setDrawingCacheEnabled(true);
                    this.afltext.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
                    Bitmap bitmap = Bitmap.createBitmap(this.afltext.getDrawingCache());
                    this.afltext.setDrawingCacheEnabled(false);
                    dismiss();
                    new SaveThread(bitmap, false).execute();
                    LogoMakerActivity.isTextOK = true;
                    mTextEditor.onDone(bitmap, textsample, align, circle, colorsample, fontsample);
                    break;
                }
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
            linkSave = savePhoto(this.bitmap);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            savingProcessing.dismiss();
//            if (linkSave.equals(yuku.ambilwarna.BuildConfig.FLAVOR) || !this.share) {
//                getActivity().finish();
//                return;
//            }
//            Intent sharingIntent = new Intent("android.intent.action.SEND");
//            sharingIntent.setType("image/*");
//            sharingIntent.putExtra("android.intent.extra.SUBJECT", "Subject Here");
//            sharingIntent.putExtra("android.intent.extra.TEXT", "Here is the share content body");
//            sharingIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(EditTextActivity.this.linkSave)));
//            EditTextActivity.this.startActivity(Intent.createChooser(sharingIntent, "Share via"));
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
                return yuku.ambilwarna.BuildConfig.FLAVOR;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return yuku.ambilwarna.BuildConfig.FLAVOR;
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

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getActivity().getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
