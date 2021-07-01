package id.indrasudirman.setoranmurojaahapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHelper.class.getSimpleName();

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "MurojaahSetiapHari.db";

    //User Table Name
    private static final String TABLE_USER = "Users";
    //Murojaah Table
    private static final String TABLE_MUROJAAH = "Murojaah";

    //Common Columns id
    private static final String ID = "id";
    //User Table Columns Name
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_MAIL = "user_email";
    private static final String COLUMN_PASSWORD_SALT = "salt";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHOTO_PATH = "photo_path";

    //Murojaah Table Columns Name
    private static final String COLUMN_ID_USER = "user_id";

    //Create Table SQL Query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(" + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME
            + " TEXT," + COLUMN_USER_MAIL + " TEXT," + COLUMN_PASSWORD_SALT
            + " TEXT," + COLUMN_PASSWORD + " TEXT, " + COLUMN_PHOTO_PATH +" TEXT "+  ")" ;

    //Drop Table Users SQL Query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    //Drop Table Murojaah SQL Query
    private String DROP_MUROJAAH_TABLE = "DROP TABLE IF EXISTS " + TABLE_MUROJAAH;

    public SQLiteHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);

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
        String[] columns = {ID};
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
            System.out.println("Salt in cursor is in getSalt " + salt);

        }
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
            System.out.println("Pwd Salt in cursor is in getPwdSalt " + salt1);

        }
        return salt1;

    }

}
