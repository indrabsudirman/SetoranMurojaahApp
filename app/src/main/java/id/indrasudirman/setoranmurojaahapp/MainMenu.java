package id.indrasudirman.setoranmurojaahapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.joda.time.Chronology;
import org.joda.time.LocalDate;
import org.joda.time.chrono.IslamicChronology;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import id.indrasudirman.setoranmurojaahapp.adapter.ListMurojaahAdapter;
import id.indrasudirman.setoranmurojaahapp.databinding.ActivityMainMenuBinding;
import id.indrasudirman.setoranmurojaahapp.databinding.LayoutToolbarProfileBinding;
import id.indrasudirman.setoranmurojaahapp.helper.SQLiteHelper;
import id.indrasudirman.setoranmurojaahapp.model.MurojaahItem;


public class MainMenu extends AppCompatActivity {

    private static final String SHARED_PREF_NAME = "sharedPrefLogin";
    private static final String KEY_EMAIL = "email";
    ActivityMainMenuBinding mainMenuBinding;
    LayoutToolbarProfileBinding layoutToolbarProfileBinding;
    private SQLiteHelper sqLiteHelper;
    private SharedPreferences sharedPreferences;
    private String userEmail;

    private RecyclerView recyclerViewListMurojaah;
    private RecyclerView.Adapter adapterListMurojaah;
    private RecyclerView.LayoutManager layoutManagerListMurojaah;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_menu);

        //View Binding change findViewById
        mainMenuBinding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        View view = mainMenuBinding.getRoot();
        setContentView(view);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        userEmail = (sharedPreferences.getString(KEY_EMAIL, "").trim());

        layoutToolbarProfileBinding = mainMenuBinding.layoutToolbarProfile;


        //Set Tanggal Masehi
        layoutToolbarProfileBinding.tanggalMasehi.setText(setTanggalMasehi() + " M");


        String[] tanggalHijri = setTanggalHijriyah();
        Log.d("Bulan ", tanggalHijri[0]);
        Log.d("Tanggal ", tanggalHijri[1]);
        Log.d("Tahun ", tanggalHijri[2]);
        //Set Tanggal Hijriyah
        layoutToolbarProfileBinding.tanggalHijriyah.setText(tanggalHijri[1]);
        layoutToolbarProfileBinding.bulanHijriyah.setText(tanggalHijri[0]);
        layoutToolbarProfileBinding.tahunHijriyah.setText(tanggalHijri[2] + " H");

        mainMenuBinding.extendedFab.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PilihSuratMurojaah.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        mainMenuBinding.logOutAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutConfirmation();
            }
        });

        ArrayList<MurojaahItem> murojaahItemArrayList = new ArrayList<>();
        murojaahItemArrayList.add(new MurojaahItem("1", "Ziyadah","Alfatihah", "Ayat 1-7"));
        murojaahItemArrayList.add(new MurojaahItem("1", "Ziyadah","Alfatihah", "Ayat 1-7"));
        murojaahItemArrayList.add(new MurojaahItem("1", "Ziyadah","Alfatihah", "Ayat 1-7"));
        murojaahItemArrayList.add(new MurojaahItem("1", "Ziyadah","Alfatihah", "Ayat 1-7"));
        murojaahItemArrayList.add(new MurojaahItem("1", "Ziyadah","Alfatihah", "Ayat 1-7"));
        murojaahItemArrayList.add(new MurojaahItem("1", "Ziyadah","Alfatihah", "Ayat 1-7"));
        murojaahItemArrayList.add(new MurojaahItem("1", "Ziyadah","Alfatihah", "Ayat 1-7"));
        murojaahItemArrayList.add(new MurojaahItem("1", "Ziyadah","Alfatihah", "Ayat 1-7"));
        murojaahItemArrayList.add(new MurojaahItem("1", "Ziyadah","Alfatihah", "Ayat 1-7"));
        murojaahItemArrayList.add(new MurojaahItem("1", "Ziyadah","Alfatihah", "Ayat 1-7"));
        murojaahItemArrayList.add(new MurojaahItem("1", "Ziyadah","Alfatihah", "Ayat 1-7"));
        murojaahItemArrayList.add(new MurojaahItem("1", "Ziyadah","Alfatihah", "Ayat 1-7"));
        murojaahItemArrayList.add(new MurojaahItem("1", "Ziyadah","Alfatihah", "Ayat 1-7"));
        murojaahItemArrayList.add(new MurojaahItem("1", "Ziyadah","Alfatihah", "Ayat 1-7"));
        murojaahItemArrayList.add(new MurojaahItem("1", "Ziyadah","Alfatihah", "Ayat 1-7"));


        recyclerViewListMurojaah = findViewById(R.id.recyclerViewListMurojaah);
        layoutManagerListMurojaah = new LinearLayoutManager(this);
        adapterListMurojaah = new ListMurojaahAdapter(murojaahItemArrayList);
        recyclerViewListMurojaah.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        recyclerViewListMurojaah.setLayoutManager(layoutManagerListMurojaah);
        recyclerViewListMurojaah.setAdapter(adapterListMurojaah);
    }

    public String[] setTanggalHijriyah() {
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

        return new String[]{monthName, String.valueOf(todayHijri.getDayOfMonth()), String.valueOf(todayHijri.getYear())};
    }

    public String setTanggalMasehi() {
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
        setSupportActionBar(findViewById(R.id.toolbar));
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbar.setTitle(title);
    }

    private void logOutConfirmation() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainMenu.this);
        alertDialogBuilder
                .setTitle("Yakin Keluar!")
                .setIcon(R.drawable.ic_questions)
                .setMessage("Anda yakin akan keluar akun ?")
                .setCancelable(false)
                .setPositiveButton("Batal keluar",
                        (dialogInterface, i) -> Toast.makeText(getApplicationContext(), "Anda batal keluar", Toast.LENGTH_SHORT).show())

                .setNegativeButton("Keluar",
                        (dialogInterface, i) -> {
                            // Delete SharedPreferences save
                            sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            startActivity(new Intent(getApplicationContext()
                                    , MainActivity.class));
                            overridePendingTransition(0, 0);
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}