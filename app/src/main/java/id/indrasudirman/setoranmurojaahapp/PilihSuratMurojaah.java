package id.indrasudirman.setoranmurojaahapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import id.indrasudirman.setoranmurojaahapp.databinding.ActivityPilihSuratMurojaahBinding;
import id.indrasudirman.setoranmurojaahapp.databinding.DetailSuratBinding;

public class PilihSuratMurojaah extends AppCompatActivity {

    //View Binding Variable
    private ActivityPilihSuratMurojaahBinding pilihSuratMurojaahBinding;

    RecyclerView recyclerViewSurat;
    SuratAdapter suratAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<Surat> suratList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pilih_surat_murojaah);
    }
}