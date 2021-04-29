package id.indrasudirman.setoranmurojaahapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import id.indrasudirman.setoranmurojaahapp.databinding.ActivityMainBinding;
import id.indrasudirman.setoranmurojaahapp.databinding.ActivityMainMenuBinding;
import id.indrasudirman.setoranmurojaahapp.databinding.LayoutToolbarProfileBinding;

public class MainMenu extends AppCompatActivity {

    ActivityMainMenuBinding mainMenuBinding;
    LayoutToolbarProfileBinding layoutToolbarProfileBinding;

    AppCompatTextView tanggalMasehi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_menu);

        //View Binding change findViewById
        mainMenuBinding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        layoutToolbarProfileBinding = LayoutToolbarProfileBinding.inflate(getLayoutInflater());
        View view = mainMenuBinding.getRoot();
        setContentView(view);

        tanggalMasehi = findViewById(R.id.tanggalMasehi);

        String todayTanggalMasehi = setTanggalMasehi();

        tanggalMasehi.setText(todayTanggalMasehi);
    }

    private String setTanggalMasehi() {
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String todayDateMasehi = df.format(date);
        Log.d("TAG", todayDateMasehi);
        return todayDateMasehi;
    }

    public void setToolbar(@Nullable String title) {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbar.setTitle(title);
    }
}