package id.indrasudirman.setoranmurojaahapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tampilkan_murojaah_database);

        //ViewBinding Activity Tampil Murojaah
        activityTampilkanMurojaahDatabaseBinding = ActivityTampilkanMurojaahDatabaseBinding.inflate(getLayoutInflater());
        view = activityTampilkanMurojaahDatabaseBinding.getRoot();
        setContentView(view);

        sqLiteHelper = new SQLiteHelper(this);

        createTampilMurojaahArrayList();

        buildRecyclerViewTampilMurojaah();

    }

    private void buildRecyclerViewTampilMurojaah() {
        tampilMurojaahLayoutManager = new LinearLayoutManager(this);
        tampilMurojaahAdapter = new TampilMurojaahAdapter(tampilMurojaahArrayList);
        activityTampilkanMurojaahDatabaseBinding.recyclerViewMurojaahmu.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        activityTampilkanMurojaahDatabaseBinding.recyclerViewMurojaahmu.setLayoutManager(tampilMurojaahLayoutManager);
        activityTampilkanMurojaahDatabaseBinding.recyclerViewMurojaahmu.setAdapter(tampilMurojaahAdapter);
    }

    private void createTampilMurojaahArrayList() {
        if (tampilMurojaahArrayList == null) {
            tampilMurojaahArrayList = new ArrayList<>();
        }

//        tampilMurojaahArrayList = sqLiteHelper.getTampilMurojaah();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}