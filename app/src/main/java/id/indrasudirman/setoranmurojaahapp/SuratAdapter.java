package id.indrasudirman.setoranmurojaahapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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

            suratKe = detailSuratBinding.suratKe;
            namaSurat = detailSuratBinding.namaSurat;
            jumlahAyat = detailSuratBinding.jumlahAyat;
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
            Toast.makeText(context, namaSurat, Toast.LENGTH_SHORT).show();

        }
    }
}
