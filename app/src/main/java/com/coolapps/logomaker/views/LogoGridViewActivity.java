package com.coolapps.logomaker.views;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.coolapps.logomaker.R;
import com.coolapps.logomaker.adapter.LogoGridviewAdapter;
import com.coolapps.logomaker.utilities.ConstantData;

/**
 * Created by apple on 3/3/18.
 */

public class LogoGridViewActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private GridView specificlogogridview;
    private LogoGridviewAdapter mLogoGridViewAdapter;
    private String logoname;

    private Intent it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logogridview);

        it = getIntent();
        logoname = it.getStringExtra("logoname");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(logoname);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
//                AdsUtility.showIntestitialAds();
            }
        });


        initialized();

    }

    private void initialized(){

        specificlogogridview = findViewById(R.id.specificlogogridview);
        mLogoGridViewAdapter = new LogoGridviewAdapter(this, R.layout.logo_specific_item_gridview, ConstantData.specificLogo);
        specificlogogridview.setAdapter(mLogoGridViewAdapter);
        specificlogogridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(LogoGridViewActivity.this, LogoMakerActivity.class));
            }
        });
    }
}
