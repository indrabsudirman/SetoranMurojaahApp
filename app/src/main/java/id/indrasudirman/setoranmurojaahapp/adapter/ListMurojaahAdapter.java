package id.indrasudirman.setoranmurojaahapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.indrasudirman.setoranmurojaahapp.R;
import id.indrasudirman.setoranmurojaahapp.databinding.DetailItemMurojaahBinding;
import id.indrasudirman.setoranmurojaahapp.MurojaahItem;

public class ListMurojaahAdapter extends RecyclerView.Adapter<ListMurojaahAdapter.ListMurojaahViewHolder> {

    private ArrayList<MurojaahItem> iMurojaahItemArrayList;

    public static class ListMurojaahViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView iNumberMurojaah;
        private AppCompatTextView iTypeMurojaah;
        private AppCompatTextView iNamaSurat;
        private AppCompatTextView iAyatMurojaah;
        private DetailItemMurojaahBinding detailItemMurojaahBinding;

        public ListMurojaahViewHolder(@NonNull View itemView) {
            super(itemView);
            //ga yakin working, nanti coba di cek dan Google dulu
//            iNumberMurojaah = detailItemMurojaahBinding.numberMurojaah;
            iNumberMurojaah = itemView.findViewById(R.id.numberTextView);
            iTypeMurojaah = itemView.findViewById(R.id.murojaahTextView);
            iNamaSurat = itemView.findViewById(R.id.suratNameTextView);
            iAyatMurojaah = itemView.findViewById(R.id.ayatTextView);

        }
    }

    public ListMurojaahAdapter (ArrayList<MurojaahItem> murojaahItemArrayList) {
        iMurojaahItemArrayList =  murojaahItemArrayList;
    }

    @NonNull
    @Override
    public ListMurojaahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item_murojaah, parent, false);
        ListMurojaahViewHolder murojaahViewHolder = new ListMurojaahViewHolder(view);
        return murojaahViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListMurojaahViewHolder holder, int position) {
        MurojaahItem murojaahItem = iMurojaahItemArrayList.get(position);


        holder.iNumberMurojaah.setText((String.valueOf(position + 1 +". ")));
        holder.iTypeMurojaah.setText(murojaahItem.getTypeMurojaah());
        holder.iNamaSurat.setText(murojaahItem.getNamaSurat());
        holder.iAyatMurojaah.setText(murojaahItem.getAyatMurojaah());
    }

    @Override
    public int getItemCount() {
        return iMurojaahItemArrayList.size();
    }

}
