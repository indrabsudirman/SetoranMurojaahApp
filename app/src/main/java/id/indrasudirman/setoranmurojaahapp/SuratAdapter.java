package id.indrasudirman.setoranmurojaahapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import id.indrasudirman.setoranmurojaahapp.databinding.ActivityMainBinding;
import id.indrasudirman.setoranmurojaahapp.databinding.DetailSuratBinding;

public class SuratAdapter extends RecyclerView.Adapter <SuratAdapter.ViewHolder> {
    private Context context;
    private List<Surat> suratList;
    private RecyclerView recyclerViewSurat;
    private final View.OnClickListener onClickListener = new MyOnClickListener();

    public static class ViewHolder extends RecyclerView.ViewHolder{
        AppCompatTextView suratKe;
        AppCompatTextView namaSurat;
        AppCompatTextView jumlahAyat;
        DetailSuratBinding detailSuratBinding;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //View Binding change findViewById
//            detailSuratBinding = ActivityMainBinding.inflate(getLayoutInflater());
//            itemView = detailSuratBinding.getRoot();
//            setContentView(view);

            suratKe = itemView.findViewById(R.id.suratKe);
            namaSurat = itemView.findViewById(R.id.namaSurat);
            jumlahAyat = itemView.findViewById(R.id.jumlahAyat);
        }
    }

    public SuratAdapter (Context context, List<Surat> suratList, RecyclerView recyclerViewSurat) {
        this.context = context;
        this.suratList = suratList;
        this.recyclerViewSurat = recyclerViewSurat;
    }

    @NonNull
    @Override
    public SuratAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.detail_surat, parent, false);
        view.setOnClickListener(onClickListener);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SuratAdapter.ViewHolder holder, int position) {
        Surat surat = suratList.get(position);
        holder.suratKe.setText(""+surat.getSuratKe());
        holder.namaSurat.setText(surat.getNamaSurat());
        holder.jumlahAyat.setText(surat.getJumlahAyat());

    }

    @Override
    public int getItemCount() {
        return suratList.size();
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int itemPosition = recyclerViewSurat.getChildLayoutPosition(v);
            String namaSurat = suratList.get(itemPosition).getNamaSurat();
            String jumlahAyat = suratList.get(itemPosition).getJumlahAyat();
//            Toast.makeText(context, namaSurat + "Jumlah ayat : " + jumlahAyat, Toast.LENGTH_SHORT).show();


            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
            bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet_ayat);

            LinearLayoutCompat linearLayoutCheckBox = bottomSheetDialog.findViewById(R.id.linearLayoutCheckBox);
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT
            );

            int margin = (int) convertDpToPixel(30F, context);
            params.setMargins(margin, 0,0,0);



            AppCompatCheckBox compatCheckBox;
            int banyakAyat = Integer.parseInt(jumlahAyat);

            for (int i = 1; i <= banyakAyat; i++) {
                compatCheckBox = new AppCompatCheckBox(context);
                compatCheckBox.setId(i);
                compatCheckBox.setText("Ayat " + i);
                int margin1 = (int) convertDpToPixel(10F, context);
                compatCheckBox.setPadding(margin1,0,0,0);
                linearLayoutCheckBox.addView(compatCheckBox, params);

//                constraintSet.clone(constraintLayout);
//                constraintSet.connect(compatCheckBox.getId(), ConstraintSet.TOP, compatCheckBox.getId() + i, ConstraintSet.BOTTOM, 0);
//                constraintSet.applyTo(constraintLayout);
            }

//            AppCompatCheckBox compatCheckBox = new AppCompatCheckBox(context);
//            compatCheckBox.setId(R.id.checkbox2);
//            compatCheckBox.setText("Ayat 1");
//            compatCheckBox.setPadding(25, 0,0,0);
//            constraintLayout.addView(compatCheckBox, params);
//
//            constraintSet.clone(constraintLayout);
//            constraintSet.connect(compatCheckBox.getId(), ConstraintSet.TOP, R.id.checkbox1, ConstraintSet.BOTTOM, 0);
//            constraintSet.applyTo(constraintLayout);
//
//            compatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (compatCheckBox.isSelected()) {
//                        Toast.makeText(context, "Check", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(context, "Uncheck", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });





//            constraintLayout.addView(checkBox);
            bottomSheetDialog.show();

        }

        private float convertDpToPixel(float dp, Context context) {
            return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        }
    }
}
