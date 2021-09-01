package id.indrasudirman.setoranmurojaahapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;

import org.jetbrains.annotations.NotNull;

import id.indrasudirman.setoranmurojaahapp.R;
import id.indrasudirman.setoranmurojaahapp.databinding.LayoutBottomsheetDownloadMurojaahBinding;

public class BottomSheetDownloadMurojaah extends BottomSheetDialogFragment {


    private View view;
    private LayoutBottomsheetDownloadMurojaahBinding bottomsheetDownloadMurojaahBinding;

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



        return view;
    }

    private void pilihTanggalMurojaah() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Pilih Tanggal");
        MaterialDatePicker materialDatePicker = builder.build();


        materialDatePicker.show(getParentFragmentManager(), "TAG");
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }
}
