package id.indrasudirman.setoranmurojaahapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    private ConstraintLayout constraintLayout;

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

    public SuratAdapter (Context context, List<Surat> suratList, RecyclerView recyclerViewSurat, ConstraintLayout constraintLayout) {
        this.context = context;
        this.suratList = suratList;
        this.recyclerViewSurat = recyclerViewSurat;
        this.constraintLayout = constraintLayout;
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

            AppCompatCheckBox checkBox = new AppCompatCheckBox(context);
            checkBox.setText("Ayat 99");
            checkBox.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
            bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet_ayat);
            constraintLayout.addView(checkBox);
            bottomSheetDialog.show();

        }
    }
}
