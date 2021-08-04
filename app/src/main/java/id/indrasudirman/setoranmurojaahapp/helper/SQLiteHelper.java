package id.indrasudirman.setoranmurojaahapp.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import id.indrasudirman.setoranmurojaahapp.model.Murojaah;
import id.indrasudirman.setoranmurojaahapp.model.MurojaahItem;
import id.indrasudirman.setoranmurojaahapp.model.User;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHelper.class.getSimpleName();

    //Database Version
    private static final int DATABASE_VERSION = 3;

    //Database Name
    private static final String DATABASE_NAME = "MurojaahSetiapHari.db";

    //User Table Name
    private static final String TABLE_USER = "Users";
    //Murojaah Table
    private static final String TABLE_MUROJAAH = "Murojaah";

    //Common column names for Users table is PK, for Murojaah table is FK
    private static final String USER_ID = "user_id";
    //User Table Columns Name
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_MAIL = "user_email";
    private static final String COLUMN_PASSWORD_SALT = "salt";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHOTO_PATH = "photo_path";

    //Murojaah Table Columns Name
    private static final String MUROJAAH_ID = "murojaah_id";
    private static final String MUROJAAH_TYPE = "type_murojaah";
    private static final String DATE_MASEHI = "date_masehi";
    private static final String DATE_HIJRI = "date_hijri";
    private static final String SURAT = "surat";
    private static final String AYAT = "ayat";

    //Create User Table SQL Query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(" + USER_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USER_NAME
            + " TEXT, " + COLUMN_USER_MAIL + " TEXT, " + COLUMN_PASSWORD_SALT
            + " TEXT, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_PHOTO_PATH +" TEXT "+  ")" ;

    //Create Murojaah Table SQL QUery
    private String CREATE_MUROJAAH_TABLE = "CREATE TABLE " + TABLE_MUROJAAH + "(" + MUROJAAH_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_ID + " INTEGER, "
            + MUROJAAH_TYPE + " TEXT, " + DATE_MASEHI + " TEXT, " + DATE_HIJRI + " TEXT, "
            + SURAT + " TEXT, " + AYAT + " TEXT " + ")";

    //Drop Table Users SQL Query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    //Drop Table Murojaah SQL Query
    private String DROP_MUROJAAH_TABLE = "DROP TABLE IF EXISTS " + TABLE_MUROJAAH;

    public SQLiteHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create Users Table
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        //Create Murojaah Table
        sqLiteDatabase.execSQL(CREATE_MUROJAAH_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //Drop User Table if exist
        sqLiteDatabase.execSQL(DROP_USER_TABLE);
        //Drop Murojaah Table if exist
        sqLiteDatabase.execSQL(DROP_MUROJAAH_TABLE);
        //Create New Table
        onCreate(sqLiteDatabase);

    }

    /**
     * This method is to create user record
     */
    public void addUser(User users) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_NAME, users.getName());
        contentValues.put(COLUMN_USER_MAIL, users.getEmail());
        contentValues.put(COLUMN_PASSWORD_SALT, users.getSalt());
        contentValues.put(COLUMN_PASSWORD, users.getPassword());

        //Inserting row
        sqLiteDatabase.insert(TABLE_USER, null, contentValues);
        sqLiteDatabase.close();
    }

    /**
     * This method to check user exist or not
     * @param email
     * @return true/false
     */
    public boolean checkUser (String email) {
        //array of columns to fetch
        String[] columns = {USER_ID};
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        //Selection criteria
        String selection = COLUMN_USER_MAIL + " = ?";

        //Selection argument
        String [] selectionArgs = {email};

        //Query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'indrabsudirman@gmail.com';
         */
        Cursor cursor = sqLiteDatabase.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        sqLiteDatabase.close();

        return cursorCount > 0;
    }

    public String getSalt(String email) {
        String salt = null;

        String[] columns = {COLUMN_PASSWORD_SALT};
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        //Selection criteria
        String selection = COLUMN_USER_MAIL + " = ?";

        //Selection argument
        String[] selectionArgs = {email};

        //Query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'indrabsudirman@gmail.com';
         */
        Cursor cursor = sqLiteDatabase.query(TABLE_USER, //Table to query
                columns, // column to return
                selection, //Select base on
                selectionArgs, //select argument
                null, //The values for the WHERE clause
                null, //group the rows
                null); //filter by row groups

        if (cursor.moveToFirst()) {
            salt = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD_SALT));
//            System.out.println("Salt in cursor is in getSalt " + salt);

        }
        cursor.close();
        sqLiteDatabase.close();
        return salt;

    }

    public String getPwdSalt(String email) {
        String salt1 = null;

        String[] columns = {COLUMN_PASSWORD};
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        //Selection criteria
        String selection = COLUMN_USER_MAIL + " = ?";

        //Selection argument
        String[] selectionArgs = {email};

        //Query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'indrabsudirman@gmail.com';
         */
        Cursor cursor = sqLiteDatabase.query(TABLE_USER, //Table to query
                columns, // column to return
                selection, //Select base on
                selectionArgs, //select argument
                null, //The values for the WHERE clause
                null, //group the rows
                null); //filter by row groups

        if (cursor.moveToFirst()) {
            salt1 = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
//            System.out.println("Pwd Salt in cursor is in getPwdSalt " + salt1);

        }
        cursor.close();
        sqLiteDatabase.close();
        return salt1;

    }

    /**
     * This method is to create murojaah record
     */
    public void addMurojaah(Murojaah murojaah, String userID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID, userID);
        contentValues.put(MUROJAAH_TYPE, murojaah.getTypeMurojaah());
        contentValues.put(DATE_MASEHI, murojaah.getDateMasehi());
        contentValues.put(DATE_HIJRI, murojaah.getDateHijri());
        contentValues.put(SURAT, murojaah.getSurat());
        contentValues.put(AYAT, murojaah.getAyat());

        //Inserting row
        sqLiteDatabase.insert(TABLE_MUROJAAH, null, contentValues);
        sqLiteDatabase.close();
    }

    public String getUserId(String email) {
        String userID = null;

        String[] columns = {USER_ID};
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        //Selection criteria
        String selection = COLUMN_USER_MAIL + " = ?";

        //Selection argument
        String[] selectionArgs = {email};

        //Query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'indrabsudirman@gmail.com';
         */
        Cursor cursor = sqLiteDatabase.query(TABLE_USER, //Table to query
                columns, // column to return
                selection, //Select base on
                selectionArgs, //select argument
                null, //The values for the WHERE clause
                null, //group the rows
                null); //filter by row groups

        if (cursor.moveToFirst()) {
            userID = cursor.getString(cursor.getColumnIndex(USER_ID));
//            System.out.println("Pwd Salt in cursor is in getPwdSalt " + userID);

        }
        cursor.close();
        sqLiteDatabase.close();
        return userID;

    }

    public String getTypeMurojaahHarianDB (String id) {
        String typeMurojaah = null;

        String[] columns = {MUROJAAH_TYPE};
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        //Selection criteria
        String selection = USER_ID + " = ?";

        //Selection argument
        String[] selectionArgs = {id};

        //Query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'indrabsudirman@gmail.com';
         */
        Cursor cursor = sqLiteDatabase.query(TABLE_MUROJAAH, //Table to query
                columns, // column to return
                selection, //Select base on
                selectionArgs, //select argument
                null, //The values for the WHERE clause
                null, //group the rows
                null); //filter by row groups

        if (cursor.moveToLast()) {
            typeMurojaah = cursor.getString(cursor.getColumnIndex(MUROJAAH_TYPE));
            System.out.println("Tipe Murojaah " + typeMurojaah);

        }
        cursor.close();
        sqLiteDatabase.close();



        return typeMurojaah;
    }

    public ArrayList<MurojaahItem> getMurojaahHarianDB (String id, String dateToday) {
        ArrayList<MurojaahItem> murojaahArrayList = new ArrayList<>();

        String[] columns = {MUROJAAH_TYPE, SURAT, AYAT};
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        //Selection criteria
        String selection = USER_ID + " = ? AND " + DATE_MASEHI + " = ?";

        //Selection argument
        String[] selectionArgs = {id, dateToday};

        //Order by String
        String orderBy = MUROJAAH_TYPE + " DESC";

        //Query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'indrabsudirman@gmail.com';
         */
        Cursor cursor = sqLiteDatabase.query(TABLE_MUROJAAH, //Table to query
                columns, // column to return
                selection, //Select base on
                selectionArgs, //select argument
                null, //The values for the WHERE clause
                null, //group the rows
                orderBy); //filter by row groups

//        cursor.moveToNext();
        while (cursor.moveToNext()){
            String columnTypeMurojaah = cursor.getString(cursor.getColumnIndex(MUROJAAH_TYPE));
            String columnSuratMurojaah = cursor.getString(cursor.getColumnIndex(SURAT));
            String columnAyatMurojaah = cursor.getString(cursor.getColumnIndex(AYAT));
            MurojaahItem murojaahItem = new MurojaahItem(columnTypeMurojaah, columnSuratMurojaah, "Ayat "+columnAyatMurojaah);
            murojaahArrayList.add(murojaahItem);
//            System.out.println("Tipe Murojaah " + typeMurojaah);

        }
        cursor.close();
        sqLiteDatabase.close();



        return murojaahArrayList;
    }

}
