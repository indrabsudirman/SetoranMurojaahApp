package id.indrasudirman.setoranmurojaahapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;


import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

import id.indrasudirman.setoranmurojaahapp.databinding.ActivitySignUpBinding;
import id.indrasudirman.setoranmurojaahapp.helper.SQLiteHelper;
import id.indrasudirman.setoranmurojaahapp.model.User;

import static id.indrasudirman.setoranmurojaahapp.tools.PasswordMD5WithSalt.byteArrayToHexString;
import static id.indrasudirman.setoranmurojaahapp.tools.PasswordMD5WithSalt.digest;
import static id.indrasudirman.setoranmurojaahapp.tools.PasswordMD5WithSalt.getSalt;


public class SignUp extends AppCompatActivity {

    //View Binding Variable
    private ActivitySignUpBinding activitySignUpBinding;

    private String name, userName;
    private String saltPwdDB, pwdSaltedDB;
    private Boolean allFieldValid = false;

    private User user;
    private SQLiteHelper sqLiteHelper;

    private static final String TAG = SignUp.class.getSimpleName();


    public SignUp() {
        //Default constructor

    }

    public String getSaltPwdDB() {
        return saltPwdDB;
    }

    public void setSaltPwdDB(String saltPwdDB) {
        this.saltPwdDB = saltPwdDB;
    }

    public String getPwdSaltedDB() {
        return pwdSaltedDB;
    }

    public void setPwdSaltedDB(String pwdSaltedDB) {
        this.pwdSaltedDB = pwdSaltedDB;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //View Binding change findViewById
        activitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = activitySignUpBinding.getRoot();
        setContentView(view);

        sqLiteHelper = new SQLiteHelper(this);
        user = new User();

        loginHere();

        //Adding Sign Up click listener
        activitySignUpBinding.appCompatButtonSignUp.setOnClickListener(view1 -> {

            try {
                checkFieldSignUp();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            if (allFieldValid) {

                if (!sqLiteHelper.checkUser(Objects.requireNonNull(activitySignUpBinding.textInputEditTextEmail.getText()).toString().trim())) {
                    user.setName(Objects.requireNonNull(activitySignUpBinding.textInputEditTextName.getText()).toString().trim());
                    user.setEmail(activitySignUpBinding.textInputEditTextEmail.getText().toString().trim());
                    user.setSalt(getSaltPwdDB());
                    Log.d(TAG, "Salt mau input = " + getSaltPwdDB());
                    user.setPassword(getPwdSaltedDB());
                    Log.d(TAG, "Salt mau input = " + getPwdSaltedDB());

                    sqLiteHelper.addUser(user);

                    activitySignUpBinding.textInputEditTextName.setText("");
                    activitySignUpBinding.textInputEditTextEmail.setText("");
                    activitySignUpBinding.textInputEditTextPassword.setText("");
                    activitySignUpBinding.textInputEditTextPasswordConfirm.setText("");

                    //Snack Bar to show success message that record saved successfully
                    Snackbar.make(activitySignUpBinding.nestedScrollView, "Registration Successful, please login", Snackbar.LENGTH_LONG).show();

                } else {
                    //Snack Bar to show error message that record already exists
                    Snackbar.make(activitySignUpBinding.nestedScrollView, "Email Already Exists", Snackbar.LENGTH_LONG).show();
                }
                allFieldValid = false;
                allFieldValid = false;

            }


        });
    }

    private void loginHere() {
        SpannableString spannableString = new SpannableString("Already a member? Login");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        };
        spannableString.setSpan(clickableSpan, 18,23, 0);
        activitySignUpBinding.appCompatTextViewLoginLink.setMovementMethod(LinkMovementMethod.getInstance());
        activitySignUpBinding.appCompatTextViewLoginLink.setText(spannableString);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }

    public void checkFieldSignUp() throws NoSuchAlgorithmException {
        int length = activitySignUpBinding.textInputEditTextPassword.length();
        int lengthPass = activitySignUpBinding.textInputEditTextPasswordConfirm.length();
        char [] passwordUserChar = new char[length];
        char [] passwordUserCharConfirm = new char[lengthPass];

        name = activitySignUpBinding.textInputEditTextName.getText().toString().trim();
        userName = activitySignUpBinding.textInputEditTextEmail.getText().toString().trim();
        //Getting value from all EditText and storing into variables

        Objects.requireNonNull(activitySignUpBinding.textInputEditTextPassword.getText()).getChars(0, length, passwordUserChar,0);
        Objects.requireNonNull(activitySignUpBinding.textInputEditTextPasswordConfirm.getText()).getChars(0, lengthPass, passwordUserCharConfirm, 0);

        if (name.isEmpty()){
            activitySignUpBinding.textInputLayoutName.setError("Nama tidak boleh kosong!");
        } else if (userName.isEmpty() && activitySignUpBinding.textInputEditTextPassword.length() == 0 && activitySignUpBinding.textInputEditTextPasswordConfirm.length()== 0) {
            activitySignUpBinding.textInputLayoutName.setError(null);
            activitySignUpBinding.textInputLayoutEmail.setError("Email tidak boleh kosong!");
            activitySignUpBinding.textInputLayoutPassword.setError("Password tidak boleh kosong!");
            activitySignUpBinding.textInputLayoutPasswordConfirm.setError("Konfirmasi Password tidak boleh kosong!");
        } else if (validateUserName()){
            activitySignUpBinding.textInputLayoutName.setError(null);
            activitySignUpBinding.textInputLayoutEmail.setError(null);
            if (Arrays.equals(passwordUserChar, passwordUserCharConfirm)) {
                if (passwordUserChar.length <= 5 && passwordUserCharConfirm.length <= 5) {
                    activitySignUpBinding.textInputLayoutPassword.setError("Password terlalu pendek!");
                    activitySignUpBinding.textInputLayoutPasswordConfirm.setError("Password terlalu pendek!");
                } else {
                    activitySignUpBinding.textInputLayoutPassword.setError(null);
                    activitySignUpBinding.textInputLayoutPasswordConfirm.setError(null);
                    byte [] salt = getSalt();
                    Log.d(TAG, "Salt number "+Arrays.toString(salt));
                    String saltPwd = byteArrayToHexString(salt);
                    String pwdSalted = digest(passwordUserChar, salt);

                    setSaltPwdDB(saltPwd);
                    setPwdSaltedDB(pwdSalted);

                    //Change char to 0
                    Arrays.fill(passwordUserChar, '0');
                    Arrays.fill(passwordUserCharConfirm, '0');
                    allFieldValid = true;

                }
            } else {
                activitySignUpBinding.textInputLayoutPassword.setError("Password tidak sama!");
                activitySignUpBinding.textInputLayoutPasswordConfirm.setError("Password tidak sama!");
            }
        }

    }

    private boolean validateUserName() {
        if (!checkUserNameValid(activitySignUpBinding.textInputEditTextEmail)) {
            activitySignUpBinding.textInputLayoutEmail.setError("Isi email valid!");
            return false;
        } else {
            return true;
        }

    }

    private boolean checkUserNameValid(TextInputEditText textInputEditTextUsername) {
        CharSequence charSequence = textInputEditTextUsername.getText().toString().trim();
        return (!TextUtils.isEmpty(charSequence) && Patterns.EMAIL_ADDRESS.matcher(charSequence).matches());
    }


}