package com.coolapps.logomaker.views;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.coolapps.logomaker.R;
import com.coolapps.logomaker.fragments.AnimalFragment;
import com.coolapps.logomaker.fragments.ArtFragment;
import com.coolapps.logomaker.fragments.BeautyFragment;
import com.coolapps.logomaker.fragments.BusinessFragment;
import com.coolapps.logomaker.fragments.CommunFragment;
import com.coolapps.logomaker.fragments.ComputerFragment;
import com.coolapps.logomaker.fragments.CustomPhotoFragment;
import com.coolapps.logomaker.fragments.EducationFragment;
import com.coolapps.logomaker.fragments.EnterFragment;
import com.coolapps.logomaker.fragments.EventsFragment;
import com.coolapps.logomaker.fragments.FoodFragment;
import com.coolapps.logomaker.fragments.HealthFragment;
import com.coolapps.logomaker.fragments.HeartFragment;
import com.coolapps.logomaker.fragments.KidsFragment;
import com.coolapps.logomaker.fragments.PatternFragment;
import com.coolapps.logomaker.fragments.ShapesFragment;
import com.coolapps.logomaker.fragments.ShopFragment;
import com.coolapps.logomaker.fragments.SportsFragment;
import com.coolapps.logomaker.fragments.ThreedFragment;
import com.coolapps.logomaker.utilities.ConstantData;
import com.coolapps.logomaker.utilities.Item;

import java.util.ArrayList;

/**
 * Created by waqar on 15/08/2017.
 */

public class StickerTabActivity extends AppCompatActivity {

    public ArrayList<Item> bgImages;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    public static int pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab);

        initialized();

    }

    private void initialized() {

        mViewPager = findViewById(R.id.pager);

        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager paramFragmentManager) {
            // TODO Auto-generated constructor stub
            super(paramFragmentManager);
        }

        public int getCount() {
            return 19;
        }

        public Fragment getItem(int paramInt) {

            switch (paramInt) {
                case 0:
                    return CustomPhotoFragment.newInstance(0, bgImages);

                case 1:
                    bgImages = new ArrayList();
                    bgImages = ConstantData.art;

                    return ArtFragment.newInstance(0, bgImages);

                case 2:
                    bgImages = new ArrayList();
                    bgImages = ConstantData.animals;

                    return AnimalFragment.newInstance(0, bgImages);
                case 3:
                    bgImages = new ArrayList();
                    bgImages = ConstantData.beauty;

                    return BeautyFragment.newInstance(0, bgImages);
                case 4:
                    bgImages = new ArrayList();
                    bgImages = ConstantData.commun;

                    return CommunFragment.newInstance(0, bgImages);
                case 5:
                    bgImages = new ArrayList();
                    bgImages = ConstantData.business;

                    return BusinessFragment.newInstance(0, bgImages);
                case 6:
                    bgImages = new ArrayList();
                    bgImages = ConstantData.computer;

                    return ComputerFragment.newInstance(0, bgImages);
                case 7:
                    bgImages = new ArrayList();
                    bgImages = ConstantData.education;

                    return EducationFragment.newInstance(0, bgImages);
                case 8:
                    bgImages = new ArrayList();
                    bgImages = ConstantData.enter;

                    return EnterFragment.newInstance(0, bgImages);
                case 9:
                    bgImages = new ArrayList();
                    bgImages = ConstantData.events;

                    return EventsFragment.newInstance(0, bgImages);
                case 10:
                    bgImages = new ArrayList();
                    bgImages = ConstantData.food;

                    return FoodFragment.newInstance(0, bgImages);
                case 11:
                    bgImages = new ArrayList();
                    bgImages = ConstantData.health;

                    return HealthFragment.newInstance(0, bgImages);
                case 12:
                    bgImages = new ArrayList();
                    bgImages = ConstantData.heart;

                    return HeartFragment.newInstance(0, bgImages);
                case 13:
                    bgImages = new ArrayList();
                    bgImages = ConstantData.kids;

                    return KidsFragment.newInstance(0, bgImages);
                case 14:
                    bgImages = new ArrayList();
                    bgImages = ConstantData.pattern;

                    return PatternFragment.newInstance(0, bgImages);
                case 15:
                    bgImages = new ArrayList();
                    bgImages = ConstantData.shaps;

                    return ShapesFragment.newInstance(0, bgImages);
                case 16:
                    bgImages = new ArrayList();
                    bgImages = ConstantData.shop;

                    return ShopFragment.newInstance(0, bgImages);
                case 17:
                    bgImages = new ArrayList();
                    bgImages = ConstantData.sports;

                    return SportsFragment.newInstance(0, bgImages);
                case 18:
                    bgImages = new ArrayList();
                    bgImages = ConstantData.threed;

                    return ThreedFragment.newInstance(0, bgImages);

                default:
                    return null;
            }

        }


        public CharSequence getPageTitle(int paramInt) {
            switch (paramInt) {

                case 0:
                    return "Custom Photo";
                case 1:
                    return "Art";
                case 2:
                    return "Animals";
                case 3:
                    return "Beauty";
                case 4:
                    return "Communication";
                case 5:
                    return "Business";
                case 6:
                    return "Computer";
                case 7:
                    return "Education";
                case 8:
                    return "Enter";
                case 9:
                    return "Event";
                case 10:
                    return "Food";
                case 11:
                    return "Health";
                case 12:
                    return "Heart";
                case 13:
                    return "Kids";
                case 14:
                    return "Pattern";
                case 15:
                    return "Shape";
                case 16:
                    return "Shop";
                case 17:
                    return "Sports";
                case 18:
                    return "Threed";

                default:
                    return null;
            }

        }
    }


}