package com.coolapps.logomaker.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.coolapps.logomaker.R;
import com.coolapps.logomaker.adapter.LogoGridviewAdapter;
import com.coolapps.logomaker.utilities.ConstantData;


public class StickerCatDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = StickerCatDialogFragment.class.getSimpleName();
    public static final String EXTRA_INPUT_TEXT = "extra_input_text";
    public static final String EXTRA_COLOR_CODE = "extra_color_code";
    private RecyclerView rvtext;

    private LogoGridviewAdapter mLogoGridViewAdapter;
    private GridView imageGridView;

    private StickerEditor mStickerEditor;


    public interface StickerEditor {
        void onDone(int position);
    }

    //Callback to listener if user is done with text editing
    public void setOnStickerEditorListener(StickerCatDialogFragment.StickerEditor stickerEditor) {
        mStickerEditor = stickerEditor;
    }


    public static StickerCatDialogFragment show(@NonNull AppCompatActivity appCompatActivity) {
        Bundle args = new Bundle();
        StickerCatDialogFragment fragment = new StickerCatDialogFragment();
        fragment.setArguments(args);
        fragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return fragment;
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
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_sticker_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageGridView = view.findViewById(R.id.specificlogogridview);

        mLogoGridViewAdapter = new LogoGridviewAdapter(getActivity(), R.layout.logo_item_gridview, ConstantData.mainLogo);
        imageGridView.setAdapter(mLogoGridViewAdapter);
        imageGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mStickerEditor.onDone(position);
                dismiss();
            }
        });


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imgClose:
                getActivity().finish();
                break;
        }
    }


}
