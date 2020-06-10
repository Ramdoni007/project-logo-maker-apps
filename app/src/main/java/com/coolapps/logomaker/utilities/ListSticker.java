package com.coolapps.logomaker.utilities;

import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coolapps.logomaker.R;
import com.coolapps.logomaker.adapter.ImageAdapterSticker;

public class ListSticker extends AppCompatActivity {
    public static int CAMERA_HEIGHT;
    public static int CAMERA_WIDTH;
//    private AdView adView;
    private GridView gridView;
    public String[] mThumbIds = new String[]{"sticker_thumb_1.png", "sticker_thumb_2.png", "sticker_thumb_3.png", "sticker_thumb_4.png", "sticker_thumb_5.png", "sticker_thumb_6.png", "sticker_thumb_7.png", "sticker_thumb_8.png", "sticker_thumb_9.png", "sticker_thumb_10.png", "sticker_thumb_11.png", "sticker_thumb_12.png", "sticker_thumb_13.png", "sticker_thumb_14.png", "sticker_thumb_15.png", "sticker_thumb_16.png", "sticker_thumb_17.png", "sticker_thumb_18.png", "sticker_thumb_19.png", "sticker_thumb_20.png", "sticker_thumb_21.png", "sticker_thumb_22.png", "sticker_thumb_23.png", "sticker_thumb_24.png", "sticker_thumb_25.png", "sticker_thumb_26.png", "sticker_thumb_27.png", "sticker_thumb_28.png", "sticker_thumb_29.png", "sticker_thumb_30.png", "sticker_thumb_31.png", "sticker_thumb_32.png", "sticker_thumb_33.png", "sticker_thumb_34.png", "sticker_thumb_35.png", "sticker_thumb_36.png", "sticker_thumb_37.png", "sticker_thumb_38.png", "sticker_thumb_39.png", "sticker_thumb_40.png", "sticker_thumb_41.png", "sticker_thumb_42.png", "sticker_thumb_43.png", "sticker_thumb_44.png", "sticker_thumb_45.png", "sticker_thumb_46.png", "sticker_thumb_47.png", "sticker_thumb_48.png", "sticker_thumb_49.png", "sticker_thumb_50.png", "sticker_thumb_51.png", "sticker_thumb_52.png", "sticker_thumb_53.png", "sticker_thumb_54.png", "sticker_thumb_55.png", "sticker_thumb_56.png", "sticker_thumb_57.png", "sticker_thumb_58.png", "sticker_thumb_59.png", "sticker_thumb_60.png", "sticker_thumb_61.png", "sticker_thumb_62.png", "sticker_thumb_63.png", "sticker_thumb_64.png", "sticker_thumb_65.png", "sticker_thumb_66.png", "sticker_thumb_67.png", "sticker_thumb_68.png", "sticker_thumb_69.png", "sticker_thumb_70.png", "sticker_thumb_71.png", "sticker_thumb_72.png", "sticker_thumb_73.png", "sticker_thumb_74.png", "sticker_thumb_75.png", "sticker_thumb_76.png", "sticker_thumb_77.png", "sticker_thumb_78.png", "sticker_thumb_79.png", "sticker_thumb_80.png", "sticker_thumb_81.png", "sticker_thumb_82.png", "sticker_thumb_83.png", "sticker_thumb_84.png", "sticker_thumb_85.png", "sticker_thumb_86.png", "sticker_thumb_87.png", "sticker_thumb_88.png", "sticker_thumb_89.png", "sticker_thumb_90.png", "sticker_thumb_91.png", "sticker_thumb_92.png", "sticker_thumb_93.png", "sticker_thumb_94.png", "sticker_thumb_95.png", "sticker_thumb_96.png", "sticker_thumb_97.png", "sticker_thumb_98.png", "sticker_thumb_99.png", "sticker_thumb_100.png", "sticker_thumb_101.png", "sticker_thumb_102.png", "sticker_thumb_103.png", "sticker_thumb_104.png", "sticker_thumb_105.png", "sticker_thumb_106.png", "sticker_thumb_107.png", "sticker_thumb_108.png", "sticker_thumb_109.png", "sticker_thumb_110.png", "sticker_thumb_111.png", "sticker_thumb_112.png", "sticker_thumb_113.png", "sticker_thumb_114.png", "sticker_thumb_115.png", "sticker_thumb_116.png", "sticker_thumb_117.png", "sticker_thumb_118.png", "sticker_thumb_119.png", "sticker_thumb_120.png", "sticker_thumb_121.png", "sticker_thumb_122.png", "sticker_thumb_123.png", "sticker_thumb_124.png", "sticker_thumb_125.png", "sticker_thumb_126.png", "sticker_thumb_127.png", "sticker_thumb_128.png", "sticker_thumb_129.png", "sticker_thumb_130.png", "sticker_thumb_131.png", "sticker_thumb_132.png", "sticker_thumb_133.png", "sticker_thumb_134.png", "sticker_thumb_135.png", "sticker_thumb_136.png", "sticker_thumb_137.png", "sticker_thumb_138.png", "sticker_thumb_139.png", "sticker_thumb_140.png", "sticker_thumb_141.png", "sticker_thumb_142.png", "sticker_thumb_143.png", "sticker_thumb_144.png", "sticker_thumb_145.png", "sticker_thumb_146.png", "sticker_thumb_147.png", "sticker_thumb_148.png", "sticker_thumb_149.png", "sticker_thumb_140.png", "sticker_thumb_151.png", "sticker_thumb_152.png", "sticker_thumb_153.png", "sticker_thumb_154.png", "sticker_thumb_155.png", "sticker_thumb_156.png", "sticker_thumb_157.png", "sticker_thumb_158.png", "sticker_thumb_159.png", "sticker_thumb_150.png", "sticker_thumb_161.png", "sticker_thumb_162.png", "sticker_thumb_163.png", "sticker_thumb_164.png", "sticker_thumb_165.png", "sticker_thumb_166.png", "sticker_thumb_167.png", "sticker_thumb_168.png", "sticker_thumb_169.png", "sticker_thumb_160.png", "sticker_thumb_171.png", "sticker_thumb_172.png", "sticker_thumb_173.png", "sticker_thumb_174.png", "sticker_thumb_175.png", "sticker_thumb_176.png", "sticker_thumb_177.png", "sticker_thumb_178.png", "sticker_thumb_179.png", "sticker_thumb_180.png", "sticker_thumb_181.png", "sticker_thumb_182.png", "sticker_thumb_183.png", "sticker_thumb_184.png", "sticker_thumb_185.png", "sticker_thumb_186.png", "sticker_thumb_187.png", "sticker_thumb_188.png", "sticker_thumb_189.png", "sticker_thumb_190.png", "sticker_thumb_191.png", "sticker_thumb_192.png", "sticker_thumb_193.png", "sticker_thumb_194.png", "sticker_thumb_195.png", "sticker_thumb_196.png", "sticker_thumb_197.png", "sticker_thumb_198.png", "sticker_thumb_199.png", "sticker_thumb_120.png", "sticker_thumb_201.png", "sticker_thumb_202.png", "sticker_thumb_203.png", "sticker_thumb_204.png", "sticker_thumb_205.png", "sticker_thumb_206.png", "sticker_thumb_207.png", "sticker_thumb_208.png", "sticker_thumb_209.png", "sticker_thumb_210.png", "sticker_thumb_211.png", "sticker_thumb_212.png", "sticker_thumb_213.png", "sticker_thumb_214.png", "sticker_thumb_215.png", "sticker_thumb_216.png", "sticker_thumb_217.png", "sticker_thumb_218.png", "sticker_thumb_219.png", "sticker_thumb_220.png", "sticker_thumb_221.png", "sticker_thumb_222.png", "sticker_thumb_223.png", "sticker_thumb_224.png", "sticker_thumb_225.png", "sticker_thumb_226.png", "sticker_thumb_227.png", "sticker_thumb_228.png", "sticker_thumb_229.png", "sticker_thumb_230.png", "sticker_thumb_231.png", "sticker_thumb_232.png", "sticker_thumb_233.png", "sticker_thumb_234.png", "sticker_thumb_235.png", "sticker_thumb_236.png", "sticker_thumb_237.png", "sticker_thumb_238.png", "sticker_thumb_239.png", "sticker_thumb_240.png", "sticker_thumb_241.png", "sticker_thumb_242.png", "sticker_thumb_243.png", "sticker_thumb_244.png", "sticker_thumb_245.png", "sticker_thumb_246.png", "sticker_thumb_247.png", "sticker_thumb_248.png", "sticker_thumb_249.png", "sticker_thumb_250.png"};
    private RelativeLayout mainLayout;

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        super.onCreate(savedInstanceState);
        this.mainLayout = new RelativeLayout(this);
        this.mainLayout.setBackgroundColor(Color.parseColor("#383330"));
        setContentView(R.layout.choice_frame);
        ((TextView) findViewById(R.id.txt_tit)).setText("Add Heart");
        Display display = getWindowManager().getDefaultDisplay();
        CAMERA_WIDTH = display.getWidth();
        CAMERA_HEIGHT = display.getHeight();
        this.gridView = findViewById(R.id.gridView1);
        this.gridView.setBackgroundColor(Color.parseColor("#ffffff"));
        this.gridView.setAdapter(new ImageAdapterSticker(this, this.mThumbIds));
        this.gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                ListSticker.this.finish();
                Editor editor = PreferenceManager.getDefaultSharedPreferences(ListSticker.this).edit();
                editor.putInt("com.vt.na.sticker_id", arg2 + 1);
                editor.putInt("com.vt.na.sticker_id.true", 1);
                editor.apply();
            }
        });
        this.gridView.setSelection(PreferenceManager.getDefaultSharedPreferences(this).getInt("com.vt.na.sticker_id", 1));
        Toast.makeText(this, "Sroll to show more Sticker", Toast.LENGTH_LONG).show();
        RelativeLayout adViewContainer = findViewById(R.id.adViewContainer);
//        this.adView = new AdView(this, getResources().getString(R.string.fb_banner), AdSize.BANNER_320_50);
//        adViewContainer.addView(this.adView);
//        this.adView.loadAd();
    }
}
