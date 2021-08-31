package id.indrasudirman.setoranmurojaahapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.fonts.Font;
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
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;
import org.joda.time.Chronology;
import org.joda.time.LocalDate;
import org.joda.time.chrono.IslamicChronology;
import org.w3c.dom.Text;

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
import id.indrasudirman.setoranmurojaahapp.fragment.BottomSheet;
import id.indrasudirman.setoranmurojaahapp.fragment.BottomSheetShare;
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
    private String userEmail, userName;
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
    private boolean isTrue = false;

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
//        layoutShareMurojaahHarianBinding

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        userEmail = (sharedPreferences.getString(KEY_EMAIL, "").trim());

        userName = sqLiteHelper.getUserName(userEmail);
        layoutToolbarProfileBinding.profileUserName.setText(userName);
        layoutToolbarProfileBinding.profileEmail.setText(userEmail);

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
                checkPermissionToChangeImageProfile();
            }
        });

        //Load image from database if exist, or load image default is not exist in database
        loadProfileDefault();

        // Clearing older images from cache directory
        // don't call this line if you want to choose multiple images in the same activity
        // call this once the bitmap(s) usage is over
        ChangeImageProfileActivity.clearCache(this);

    }

    @SuppressLint("NonConstantResourceId")
    private void bottomAppBarMenuClickListener() {
        mainMenuNavigationDrawerBinding.mainMenuNavDrawer.bottomAppBar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.shareMurojaah) {

                checkPermissionToSaveImageMurojaahHarian();

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
//        mainMenuNavigationDrawerBinding.mainMenuNavDrawer.layoutToolbarProfile.imageViewUser.setImageBitmap(bitmap);
    }

    private Bitmap takeScreenShot(RecyclerView recyclerView) {
        Bitmap bitmap = null;
        if (recyclerView.getWidth() > 0 | recyclerView.getMeasuredHeight() > 0) {
            recyclerView.measure(
                    View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            bitmap = Bitmap.createBitmap(recyclerView.getWidth() + 60, recyclerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(getColor(R.color.white)); //Lama ini mikirin disini, kenapa pas take screenshot selalu blackscreen. Ternyata harus set warna dasar dulu.. Alhamdulillah :)
            recyclerView.draw(canvas);
        } else {

            Toast.makeText(getApplicationContext(), "List Murojaah kosong", Toast.LENGTH_SHORT).show();
        }
        return bitmap;

    }




    public Bitmap addPaddingTopForBitmap(Bitmap bitmap) {

        Bitmap outputBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight() + 500, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputBitmap);
        canvas.drawColor(getColor(R.color.teal_700));
        canvas.drawBitmap(bitmap, 0, 500, null);

        return outputBitmap;
    }

    private int getApproxXToCenterText (String text, Typeface typeface, int fontSize, int widthToFitStringInto) {
        Paint paint = new Paint();
        paint.setTypeface(typeface);
        paint.setTextSize(fontSize);
        float textWidth = paint.measureText(text);
        return (int) ((widthToFitStringInto-textWidth)/2f) - (int) (fontSize/2f);
    }

    public Bitmap drawStringOnBitmap(Bitmap src) {

//        Bitmap result// = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

        String dateMasehi = setTanggalMasehi();
        String[] tanggalHijri = setTanggalHijriyah();
        int userNameLength = userName.length();
        int x = src.getWidth() - userNameLength;
        int y = 0;
        Canvas canvas = new Canvas(src);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE); //Color text
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        paint.setTextSize(50);


        canvas.drawBitmap(src, 0, 0, paint);
        canvas.drawText(userName, 85, 340, paint);
        paint.setTextSize(40);
        canvas.drawText(dateMasehi + " M", 85, 400, paint);
        canvas.drawText(tanggalHijri[1], 85, 460, paint); //Tanggal
        canvas.drawText(tanggalHijri[0], 150, 460, paint); //Bulan
        canvas.drawText(tanggalHijri[2] + " H", 280, 460, paint); //Tahun

        return src;

    }

    private Bitmap addWaterMark(Bitmap src) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);

        Bitmap waterMark = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.setoran_murojaah_logo);
        canvas.drawBitmap(waterMark, src.getWidth() / 2 - (waterMark.getWidth() / 2), 0, null);

        return result;
    }



    //Not get screen shot all Recyclerview. Some number ayat missing
    public static Bitmap getRecyclerViewScreenShot(RecyclerView view) {
        int size = view.getAdapter().getItemCount();
        RecyclerView.ViewHolder holder = view.getAdapter().createViewHolder(view, 0);
        view.getAdapter().onBindViewHolder(holder, 0);
        holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        holder.itemView.layout(0,0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
        Bitmap bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), holder.itemView.getMeasuredHeight() * size,
                Bitmap.Config.ARGB_8888);

        Canvas bigCanvas = new Canvas(bigBitmap);
        bigCanvas.drawColor(Color.WHITE);

        Paint paint = new Paint();
        int iHeight = 0;

        holder.itemView.setDrawingCacheEnabled(true);
        holder.itemView.buildDrawingCache();

        bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 0f, iHeight, paint);
        holder.itemView.setDrawingCacheEnabled(false);
        holder.itemView.destroyDrawingCache();
        iHeight += holder.itemView.getMeasuredHeight();

        for (int i = 0; i < size; i++) {
            view.getAdapter().onBindViewHolder(holder, i);
            holder.itemView.setDrawingCacheEnabled(true);
            holder.itemView.buildDrawingCache();
            bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 0f, iHeight, paint);
            iHeight += holder.itemView.getMeasuredHeight();
            holder.itemView.setDrawingCacheEnabled(false);
            holder.itemView.destroyDrawingCache();
        }


        return bigBitmap;


    }

    //Save Bitmap Murojaah Harian to Gallery
    private void saveImageToGallery(Bitmap bitmap) {


        if (Build.VERSION.SDK_INT >= 29) {
            Log.d(MainMenu.class.getName(), "OS Android adalah " + Build.VERSION.SDK_INT);
            @SuppressLint("SimpleDateFormat") String title = "murojaah" + new SimpleDateFormat("yyyyMMddHHmmss'.png'").format(new Date());
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
            String fileName = "murojaah" + new SimpleDateFormat("yyyyMMddHHmmss'.png'").format(new Date());
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


    private void checkPermissionToChangeImageProfile() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOption();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(getApplicationContext()
                                    , "Gagal mengganti foto profil, permission ditolak!", Toast.LENGTH_SHORT).show();
                            showSettingDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void checkPermissionToSaveImageMurojaahHarian() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Bitmap bitmap = takeScreenShot(listMurojaahBinding.recyclerViewListMurojaah);
                        if (bitmap != null) {
                            Bitmap bitmap3 = addPaddingTopForBitmap(bitmap);
                            Bitmap bitmap4 = drawStringOnBitmap(bitmap3);
                            Bitmap bitmap5 = addWaterMark(bitmap4);
//                            saveImageToGallery(bitmap5);
                            shareMurojaahHarian(bitmap5);
                            isTrue = true;

                        } else {
                            Toast.makeText(getApplicationContext(), "List Murojaah kosong", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getApplicationContext()
                        , "Gagal share Murojaah harian, permission ditolak!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
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
    public void shareMurojaahHarian(Bitmap bitmap) {
//        BottomSheetShare bottomSheetShare = new BottomSheetShare();
//        bottomSheetShare.show(getSupportFragmentManager(), "TAG");
        String shareMessage = "Assalamu'alaikum Ustad/Ustadzah, " + userName + " share murojaah hari ini. Terima kasih";
        Uri imageUri;
        String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bitmap, pathImage, null);
        imageUri = Uri.parse(path);
        //Use Intent to share image
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(intent, "Share Murojaah to"));
//        BottomSheet bottomSheet = BottomSheet.

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE) {

            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    //you can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    //save image to local Gallery
                    saveImageToGallery(bitmap);

                    //loading profile image from local cache
                    loadProfile(uri.toString());
                    //save image path to database

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String s = this.pathImage;
            Log.d("TAG", "User photo path in onActivityResult " + s); //null
            String profileEmailS = (String) mainMenuNavigationDrawerBinding.mainMenuNavDrawer.layoutToolbarProfile.profileEmail.getText();
//            user.setEmail(profileEmailS);
//            Log.d("TAG", "Email user in onActivityResult " + user.getEmail());
            //Update image path to database
            sqLiteHelper.updateUserImage(profileEmailS, s);
        }
    }

    private void loadProfile(String url) {
        Log.d("TAG", " image cache path :" + url);

        Glide.with(this).load(url)
                .into(mainMenuNavigationDrawerBinding.mainMenuNavDrawer.layoutToolbarProfile.imageViewUser);
        mainMenuNavigationDrawerBinding.mainMenuNavDrawer.layoutToolbarProfile.imageViewUser.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }

    private void loadProfileDefault() {
        String profileEmailS = (String) mainMenuNavigationDrawerBinding.mainMenuNavDrawer.layoutToolbarProfile.profileEmail.getText();


        if (sqLiteHelper.imagePathAlready(profileEmailS) != null) {
            String filename = sqLiteHelper.imagePathAlready(profileEmailS);
            Log.d("TAG", "Image name dari database, adalah : " + filename);

//            File file = new File(Environment.getExternalStorageDirectory().toString() + '/' + getString(R.string.app_name) + '/' + filename);
            if (Build.VERSION.SDK_INT >= 29) {
                File file = new File("/storage/emulated/0/" + Environment.DIRECTORY_PICTURES + File.separator + getString(R.string.app_name) + File.separator + filename);
                Uri imageUri = Uri.fromFile(file);

                Glide.with(this).load(imageUri)
                        .into(mainMenuNavigationDrawerBinding.mainMenuNavDrawer.layoutToolbarProfile.imageViewUser);
            } else {
                File file = new File("/storage/emulated/0/" + getString(R.string.app_name) + File.separator + filename);
                Uri imageUri = Uri.fromFile(file);

                Glide.with(this).load(imageUri)
                        .into(mainMenuNavigationDrawerBinding.mainMenuNavDrawer.layoutToolbarProfile.imageViewUser);
            }

        } else {
            Glide.with(this).load(R.mipmap.account_blue)
                    .into(mainMenuNavigationDrawerBinding.mainMenuNavDrawer.layoutToolbarProfile.imageViewUser);
        }
        mainMenuNavigationDrawerBinding.mainMenuNavDrawer.layoutToolbarProfile.imageViewUser.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));

    }
}