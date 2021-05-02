package id.indrasudirman.setoranmurojaahapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;

import android.annotation.SuppressLint;
import android.icu.util.IslamicCalendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.joda.time.Chronology;
import org.joda.time.LocalDate;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.IslamicChronology;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import id.indrasudirman.setoranmurojaahapp.databinding.ActivityMainBinding;
import id.indrasudirman.setoranmurojaahapp.databinding.ActivityMainMenuBinding;
import id.indrasudirman.setoranmurojaahapp.databinding.LayoutToolbarProfileBinding;

public class MainMenu extends AppCompatActivity {

    ActivityMainMenuBinding mainMenuBinding;
    LayoutToolbarProfileBinding layoutToolbarProfileBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_menu);

        //View Binding change findViewById
        mainMenuBinding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        View view = mainMenuBinding.getRoot();
        setContentView(view);

        layoutToolbarProfileBinding = mainMenuBinding.layoutToolbarProfile;


        //Set Tanggal Masehi
        layoutToolbarProfileBinding.tanggalMasehi.setText(setTanggalMasehi());
        //Set Tanggal Hijriyah
        layoutToolbarProfileBinding.tanggalHijriyah.setText(setTanggalMasehi());

        String[] tanggalHijri = setTanggalHijriyah();
        Log.d("Bulan ", tanggalHijri[0]);
        Log.d("Tanggal ", tanggalHijri[1]);
        Log.d("Tahun ", tanggalHijri[2]);
    }

    private String[] setTanggalHijriyah() {
        // List of Hijriyah months https://ocmic.org.uk/12-islamic-months/
        String[] months = {
                "ٱلْمُحَرَّم", "صَفَر", "رَبِيع ٱلْأَوَّل", "رَبِيع ٱلْآخِر",
                "جُمَادَىٰ ٱلْأُولَىٰ", "جُمَادَىٰ ٱلْآخِرَة", "رَجَب", "شَعْبَان",
                "رَمَضَان", "شَوَّال", "ذُو ٱلْقَعْدَة", "ذُو ٱلْحِجَّة"};
//        IslamicCalendar islamicCalendar = new IslamicCalendar(); //API 24

        Chronology hijri = IslamicChronology.getInstance();

        LocalDate localDate = LocalDate.now();
        GregorianCalendar calendar = new GregorianCalendar();
        LocalDate todayHijri = new LocalDate(localDate.toDateTimeAtStartOfDay(), hijri);
        Log.d("hijri", String.valueOf(todayHijri));
        int numberMonth = todayHijri.getMonthOfYear() - 1;
        Log.d("hijri month", String.valueOf(numberMonth));
        String monthName = months[numberMonth];
        Log.d("hijri month", monthName);
        String dateHijri = monthName + " " + todayHijri.getYear();
        Log.d("hijri date cool", dateHijri);

        return new String[] {monthName, String.valueOf(todayHijri.getDayOfMonth()), String.valueOf(todayHijri.getYear())};
    }

    private String setTanggalMasehi() {
        String[] months = {
                "Januari", "Februari", "Maret", "April",
                "Mei", "Juni", "Juli", "Agustus",
                "September", "Oktober", "November", "Desember"};

        GregorianCalendar calendar = new GregorianCalendar();

        String i = calendar.get(Calendar.DATE) + " " + months[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR);
        Log.d("Tanggal ", i);
        return i;


    }

    public void setToolbar(@Nullable String title) {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbar.setTitle(title);
    }
}