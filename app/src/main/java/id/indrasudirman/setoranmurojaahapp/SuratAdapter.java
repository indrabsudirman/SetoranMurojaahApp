package id.indrasudirman.setoranmurojaahapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.indrasudirman.setoranmurojaahapp.databinding.DetailItemSuratBinding;


public class SuratAdapter extends RecyclerView.Adapter<SuratAdapter.ViewHolder> {
    private final View.OnClickListener onClickListener = new MyOnClickListener();
    private final Context context;
    private final List<Surat> suratList;
    private final RecyclerView recyclerViewSurat;

    public SuratAdapter(Context context, List<Surat> suratList, RecyclerView recyclerViewSurat) {
        this.context = context;
        this.suratList = suratList;
        this.recyclerViewSurat = recyclerViewSurat;
    }

    @NonNull
    @Override
    public SuratAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.detail_item_surat, parent, false);
        view.setOnClickListener(onClickListener);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SuratAdapter.ViewHolder holder, int position) {
        Surat surat = suratList.get(position);
        holder.suratKe.setText("" + surat.getSuratKe());
        holder.namaSurat.setText(surat.getNamaSurat());
        holder.jumlahAyat.setText(surat.getJumlahAyat());

    }

    @Override
    public int getItemCount() {
        return suratList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView suratKe;
        AppCompatTextView namaSurat;
        AppCompatTextView jumlahAyat;
        DetailItemSuratBinding detailSuratBinding;


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

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int itemPosition = recyclerViewSurat.getChildLayoutPosition(v);
            String jumlahAyat = suratList.get(itemPosition).getJumlahAyat();
        }

        private float convertDpToPixel(float dp, Context context) {
            return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        }
    }
}
