package com.myinfos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.myinfos.db.MyDb;
import com.myinfos.models.InfoDetails;

import java.util.List;

/**
 * Created by admin on 14/07/2016.
 */
public class SwipeActivity extends AppCompatActivity {

    int NUM_ITEMS = 0;
    PlanetFragmentPagerAdapter planetFragmentPagerAdapter;
    ViewPager viewPager;
    int selectedPos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        selectedPos=getIntent().getIntExtra("position",0);
        NUM_ITEMS=getIntent().getIntExtra("count",0);
        planetFragmentPagerAdapter = new PlanetFragmentPagerAdapter(getSupportFragmentManager(),selectedPos);
        viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(planetFragmentPagerAdapter);
        Button button = (Button)findViewById(R.id.goto_previous);
        button.setOnClickListener(btnListener);
        button = (Button)findViewById(R.id.goto_next);
        button.setOnClickListener(btnListener);
        viewPager.setCurrentItem(selectedPos);
    }

    private View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.goto_previous:
                    viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                    break;
                case R.id.goto_next:
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                    break;
            }
        }
    };


    public class PlanetFragmentPagerAdapter extends FragmentPagerAdapter {

        public PlanetFragmentPagerAdapter(FragmentManager fm, int selectedPos) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {

            SwipeFragment fragment = new SwipeFragment();
            return SwipeFragment.newInstance(position);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
