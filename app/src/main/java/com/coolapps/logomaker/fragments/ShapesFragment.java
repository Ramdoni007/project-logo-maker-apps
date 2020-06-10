package com.coolapps.logomaker.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.coolapps.logomaker.R;
import com.coolapps.logomaker.adapter.DefaultBGAdapter;
import com.coolapps.logomaker.utilities.AdsUtility;
import com.coolapps.logomaker.utilities.Item;
import com.coolapps.logomaker.views.LogoMakerActivity;

import java.util.ArrayList;

public class ShapesFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    ArrayList<Item> lines;
    private LinearLayout adView;
//    private static int ids;

    public static ShapesFragment newInstance(int paramInt,
                                             ArrayList<Item> paramArrayList) {
        ShapesFragment localBarishFragment = new ShapesFragment();
        localBarishFragment.lines = paramArrayList;
//        ids = paramInt;
        return localBarishFragment;
    }


    public View onCreateView(LayoutInflater paramLayoutInflater,
                             ViewGroup paramViewGroup, Bundle paramBundle) {

        View localView = paramLayoutInflater.inflate(R.layout.fragment_background,
                paramViewGroup, false);

        GridView localListView = localView
                .findViewById(R.id.bggridview);
        adView = localView.findViewById(R.id.banneradd);
        AdsUtility.admobBannerCall(this.getActivity(), adView);
        localListView.setAdapter(new DefaultBGAdapter(getActivity(), lines));
        localListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(
                            AdapterView<?> paramAnonymousAdapterView,
                            View paramAnonymousView, int paramAnonymousInt,
                            long paramAnonymousLong) {
                        LogoMakerActivity.onStickSelect(paramAnonymousInt, 15);
                        getActivity().finish();

                    }
                });
        return localView;
    }


}
