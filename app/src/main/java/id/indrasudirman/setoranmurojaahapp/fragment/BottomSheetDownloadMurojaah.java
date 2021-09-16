package id.indrasudirman.setoranmurojaahapp.fragment;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Pair;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.indrasudirman.setoranmurojaahapp.MainMenu;
import id.indrasudirman.setoranmurojaahapp.R;
import id.indrasudirman.setoranmurojaahapp.TampilkanMurojaahDatabase;
import id.indrasudirman.setoranmurojaahapp.databinding.LayoutBottomsheetDownloadMurojaahBinding;

public class BottomSheetDownloadMurojaah extends BottomSheetDialogFragment {


    private View view;
    private LayoutBottomsheetDownloadMurojaahBinding bottomsheetDownloadMurojaahBinding;
    private String item;
    private boolean isTrue = false;
    private String startDateToDb, endDateToDb;

    private final String[] months = {
            "Januari", "Februari", "Maret", "April",
            "Mei", "Juni", "Juli", "Agustus",
            "September", "Oktober", "November", "Desember"};

    //Default Constructor
    public BottomSheetDownloadMurojaah() {
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        bottomsheetDownloadMurojaahBinding = LayoutBottomsheetDownloadMurojaahBinding.inflate(inflater, container, false);
        view = bottomsheetDownloadMurojaahBinding.getRoot();

        bottomsheetDownloadMurojaahBinding.pilihTanggalMurojaah.setOnClickListener(view -> {
            pilihTanggalMurojaah();
        });

        //Set Spinner adapter
        setSpinnerAdapterAndListener();

        //setDefaultDateTextView
        setDefaultDateTextView();

        //Set listener if button download click
        bottomsheetDownloadMurojaahBinding.downloadMurojaah.setOnClickListener(view1 -> {
            downloadMurojaah();
        });


        return view;
    }

    // Download murojaah button
    private void downloadMurojaah() {
        if (isTrue) {
            SpannableStringBuilder sStringTitle = new SpannableStringBuilder("Konfimasi Donwload");
            sStringTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            //Set Title
            alertDialog
                    .setTitle(sStringTitle)
                    .setCancelable(false)
                    .setMessage("Anda ingin donwload murojaah atau hanya ingin tampilkan saja?")
                    .setPositiveButton("Download", ((dialogInterface, i) -> {
                        Snackbar.make(bottomsheetDownloadMurojaahBinding.coordinatorLayoutMain, "Anda pilih download",
                                Snackbar.LENGTH_SHORT).show();
                    }))
                    .setNegativeButton("Tampilkan", (((dialogInterface, i) -> {
                        if (startDateToDb == null && endDateToDb == null) {
                            Calendar startCalender = Calendar.getInstance();
                            int startMonth;
                            startMonth = startCalender.get(Calendar.MONTH);
                            startMonth = startMonth + 1;
                            String defaultDateToDb = startCalender.get(Calendar.YEAR) + "-" + startMonth + "-" + startCalender.get(Calendar.DATE);
                            Intent intent = new Intent(getContext(), TampilkanMurojaahDatabase.class);
                            intent.putExtra("start_date_select", defaultDateToDb);
                            intent.putExtra("end_date_select", defaultDateToDb);
                            startActivity(intent);
                            getActivity().overridePendingTransition(0, 0);
                        } else {
                            Intent intent = new Intent(getContext(), TampilkanMurojaahDatabase.class);
                            intent.putExtra("start_date_select", startDateToDb);
                            intent.putExtra("end_date_select", endDateToDb);
                            startActivity(intent);
                            getActivity().overridePendingTransition(0, 0);
                        }
                    })));

            alertDialog.show();

        } else {
            Snackbar.make(bottomsheetDownloadMurojaahBinding.coordinatorLayoutMain,
                    "Pilih tipe Murojaah dahulu!", Snackbar.LENGTH_SHORT).show();
        }

    }

    //setDefaultDateTextView
    private void setDefaultDateTextView() {
        Calendar startCalender = Calendar.getInstance();
        String startDate1 = startCalender.get(Calendar.DATE) + " " + months[startCalender.get(Calendar.MONTH)] + " " + startCalender.get(Calendar.YEAR);
        bottomsheetDownloadMurojaahBinding.textViewStartDateShow.setText(startDate1 + " M");
        bottomsheetDownloadMurojaahBinding.textViewEndDateShow.setText(startDate1 + " M");
    }

    private void underlinePilihTipeMurojaahTextView() {
        SpannableString[] spannableString = new SpannableString[]{
                new SpannableString("Pilih tipe Murojaah"),
                new SpannableString("Dari Tanggal"),
                new SpannableString("Sampai Tanggal")};
        spannableString[0].setSpan(new UnderlineSpan(), 0, 19, 0);
        spannableString[1].setSpan(new UnderlineSpan(), 0, 12, 0);
        spannableString[2].setSpan(new UnderlineSpan(), 0, 14, 0);
        bottomsheetDownloadMurojaahBinding.textViewPilihTipeMurojaah.setText(spannableString[0]);
        bottomsheetDownloadMurojaahBinding.textViewStartDate.setText(spannableString[1]);
        bottomsheetDownloadMurojaahBinding.textViewEndDate.setText(spannableString[2]);
    }

    private void setSpinnerAdapterAndListener() {
        List<String> typeMurojaah = new ArrayList<>();
        typeMurojaah.add(0, "Pilih tipe Murojaah");
        typeMurojaah.add("Semua");
        typeMurojaah.add("Ziyadah");
        typeMurojaah.add("Murojaah");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, typeMurojaah);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bottomsheetDownloadMurojaahBinding.spinnerTypeMurojaah.setAdapter(arrayAdapter);

        bottomsheetDownloadMurojaahBinding.spinnerTypeMurojaah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Pilih tipe Murojaah")) {
                    isTrue = false;
                } else {
                    item = adapterView.getItemAtPosition(i).toString();
                    Snackbar.make(bottomsheetDownloadMurojaahBinding.coordinatorLayoutMain, "Tipe Murojaah : " + item, Snackbar.LENGTH_SHORT).show();
                    isTrue = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void pilihTanggalMurojaah() {


        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Pilih Tanggal Download");
//        builder.set
        MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();


        materialDatePicker.show(getParentFragmentManager(), "TAG");
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            if (selection.first != null && selection.second != null) {
                //Start Date
                Calendar startCalender = Calendar.getInstance();
                startCalender.setTimeInMillis(selection.first);
                int startMonth;
                startMonth = startCalender.get(Calendar.MONTH);
                startMonth = startMonth + 1;
                startDateToDb = startCalender.get(Calendar.YEAR) + "-" + startMonth + "-" + startCalender.get(Calendar.DATE);
                Log.d("Start tanggal Masehi untuk query Download ", startDateToDb);
                String startDate = startCalender.get(Calendar.DATE) + " " + months[startCalender.get(Calendar.MONTH)] + " " + startCalender.get(Calendar.YEAR);
                bottomsheetDownloadMurojaahBinding.textViewStartDateShow.setText(startDate + " M");

                //End Date
                Calendar endCalender = Calendar.getInstance();
                endCalender.setTimeInMillis(selection.second);
                int endMonth;
                endMonth = endCalender.get(Calendar.MONTH);
                endMonth = endMonth + 1;
                endDateToDb = endCalender.get(Calendar.YEAR) + "-" + endMonth + "-" + endCalender.get(Calendar.DATE);
                Log.d("End tanggal Masehi untuk query Download ", endDateToDb);
                String endDate1 = endCalender.get(Calendar.DATE) + " " + months[endCalender.get(Calendar.MONTH)] + " " + endCalender.get(Calendar.YEAR);
                bottomsheetDownloadMurojaahBinding.textViewEndDateShow.setText(endDate1 + " M");

                Log.d("TAG", "OnPositiveButtonClick : " + startDate + " sampai " + endDate1);

            }
        });
        //While close by back button, or touch top screen
        materialDatePicker.addOnCancelListener(dialogInterface ->
                Snackbar.make(bottomsheetDownloadMurojaahBinding.coordinatorLayoutMain, "Batal memilih tanggal",
                        Snackbar.LENGTH_SHORT).show());
        //While cancel button was clicked
        materialDatePicker.addOnNegativeButtonClickListener(view ->
                Snackbar.make(bottomsheetDownloadMurojaahBinding.coordinatorLayoutMain, "Batal memilih tanggal",
                        Snackbar.LENGTH_SHORT).show());


    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }


}
