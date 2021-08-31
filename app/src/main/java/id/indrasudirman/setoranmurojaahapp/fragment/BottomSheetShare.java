package id.indrasudirman.setoranmurojaahapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import id.indrasudirman.setoranmurojaahapp.databinding.LayoutBottomsheetShareBinding;

public class BottomSheetShare extends BottomSheetDialogFragment {

    private LayoutBottomsheetShareBinding layoutBottomsheetShareBinding;

    //Default Constructor
    public BottomSheetShare() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBottomsheetShareBinding = LayoutBottomsheetShareBinding.inflate(inflater, container, false);
        View view = layoutBottomsheetShareBinding.getRoot();
        return view;
    }
}
