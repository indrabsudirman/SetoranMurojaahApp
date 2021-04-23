package id.indrasudirman.setoranmurojaahapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHelper.class.getSimpleName();

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "MurojaahSetiapHari.db";

    //User Table Name
    private static final String TABLE_USER = "Users";

    //User Table Columns Name
    private static final String COLUMN_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_MAIL = "user_email";
    private static final String COLUMN_PASSWORD_SALT = "salt";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHOTO_PATH = "photo_path";


    //Create Table SQL Query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME
            + " TEXT," + COLUMN_USER_MAIL + " TEXT," + COLUMN_PASSWORD_SALT
            + " TEXT," + COLUMN_PASSWORD + " TEXT, " + COLUMN_PHOTO_PATH +" TEXT "+  ")" ;

    //Drop Table SQL Query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

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

}
