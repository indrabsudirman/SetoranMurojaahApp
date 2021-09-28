package id.indrasudirman.setoranmurojaahapp.fragment;


import static android.content.Context.MODE_PRIVATE;

import static com.yalantis.ucrop.UCropFragment.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
import androidx.core.content.FileProvider;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import id.indrasudirman.setoranmurojaahapp.BuildConfig;
import id.indrasudirman.setoranmurojaahapp.MainMenu;
import id.indrasudirman.setoranmurojaahapp.R;
import id.indrasudirman.setoranmurojaahapp.TampilkanMurojaahDatabase;
import id.indrasudirman.setoranmurojaahapp.databinding.LayoutBottomsheetDownloadMurojaahBinding;
import id.indrasudirman.setoranmurojaahapp.helper.SQLiteHelper;
import id.indrasudirman.setoranmurojaahapp.model.TampilMurojaah;

public class BottomSheetDownloadMurojaah extends BottomSheetDialogFragment {


    // Request code for selecting a PDF document.
    private static final int PICK_PDF_FILE = 2;
    private static final String SHARED_PREF_NAME = "sharedPrefLogin";
    private static final String KEY_EMAIL = "email";
    private static final int PAGE_WIDTH = 1200;
    private static final int PAGE_HEIGHT = 2010;
    private final String[] months = {
            "Januari", "Februari", "Maret", "April",
            "Mei", "Juni", "Juli", "Agustus",
            "September", "Oktober", "November", "Desember"};
    private View view;
    private LayoutBottomsheetDownloadMurojaahBinding bottomsheetDownloadMurojaahBinding;
    private String tipeMurojaah;
    private boolean isTrue = false;
    private String startDateToDb, endDateToDb, defaultDateToDb;
    private Bitmap headerBitmap, scaleBitmap;
    private SharedPreferences sharedPreferences;
    private String userEmail, userName;
    private SQLiteHelper sqLiteHelper;
    private ArrayList<TampilMurojaah> tampilMurojaahArrayList;
    private TampilMurojaah tampilMurojaah;
    private MainMenu mainMenu;
    private Uri pdfUriForQ;
    private boolean bulanSama = true;

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

        headerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rekap_setoran_murojaah_header);
        scaleBitmap = Bitmap.createScaledBitmap(headerBitmap, PAGE_WIDTH, 318, false);

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
            if (bulanSama) {
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
                            } else if (tipeMurojaah.equals("Ziyadah")) {
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
                SpannableStringBuilder sStringTitle = new SpannableStringBuilder("Warning!");
                sStringTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                //Set Title
                alertDialog
                        .setTitle(sStringTitle)
                        .setCancelable(false)
                        .setMessage("Download murojaah harus 1 bulan yang sama")
                        .setPositiveButton("Oke, ulang lagi", ((dialogInterface, i) -> {


                        }));
                alertDialog.show();
            }

        } else {
            SpannableStringBuilder sStringTitle = new SpannableStringBuilder("Warning!");
            sStringTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            //Set Title
            alertDialog
                    .setTitle(sStringTitle)
                    .setCancelable(false)
                    .setMessage("Pilih tipe murojaah dahulu!")
                    .setPositiveButton("Oke, ulang lagi", ((dialogInterface, i) -> {


                    }));
            alertDialog.show();
        }

    }

    private void checkPermissionToSavePdf() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        createPdfMurojaah();
                        generatedPdf();
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
    private void generatedPdf() {
        tampilMurojaahArrayList = new ArrayList<>();
        if (startDateToDb == null && endDateToDb == null) {
            Log.d(BottomSheetDownloadMurojaah.class.getName(), " Ini dra " + tampilMurojaahArrayList.toString());
            switch (tipeMurojaah) {
                case "Semua": {
                    tampilMurojaahArrayList = sqLiteHelper.getTampilMurojaahDBAll(sqLiteHelper.getUserId(userEmail), defaultDateToDb, defaultDateToDb);
                    break;
                }
                case "Murojaah": {
                    String tipe = "Murojaah";
                    tampilMurojaahArrayList = sqLiteHelper.getTampilMurojaahDBOnlyTypeSelected(sqLiteHelper.getUserId(userEmail), defaultDateToDb, defaultDateToDb, tipe);
                    break;
                }
                case "Ziyadah": {
                    String tipe = "Ziyadah";
                    tampilMurojaahArrayList = sqLiteHelper.getTampilMurojaahDBOnlyTypeSelected(sqLiteHelper.getUserId(userEmail), defaultDateToDb, defaultDateToDb, tipe);
                    break;
                }
            }
        } else {
            Log.d(BottomSheetDownloadMurojaah.class.getName(), " Ini dra " + tampilMurojaahArrayList.toString());
            switch (tipeMurojaah) {
                case "Semua": {
                    tampilMurojaahArrayList = sqLiteHelper.getTampilMurojaahDBAll(sqLiteHelper.getUserId(userEmail), startDateToDb, endDateToDb);
                    break;
                }
                case "Murojaah": {
                    String tipe = "Murojaah";
                    tampilMurojaahArrayList = sqLiteHelper.getTampilMurojaahDBOnlyTypeSelected(sqLiteHelper.getUserId(userEmail), startDateToDb, endDateToDb, tipe);
                    break;
                }
                case "Ziyadah": {
                    String tipe = "Ziyadah";
                    tampilMurojaahArrayList = sqLiteHelper.getTampilMurojaahDBOnlyTypeSelected(sqLiteHelper.getUserId(userEmail), startDateToDb, endDateToDb, tipe);
                    break;
                }
            }
        }


        int row = tampilMurojaahArrayList.size();
        tampilMurojaah = new TampilMurojaah();
        mainMenu = new MainMenu();

        PdfDocument pdfDocument = new PdfDocument();
        if (row <= 19) {
            createPdfMurojaahNew(row, 1, pdfDocument);
        } else {

            createPdfMurojaahNew(row, 1, pdfDocument);
            createPdfMurojaahNew(row, 2, pdfDocument);
        }


        // write the document content
        if (Build.VERSION.SDK_INT >= 29) {
            Log.d("Timber ini, OS Android adalah %s", String.valueOf(Build.VERSION.SDK_INT));
            //SDK Lower 29
            @SuppressLint("SimpleDateFormat") String fileName = "rekap_murojaah_" + new SimpleDateFormat("yyyyMMddHHmmss'.pdf'").format(new Date());
            OutputStream outputStream;
            ContentResolver contentResolver = getActivity().getContentResolver();
            ContentValues contentValues = new ContentValues();

            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + File.separator + getString(R.string.app_name));
            Uri pdfUriForQ = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);


            try {
                outputStream = contentResolver.openOutputStream(pdfUriForQ);
                savePdfToStream(pdfDocument, outputStream);
                getActivity().getContentResolver().update(pdfUriForQ, contentValues, null, null);
                // close the document
                pdfDocument.close();
                if (Build.VERSION.SDK_INT >= 30) {
                    Snackbar.make(bottomsheetDownloadMurojaahBinding.coordinatorLayoutMain,
                            "Rekap Murojaah berhasil download", Snackbar.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Rekap Murojaah berhasil download", Toast.LENGTH_SHORT).show();
                    BottomSheetDownloadMurojaah.this.dismiss();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                if (Build.VERSION.SDK_INT >= 30) {
                    Snackbar.make(bottomsheetDownloadMurojaahBinding.coordinatorLayoutMain,
                            "Gagal download Murojaah: " + e.toString(), Snackbar.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Gagal download Murojaah: " + e.toString(), Toast.LENGTH_SHORT).show();
                    BottomSheetDownloadMurojaah.this.dismiss();
                }
            }
            openPdf(new File(getRealPathFromURI(getContext(),pdfUriForQ)));
            Log.e("30", String.valueOf(Uri.parse(getRealPathFromURI(getContext().getApplicationContext(), pdfUriForQ))));
            System.out.println(Uri.parse(getRealPathFromURI(getContext().getApplicationContext(), pdfUriForQ)));

        } else {
            //SDK Lower 29
            File directory = new File(Environment.getExternalStorageDirectory().toString() + '/' + getString(R.string.app_name));
            if (!directory.exists()) {
                directory.mkdirs();
            }
            @SuppressLint("SimpleDateFormat")
            String fileName = "rekap_murojaah_" + new SimpleDateFormat("yyyyMMddHHmmss'.pdf'").format(new Date());
            File file = new File(directory, fileName);


            try {
                pdfDocument.writeTo(new FileOutputStream(file));
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Gagal download Murojaah: " + e.toString(), Toast.LENGTH_LONG).show();
            }

            // close the document
            pdfDocument.close();
            Toast.makeText(getContext(), "Rekap Murojaah berhasil download", Toast.LENGTH_SHORT).show();
            BottomSheetDownloadMurojaah.this.dismiss();

            //Open pdf file after success created
            openPdf(file);
        }
    }
    private void createPdfMurojaahNew(int row, int numPage, PdfDocument pdfDocument) {
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, numPage).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        canvas.drawBitmap(scaleBitmap, 0, 0, paint);

        titlePaint.setColor(Color.WHITE);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(35);
        canvas.drawText(userName, PAGE_WIDTH / 2, 170, titlePaint);
        //Set size to 50
        titlePaint.setTextSize(20);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        if (startDateToDb == null && endDateToDb == null) {

            Log.d(BottomSheetDownloadMurojaah.class.getName(), " Ini dra " + tampilMurojaahArrayList.toString());
            canvas.drawText("Dari tgl " + setDefaultDateForView(defaultDateToDb) + " sampai tgl " + setDefaultDateForView(defaultDateToDb), PAGE_WIDTH / 2, 210, titlePaint);

            switch (tipeMurojaah) {
                case "Semua": {
                    tampilMurojaahArrayList = sqLiteHelper.getTampilMurojaahDBAll(sqLiteHelper.getUserId(userEmail), defaultDateToDb, defaultDateToDb);
                    break;
                }
                case "Murojaah": {
                    String tipe = "Murojaah";
                    tampilMurojaahArrayList = sqLiteHelper.getTampilMurojaahDBOnlyTypeSelected(sqLiteHelper.getUserId(userEmail), defaultDateToDb, defaultDateToDb, tipe);
                    break;
                }
                case "Ziyadah": {
                    String tipe = "Ziyadah";
                    tampilMurojaahArrayList = sqLiteHelper.getTampilMurojaahDBOnlyTypeSelected(sqLiteHelper.getUserId(userEmail), defaultDateToDb, defaultDateToDb, tipe);
                    break;
                }
            }
        } else {
            Log.d(BottomSheetDownloadMurojaah.class.getName(), " Ini dra " + tampilMurojaahArrayList.toString());
            canvas.drawText("Dari tgl " + setDefaultDateForView(startDateToDb) + " sampai tgl " + setDefaultDateForView(endDateToDb), PAGE_WIDTH / 2, 210, titlePaint);

            switch (tipeMurojaah) {
                case "Semua": {
                    tampilMurojaahArrayList = sqLiteHelper.getTampilMurojaahDBAll(sqLiteHelper.getUserId(userEmail), startDateToDb, endDateToDb);
                    break;
                }
                case "Murojaah": {
                    String tipe = "Murojaah";
                    tampilMurojaahArrayList = sqLiteHelper.getTampilMurojaahDBOnlyTypeSelected(sqLiteHelper.getUserId(userEmail), startDateToDb, endDateToDb, tipe);
                    break;
                }
                case "Ziyadah": {
                    String tipe = "Ziyadah";
                    tampilMurojaahArrayList = sqLiteHelper.getTampilMurojaahDBOnlyTypeSelected(sqLiteHelper.getUserId(userEmail), startDateToDb, endDateToDb, tipe);
                    break;
                }
            }
        }

        titlePaint.setTextSize(15);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        canvas.drawText(getString(R.string.bacalah_al_quran), 405, 290, titlePaint);
        //Draw table rectangle
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);

        //Max row in a page 19.
//        row = 19;//tampilMurojaahArrayList.size();
        int space = 80;
        int top = 340;
        int y = 386;
        int addY = 80;
        canvas.drawLine(20, top, PAGE_WIDTH - 20, top, paint);
        top = top + space;
        canvas.drawLine(20, top, PAGE_WIDTH - 20, top, paint);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(25);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("No", 50, y, paint);
        canvas.drawText("Tanggal", 220, y, paint);
        canvas.drawText("Tipe Murojaah", 470, y, paint);
        canvas.drawText("Surat", 800, y, paint);
        canvas.drawText("Ayat", 1050, y, paint);
        //Set text to normal (not bold)
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        top = top + space;
        y = y + addY;
        int yDateMasehi = 452;
        int yDateHijri = 485;
        int addYDateSpace = 80;

        for (int i = 0; i < row; i++) {

            canvas.drawLine(20, top, PAGE_WIDTH - 20, top, paint);
            canvas.drawText((i + 1) + ". ", 50, y, paint);
//            canvas.drawText(setDefaultDateForView(tampilMurojaahArrayList.get(i).getTanggalMasehi()), 140, yDateMasehi, paint);
//            canvas.drawText(tampilMurojaahArrayList.get(i).getTanggalHijriah(), 140, yDateHijri, paint);
//            canvas.drawText(tampilMurojaahArrayList.get(i).getBulanHijriah(), 177, yDateHijri, paint);
//            canvas.drawText(tampilMurojaahArrayList.get(i).getTahunHijriah(), 300, yDateHijri, paint);
//            canvas.drawText(tampilMurojaahArrayList.get(i).getTipeMurojaah(), 500, y, paint);
//            canvas.drawText(tampilMurojaahArrayList.get(i).getSurat(), 800, y, paint);
//            canvas.drawText(tampilMurojaahArrayList.get(i).getAyat(), 1050, y, paint);
            top = top + space;
            y = y + addY;
            yDateMasehi = yDateMasehi + addYDateSpace;
            yDateHijri = yDateHijri + addYDateSpace;
        }
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(numPage), PAGE_WIDTH / 2, PAGE_HEIGHT - 20, paint);


        pdfDocument.finishPage(page);
    }

    private void savePdfToStream(PdfDocument pdfDocument, OutputStream outputStream) {
        if (outputStream != null) {
            try {
                pdfDocument.writeTo(outputStream);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openPdf(File pdfFile) {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "openPdf() called with: magazine = [" + pdfFile + "]");

        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            File file = new File(Uri.parse(pdfFile.getPath()).getPath());
            Uri uri = FileProvider.getUriForFile(getContext(),
                    BuildConfig.APPLICATION_ID + ".provider", file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else {
            intent.setDataAndType(Uri.parse(pdfFile.getPath()), "application/pdf");
        }

        try {
            startActivity(intent);
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }


    private void viewPdf(File file) {
        Uri outputFileUri;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        outputFileUri = Uri.fromFile(file);
        intent.setDataAndType(outputFileUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent in = Intent.createChooser(intent, "Open File");

        try {
            startActivity(in);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "Gagal membuka pdf, No PDF Viewer Installed", Toast.LENGTH_SHORT).show();

        }
    }

    private void viewPdfQ(Uri uri) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent in = Intent.createChooser(intent, "Open File");

        try {
            startActivity(in);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "Gagal membuka pdf, No PDF Viewer Installed", Toast.LENGTH_SHORT).show();

        }
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e(TAG, "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
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

                //Logic untuk 1 bulan sama
                if (startCalender.get(Calendar.MONTH) == endCalender.get(Calendar.MONTH)) {
                    bulanSama = true;
                    Log.d("TAG", "Bulan sama");
                } else {
                    bulanSama = false;
                    Log.d("TAG", "Bulan beda");
                }

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

    // chops a list into non-view sublists of length L
    private static <T> List<List<T>> chopped (List<T> list, final int L) {
        List<List<T>> parts = new ArrayList<List<T>>();
        final int N = list.size();
        for (int i = 0; i < N; i+= L) {
            parts.add(new ArrayList<T>(
                    list.subList(i, Math.min(N, i + L))
            ));
        }
        return parts;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }


}
