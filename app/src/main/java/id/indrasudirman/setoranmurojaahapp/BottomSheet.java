package id.indrasudirman.setoranmurojaahapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import id.indrasudirman.setoranmurojaahapp.databinding.LayoutBottomsheetAyatCheckboxBinding;
import it.sephiroth.android.library.checkbox3state.CheckBox3;

public class BottomSheet extends BottomSheetDialogFragment {

    LayoutBottomsheetAyatCheckboxBinding layoutBottomSheetAyatTestBinding;
    private CheckBox3 semuaAyat;
    private boolean listenToUpdates = true;
    private AppCompatCheckBox compatCheckBox;
    private final List<AppCompatCheckBox> checkBoxesArray = new ArrayList<>();
    private final List<Integer> daftarAyatList = new ArrayList<>();
    private String namaSurat;
    private String jumlahAyat;


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


        //Get value from activity before
        assert getArguments() != null;
        namaSurat = getArguments().getString("namaSurat");
        jumlahAyat = getArguments().getString("jumlahAyat");
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
        semuaAyat.setOnCheckedChangeListener((buttonView, isChecked) -> {
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
        });

        for (CheckBox it : checkBoxesArray) {
            it.setOnCheckedChangeListener((buttonView, isChecked) -> {

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
                    Log.e("BottomSheet.class", "Ayat " + it.getId());
                    daftarAyatList.add(it.getId());
                } else {
                    daftarAyatList.remove((Integer) it.getId());
                }
                Collections.sort(daftarAyatList);
            });
        }

        // Button click listener, when click simpan murojaah
        layoutBottomSheetAyatTestBinding.tambahMurojaah.setOnClickListener(v -> {
            checkAyatCheckBox();
        });


        return view;
    }

    private float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);

    }

    private void checkAyatCheckBox() {
        String ayatMurojaah = null;
        if (daftarAyatList.size() == 1) {
            ayatMurojaah = String.valueOf(daftarAyatList.get(0));
            SpannableStringBuilder sStringTitle = new SpannableStringBuilder("Simpan Murojaah");
            sStringTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            //Set Title
            alertDialog.setTitle(sStringTitle);
            //Set Cancelable false
            alertDialog.setCancelable(false);
            String[] strings = new String[]{"Yakin ingin menambahkan murojaah Surat ", " dengan ayat ", "?"};
            alertDialog.setMessage(strings[0] + namaSurat + strings[1] + ayatMurojaah + strings[2]);
            alertDialog.setPositiveButton("Ya", (dialog, which) -> {
                new Handler(Looper.getMainLooper()).postDelayed(BottomSheet.this::dismiss, 500);
                Toast.makeText(getContext(), "Murojaah tersimpan", Toast.LENGTH_SHORT).show();

            });
            alertDialog.setNegativeButton("Tidak", (dialog, which) -> {
                new Handler(Looper.getMainLooper()).postDelayed(BottomSheet.this::dismiss, 500);
                Toast.makeText(getContext(), "Batal simpan", Toast.LENGTH_SHORT).show();
            });

            alertDialog.show();
        } if (daftarAyatList.size() > 1) {
            Log.e("BottomSheet.class", "Anda pilih ayat lebih dari 1");
            if (getMissingInt((ArrayList<Integer>) daftarAyatList)) {
                Log.e("BottomSheet.class", "Anda pilih ayat berurutan");
                ayatMurojaah = daftarAyatList.get(0) + " - " + daftarAyatList.get(daftarAyatList.size() - 1);

                SpannableStringBuilder sStringTitle = new SpannableStringBuilder("Simpan Murojaah");
                sStringTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                //Set Title
                alertDialog.setTitle(sStringTitle);
                //Set Cancelable false
                alertDialog.setCancelable(false);
                String[] strings = new String[]{"Yakin ingin menambahkan murojaah Surat ", " dengan ayat ", "?"};
                alertDialog.setMessage(strings[0] + namaSurat + strings[1] + ayatMurojaah + strings[2]);
                alertDialog.setPositiveButton("Ya", (dialog, which) -> {
                    new Handler(Looper.getMainLooper()).postDelayed(BottomSheet.this::dismiss, 500);
                    Toast.makeText(getContext(), "Murojaah tersimpan", Toast.LENGTH_SHORT).show();

                });
                alertDialog.setNegativeButton("Tidak", (dialog, which) -> {
                    new Handler(Looper.getMainLooper()).postDelayed(BottomSheet.this::dismiss, 500);
                    Toast.makeText(getContext(), "Batal simpan", Toast.LENGTH_SHORT).show();
                });

                alertDialog.show();
            } else {
                SpannableStringBuilder sStringTitle = new SpannableStringBuilder("Error");
                sStringTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Log.e("BottomSheet.class", "Anda pilih ayat tidak berurutan");
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                //Set Title
                alertDialog.setTitle(sStringTitle);
                //Set Cancelable false
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Anda tidak bisa memilih ayat acak/harus berurutan!");
                alertDialog.setPositiveButton("Oke", (dialog, which) -> Toast.makeText(getContext(), "Batal simpan", Toast.LENGTH_SHORT).show());
                alertDialog.show();
            }
        }
        if (daftarAyatList.size() == 0) {
            Toast.makeText(getContext(), "Ayat murojaah belum dipilih", Toast.LENGTH_SHORT).show();
        }
        Log.e("BottomSheet.class", "Surat " + namaSurat + " Ayat " + ayatMurojaah);
    }

    private boolean getMissingInt(ArrayList<Integer> a) {
        List<Integer> arr = new ArrayList<>();

        int j = a.get(0);
        for (int i = 0; i < a.size(); i++) {
            if (j != a.get(i)) {
                arr.add(j);
                i--;
            }
            j++;
        }
        if (arr.isEmpty()) {
//            System.out.println("No missing numbers");
            return true;
        }
//        System.out.println("missing numbers are ");
        for (int r : arr) {
            System.out.println(r);

        }
        return false;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }
}
