package id.indrasudirman.setoranmurojaahapp.fragment;


import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Pair;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import id.indrasudirman.setoranmurojaahapp.R;
import id.indrasudirman.setoranmurojaahapp.TampilkanMurojaahDatabase;
import id.indrasudirman.setoranmurojaahapp.databinding.LayoutBottomsheetDownloadMurojaahBinding;
import id.indrasudirman.setoranmurojaahapp.helper.SQLiteHelper;
import id.indrasudirman.setoranmurojaahapp.model.TampilMurojaah;

public class BottomSheetDownloadMurojaah extends BottomSheetDialogFragment {


    private static final String SHARED_PREF_NAME = "sharedPrefLogin";
    private static final String KEY_EMAIL = "email";
    private static final int PAGE_WIDTH = 1200;
    private View view;
    private LayoutBottomsheetDownloadMurojaahBinding bottomsheetDownloadMurojaahBinding;
    private String tipeMurojaah;
    private boolean isTrue = false;
    private String startDateToDb, endDateToDb, defaultDateToDb;
    private Bitmap headerBitmap, scaleBitmap;
    private SharedPreferences sharedPreferences;
    private String userEmail, userName;
    private SQLiteHelper sqLiteHelper;

    private final String[] months = {
            "Januari", "Februari", "Maret", "April",
            "Mei", "Juni", "Juli", "Agustus",
            "September", "Oktober", "November", "Desember"};

    //Default Constructor
    public BottomSheetDownloadMurojaah() {
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        bottomsheetDownloadMurojaahBinding = LayoutBottomsheetDownloadMurojaahBinding.inflate(inflater, container, false);
        view = bottomsheetDownloadMurojaahBinding.getRoot();

        bottomsheetDownloadMurojaahBinding.pilihTanggalMurojaah.setOnClickListener(view -> {
            pilihTanggalMurojaah();
        });

        sqLiteHelper = new SQLiteHelper(getContext());

        Calendar startCalender = Calendar.getInstance();
        int startMonth;
        startMonth = startCalender.get(Calendar.MONTH);
        startMonth = startMonth + 1;
        defaultDateToDb = startCalender.get(Calendar.YEAR) + "-" + startMonth + "-" + startCalender.get(Calendar.DATE);

        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        userEmail = (sharedPreferences.getString(KEY_EMAIL, "").trim());
        userName = sqLiteHelper.getUserName(userEmail);

        headerBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.rekap_setoran_murojaah_header);
        scaleBitmap = Bitmap.createScaledBitmap(headerBitmap, 1200, 518, false);

        //Set Spinner adapter
        setSpinnerAdapterAndListener();

        //setDefaultDateTextView
        setDefaultDateTextView();

        //Set listener if button download click
        bottomsheetDownloadMurojaahBinding.downloadMurojaah.setOnClickListener(view1 -> {
            downloadMurojaah();
        });


        return view;
    }

    // Download murojaah button
    private void downloadMurojaah() {
        if (isTrue) {
            SpannableStringBuilder sStringTitle = new SpannableStringBuilder("Konfimasi Donwload");
            sStringTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            //Set Title
            alertDialog
                    .setTitle(sStringTitle)
                    .setCancelable(false)
                    .setMessage("Anda ingin donwload murojaah atau hanya ingin tampilkan saja?")
                    .setPositiveButton("Download", ((dialogInterface, i) -> {

                        checkPermissionToSavePdf();
                    }))
                    .setNegativeButton("Tampilkan", (((dialogInterface, i) -> {
                        if (startDateToDb == null && endDateToDb == null) {

                            switch (tipeMurojaah) {
                                case "Semua": {
                                    String tipe = "Semua";
                                    Intent intent = new Intent(getContext(), TampilkanMurojaahDatabase.class);
                                    intent.putExtra("start_date_select", defaultDateToDb);
                                    intent.putExtra("end_date_select", defaultDateToDb);
                                    intent.putExtra("tipe_murojaah", tipe);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(0, 0);
                                    break;
                                }
                                case "Murojaah": {
                                    String tipe = "Murojaah";
                                    Intent intent = new Intent(getContext(), TampilkanMurojaahDatabase.class);
                                    intent.putExtra("start_date_select", defaultDateToDb);
                                    intent.putExtra("end_date_select", defaultDateToDb);
                                    intent.putExtra("tipe_murojaah", tipe);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(0, 0);
                                    break;
                                }
                                case "Ziyadah": {
                                    String tipe = "Ziyadah";
                                    Intent intent = new Intent(getContext(), TampilkanMurojaahDatabase.class);
                                    intent.putExtra("start_date_select", defaultDateToDb);
                                    intent.putExtra("end_date_select", defaultDateToDb);
                                    intent.putExtra("tipe_murojaah", tipe);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(0, 0);
                                    break;
                                }
                            }
                        } else if (tipeMurojaah.equals("Semua")) {
                            Intent intent = new Intent(getContext(), TampilkanMurojaahDatabase.class);
                            intent.putExtra("start_date_select", startDateToDb);
                            intent.putExtra("end_date_select", endDateToDb);
                            intent.putExtra("tipe_murojaah", tipeMurojaah);
                            startActivity(intent);
                            getActivity().overridePendingTransition(0, 0);
                        } else if (tipeMurojaah.equals("Murojaah")) {
                            Intent intent = new Intent(getContext(), TampilkanMurojaahDatabase.class);
                            intent.putExtra("start_date_select", startDateToDb);
                            intent.putExtra("end_date_select", endDateToDb);
                            intent.putExtra("tipe_murojaah", tipeMurojaah);
                            startActivity(intent);
                            getActivity().overridePendingTransition(0, 0);
                        }else if (tipeMurojaah.equals("Ziyadah")) {
                            Intent intent = new Intent(getContext(), TampilkanMurojaahDatabase.class);
                            intent.putExtra("start_date_select", startDateToDb);
                            intent.putExtra("end_date_select", endDateToDb);
                            intent.putExtra("tipe_murojaah", tipeMurojaah);
                            startActivity(intent);
                            getActivity().overridePendingTransition(0, 0);
                        }
                    })));

            alertDialog.show();

        } else {
            Snackbar.make(bottomsheetDownloadMurojaahBinding.coordinatorLayoutMain,
                    "Pilih tipe Murojaah dahulu!", Snackbar.LENGTH_SHORT).show();
        }

    }

    private void checkPermissionToSavePdf() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        createPdfMurojaah();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Snackbar.make(bottomsheetDownloadMurojaahBinding.coordinatorLayoutMain,
                                "Gagal download Murojaah, permission ditolak!", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void createPdfMurojaah() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        canvas.drawBitmap(scaleBitmap, 0, 0, paint);

        titlePaint.setColor(Color.WHITE);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(35);
        canvas.drawText(userName, PAGE_WIDTH/2, 270, titlePaint);
        //Set size to 50
        titlePaint.setTextSize(20);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        if (startDateToDb == null && endDateToDb == null) {
            canvas.drawText("Dari tgl " + setDefaultDateForView(defaultDateToDb) + " sampai tgl " + setDefaultDateForView(defaultDateToDb), PAGE_WIDTH/2, 320, titlePaint);
        }
        titlePaint.setTextSize(15);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        canvas.drawText(getString(R.string.bacalah_al_quran), 400, 480, titlePaint);
        //Draw table rectangle
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);

        int count = 10;
        int space = 60;
        int top = 540;
        canvas.drawLine(20,top, PAGE_WIDTH-20, top, paint);
        top = top + space;
        canvas.drawLine(20,top, PAGE_WIDTH-20, top, paint);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(25);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("No", 40, 577, paint);
        top = top + space;

        for (int i = 0; i < count; i++) {

            canvas.drawLine(20,top, PAGE_WIDTH-20, top, paint);
            top = top + space;


        }






        pdfDocument.finishPage(page);
        ProgressDialog progressdialog = new ProgressDialog(getContext());

        // write the document content
        String targetPdf = "/sdcard/pdffromScroll.pdf";
        File filePath;
        filePath = new File(targetPdf);
        try {
            pdfDocument.writeTo(new FileOutputStream(filePath));
            progressdialog.setMessage("Please Wait....");
            progressdialog.setCancelable(false);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        pdfDocument.close();
        Toast.makeText(getContext(), "PDF of Scroll is created!!!", Toast.LENGTH_SHORT).show();

        progressdialog.dismiss();




    }

    private String setDefaultDateForView(String dateFrom) {
        String[] months = {
                "Januari", "Februari", "Maret", "April",
                "Mei", "Juni", "Juli", "Agustus",
                "September", "Oktober", "November", "Desember"};
        String i = "";


//        dateFrom = tampilMurojaah.getTanggalMasehi();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(dateFrom);
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            i = calendar.get(Calendar.DATE) + " " + months[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR) + " M";
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return i;
    }

    //setDefaultDateTextView
    private void setDefaultDateTextView() {
        Calendar startCalender = Calendar.getInstance();
        String startDate1 = startCalender.get(Calendar.DATE) + " " + months[startCalender.get(Calendar.MONTH)] + " " + startCalender.get(Calendar.YEAR);
        bottomsheetDownloadMurojaahBinding.textViewStartDateShow.setText(startDate1 + " M");
        bottomsheetDownloadMurojaahBinding.textViewEndDateShow.setText(startDate1 + " M");
    }

    private void underlinePilihTipeMurojaahTextView() {
        SpannableString[] spannableString = new SpannableString[]{
                new SpannableString("Pilih tipe Murojaah"),
                new SpannableString("Dari Tanggal"),
                new SpannableString("Sampai Tanggal")};
        spannableString[0].setSpan(new UnderlineSpan(), 0, 19, 0);
        spannableString[1].setSpan(new UnderlineSpan(), 0, 12, 0);
        spannableString[2].setSpan(new UnderlineSpan(), 0, 14, 0);
        bottomsheetDownloadMurojaahBinding.textViewPilihTipeMurojaah.setText(spannableString[0]);
        bottomsheetDownloadMurojaahBinding.textViewStartDate.setText(spannableString[1]);
        bottomsheetDownloadMurojaahBinding.textViewEndDate.setText(spannableString[2]);
    }

    private void setSpinnerAdapterAndListener() {
        List<String> typeMurojaah = new ArrayList<>();
        typeMurojaah.add(0, "Pilih tipe Murojaah");
        typeMurojaah.add("Semua");
        typeMurojaah.add("Ziyadah");
        typeMurojaah.add("Murojaah");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, typeMurojaah);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bottomsheetDownloadMurojaahBinding.spinnerTypeMurojaah.setAdapter(arrayAdapter);

        bottomsheetDownloadMurojaahBinding.spinnerTypeMurojaah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Pilih tipe Murojaah")) {
                    isTrue = false;
                } else {
                    tipeMurojaah = adapterView.getItemAtPosition(i).toString();
                    isTrue = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void pilihTanggalMurojaah() {


        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Pilih Tanggal Download");
//        builder.set
        MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();


        materialDatePicker.show(getParentFragmentManager(), "TAG");
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            if (selection.first != null && selection.second != null) {
                //Start Date
                Calendar startCalender = Calendar.getInstance();
                startCalender.setTimeInMillis(selection.first);
                int startMonth;
                startMonth = startCalender.get(Calendar.MONTH);
                startMonth = startMonth + 1;
                startDateToDb = startCalender.get(Calendar.YEAR) + "-" + startMonth + "-" + startCalender.get(Calendar.DATE);
                Log.d("Start tanggal Masehi untuk query Download ", startDateToDb);
                String startDate = startCalender.get(Calendar.DATE) + " " + months[startCalender.get(Calendar.MONTH)] + " " + startCalender.get(Calendar.YEAR);
                bottomsheetDownloadMurojaahBinding.textViewStartDateShow.setText(startDate + " M");

                //End Date
                Calendar endCalender = Calendar.getInstance();
                endCalender.setTimeInMillis(selection.second);
                int endMonth;
                endMonth = endCalender.get(Calendar.MONTH);
                endMonth = endMonth + 1;
                endDateToDb = endCalender.get(Calendar.YEAR) + "-" + endMonth + "-" + endCalender.get(Calendar.DATE);
                Log.d("End tanggal Masehi untuk query Download ", endDateToDb);
                String endDate1 = endCalender.get(Calendar.DATE) + " " + months[endCalender.get(Calendar.MONTH)] + " " + endCalender.get(Calendar.YEAR);
                bottomsheetDownloadMurojaahBinding.textViewEndDateShow.setText(endDate1 + " M");

                Log.d("TAG", "OnPositiveButtonClick : " + startDate + " sampai " + endDate1);

            }
        });
        //While close by back button, or touch top screen
        materialDatePicker.addOnCancelListener(dialogInterface ->
                Snackbar.make(bottomsheetDownloadMurojaahBinding.coordinatorLayoutMain, "Batal memilih tanggal",
                        Snackbar.LENGTH_SHORT).show());
        //While cancel button was clicked
        materialDatePicker.addOnNegativeButtonClickListener(view ->
                Snackbar.make(bottomsheetDownloadMurojaahBinding.coordinatorLayoutMain, "Batal memilih tanggal",
                        Snackbar.LENGTH_SHORT).show());


    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }


}
