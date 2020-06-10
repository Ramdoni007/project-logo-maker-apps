package com.coolapps.logomaker.views;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.coolapps.logomaker.R;
import com.coolapps.logomaker.adapter.GridViewAdapter;
import com.coolapps.logomaker.utilities.AdsUtility;
import com.coolapps.logomaker.utilities.FreeCollageDone;
import com.coolapps.logomaker.utilities.Item;
import com.coolapps.logomaker.utilities.NetworkUtils;


public class MyWorkActivity extends Activity {

	public static String[] FilePathStrings;
	public static String[] FileNameStrings;
	public static final String LINK_PHOTO = "link_photo_es";
	private File[] listFile;
	GridView grid;
	GridViewAdapter adapter;
	File file;
	ArrayList<Item> path;
	ImageView imgClose;

	private LinearLayout adView;

	private int widthPixel;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mywork);

		adView = findViewById(R.id.adView);
		if(!NetworkUtils.isNetworkAvailable(this)) {

			adView.setVisibility(View.GONE);

		}else {


			adView.setVisibility(View.VISIBLE);
			AdsUtility.admobLargBannerCall(this, adView);

		}
		AdsUtility.InterstitialAdmob(this);

		DisplayMetrics mDisplayMatrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMatrics);
		widthPixel = mDisplayMatrics.widthPixels;

		initialized();
	}


	private void initialized() {

		imgClose = findViewById(R.id.imgClose);
		imgClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AdsUtility.showIntestitialAds();
				finish();
			}
		});

		path = new ArrayList<Item>();

		File file = new File(Environment.getExternalStorageDirectory(), "LogoMakerPro");


		if (!file.exists()) {
			file.mkdir();
		}

		if (file.isDirectory()) {
			listFile = file.listFiles();

			FilePathStrings = new String[listFile.length];

			FileNameStrings = new String[listFile.length];

			for (int i = 0; i < listFile.length; i++) {

				FilePathStrings[i] = listFile[i].getAbsolutePath();

				FileNameStrings[i] = listFile[i].getName();

				path.add(new Item(listFile[i].getAbsolutePath()));

			}

			if(FilePathStrings.length > 0){
				grid = findViewById(R.id.gridView);

				adapter = new GridViewAdapter(this, path);

				grid.setAdapter(adapter);

				grid.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
											int position, long id) {

						if (FileNameStrings[position] == null) {

						} else {

							File f = new File(FilePathStrings[position]);

							Uri uri = Uri.fromFile(f);

							Intent i = new Intent(MyWorkActivity.this,
									FreeCollageDone.class);
//							i.setData(uri);
							i.putExtra(LINK_PHOTO, FilePathStrings[position]);
							i.putExtra("picresolution", widthPixel);
							startActivity(i);
							AdsUtility.showIntestitialAds();
							finish();

						}

					}

				});
			}
		}


	}

	@Override
	public void onResume() {
		super.onResume();
	}
}
