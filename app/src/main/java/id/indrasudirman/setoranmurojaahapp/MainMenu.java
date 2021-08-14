package id.indrasudirman.setoranmurojaahapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.joda.time.Chronology;
import org.joda.time.LocalDate;
import org.joda.time.chrono.IslamicChronology;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import id.indrasudirman.setoranmurojaahapp.adapter.ListMurojaahAdapter;
import id.indrasudirman.setoranmurojaahapp.databinding.ActivityMainMenuBinding;
import id.indrasudirman.setoranmurojaahapp.databinding.LayoutToolbarProfileBinding;
import id.indrasudirman.setoranmurojaahapp.databinding.ListMurojaahBinding;
import id.indrasudirman.setoranmurojaahapp.helper.SQLiteHelper;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class MainMenu extends AppCompatActivity {

    private static final String SHARED_PREF_NAME = "sharedPrefLogin";
    private static final String SHARED_PREF_DATE = "sharedPrefDate";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DATE = "date";
    private ActivityMainMenuBinding mainMenuBinding;
    private LayoutToolbarProfileBinding layoutToolbarProfileBinding;
    private ListMurojaahBinding listMurojaahBinding;
    private SQLiteHelper sqLiteHelper;
    private SharedPreferences sharedPreferences, sharedPreferencesDate;
    private String userEmail;
    private String dateString;
    private RecyclerView.Adapter adapterListMurojaah;
    private RecyclerView.LayoutManager layoutManagerListMurojaah;
    private ArrayList<MurojaahItem> murojaahItemArrayList;

    public ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
            String murojaahSuratHarianDelete = murojaahItemArrayList.get(viewHolder.getAdapterPosition()).getNamaSurat();
            String typeMurojaahHarianDelete = murojaahItemArrayList.get(viewHolder.getAdapterPosition()).getTypeMurojaah();
            final int position = viewHolder.getAdapterPosition();
            //backup
            final MurojaahItem deleteMurojaah = murojaahItemArrayList.get(viewHolder.getAdapterPosition());
            final int deleteIndexMurojaah = viewHolder.getAdapterPosition();
            MurojaahItem murojaahItem = new MurojaahItem();

            if (direction == ItemTouchHelper.LEFT | direction == ItemTouchHelper.RIGHT) {
                //Delete murojaah list
                murojaahItemArrayList.remove(position);
                adapterListMurojaah.notifyItemRemoved(position);
                //Update list number RecyclerView while item was deleted
                adapterListMurojaah.notifyItemRangeChanged(position, murojaahItemArrayList.size());
                adapterListMurojaah.notifyDataSetChanged();

                //Delete murojaah harian database by row today
                sqLiteHelper.deleteMurojaahHarianDB(setTanggalMasehi() + " M");

                //Insert new murojaah harian database by row today from recyclerview
                if (!murojaahItemArrayList.isEmpty()) {
                    String userID = sqLiteHelper.getUserId(userEmail);
                    String[] tanggalHijriArray = setTanggalHijriyah();
                    String tglHijri = "H " + tanggalHijriArray[0] + " ," + tanggalHijriArray[1] + " ," + tanggalHijriArray[2];
                    sqLiteHelper.addMurojaahHarianDB(murojaahItemArrayList, userID, setTanggalMasehi() + " M", tglHijri);
                    ArraylistToJson(murojaahItemArrayList);
                }

                Snackbar.make(listMurojaahBinding.recyclerViewListMurojaah, typeMurojaahHarianDelete + " "+ murojaahSuratHarianDelete + " dihapus", Snackbar.LENGTH_LONG)
                        .setAction("Batal", v -> {
                            //Restore murojaah list
                            murojaahItemArrayList.add(deleteIndexMurojaah, deleteMurojaah);
                            adapterListMurojaah.notifyItemInserted(deleteIndexMurojaah);
                            //Update list number RecyclerView while item was restore
                            adapterListMurojaah.notifyItemRangeChanged(deleteIndexMurojaah, murojaahItemArrayList.size());
                            adapterListMurojaah.notifyDataSetChanged();
                            //Delete murojaah harian database by row today
                            sqLiteHelper.deleteMurojaahHarianDB(setTanggalMasehi() + " M");

                            //Insert new murojaah harian database by row today from recyclerview
                            if (!murojaahItemArrayList.isEmpty()) {
                                String userID = sqLiteHelper.getUserId(userEmail);
                                String[] tanggalHijriArray = setTanggalHijriyah();
                                String tglHijri = "H " + tanggalHijriArray[0] + " ," + tanggalHijriArray[1] + " ," + tanggalHijriArray[2];
                                sqLiteHelper.addMurojaahHarianDB(murojaahItemArrayList, userID, setTanggalMasehi() + " M", tglHijri);
                                ArraylistToJson(murojaahItemArrayList);
                            }
                        })
                        .show();
            }

        }

        @Override
        public void onChildDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftLabel("Hapus")
                    .addSwipeRightLabel("Hapus")
                    .setSwipeRightLabelColor(ContextCompat.getColor(MainMenu.this, R.color.white))
                    .setSwipeLeftLabelColor(ContextCompat.getColor(MainMenu.this, R.color.white))
                    .addBackgroundColor(ContextCompat.getColor(MainMenu.this, R.color.colorRed))
                    .addActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void printArrayList(ArrayList<MurojaahItem> murojaahItemArrayList) {
        for (MurojaahItem murojaahItem : murojaahItemArrayList){
            System.out.println(murojaahItem);
        }
    }

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
        sharedPreferencesDate = getSharedPreferences(SHARED_PREF_DATE, MODE_PRIVATE);
        dateString = (sharedPreferencesDate.getString(KEY_DATE, "").trim());

        sqLiteHelper = new SQLiteHelper(this);

        layoutToolbarProfileBinding = mainMenuBinding.layoutToolbarProfile;
        listMurojaahBinding = mainMenuBinding.listMurojaah;

        //Set Tanggal Masehi
        layoutToolbarProfileBinding.tanggalMasehi.setText(setTanggalMasehi() + " M");


        String[] tanggalHijri = setTanggalHijriyah();
//        Log.d("Bulan ", tanggalHijri[0]);
//        Log.d("Tanggal ", tanggalHijri[1]);
//        Log.d("Tahun ", tanggalHijri[2]);
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

        Intent intent = getIntent();
        if (intent.hasExtra("murojaah_list")) {
            if (murojaahItemArrayList == null) {
                murojaahItemArrayList = new ArrayList<>();
            }
            murojaahItemArrayList.clear();
            murojaahItemArrayList = sqLiteHelper.getMurojaahHarianDB(sqLiteHelper.getUserId(userEmail), setTanggalMasehi() + " M");

        }

        createMurojaahArrayList();

        buildRecyclerViewMurojaah();

        GregorianCalendar calendar = new GregorianCalendar();
        LocalDate date = LocalDate.fromDateFields(calendar.getTime());
        String localDateString = date.toString();
        SharedPreferences.Editor editor = sharedPreferencesDate.edit();
        editor.putString(KEY_DATE, localDateString);
        editor.apply();
        Log.d("Date  ", localDateString);

    }

    //method add Murojaah Item in a list
    public void addMurojaahItem(int position, MurojaahItem murojaahItemArrayList) {
//        murojaahItemArrayList.add(position, murojaahItemArrayList.get(position));
    }

    //method remove Murojaah Item in a list
    public void removeMurojaahItem(int position) {
    }

    public void createMurojaahArrayList() {
        if (murojaahItemArrayList == null) {
            murojaahItemArrayList = new ArrayList<>();
        }

        murojaahItemArrayList = sqLiteHelper.getMurojaahHarianDB(sqLiteHelper.getUserId(userEmail), setTanggalMasehi() + " M");

        if (!dateString.isEmpty()) {
            LocalDate localDate = LocalDate.now();
            LocalDate localDateSharedPref = LocalDate.parse(dateString);
            boolean newDate = localDate.isEqual(localDateSharedPref);
            if (newDate) {
                System.out.println("yes, it's same day");
            } else {
                System.out.println("no, it's not same day");
                murojaahItemArrayList.clear();
            }
        }


        boolean listEmpty = murojaahItemArrayList.isEmpty();

        if (listEmpty) {
            listMurojaahBinding.recyclerViewListMurojaah.setVisibility(View.GONE);
            listMurojaahBinding.textViewRecylerEmpty.setVisibility(View.VISIBLE);
        } else {
            listMurojaahBinding.recyclerViewListMurojaah.setVisibility(View.VISIBLE);
            listMurojaahBinding.textViewRecylerEmpty.setVisibility(View.GONE);
        }


        //Implement swipe left to remove murojaah array list.
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(listMurojaahBinding.recyclerViewListMurojaah);


    }

    public void buildRecyclerViewMurojaah() {
        layoutManagerListMurojaah = new LinearLayoutManager(this);
        adapterListMurojaah = new ListMurojaahAdapter(murojaahItemArrayList);
        listMurojaahBinding.recyclerViewListMurojaah.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        listMurojaahBinding.recyclerViewListMurojaah.setLayoutManager(layoutManagerListMurojaah);
        listMurojaahBinding.recyclerViewListMurojaah.setAdapter(adapterListMurojaah);
    }

    public String[] setTanggalHijriyah() {
        // List of Hijriyah months https://ocmic.org.uk/12-islamic-months/
        String[] months = {
                "ٱلْمُحَرَّم", "صَفَر", "رَبِيع ٱلْأَوَّل", "رَبِيع ٱلْآخِر",
                "جُمَادَىٰ ٱلْأُولَىٰ", "جُمَادَىٰ ٱلْآخِرَة", "رَجَب", "شَعْبَان",
                "رَمَضَان", "شَوَّال", "ذُو ٱلْقَعْدَة", "ذُو ٱلْحِجَّة"};

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
                .setPositiveButton("Keluar",
                        (dialogInterface, i) -> {
                            // Delete SharedPreferences save
                            sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            startActivity(new Intent(getApplicationContext()
                                    , MainActivity.class));
                            overridePendingTransition(0, 0);
                        })

                .setNegativeButton("Batal keluar",
                        (dialogInterface, i) -> Toast.makeText(getApplicationContext(), "Anda batal keluar", Toast.LENGTH_SHORT).show());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void ArraylistToJson (ArrayList<MurojaahItem> murojaahItemArrayList) {
        Gson gson = new Gson();
        String json = gson.toJson(murojaahItemArrayList);
        System.out.println("jsonnya : " + json);
    }




}