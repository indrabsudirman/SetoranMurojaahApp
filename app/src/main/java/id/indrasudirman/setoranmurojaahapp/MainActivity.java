package id.indrasudirman.setoranmurojaahapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

import id.indrasudirman.setoranmurojaahapp.databinding.ActivityMainBinding;
import id.indrasudirman.setoranmurojaahapp.helper.SQLiteHelper;

import static id.indrasudirman.setoranmurojaahapp.tools.PasswordMD5WithSalt.digest;
import static id.indrasudirman.setoranmurojaahapp.tools.PasswordMD5WithSalt.hexStringToByteArray;

public class MainActivity extends AppCompatActivity {

    //View Binding Variable
    private ActivityMainBinding mainBinding;

    private SharedPreferences sharedPreferences;

    private SQLiteHelper sqLiteHelper;
    private int length;
    private char[] pwd;
    private String userName, userPassword;
    private Boolean allFieldValid = false;

    private static final String SHARED_PREF_NAME = "sharedPrefLogin";
    private static final String KEY_EMAIL = "email";

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //View Binding change findViewById
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mainBinding.getRoot();
        setContentView(view);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        sqLiteHelper = new SQLiteHelper(MainActivity.this);

        //Check if sharedPreferences data available
        String emailSharedPref = sharedPreferences.getString(KEY_EMAIL, null);
        // if sharedPreferences available so directly to MainMenu.class
        if (emailSharedPref != null) {
            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(intent);
        }

        registerHere();
//        skipRegister();

        mainBinding.appCompatButtonLogin.setOnClickListener(v -> loginClick());




    }

    private void loginClick() {
        //Call method to check all field valid
        checkFieldLogin();

        length = mainBinding.textInputEditTextPassword.length();
        pwd = new char[length];
        Objects.requireNonNull(mainBinding.textInputEditTextPassword.getText()).getChars(0, length, pwd, 0);
        System.out.println("Pass user sebelum dibuat 0 " + Arrays.toString(pwd));
        if (allFieldValid) {

            //Get salt from database
            String salt = sqLiteHelper.getSalt(Objects.requireNonNull(mainBinding.textInputEditTextEmail.getText()).toString().trim());
            if (salt != null) {
                //convert salt to byte
                byte[] saltByte = hexStringToByteArray(salt);
                System.out.println("Salt number dari db "+Arrays.toString(saltByte));
                try {

                    userPassword = digest(pwd, saltByte);
                    System.out.println("Pass User "+userPassword);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                if (userPassword.equals(sqLiteHelper.getPwdSalt(mainBinding.textInputEditTextEmail.getText().toString().trim()))) {
                    //Save to sharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_EMAIL, mainBinding.textInputEditTextEmail.getText().toString().trim());
                    editor.apply();
                    mainBinding.textInputEditTextEmail.setText("");
                    mainBinding.textInputEditTextPassword.setText("");
                    allFieldValid = false;
                    Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                } else {

                    Snackbar.make(mainBinding.nestedScrollView, "Password salah", Snackbar.LENGTH_LONG).show();
                }

            } else {
                Snackbar.make(mainBinding.nestedScrollView, "Login failed, email not found", Snackbar.LENGTH_LONG).show();
                mainBinding.textInputEditTextEmail.setText("");
                mainBinding.textInputEditTextPassword.setText("");
                allFieldValid = false;
            }

        }
        Arrays.fill(pwd, '0');
        System.out.println("Pass setelah dibuat 0 " + Arrays.toString(pwd));

    }

    private void checkFieldLogin() {

        userName = Objects.requireNonNull(mainBinding.textInputEditTextEmail.getText()).toString(); //Objects.requireNonNull(username.getText()).toString();
        int length = mainBinding.textInputEditTextPassword.length(); //password.length();
        char[] passwordUserChar = new char[length];

        Objects.requireNonNull(mainBinding.textInputEditTextPassword.getText()).getChars(0, length, passwordUserChar, 0);

        if (userName.isEmpty() && mainBinding.textInputEditTextPassword.length() == 0) {
            mainBinding.textInputLayoutEmail.setError("Username tidak boleh kosong!");
            mainBinding.textInputLayoutPassword.setError("Password tidak boleh kosong!");
        } else if (validateUserName()) {
            mainBinding.textInputLayoutEmail.setError(null);
            if (mainBinding.textInputEditTextPassword.length() == 0) {
                mainBinding.textInputLayoutPassword.setError("Password tidak boleh kosong!");
            } else {
                mainBinding.textInputLayoutPassword.setError(null);
                allFieldValid = true;
                Arrays.fill(passwordUserChar, '0');
                Log.d(TAG, "Pass " + Arrays.toString(passwordUserChar));
            }
        }

    }

    private boolean validateUserName() {
        userName = mainBinding.textInputEditTextEmail.getText().toString();
        if (userName.isEmpty()) {
            mainBinding.textInputEditTextEmail.setError("Username tidak boleh kosong!");
            return false;
        } else if (!checkUserNameValid(mainBinding.textInputEditTextEmail)) {
            mainBinding.textInputLayoutEmail.setError("Username diisi email valid!");
            return false;
        } else {
            return true;
        }

    }

    private boolean checkUserNameValid(TextInputEditText username) {
        CharSequence charSequence = username.getText().toString().trim();
        return (!TextUtils.isEmpty(charSequence) && Patterns.EMAIL_ADDRESS.matcher(charSequence).matches());
    }

    private void registerHere() {
        SpannableString spannableString = new SpannableString("Don't have account! Register here!");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        };
        spannableString.setSpan(clickableSpan, 20, 34, 0);
        mainBinding.appCompatTextViewRegisterLink.setMovementMethod(LinkMovementMethod.getInstance());
        mainBinding.appCompatTextViewRegisterLink.setText(spannableString);
    }

//    private void skipRegister() {
//        SpannableString spannableString = new SpannableString("Don't want to register, skip here!");
//        ClickableSpan clickableSpan = new ClickableSpan() {
//            @Override
//            public void onClick(@NonNull View view) {
//                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
//                startActivity(intent);
//                overridePendingTransition(0,0);
//            }
//        };
//        spannableString.setSpan(clickableSpan, 24, 34, 0);
//        mainBinding.appCompatTextViewSkipRegister.setMovementMethod(LinkMovementMethod.getInstance());
//        mainBinding.appCompatTextViewSkipRegister.setText(spannableString);
//    }
}