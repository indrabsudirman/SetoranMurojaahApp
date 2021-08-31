package id.indrasudirman.setoranmurojaahapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import id.indrasudirman.setoranmurojaahapp.R;
import id.indrasudirman.setoranmurojaahapp.databinding.LayoutBottomsheetEditAccountBinding;

public class BottomSheetEditAccount extends BottomSheetDialogFragment {

    private LayoutBottomsheetEditAccountBinding layoutBottomsheetEditAccountBinding;
    private SendDataInterface mSendDataInterface;

    //Default Constructor
    public BottomSheetEditAccount(SendDataInterface sendDataInterface) {
        this.mSendDataInterface = sendDataInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBottomsheetEditAccountBinding = LayoutBottomsheetEditAccountBinding.inflate(inflater, container, false);
        View view = layoutBottomsheetEditAccountBinding.getRoot();
        return view;
    }

    public interface SendDataInterface{
        void userName(String userName, String userEmail);
//        void userEmail(String userEmail);
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }
}
