package com.toto.downloader;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
 
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.onesignal.OneSignal;
import com.toto.downloader.history_f_indratech.History;
import com.toto.downloader.utils.AdmobAds;
import com.toto.downloader.utils.FbAds;
import com.toto.downloader.w_f_indratech.Whatsapp;
import com.toto.downloader.browsing_feature.BrowserManager;
import com.toto.downloader.download_feature.fragments.Downloads;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener{

    private static final String TAG = "VD_Debug";
    private EditText searchTextBar;
    private ImageView indratech_search_cancel;
    private ImageView btn_search, indratech_side_menu_image;
    private BrowserManager browserManager;
    private Uri appLinkData;
    private FragmentManager manager;
    private LinearLayout toolbar;
    private String adMode;
    private AdmobAds admobAds;
    private FbAds fbAds;
    private SliderLayout sliderLayout;
    private HashMap<String, String> HashMapForURL;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ConstraintLayout container;
   // private FirebaseFirestore db;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_indratech);



        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        //check if user logged in
        sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean("loggedIn", true);
        editor.apply();

        boolean isLogged = sp.getBoolean("loggedIn", false);

        Log.d(TAG, isLogged+"main");

        container = findViewById(R.id.container);

        // Slider
        sliderLayout = findViewById(R.id.slider);




        // navigation drawer
        indratech_side_menu_image = findViewById(R.id.indratech_side_menu_image);
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.indratech_side_menu);

        drawer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        navigationView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        container.setBackgroundColor(getResources().getColor(android.R.color.white));

        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            private float scaleFactor = 4f;
            @Override
            public void onDrawerSlide(   View drawerView, float slideOffset) {
                float slideX = drawerView.getWidth() * slideOffset;
                container.setTranslationX(slideX);
                container.setScaleX(1 - (slideOffset / scaleFactor));
                container.setScaleY(1 - (slideOffset / scaleFactor));
            }

            @Override
            public void onDrawerOpened(   View drawerView) {

            }

            @Override
            public void onDrawerClosed(   View drawerView) {


                container.setBackgroundColor(getResources().getColor(android.R.color.white));

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        indratech_side_menu_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        drawer.setScrimColor(Color.TRANSPARENT);
        drawer.setDrawerElevation(0f);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(   MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        homeClicked();
                        break;
                    case R.id.nav_history:
                       historyClicked();
                        break;

                    case R.id.nav_downloads:
                        downloadClicked();
                        break;

                    case R.id.nav_share:
                        indratech_shareApp();
                        break;

                    case R.id.nav_rate:
                        indratech_rateApp();
                        break;

                    case R.id.nav_more_app:
                        indratech_rateApp();
                        break;

                    case R.id.nav_privacy:
                        indratech_privacyPolicyClicked();
                        break;

                    case R.id.nav_info:
                        aboutUsClicked();
                        break;



                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });




        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        //String appLinkAction = appLinkIntent.getAction();
        appLinkData = appLinkIntent.getData();

        manager = this.getSupportFragmentManager();
        // This is for creating browser_indratech manager fragment
        if ((browserManager = (BrowserManager) this.getSupportFragmentManager().findFragmentByTag("BM")) == null)
        {
            manager.beginTransaction()
                    .add(browserManager = new BrowserManager(), "BM").commit();
        }


        toolbar = findViewById(R.id.toolbar);

        setUPBrowserToolbarView();
        setUpVideoSites();

        //Set ad fb or admob from firebase.







    }

    private void setUPBrowserToolbarView(){

        // Toolbar search
        indratech_search_cancel = findViewById(R.id.indratech_search_cancel);
        indratech_search_cancel.setOnClickListener(this);
        btn_search = findViewById(R.id.btn_search);
        searchTextBar = findViewById(R.id.indratech_search_bar);

        TextWatcher searchViewTextWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    indratech_search_cancel.setVisibility(View.GONE);
                } else {
                    indratech_search_cancel.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        };
        searchTextBar.addTextChangedListener(searchViewTextWatcher);
        searchTextBar.setOnEditorActionListener(this);
        btn_search.setOnClickListener(this);

        //Toolbar home button
        ImageView toolbar_home = findViewById(R.id.indratech_btn_home);
        toolbar_home.setOnClickListener(this);

        //Settings
        ImageView settings = findViewById(R.id.btn_settings);
        settings.setOnClickListener(this);

        //About us
        ImageView about_us = findViewById(R.id.indratech_btn_info);
        about_us.setOnClickListener(this);
    }

    private void setUpVideoSites(){
        // Video sites link
        RecyclerView videoSites = findViewById(R.id.rvVideoSitesList);
        videoSites.setNestedScrollingEnabled(false);
        videoSites.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
        videoSites.setAdapter(new VideoSitesList(this));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.indratech_search_cancel:
                searchTextBar.getText().clear();
                break;
            case R.id.indratech_btn_home:
                showToolbar();
                showSlider();
                searchTextBar.getText().clear();
                getBrowserManager().closeAllWindow();
                break;
            case R.id.btn_settings:
                settingsClicked();
                break;
            case R.id.btn_search:
                new WebConnect(searchTextBar, this).connect();
                break;
            case R.id.indratech_btn_info:
                aboutUsClicked();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_GO) {
            new WebConnect(searchTextBar, this).connect();
        }
        return handled;
    }

    @Override
    public void onBackPressed() {
        if (manager.findFragmentByTag("Downloads") != null ||
                manager.findFragmentByTag("History") != null ||
                    manager.findFragmentByTag("Whatsapp") != null) {
            VDApp.getInstance().getOnBackPressedListener().onBackpressed();
            browserManager.resumeCurrentWindow();
        }
        else if (manager.findFragmentByTag("settings_indratech") != null) {
            VDApp.getInstance().getOnBackPressedListener().onBackpressed();
            browserManager.resumeCurrentWindow();
        }
        else if (VDApp.getInstance().getOnBackPressedListener() != null) {
            VDApp.getInstance().getOnBackPressedListener().onBackpressed();
        }else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else  {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create()
                    .show();
        }
    }

    public BrowserManager getBrowserManager() {
        return browserManager;
    }

    public interface OnBackPressedListener {
        void onBackpressed();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        VDApp.getInstance().setOnBackPressedListener(onBackPressedListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (appLinkData != null) {
            browserManager.newWindow(appLinkData.toString());
        }
        browserManager.updateAdFilters();
    }

    public void browserClicked() {
        browserManager.unhideCurrentWindow();
    }

    private void downloadClicked(){
        closeHistory();
        closeWhatsapp();
        if (manager.findFragmentByTag("Downloads") == null) {
            browserManager.hideCurrentWindow();
            browserManager.pauseCurrentWindow();
            manager.beginTransaction().add(R.id.indratech_main_content, new Downloads(), "Downloads").commit();
        }
    }

    public void whatsappClicked(){
        closeDownloads();
        closeHistory();
        if (manager.findFragmentByTag("Whatsapp") == null) {
            browserManager.hideCurrentWindow();
            browserManager.pauseCurrentWindow();
            manager.beginTransaction().add(R.id.indratech_main_content, new Whatsapp(), "Whatsapp").commit();
        }
    }

    private void historyClicked(){
        closeDownloads();
        closeWhatsapp();
        if (manager.findFragmentByTag("History") == null) {
            browserManager.hideCurrentWindow();
            browserManager.pauseCurrentWindow();
            manager.beginTransaction().add(R.id.indratech_main_content, new History(), "History").commit();
        }
    }

    private void settingsClicked(){
        if (manager.findFragmentByTag("settings_indratech") == null) {
            browserManager.hideCurrentWindow();
            browserManager.pauseCurrentWindow();
            manager.beginTransaction().add(R.id.indratech_main_content, new Settings(), "settings_indratech").commit();
        }
    }

    private void aboutUsClicked(){
        if(manager.findFragmentByTag("AboutUs") == null){
            browserManager.hideCurrentWindow();
            browserManager.pauseCurrentWindow();
            manager.beginTransaction().add(R.id.indratech_main_content, new AboutUs(), "AboutUs").commit();
        }
    }

    private void homeClicked(){
        browserManager.unhideCurrentWindow();
        browserManager.resumeCurrentWindow();
        closeDownloads();
        closeHistory();
        closeWhatsapp();
    }

    private void closeDownloads() {
        Fragment fragment = manager.findFragmentByTag("Downloads");
        if (fragment != null) {
            manager.beginTransaction().remove(fragment).commit();
        }
    }

    private void closeHistory() {
        Fragment fragment = manager.findFragmentByTag("History");
        if (fragment != null) {
            manager.beginTransaction().remove(fragment).commit();
        }
    }

    private void closeWhatsapp() {
        Fragment fragment = manager.findFragmentByTag("Whatsapp");
        if (fragment != null) {
            manager.beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,    String[] permissions,    int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        onRequestPermissionsResultCallback.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }

    private ActivityCompat.OnRequestPermissionsResultCallback onRequestPermissionsResultCallback;

    public void setOnRequestPermissionsResultListener(ActivityCompat
                                                              .OnRequestPermissionsResultCallback
                                                              onRequestPermissionsResultCallback) {
        this.onRequestPermissionsResultCallback = onRequestPermissionsResultCallback;
    }

    public void hideToolbar(){
        toolbar.setVisibility(View.GONE);
    }

    public void showToolbar(){
        toolbar.setVisibility(View.VISIBLE);
    }

    public void hideSlider(){
        sliderLayout.setVisibility(View.GONE);

    }

    public void showSlider(){
        sliderLayout.setVisibility(View.VISIBLE);
    }

    public void loadInterstitialAd(){
        if(adMode.equals("0"))
            fbAds.loadInterstitialAd();
        else
            admobAds.loadInterstitialAd();
    }

    private void indratech_privacyPolicyClicked(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacy_policy_link_indratech)));
        startActivity(browserIntent);
    }

    public void indratech_shareApp()
    {
        StringBuilder msg = new StringBuilder();
        msg.append(getString(R.string.share_msg_indratech));
        msg.append("\n");
        msg.append("https://play.google.com/store/apps/details?id=com.toto.downloader");
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg.toString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void indratech_rateApp()
    {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, this.getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        else
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        intent.addFlags(flags);
        return intent;
    }


    @Override
    protected void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }
}
