package id.indrasudirman.setoranmurojaahapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
//            Toast.makeText(context, namaSurat, Toast.LENGTH_SHORT).show();
//            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
//                    context, R.style.BottomSheetDialogTheme
//            );
//            bottomSheetDialog.show();
//            View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_sheet_ayat,
//                    (LinearLayoutCompat))
//            ConstraintLayout constraintLayoutCheckBox = v.findViewById(R.id.constraintLayoutCheckBox);


            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
            bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet_ayat);

            ConstraintLayout constraintLayout = bottomSheetDialog.findViewById(R.id.constraintLayoutCheckBox);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);

//            int [] id = {1,2,3}

            AppCompatCheckBox checkbox3 = bottomSheetDialog.findViewById(R.id.checkbox3);
            AppCompatCheckBox compatCheckBox = new AppCompatCheckBox(context);
//            constraintSet.connect(compatCheckBox, ConstraintSet.TOP, R.id.checkbox3, ConstraintSet.BOTTOM  );
            compatCheckBox.setId(R.id.checkbox4);
            compatCheckBox.setText("Ayat 99");
            constraintSet.connect(R.id.checkbox4, ConstraintSet.TOP, R.id.checkbox3, ConstraintSet.BOTTOM  );
            constraintLayout.addView(compatCheckBox);

//            constraintLayout.addView(checkBox);
            bottomSheetDialog.show();

        }
    }
}
