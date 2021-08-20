package id.indrasudirman.setoranmurojaahapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;
import org.joda.time.Chronology;
import org.joda.time.LocalDate;
import org.joda.time.chrono.IslamicChronology;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import id.indrasudirman.setoranmurojaahapp.adapter.ListMurojaahAdapter;
import id.indrasudirman.setoranmurojaahapp.databinding.ActivityMainMenuBinding;
import id.indrasudirman.setoranmurojaahapp.databinding.LayoutShareMurojaahHarianBinding;
import id.indrasudirman.setoranmurojaahapp.databinding.LayoutToolbarProfileBinding;
import id.indrasudirman.setoranmurojaahapp.databinding.ListMurojaahBinding;
import id.indrasudirman.setoranmurojaahapp.databinding.MainMenuNavigationDrawerBinding;
import id.indrasudirman.setoranmurojaahapp.helper.SQLiteHelper;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_IMAGE = 100;
    private static final String SHARED_PREF_NAME = "sharedPrefLogin";
    private static final String SHARED_PREF_DATE = "sharedPrefDate";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DATE = "date";
    private ActivityMainMenuBinding mainMenuBinding;
    private LayoutToolbarProfileBinding layoutToolbarProfileBinding;
    private ListMurojaahBinding listMurojaahBinding;
    private SQLiteHelper sqLiteHelper;
    private SharedPreferences sharedPreferences, sharedPreferencesDate;
    private String userEmail;
    private String dateString;
    private RecyclerView.Adapter adapterListMurojaah;
    private RecyclerView.LayoutManager layoutManagerListMurojaah;
    private ArrayList<MurojaahItem> murojaahItemArrayList;
    private MainMenuNavigationDrawerBinding mainMenuNavigationDrawerBinding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private View view;
    private String fileUri;
    private String pathImage;
    private LayoutShareMurojaahHarianBinding layoutShareMurojaahHarianBinding;

    public ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
            String murojaahSuratHarianDelete = murojaahItemArrayList.get(viewHolder.getAbsoluteAdapterPosition()).getNamaSurat();
            String typeMurojaahHarianDelete = murojaahItemArrayList.get(viewHolder.getAbsoluteAdapterPosition()).getTypeMurojaah();
            final int position = viewHolder.getAbsoluteAdapterPosition();
            //backup
            final MurojaahItem deleteMurojaah = murojaahItemArrayList.get(viewHolder.getAbsoluteAdapterPosition());
            final int deleteIndexMurojaah = viewHolder.getAbsoluteAdapterPosition();
            MurojaahItem murojaahItem = new MurojaahItem();

            if (direction == ItemTouchHelper.LEFT | direction == ItemTouchHelper.RIGHT) {
                //Delete murojaah list
                murojaahItemArrayList.remove(position);
                adapterListMurojaah.notifyItemRemoved(position);
                //Update list number RecyclerView while item was deleted
                adapterListMurojaah.notifyItemRangeChanged(position, murojaahItemArrayList.size());
                adapterListMurojaah.notifyDataSetChanged();

                //Delete murojaah harian database by row today
                sqLiteHelper.deleteMurojaahHarianDB(setTanggalMasehi() + " M");

                //Insert new murojaah harian database by row today from recyclerview
                if (!murojaahItemArrayList.isEmpty()) {
                    String userID = sqLiteHelper.getUserId(userEmail);
                    String[] tanggalHijriArray = setTanggalHijriyah();
                    String tglHijri = "H " + tanggalHijriArray[0] + " ," + tanggalHijriArray[1] + " ," + tanggalHijriArray[2];
                    sqLiteHelper.addMurojaahHarianDB(murojaahItemArrayList, userID, setTanggalMasehi() + " M", tglHijri);
                    ArraylistToJson(murojaahItemArrayList);
                }

                Snackbar.make(listMurojaahBinding.recyclerViewListMurojaah, typeMurojaahHarianDelete + " "+ murojaahSuratHarianDelete + " dihapus", Snackbar.LENGTH_LONG)
                        .setAction("Batal", v -> {
                            //Restore murojaah list
                            murojaahItemArrayList.add(deleteIndexMurojaah, deleteMurojaah);
                            adapterListMurojaah.notifyItemInserted(deleteIndexMurojaah);
                            //Update list number RecyclerView while item was restore
                            adapterListMurojaah.notifyItemRangeChanged(deleteIndexMurojaah, murojaahItemArrayList.size());
                            adapterListMurojaah.notifyDataSetChanged();
                            //Delete murojaah harian database by row today
                            sqLiteHelper.deleteMurojaahHarianDB(setTanggalMasehi() + " M");

                            //Insert new murojaah harian database by row today from recyclerview
                            if (!murojaahItemArrayList.isEmpty()) {
                                String userID = sqLiteHelper.getUserId(userEmail);
                                String[] tanggalHijriArray = setTanggalHijriyah();
                                String tglHijri = "H " + tanggalHijriArray[0] + " ," + tanggalHijriArray[1] + " ," + tanggalHijriArray[2];
                                sqLiteHelper.addMurojaahHarianDB(murojaahItemArrayList, userID, setTanggalMasehi() + " M", tglHijri);
                                ArraylistToJson(murojaahItemArrayList);
                            }
                        })
                        .show();
            }

        }

        @Override
        public void onChildDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftLabel("Hapus")
                    .addSwipeRightLabel("Hapus")
                    .setSwipeRightLabelColor(ContextCompat.getColor(MainMenu.this, R.color.white))
                    .setSwipeLeftLabelColor(ContextCompat.getColor(MainMenu.this, R.color.white))
                    .addBackgroundColor(ContextCompat.getColor(MainMenu.this, R.color.colorRed))
                    .addActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    @SuppressLint({"SetTextI18n", "WrongConstant", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_menu);

        //View Binding change findViewById
        mainMenuNavigationDrawerBinding = MainMenuNavigationDrawerBinding.inflate(getLayoutInflater());
        view = mainMenuNavigationDrawerBinding.getRoot();
        setContentView(view);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        userEmail = (sharedPreferences.getString(KEY_EMAIL, "").trim());
        sharedPreferencesDate = getSharedPreferences(SHARED_PREF_DATE, MODE_PRIVATE);
        dateString = (sharedPreferencesDate.getString(KEY_DATE, "").trim());

        sqLiteHelper = new SQLiteHelper(this);

        layoutToolbarProfileBinding = mainMenuNavigationDrawerBinding.mainMenuNavDrawer.layoutToolbarProfile;
        listMurojaahBinding = mainMenuNavigationDrawerBinding.mainMenuNavDrawer.listMurojaah;

        //Set Tanggal Masehi
        layoutToolbarProfileBinding.tanggalMasehi.setText(setTanggalMasehi() + " M");


        String[] tanggalHijri = setTanggalHijriyah();
//        Log.d("Bulan ", tanggalHijri[0]);
//        Log.d("Tanggal ", tanggalHijri[1]);
//        Log.d("Tahun ", tanggalHijri[2]);
        //Set Tanggal Hijriyah
        layoutToolbarProfileBinding.tanggalHijriyah.setText(tanggalHijri[1]);
        layoutToolbarProfileBinding.bulanHijriyah.setText(tanggalHijri[0]);
        layoutToolbarProfileBinding.tahunHijriyah.setText(tanggalHijri[2] + " H");

        mainMenuNavigationDrawerBinding.mainMenuNavDrawer.extendedFab.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PilihSuratMurojaah.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });


        Intent intent = getIntent();
        if (intent.hasExtra("murojaah_list")) {
            if (murojaahItemArrayList == null) {
                murojaahItemArrayList = new ArrayList<>();
            }
            murojaahItemArrayList.clear();
            murojaahItemArrayList = sqLiteHelper.getMurojaahHarianDB(sqLiteHelper.getUserId(userEmail), setTanggalMasehi() + " M");

        }

        createMurojaahArrayList();

        buildRecyclerViewMurojaah();

        GregorianCalendar calendar = new GregorianCalendar();
        LocalDate date = LocalDate.fromDateFields(calendar.getTime());
        String localDateString = date.toString();
        SharedPreferences.Editor editor = sharedPreferencesDate.edit();
        editor.putString(KEY_DATE, localDateString);
        editor.apply();
        Log.d("Date  ", localDateString);

        drawerLayout = mainMenuNavigationDrawerBinding.drawerLayout;
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mainMenuNavigationDrawerBinding.drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        mainMenuNavigationDrawerBinding.mainMenuNavDrawer.bottomAppBar.setNavigationOnClickListener(view1 -> {
//            Toast.makeText(getApplicationContext(),"your icon was clicked",Toast.LENGTH_SHORT).show();
            if (!drawerLayout.isDrawerOpen(Gravity.START)) {
                drawerLayout.openDrawer(Gravity.START);
            } else {
                drawerLayout.closeDrawer(Gravity.END);
            }
        });

        //Set event ClickListener on Navigation Drawer
        mainMenuNavigationDrawerBinding.navView.setNavigationItemSelectedListener(this);
        //Set event ClickListener on Bottom App Bar
        bottomAppBarMenuClickListener();

        mainMenuNavigationDrawerBinding.mainMenuNavDrawer.layoutToolbarProfile.imageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImageClicked();
            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    private void bottomAppBarMenuClickListener() {
        mainMenuNavigationDrawerBinding.mainMenuNavDrawer.bottomAppBar.setOnMenuItemClickListener(item -> {
            Intent shareIntent;
            String shareMessage = "Setoran Murojaah App";
            //Handle Bottom App Bar view item click here
            if (item.getItemId() == R.id.shareMurojaah) {
//                changeImageClicked();
                createImageMurojaahHarian();
//                shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/plain");
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Setoran Murojaah");
//                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
//                startActivity(Intent.createChooser(shareIntent, "Share via"));
//                overridePendingTransition(0, 0);
//                    Toast.makeText(getApplicationContext(),"Share was click",Toast.LENGTH_SHORT).show();
            }

            return false;
        });
    }


    public void createMurojaahArrayList() {
        if (murojaahItemArrayList == null) {
            murojaahItemArrayList = new ArrayList<>();
        }

        murojaahItemArrayList = sqLiteHelper.getMurojaahHarianDB(sqLiteHelper.getUserId(userEmail), setTanggalMasehi() + " M");

        if (!dateString.isEmpty()) {
            LocalDate localDate = LocalDate.now();
            LocalDate localDateSharedPref = LocalDate.parse(dateString);
            boolean newDate = localDate.isEqual(localDateSharedPref);
            if (newDate) {
                System.out.println("yes, it's same day");
            } else {
                System.out.println("no, it's not same day");
                murojaahItemArrayList.clear();
            }
        }


        boolean listEmpty = murojaahItemArrayList.isEmpty();

        if (listEmpty) {
            listMurojaahBinding.recyclerViewListMurojaah.setVisibility(View.GONE);
            listMurojaahBinding.textViewRecylerEmpty.setVisibility(View.VISIBLE);
        } else {
            listMurojaahBinding.recyclerViewListMurojaah.setVisibility(View.VISIBLE);
            listMurojaahBinding.textViewRecylerEmpty.setVisibility(View.GONE);
        }


        //Implement swipe left to remove murojaah array list.
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(listMurojaahBinding.recyclerViewListMurojaah);


    }

    public void buildRecyclerViewMurojaah() {
        layoutManagerListMurojaah = new LinearLayoutManager(this);
        adapterListMurojaah = new ListMurojaahAdapter(murojaahItemArrayList);
        listMurojaahBinding.recyclerViewListMurojaah.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        listMurojaahBinding.recyclerViewListMurojaah.setLayoutManager(layoutManagerListMurojaah);
        listMurojaahBinding.recyclerViewListMurojaah.setAdapter(adapterListMurojaah);
    }

    public String[] setTanggalHijriyah() {
        // List of Hijriyah months https://ocmic.org.uk/12-islamic-months/
        String[] months = {
                "ٱلْمُحَرَّم", "صَفَر", "رَبِيع ٱلْأَوَّل", "رَبِيع ٱلْآخِر",
                "جُمَادَىٰ ٱلْأُولَىٰ", "جُمَادَىٰ ٱلْآخِرَة", "رَجَب", "شَعْبَان",
                "رَمَضَان", "شَوَّال", "ذُو ٱلْقَعْدَة", "ذُو ٱلْحِجَّة"};

        Chronology hijri = IslamicChronology.getInstance();

        LocalDate localDate = LocalDate.now();
        GregorianCalendar calendar = new GregorianCalendar();
        LocalDate todayHijri = new LocalDate(localDate.toDateTimeAtStartOfDay(), hijri);
        Log.d("hijri", String.valueOf(todayHijri));
        int numberMonth = todayHijri.getMonthOfYear() - 1;
        Log.d("hijri month", String.valueOf(numberMonth));
        String monthName = months[numberMonth];
        Log.d("hijri month", monthName);
        String dateHijri = monthName + " " + todayHijri.getYear();
        Log.d("hijri date cool", dateHijri);

        return new String[]{monthName, String.valueOf(todayHijri.getDayOfMonth()), String.valueOf(todayHijri.getYear())};
    }

    public String setTanggalMasehi() {
        String[] months = {
                "Januari", "Februari", "Maret", "April",
                "Mei", "Juni", "Juli", "Agustus",
                "September", "Oktober", "November", "Desember"};

        GregorianCalendar calendar = new GregorianCalendar();

        String i = calendar.get(Calendar.DATE) + " " + months[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR);
        Log.d("Tanggal ", i);
        return i;


    }

    public void setToolbar(@Nullable String title) {
        setSupportActionBar(findViewById(R.id.toolbar));
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbar.setTitle(title);
    }

    private void logOutConfirmation() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainMenu.this);
        alertDialogBuilder
                .setTitle("Yakin Keluar!")
                .setIcon(R.drawable.ic_questions)
                .setMessage("Anda yakin akan keluar akun ?")
                .setCancelable(false)
                .setPositiveButton("Keluar",
                        (dialogInterface, i) -> {
                            // Delete SharedPreferences save
                            sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            startActivity(new Intent(getApplicationContext()
                                    , MainActivity.class));
                            overridePendingTransition(0, 0);
                        })

                .setNegativeButton("Batal keluar",
                        (dialogInterface, i) -> Toast.makeText(getApplicationContext(), "Anda batal keluar", Toast.LENGTH_SHORT).show());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void ArraylistToJson (ArrayList<MurojaahItem> murojaahItemArrayList) {
        Gson gson = new Gson();
        String json = gson.toJson(murojaahItemArrayList);
        System.out.println("jsonnya : " + json);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Handle navigation view item click here
        switch (item.getItemId()) {
            case R.id.backButton :
                Toast.makeText(getApplicationContext(),"Back was click",Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.myAccount :
                Toast.makeText(getApplicationContext(),"My Account was click",Toast.LENGTH_SHORT).show();
                break;
            case R.id.navSetting :
                Toast.makeText(getApplicationContext(),"Settings was click",Toast.LENGTH_SHORT).show();
                break;
            case R.id.navLogout :
                logOutConfirmation();
                break;
        }

        return false;
    }

    //Method to create Bitmap and save it into local
    public void createImageMurojaahHarian() {
//        View view = layoutShareMurojaahHarianBinding.mainLayout;
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        saveImageToGallery(bitmap);
        mainMenuNavigationDrawerBinding.mainMenuNavDrawer.layoutToolbarProfile.imageViewUser.setImageBitmap(bitmap);
    }

    //Save Bitmap Murojaah Harian to Gallery
    private void saveImageToGallery(Bitmap bitmap) {


        if (Build.VERSION.SDK_INT >= 29) {
            Log.d(MainMenu.class.getName(), "OS Android adalah " + Build.VERSION.SDK_INT);
            @SuppressLint("SimpleDateFormat") String title = "profile" + new SimpleDateFormat("yyyyMMddHHmmss'.png'").format(new Date());
            ContentValues values = contentValues();
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + getString(R.string.app_name));
            values.put(MediaStore.Images.Media.IS_PENDING, true);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, title); //set name image
            this.pathImage = title;
//            user.setImageName(title);
//            Log.d(MainMenu.class.getName(), "Image profile name from getImageName is " + user.getImageName());
            Log.d(MainMenu.class.getName(), "Image profile name from title is " + title);

            Uri uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                try {
                    saveImageToStream(bitmap, this.getContentResolver().openOutputStream(uri));
                    values.put(MediaStore.Images.Media.IS_PENDING, false);
                    this.getContentResolver().update(uri, values, null, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.d(MainMenu.class.getName(), "OS Android adalah " + Build.VERSION.SDK_INT);
            File directory = new File(Environment.getExternalStorageDirectory().toString() + '/' + getString(R.string.app_name));
//            File directory = new File(getBaseContext().getExternalFilesDir(n) + '/' + getString(R.string.app_name));
            if (!directory.exists()) {
                directory.mkdirs();
            }

            @SuppressLint("SimpleDateFormat")
            String fileName = "profile" + new SimpleDateFormat("yyyyMMddHHmmss'.png'").format(new Date());
            File file = new File(directory, fileName);

            try {
                saveImageToStream(bitmap, new FileOutputStream(file));
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            this.pathImage = fileName;
            Log.d("TAG", "Image name now at : " + fileName);

//            user.setImageName(fileName);
//            String photo = user.getImageName();
//            Log.d(TAG, "Image name now at setImageName : " + photo);
//
//            String name = user.getName(); //Null
//            Log.d(TAG, "name now at getName : " + name);

        }

    }

    private void saveImageToStream(Bitmap bitmap, OutputStream outputStream) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ContentValues contentValues() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        }
        return values;
    }


    private void changeImageClicked() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOption();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    private void showImagePickerOption() {
        ChangeImageProfileActivity.showImagePickerOption(this, new ChangeImageProfileActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });

    }

    private void launchCameraIntent() {
        Intent intent = new Intent(MainMenu.this, ChangeImageProfileActivity.class);
        intent.putExtra(ChangeImageProfileActivity.INTENT_IMAGE_PICKER_OPTION, ChangeImageProfileActivity.REQUEST_IMAGE_CAPTURE);

        //Setting aspect ratio
        intent.putExtra(ChangeImageProfileActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ChangeImageProfileActivity.INTENT_ASPECT_RATIO_X, 1); //16x9, 1x1,3:4, 3:2
        intent.putExtra(ChangeImageProfileActivity.INTENT_ASPECT_RATIO_Y, 1);

        //setting maximum bitmap width and height
        intent.putExtra(ChangeImageProfileActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ChangeImageProfileActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ChangeImageProfileActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(MainMenu.this, ChangeImageProfileActivity.class);
        intent.putExtra(ChangeImageProfileActivity.INTENT_IMAGE_PICKER_OPTION, ChangeImageProfileActivity.REQUEST_GALLERY_IMAGE);

        //setting aspect ratio
        intent.putExtra(ChangeImageProfileActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ChangeImageProfileActivity.INTENT_ASPECT_RATIO_X, 1); //16x9, 1x1, 3:4, 3:2
        intent.putExtra(ChangeImageProfileActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void showSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), ((dialog, which) -> {
            dialog.cancel();
            openSetting();
        }));
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    //navigation user to app settings
    private void openSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    //Method to share image setoran murojaah App
    public void shareMurojaahHarian(String url) {
        Picasso.with(getApplicationContext()).load(url).into(new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                try {
                    File myDir = new File(Environment.getExternalStorageDirectory() + "/setoran murojaah");
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    fileUri = myDir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
                    FileOutputStream outputStream = new FileOutputStream(fileUri);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), BitmapFactory.decodeFile(fileUri), null, null));
                //Use Intent to share image
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "Share Image"));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }
}