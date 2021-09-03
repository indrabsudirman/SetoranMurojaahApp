package id.indrasudirman.setoranmurojaahapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.indrasudirman.setoranmurojaahapp.databinding.DetailItemTampilMurojaahBinding;
import id.indrasudirman.setoranmurojaahapp.model.TampilMurojaah;

public class TampilMurojaahAdapter extends RecyclerView.Adapter<TampilMurojaahAdapter.TampilMurojaahViewHolder> {

    private final ArrayList<TampilMurojaah> tampilMurojaahArrayList;

    public TampilMurojaahAdapter(ArrayList<TampilMurojaah> tampilMurojaahArrayList) {
        this.tampilMurojaahArrayList = tampilMurojaahArrayList;
    }

    @NonNull
    @Override
    public TampilMurojaahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TampilMurojaahViewHolder(DetailItemTampilMurojaahBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TampilMurojaahViewHolder holder, int position) {
        TampilMurojaah tampilMurojaah = tampilMurojaahArrayList.get(position);
        holder.detailItemTampilMurojaahBinding.numberTextView1.setText(position + 1 + ".");
        holder.detailItemTampilMurojaahBinding.tanggalMasehi.setText(tampilMurojaah.getTanggalMasehi());
        holder.detailItemTampilMurojaahBinding.tanggalHijriyah.setText(tampilMurojaah.getTanggalHijriah());
        holder.detailItemTampilMurojaahBinding.bulanHijriyah.setText(tampilMurojaah.getBulanHijriah());
        holder.detailItemTampilMurojaahBinding.tahunHijriyah.setText(tampilMurojaah.getTahunHijriah());
        holder.detailItemTampilMurojaahBinding.murojaah.setText(tampilMurojaah.getTipeMurojaah());
        holder.detailItemTampilMurojaahBinding.surat.setText(tampilMurojaah.getSurat());
        holder.detailItemTampilMurojaahBinding.nomorAyat.setText(tampilMurojaah.getAyat());


    }

    @Override
    public int getItemCount() {
        return tampilMurojaahArrayList == null ? 0 :
                tampilMurojaahArrayList.size();
    }

    public static class TampilMurojaahViewHolder extends RecyclerView.ViewHolder {
        private final DetailItemTampilMurojaahBinding detailItemTampilMurojaahBinding;

        public TampilMurojaahViewHolder(DetailItemTampilMurojaahBinding detailItemTampilMurojaahBinding) {
            super(detailItemTampilMurojaahBinding.getRoot());
            this.detailItemTampilMurojaahBinding = detailItemTampilMurojaahBinding;
        }
    }
}
