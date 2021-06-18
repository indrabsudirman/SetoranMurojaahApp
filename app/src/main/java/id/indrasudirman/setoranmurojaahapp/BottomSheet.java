package id.indrasudirman.setoranmurojaahapp;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import id.indrasudirman.setoranmurojaahapp.databinding.LayoutBottomsheetAyatCheckboxBinding;
import it.sephiroth.android.library.checkbox3state.CheckBox3;

public class BottomSheet extends BottomSheetDialogFragment {

    private CheckBox3 semuaAyat;
    private boolean listenToUpdates = true;
    private AppCompatCheckBox compatCheckBox;
    private List<AppCompatCheckBox> checkBoxesArray = new ArrayList<>();
    private List<String> daftarAyatList = new ArrayList<>();
    LayoutBottomsheetAyatCheckboxBinding layoutBottomSheetAyatTestBinding;


    public BottomSheet() {
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        layoutBottomSheetAyatTestBinding = LayoutBottomsheetAyatCheckboxBinding.inflate(inflater, container, false);
        View view = layoutBottomSheetAyatTestBinding.getRoot();

        LinearLayoutCompat linearLayoutCheckBox = layoutBottomSheetAyatTestBinding.linearLayoutCheckBox;
        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        );

        int margin = (int) convertDpToPixel(30F, view.getContext());
        params.setMargins(margin, 0, 0, 0);



        assert getArguments() != null;
        String jumlahAyat = getArguments().getString("jumlahAyat");
        Log.e("BottomSheet.class", "Jumlah ayat dari RecyclerView :" + jumlahAyat);
        int banyakAyat = Integer.parseInt(jumlahAyat);


        for (int i = 1; i <= banyakAyat; i++) {
            compatCheckBox = new AppCompatCheckBox(view.getContext());
            compatCheckBox.setId(i);
            compatCheckBox.setText("Ayat " + i);
            compatCheckBox.setTag(i);
            checkBoxesArray.add(compatCheckBox);


            int margin1 = (int) convertDpToPixel(10F, view.getContext());
            compatCheckBox.setPadding(margin1, 0, 0, 0);


            linearLayoutCheckBox.addView(compatCheckBox, params);


        }



        semuaAyat = layoutBottomSheetAyatTestBinding.semuaAyat;
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
                    semuaAyat.setText(isChecked ? "Tidak Semua Ayat" : "Semua Ayat");
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

                        if (checkSize == checkBoxesArray.size()) {
                            semuaAyat.setCycle(R.array.sephiroth_checkbox3_cycleCheckedUncheckedOnly);
                            semuaAyat.setChecked(true, false);
                            semuaAyat.setText("Tidak Semua Ayat");
                        } else if (checkSize == 0) {
                            semuaAyat.setCycle(R.array.sephiroth_checkbox3_cycleCheckedUncheckedOnly);
                            semuaAyat.setChecked(false, false);
                            semuaAyat.setText("Semua Ayat");
                        } else {
                            semuaAyat.setCycle(R.array.sephiroth_checkbox3_cycleAll);
                            semuaAyat.setChecked(false, true);
                            semuaAyat.setText("Semua Ayat");
                        }
                        listenToUpdates = true;
                    }
                    // Get Tag Checkbox if checked
                    if (isChecked) {
                        Log.e("BottomSheet.class", "Ayat "+it.getTag().toString());
                        daftarAyatList.add(it.getTag().toString());
                        Collections.sort(daftarAyatList);
                    } else {
                        daftarAyatList.remove(it.getTag().toString());
                        Collections.sort(daftarAyatList);
                    }
                }
            });
        }

        layoutBottomSheetAyatTestBinding.tambahMurojaah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("BottomSheet.class", "Ayat dimurojaah "+daftarAyatList.toString());
            }
        });




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
