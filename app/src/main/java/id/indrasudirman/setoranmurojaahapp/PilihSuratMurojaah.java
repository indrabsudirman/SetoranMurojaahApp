package id.indrasudirman.setoranmurojaahapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import id.indrasudirman.setoranmurojaahapp.databinding.ActivityMainBinding;
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
        setContentView(R.layout.activity_pilih_surat_murojaah);

        //View Binding change findViewById
        pilihSuratMurojaahBinding = ActivityPilihSuratMurojaahBinding.inflate(getLayoutInflater());
        View view = pilihSuratMurojaahBinding.getRoot();
        setContentView(view);

        addSuratList();
        recyclerViewSurat = findViewById(R.id.recyclerViewSurat);
//        recyclerViewSurat.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewSurat.setLayoutManager(layoutManager);
        suratAdapter = new SuratAdapter(this, suratList, pilihSuratMurojaahBinding.recyclerViewSurat);
        recyclerViewSurat.setAdapter(suratAdapter);
    }

    private void addSuratList() {
        // from http://api.alquran.cloud/v1/surah

        Surat surat = new Surat(1, "ٱلْفَاتِحَة", "7");
        suratList.add(surat);

        surat = new Surat(2, "البَقَرَة", "286");
        suratList.add(surat);

        surat = new Surat(3, "آلِ عِمۡرَان", "200");
        suratList.add(surat);

        surat = new Surat(4, "النِّسَاء", "176");
        suratList.add(surat);

        surat = new Surat(5, "المَائـِدَة", "120");
        suratList.add(surat);

        surat = new Surat(6, "الأَنۡعَام", "165");
        suratList.add(surat);

        surat = new Surat(7, "الأَعۡرَاف", "206");
        suratList.add(surat);

        surat = new Surat(8, "الأَنفَال", "75");
        suratList.add(surat);

        surat = new Surat(9, "التَّوۡبَة", "129");
        suratList.add(surat);

        surat = new Surat(10, "يُونُس", "109");
        suratList.add(surat);

        surat = new Surat(11, "هُود", "123");
        suratList.add(surat);

        surat = new Surat(12, "يُوسُف", "111");
        suratList.add(surat);

        surat = new Surat(13, "الرَّعۡد", "43");
        suratList.add(surat);

        surat = new Surat(14, "إِبۡرَاهِيم", "52");
        suratList.add(surat);

        surat = new Surat(15, "الحِجۡر", "99");
        suratList.add(surat);

        surat = new Surat(16, "النَّحۡل", "128");
        suratList.add(surat);

        surat = new Surat(17, "الإِسۡرَاء", "111");
        suratList.add(surat);

        surat = new Surat(18, "الكَهۡف", "110");
        suratList.add(surat);

        surat = new Surat(19, "مَرۡيَم", "98");
        suratList.add(surat);

        surat = new Surat(20, "طه", "135");
        suratList.add(surat);

        surat = new Surat(21, "الأَنبِيَاء", "112");
        suratList.add(surat);

        surat = new Surat(22, "الحَج", "78");
        suratList.add(surat);

        surat = new Surat(23, "المُؤۡمِنُون", "118");
        suratList.add(surat);

        surat = new Surat(24, "النُّور", "64");
        suratList.add(surat);

        surat = new Surat(25, "الفُرۡقَان", "77");
        suratList.add(surat);

        surat = new Surat(26, "الشُّعَرَاء", "227");
        suratList.add(surat);

        surat = new Surat(27, "النَّمۡل", "93");
        suratList.add(surat);

        surat = new Surat(28, "القَصَص", "88");
        suratList.add(surat);

        surat = new Surat(29, "العَنكَبُوت", "69");
        suratList.add(surat);

        surat = new Surat(30, "الرُّوم", "60");
        suratList.add(surat);

        surat = new Surat(31, "لُقۡمَان", "34");
        suratList.add(surat);

        surat = new Surat(32, "السَّجۡدَة", "30");
        suratList.add(surat);

        surat = new Surat(33, "الأَحۡزَاب", "73");
        suratList.add(surat);
    }
}