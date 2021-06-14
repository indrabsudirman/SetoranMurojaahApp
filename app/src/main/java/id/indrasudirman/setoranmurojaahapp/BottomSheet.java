package id.indrasudirman.setoranmurojaahapp;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import it.sephiroth.android.library.checkbox3state.CheckBox3;

public class BottomSheet extends BottomSheetDialogFragment {

    private CheckBox3 semuaAyat;
    private boolean listenToUpdates = true;
    private AppCompatCheckBox[] checkBoxesArray;


    public BottomSheet() {
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_bottom_sheet_ayat, container, false);

//        LinearLayoutCompat linearLayoutCheckBox = view.findViewById(R.id.linearLayoutCheckBox);
//        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(
//                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
//                LinearLayoutCompat.LayoutParams.WRAP_CONTENT
//        );
//
//        int margin = (int) convertDpToPixel(30F, view.getContext());
//        params.setMargins(margin, 0,0,0);
//
//
//            AppCompatCheckBox compatCheckBox;
//            int banyakAyat = Integer.parseInt("9");

//            for (int i = 1; i <= banyakAyat; i++) {
//                compatCheckBox = new AppCompatCheckBox(view.getContext());
//                compatCheckBox.setId(i);
//                compatCheckBox.setText("Ayat " + i);
//                int margin1 = (int) convertDpToPixel(10F, view.getContext());
//                compatCheckBox.setPadding(margin1,0,0,0);
//                linearLayoutCheckBox.addView(compatCheckBox, params);
//            }

        checkBoxesArray = new AppCompatCheckBox[] {view.findViewById(R.id.checkBox2),
                view.findViewById(R.id.checkBox3),
                view.findViewById(R.id.checkBox4),
                view.findViewById(R.id.checkBox5),
                view.findViewById(R.id.checkBox6),
                view.findViewById(R.id.checkBox7),
                view.findViewById(R.id.checkBox8),
                view.findViewById(R.id.checkBox9),
                view.findViewById(R.id.checkBox10),
                view.findViewById(R.id.checkBox11),
                view.findViewById(R.id.checkBox11)};
        semuaAyat = view.findViewById(R.id.semuaAyat);
        semuaAyat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listenToUpdates) {
                    listenToUpdates = false;
                    if (!isChecked) {
                        for (CheckBox it : checkBoxesArray) {
                            it.setChecked(false);
                        }
                    } else if (isChecked) {
                        for (CheckBox it : checkBoxesArray) {
                            it.setChecked(true);
                        }
                    }
                    semuaAyat.setText(isChecked ? "Tidak Semua Ayat": "Semua Ayat");
                    semuaAyat.setCycle(R.array.sephiroth_checkbox3_cycleCheckedUncheckedOnly);
                    listenToUpdates = true;
                }
            }
        });

        for (CheckBox it : checkBoxesArray) {
            it.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (listenToUpdates) {
                        listenToUpdates = false;
                        int checkSize = 0;
                        for (CheckBox checkBox : checkBoxesArray) {
                            if (checkBox.isChecked()) {
                                checkSize++;
                            }
                        }

                        if (checkSize == checkBoxesArray.length) {
                            semuaAyat.setCycle(R.array.sephiroth_checkbox3_cycleCheckedUncheckedOnly);
                            semuaAyat.setChecked(true, false);
                            semuaAyat.setText("Tidak Semua Ayat");
                        } else if (checkSize == 0) {
                            semuaAyat.setCycle(R.array.sephiroth_checkbox3_cycleCheckedUncheckedOnly);
                            semuaAyat.setChecked(false, false);
                            semuaAyat.setText("Semua Ayat");
                        } else {
                            semuaAyat.setCycle(R.array.sephiroth_checkbox3_cycleAll);
                            semuaAyat.setChecked(false,true);
                            semuaAyat.setText("Semua Ayat");
                        }
                        listenToUpdates = true;
                    }
                }
            });
        }



        return view;
    }

    private float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);

    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }
}
