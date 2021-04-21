package id.indrasudirman.setoranmurojaahapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class SplashScreen extends Activity {

    private static int SPLASH_SCREEN_DELAY = 5000;

    private Animation topAnimation, bottomAnimation;
    private AppCompatTextView setoran, murojaah;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        setoran = findViewById(R.id.setoran);
        murojaah = findViewById(R.id.murojaah);


        startAnimationStyle();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        }, SPLASH_SCREEN_DELAY);

    }

    private void startAnimationStyle() {
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        setoran.setAnimation(topAnimation);
        murojaah.setAnimation(bottomAnimation);

    }

}
