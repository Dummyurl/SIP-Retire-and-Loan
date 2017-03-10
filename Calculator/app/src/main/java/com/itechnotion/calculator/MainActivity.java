package com.itechnotion.calculator;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private AdView mAdView;
    InterstitialAd mInterstitialAd;

    Handler handlerInterstitialAd = new Handler();
    Handler handlerBannerTen = new Handler();
    Handler handlerBannerTwo = new Handler();

    Handler handlerInterstitialTen = new Handler();
    Handler handlerInterstitialTwo = new Handler();

    Timer timerBanner, timerInterstitial;
    TimerTask timerTaskBanner, timerTaskInterstitial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (isNetworkConnected(MainActivity.this)) {
            adMobBannerInit();
            adMobInterstitialInit();
            startTimer();
        }


    }

    private void startTimer() {
        timerBanner = new Timer();

        initializeTimerTask();
        timerBanner.schedule(timerTaskBanner, 10000, 120000); //
        //  timerInterstitial.schedule(timerTaskInterstitial, 0, 120000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timerBanner != null) {
            timerBanner.cancel();
            timerBanner = null;
        }

    }

    public void initializeTimerTask() {

        timerTaskBanner = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp
                handlerBannerTwo.post(new Runnable() {
                    public void run() {
                        //Toast.makeText(getApplicationContext(), 120000 / 1000 + "", Toast.LENGTH_SHORT).show();
                        Log.e("handler :", " 20");
                        mAdView.setVisibility(View.VISIBLE);
                        handlerBannerTen.postDelayed(new Runnable() {
                            public void run() {
                                Log.e("handler :", " 10");
                                //Toast.makeText(getApplicationContext(), 10000 / 1000 + "", Toast.LENGTH_SHORT).show();
                                mAdView.setVisibility(View.GONE);
                            }

                        }, 10000);

                    }
                });
            }
        };

    }

    private void adMobBannerInit() {

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        mAdView.setVisibility(View.VISIBLE);
    }

    /**
     * Adding custom view to tab
     */
    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("SIP");
        //tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_call, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Retirement");
        // tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_launcher, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Loan");
        // tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_launcher, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    /**
     * Adding fragments to ViewPager
     *
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new SIPFragment(), "SIP");
        adapter.addFrag(new RetirementFragment(), "Retirement");
        adapter.addFrag(new LoanFragment(), "Loan");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        stoptimertask();
        handlerInterstitialAd.removeCallbacksAndMessages(null);
        handlerBannerTwo.removeCallbacksAndMessages(null);
        handlerBannerTen.removeCallbacksAndMessages(null);
        //handlerBanner.removeCallbacksAndMessages(null);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        if (timerBanner != null) {
            startTimer();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        handlerInterstitialAd.removeCallbacksAndMessages(null);
        stoptimertask();
        handlerBannerTwo.removeCallbacksAndMessages(null);
        handlerBannerTen.removeCallbacksAndMessages(null);
        handlerInterstitialTen.removeCallbacksAndMessages(null);
        handlerInterstitialTwo.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void adMobInterstitialInit() {

        //interstitial
        mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        final AdRequest adRequest = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.e("Advt", "closed");

                handlerInterstitialAd.postDelayed(new Runnable() {
                    public void run() {
                        mInterstitialAd.loadAd(adRequest);
                        handlerInterstitialAd.postDelayed(this, 10000); //now is every 2 minutes
                    }
                }, 120000); //Every 120000 ms (2 minutes)


            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


}
