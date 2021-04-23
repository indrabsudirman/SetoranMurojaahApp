package id.indrasudirman.setoranmurojaahapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.Objects;

import id.indrasudirman.setoranmurojaahapp.databinding.ActivityMainBinding;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //View Binding change findViewById
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mainBinding.getRoot();
        setContentView(view);

        registerHere();

        mainBinding.appCompatButtonLogin.setOnClickListener(v -> loginClick());




    }

    private void loginClick() {
        //Call method to check all field valid
        checkFieldLogin();

    }

    private void checkFieldLogin() {

        userName = mainBinding.textInputEditTextEmail.getText().toString(); //Objects.requireNonNull(username.getText()).toString();
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
                System.out.println("Pass " + Arrays.toString(passwordUserChar));
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
}