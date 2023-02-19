package com.toto.downloader;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroFragment;
import com.github.appintro.model.SliderPage;
 


public class IntroActivity extends AppIntro2 {

    @Override
    protected void onCreate(   Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        int balck = Color.BLACK;

        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("Welcome");
        sliderPage.setDescription("this is demo text");
        sliderPage.setImageDrawable(R.drawable.intro1);
        sliderPage.setBackgroundDrawable(R.drawable.white_background);
        sliderPage.setTitleColor(balck);
        sliderPage.setDescriptionColor(balck);

        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("This is second slider");
        sliderPage2.setDescription("this is demo text");
        sliderPage2.setImageDrawable(R.drawable.intro2);
        sliderPage2.setBackgroundDrawable(R.drawable.white_background);
        sliderPage2.setTitleColor(balck);
        sliderPage2.setDescriptionColor(balck);

        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle("This is third slider");
        sliderPage3.setDescription("this is demo text");
        sliderPage3.setImageDrawable(R.drawable.intro3);
        sliderPage3.setBackgroundDrawable(R.drawable.white_background);
        sliderPage3.setTitleColor(balck);
        sliderPage3.setDescriptionColor(balck);

        addSlide(AppIntroFragment.newInstance(sliderPage));
        addSlide(AppIntroFragment.newInstance(sliderPage2));
        addSlide(AppIntroFragment.newInstance(sliderPage3));

        setIndicatorEnabled(true);
        setProgressIndicator();

    }

    @Override
    protected void onDonePressed(   Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    @Override
    protected void onSkipPressed(   Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
