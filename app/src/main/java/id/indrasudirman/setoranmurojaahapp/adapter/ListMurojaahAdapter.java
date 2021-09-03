package id.indrasudirman.setoranmurojaahapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.indrasudirman.setoranmurojaahapp.R;
import id.indrasudirman.setoranmurojaahapp.databinding.DetailItemMurojaahBinding;
import id.indrasudirman.setoranmurojaahapp.databinding.DetailItemTampilMurojaahBinding;
import id.indrasudirman.setoranmurojaahapp.model.MurojaahItem;

public class ListMurojaahAdapter extends RecyclerView.Adapter<ListMurojaahAdapter.ListMurojaahViewHolder> {

    private ArrayList<MurojaahItem> iMurojaahItemArrayList;

    public ListMurojaahAdapter (ArrayList<MurojaahItem> murojaahItemArrayList) {
        iMurojaahItemArrayList =  murojaahItemArrayList;
    }

    @NonNull
    @Override
    public ListMurojaahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListMurojaahViewHolder(DetailItemMurojaahBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListMurojaahViewHolder holder, int position) {
        MurojaahItem murojaahItem = iMurojaahItemArrayList.get(position);


        holder.detailItemMurojaahBinding.numberTextView.setText((position + 1 + "."));
        holder.detailItemMurojaahBinding.murojaahTextView.setText(" "+murojaahItem.getTypeMurojaah());
        holder.detailItemMurojaahBinding.suratNameTextView.setText(murojaahItem.getNamaSurat());
        holder.detailItemMurojaahBinding.ayatTextView.setText("ayat " + murojaahItem.getAyatMurojaah());
    }

    @Override
    public int getItemCount() {
        return iMurojaahItemArrayList.size();
    }

    public static class ListMurojaahViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView iNumberMurojaah;
        private AppCompatTextView iTypeMurojaah;
        private AppCompatTextView iNamaSurat;
        private AppCompatTextView iAyatMurojaah;
        private DetailItemMurojaahBinding detailItemMurojaahBinding;

        public ListMurojaahViewHolder(@NonNull DetailItemMurojaahBinding detailItemMurojaahBinding) {
            super(detailItemMurojaahBinding.getRoot());
            //ga yakin working, nanti coba di cek dan Google dulu, akhirnya bisa VIewBinding https://stackoverflow.com/a/62255625
//            iNumberMurojaah = detailItemMurojaahBinding.numberMurojaah;
//            iNumberMurojaah = itemView.findViewById(R.id.numberTextView);
//            iTypeMurojaah = itemView.findViewById(R.id.murojaahTextView);
//            iNamaSurat = itemView.findViewById(R.id.suratNameTextView);
//            iAyatMurojaah = itemView.findViewById(R.id.ayatTextView);
            this.detailItemMurojaahBinding = detailItemMurojaahBinding;

        }
    }
}
