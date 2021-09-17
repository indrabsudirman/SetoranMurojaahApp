package id.indrasudirman.setoranmurojaahapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import id.indrasudirman.setoranmurojaahapp.adapter.TampilMurojaahAdapter;
import id.indrasudirman.setoranmurojaahapp.databinding.ActivityTampilkanMurojaahDatabaseBinding;
import id.indrasudirman.setoranmurojaahapp.helper.SQLiteHelper;
import id.indrasudirman.setoranmurojaahapp.model.TampilMurojaah;

public class TampilkanMurojaahDatabase extends AppCompatActivity {

    private RecyclerView.Adapter tampilMurojaahAdapter;
    private RecyclerView.LayoutManager tampilMurojaahLayoutManager;
    private ArrayList<TampilMurojaah> tampilMurojaahArrayList;
    private ActivityTampilkanMurojaahDatabaseBinding activityTampilkanMurojaahDatabaseBinding;
    private View view;
    private SQLiteHelper sqLiteHelper;
    private String userEmail;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "sharedPrefLogin";
    private static final String KEY_EMAIL = "email";
    private String startDate, endDate, typeMurojaah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tampilkan_murojaah_database);

        //ViewBinding Activity Tampil Murojaah
        activityTampilkanMurojaahDatabaseBinding = ActivityTampilkanMurojaahDatabaseBinding.inflate(getLayoutInflater());
        view = activityTampilkanMurojaahDatabaseBinding.getRoot();
        setContentView(view);

        Intent i = getIntent();
        startDate = i.getStringExtra("start_date_select");
        endDate = i.getStringExtra("end_date_select");
        typeMurojaah = i.getStringExtra("tipe_murojaah");
        Log.d("Date tampil murojaah  ", startDate + " " + endDate + " " + typeMurojaah);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        userEmail = (sharedPreferences.getString(KEY_EMAIL, "").trim());

        sqLiteHelper = new SQLiteHelper(this);

        createTampilMurojaahArrayList();

        buildRecyclerViewTampilMurojaah();

    }

    private void buildRecyclerViewTampilMurojaah() {
        tampilMurojaahLayoutManager = new LinearLayoutManager(this);
        tampilMurojaahAdapter = new TampilMurojaahAdapter(tampilMurojaahArrayList);
        activityTampilkanMurojaahDatabaseBinding.recyclerViewMurojaahmu.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
//        activityTampilkanMurojaahDatabaseBinding.recyclerViewMurojaahmu.addItemDecoration(new TampilMurojaahAdapter.MyItemDecoration)

        activityTampilkanMurojaahDatabaseBinding.recyclerViewMurojaahmu.setLayoutManager(tampilMurojaahLayoutManager);
        activityTampilkanMurojaahDatabaseBinding.recyclerViewMurojaahmu.setAdapter(tampilMurojaahAdapter);
    }

    private void createTampilMurojaahArrayList() {
        Intent intent = getIntent();

        if (intent != null) {

            if (tampilMurojaahArrayList == null) {
                tampilMurojaahArrayList = new ArrayList<>();
            }
            if (typeMurojaah.equals("Semua")) {
                tampilMurojaahArrayList.clear();
                tampilMurojaahArrayList = sqLiteHelper.getTampilMurojaahDBAll(sqLiteHelper.getUserId(userEmail), startDate, endDate);
                if (tampilMurojaahArrayList.isEmpty()) {
                    activityTampilkanMurojaahDatabaseBinding.recyclerViewMurojaahmu.setVisibility(View.GONE);
                    activityTampilkanMurojaahDatabaseBinding.textViewRecylerEmpty.setVisibility(View.VISIBLE);
                }
            } else {
                tampilMurojaahArrayList.clear();
                tampilMurojaahArrayList = sqLiteHelper.getTampilMurojaahDBOnlyTypeSelected(sqLiteHelper.getUserId(userEmail), startDate, endDate, typeMurojaah);
                if (tampilMurojaahArrayList.isEmpty()) {
                    activityTampilkanMurojaahDatabaseBinding.recyclerViewMurojaahmu.setVisibility(View.GONE);
                    activityTampilkanMurojaahDatabaseBinding.textViewRecylerEmpty.setVisibility(View.VISIBLE);
                }
            }



        }
        boolean listEmpty = tampilMurojaahArrayList.isEmpty();

        if (listEmpty) {
            activityTampilkanMurojaahDatabaseBinding.recyclerViewMurojaahmu.setVisibility(View.GONE);
            activityTampilkanMurojaahDatabaseBinding.textViewRecylerEmpty.setVisibility(View.VISIBLE);
        } else {
            activityTampilkanMurojaahDatabaseBinding.recyclerViewMurojaahmu.setVisibility(View.VISIBLE);
            activityTampilkanMurojaahDatabaseBinding.textViewRecylerEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}