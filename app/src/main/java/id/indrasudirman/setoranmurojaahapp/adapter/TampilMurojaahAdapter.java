package id.indrasudirman.setoranmurojaahapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
        String[] months = {
                "Januari", "Februari", "Maret", "April",
                "Mei", "Juni", "Juli", "Agustus",
                "September", "Oktober", "November", "Desember"};
        String i = "";

        TampilMurojaah tampilMurojaah = tampilMurojaahArrayList.get(position);
        String dateFromDB = tampilMurojaah.getTanggalMasehi();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(dateFromDB);
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            i = calendar.get(Calendar.DATE) + " " + months[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR) + " M";
        } catch (ParseException e) {
            e.printStackTrace();
        }



        holder.detailItemTampilMurojaahBinding.numberTextView1.setText(position + 1 + ".");
        holder.detailItemTampilMurojaahBinding.tanggalMasehi.setText(i);
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
