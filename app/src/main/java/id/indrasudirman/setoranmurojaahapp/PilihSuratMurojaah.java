package id.indrasudirman.setoranmurojaahapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.indrasudirman.setoranmurojaahapp.adapter.SuratAdapter;
import id.indrasudirman.setoranmurojaahapp.databinding.ActivityPilihSuratMurojaahBinding;
import id.indrasudirman.setoranmurojaahapp.fragment.BottomSheet;
import id.indrasudirman.setoranmurojaahapp.model.Surat;
import id.indrasudirman.setoranmurojaahapp.tools.RecyclerItemClickListener;

public class PilihSuratMurojaah extends AppCompatActivity {

    RecyclerView recyclerViewSurat;
    SuratAdapter suratAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<Surat> suratList = new ArrayList<>();
    //View Binding Variable
    private ActivityPilihSuratMurojaahBinding pilihSuratMurojaahBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pilih_surat_murojaah);

        //View Binding change findViewById
        pilihSuratMurojaahBinding = ActivityPilihSuratMurojaahBinding.inflate(getLayoutInflater());
        View view = pilihSuratMurojaahBinding.getRoot();
        setContentView(view);

        addSuratList();
//        recyclerViewSurat = findViewById(R.id.recyclerViewSurat);
//        recyclerViewSurat.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        pilihSuratMurojaahBinding.recyclerViewSurat.setLayoutManager(layoutManager);
        suratAdapter = new SuratAdapter(this, suratList, pilihSuratMurojaahBinding.recyclerViewSurat);
        pilihSuratMurojaahBinding.recyclerViewSurat.setAdapter(suratAdapter);

        pilihAyat();
    }

    private void pilihAyat() {
        Bundle bundle = new Bundle();
        pilihSuratMurojaahBinding.recyclerViewSurat.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), pilihSuratMurojaahBinding.recyclerViewSurat, new RecyclerItemClickListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String namaSurat = suratList.get(position).getNamaSurat();
                String jumlahAyat = suratList.get(position).getJumlahAyat();
                bundle.putString("namaSurat", namaSurat);
                bundle.putString("jumlahAyat", jumlahAyat);
//                Toast.makeText(getApplicationContext(), "Jumlah ayat : " + jumlahAyat + " Jabriko", Toast.LENGTH_SHORT).show();
                BottomSheet bottomSheet = new BottomSheet();
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(), "TAG");

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
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

        surat = new Surat(34, "سَبَإ", "54");
        suratList.add(surat);

        surat = new Surat(35, "فَاطِر", "45");
        suratList.add(surat);

        surat = new Surat(36, "يسٓ", "83");
        suratList.add(surat);

        surat = new Surat(37, "الصَّافَّات", "182");
        suratList.add(surat);

        surat = new Surat(38, "صٓ", "88");
        suratList.add(surat);

        surat = new Surat(39, "الزُّمَر", "75");
        suratList.add(surat);

        surat = new Surat(40, "غَافِر", "85");
        suratList.add(surat);

        surat = new Surat(41, "فُصِّلَت", "54");
        suratList.add(surat);

        surat = new Surat(42, "الشُّورَى", "53");
        suratList.add(surat);

        surat = new Surat(43, "الزُّخۡرُف", "89");
        suratList.add(surat);

        surat = new Surat(44, "الدُّخَان", "59");
        suratList.add(surat);

        surat = new Surat(45, "الجَاثِيَة", "37");
        suratList.add(surat);

        surat = new Surat(46, "الأَحۡقَاف", "35");
        suratList.add(surat);

        surat = new Surat(47, "مُحَمَّد", "38");
        suratList.add(surat);

        surat = new Surat(48, "الفَتۡح", "29");
        suratList.add(surat);

        surat = new Surat(49, "الحُجُرَات", "18");
        suratList.add(surat);

        surat = new Surat(50, "قٓ", "45");
        suratList.add(surat);

        surat = new Surat(51, "الذَّارِيَات", "60");
        suratList.add(surat);

        surat = new Surat(52, "الطُّور", "49");
        suratList.add(surat);

        surat = new Surat(53, "النَّجۡم", "62");
        suratList.add(surat);

        surat = new Surat(54, "القَمَر", "55");
        suratList.add(surat);

        surat = new Surat(55, "الرَّحۡمَٰن", "78");
        suratList.add(surat);

        surat = new Surat(56, "الوَاقِعَة", "96");
        suratList.add(surat);

        surat = new Surat(57, "الحَدِيد", "29");
        suratList.add(surat);

        surat = new Surat(58, "المُجَادلَة", "22");
        suratList.add(surat);

        surat = new Surat(59, "الحَشۡر", "24");
        suratList.add(surat);

        surat = new Surat(60, "المُمۡتَحنَة", "13");
        suratList.add(surat);

        surat = new Surat(61, "الصَّفّ", "14");
        suratList.add(surat);

        surat = new Surat(62, "الجُمُعَة", "11");
        suratList.add(surat);

        surat = new Surat(63, "المُنَافِقُون", "11");
        suratList.add(surat);

        surat = new Surat(64, "التَّغَابُن", "18");
        suratList.add(surat);

        surat = new Surat(65, "الطَّلَاق", "12");
        suratList.add(surat);

        surat = new Surat(66, "التَّحۡرِيم", "12");
        suratList.add(surat);

        surat = new Surat(67, "المُلۡك", "30");
        suratList.add(surat);

        surat = new Surat(68, "القَلَم", "52");
        suratList.add(surat);

        surat = new Surat(69, "الحَاقَّة", "52");
        suratList.add(surat);

        surat = new Surat(70, "المَعَارِج", "44");
        suratList.add(surat);

        surat = new Surat(71, "نُوح", "28");
        suratList.add(surat);

        surat = new Surat(72, "الجِنّ", "28");
        suratList.add(surat);

        surat = new Surat(73, "المُزَّمِّلِ", "20");
        suratList.add(surat);

        surat = new Surat(74, "المُدَّثِّر", "56");
        suratList.add(surat);

        surat = new Surat(75, "القِيَامَة", "40");
        suratList.add(surat);

        surat = new Surat(76, "الإِنسَان", "31");
        suratList.add(surat);

        surat = new Surat(77, "المُرۡسَلَات", "50");
        suratList.add(surat);

        surat = new Surat(78, "النَّبَإ", "40");
        suratList.add(surat);

        surat = new Surat(79, "النَّازِعَات", "46");
        suratList.add(surat);

        surat = new Surat(80, "عَبَس", "42");
        suratList.add(surat);

        surat = new Surat(81, "التَّكۡوِير", "29");
        suratList.add(surat);

        surat = new Surat(82, "الانفِطَار", "19");
        suratList.add(surat);

        surat = new Surat(83, "المُطَفِّفِين", "36");
        suratList.add(surat);

        surat = new Surat(84, "الانشِقَاق", "25");
        suratList.add(surat);

        surat = new Surat(85, "البُرُوج", "22");
        suratList.add(surat);

        surat = new Surat(86, "الطَّارِق", "17");
        suratList.add(surat);

        surat = new Surat(87, "الأَعۡلَى", "19");
        suratList.add(surat);

        surat = new Surat(88, "الغَاشِيَة", "26");
        suratList.add(surat);

        surat = new Surat(89, "الفَجۡر", "30");
        suratList.add(surat);

        surat = new Surat(90, "البَلَد", "20");
        suratList.add(surat);

        surat = new Surat(91, "الشَّمۡس", "15");
        suratList.add(surat);

        surat = new Surat(92, "اللَّيۡل", "21");
        suratList.add(surat);

        surat = new Surat(93, "الضُّحَى", "11");
        suratList.add(surat);

        surat = new Surat(94, "الشَّرۡح", "8");
        suratList.add(surat);

        surat = new Surat(95, "التِّين", "8");
        suratList.add(surat);

        surat = new Surat(96, "العَلَقِ", "19");
        suratList.add(surat);

        surat = new Surat(97, "القَدۡر", "5");
        suratList.add(surat);

        surat = new Surat(98, "البَيِّنَة", "8");
        suratList.add(surat);

        surat = new Surat(99, "الزَّلۡزَلَة", "8");
        suratList.add(surat);

        surat = new Surat(100, "العَادِيَات", "11");
        suratList.add(surat);

        surat = new Surat(101, "القَارِعَة", "11");
        suratList.add(surat);

        surat = new Surat(102, "التَّكَاثُر", "8");
        suratList.add(surat);

        surat = new Surat(103, "العَصۡر", "3");
        suratList.add(surat);

        surat = new Surat(104, "الهُمَزَة", "9");
        suratList.add(surat);

        surat = new Surat(105, "الفِيل", "5");
        suratList.add(surat);

        surat = new Surat(106, "قُرَيۡش", "4");
        suratList.add(surat);

        surat = new Surat(107, "المَاعُون", "7");
        suratList.add(surat);

        surat = new Surat(108, "الكَوۡثَر", "3");
        suratList.add(surat);

        surat = new Surat(109, "الكَافِرُون", "6");
        suratList.add(surat);

        surat = new Surat(110, "النَّصۡر", "3");
        suratList.add(surat);

        surat = new Surat(111, "المَسَد", "5");
        suratList.add(surat);

        surat = new Surat(112, "الإِخۡلَاص", "4");
        suratList.add(surat);

        surat = new Surat(113, "الفَلَق", "5");
        suratList.add(surat);

        surat = new Surat(114, "النَّاس", "6");
        suratList.add(surat);
    }
}