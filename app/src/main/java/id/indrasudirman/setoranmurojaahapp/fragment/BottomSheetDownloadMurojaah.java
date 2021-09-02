package id.indrasudirman.setoranmurojaahapp.fragment;


import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.indrasudirman.setoranmurojaahapp.R;
import id.indrasudirman.setoranmurojaahapp.databinding.LayoutBottomsheetDownloadMurojaahBinding;

public class BottomSheetDownloadMurojaah extends BottomSheetDialogFragment {


    private View view;
    private LayoutBottomsheetDownloadMurojaahBinding bottomsheetDownloadMurojaahBinding;
    private String item;
    private boolean isTrue = true;

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
            Snackbar.make(bottomsheetDownloadMurojaahBinding.coordinatorLayoutMain,
                    "Download click", Snackbar.LENGTH_SHORT).show();
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
                String startDate = startCalender.get(Calendar.DATE) + " " + months[startCalender.get(Calendar.MONTH)] + " " + startCalender.get(Calendar.YEAR);
                bottomsheetDownloadMurojaahBinding.textViewStartDateShow.setText(startDate + " M");

                //End Date
                Calendar endCalender = Calendar.getInstance();
                endCalender.setTimeInMillis(selection.second);
                String endDate1 = endCalender.get(Calendar.DATE) + " " + months[endCalender.get(Calendar.MONTH)] + " " + endCalender.get(Calendar.YEAR);
                bottomsheetDownloadMurojaahBinding.textViewEndDateShow.setText(endDate1 + " M");

                Log.d("TAG", "OnPositiveButtonClick : " + startDate + " " + endDate1);

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
