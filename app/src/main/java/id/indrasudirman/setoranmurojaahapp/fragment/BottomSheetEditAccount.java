package id.indrasudirman.setoranmurojaahapp.fragment;

import static android.content.Context.MODE_PRIVATE;

import static id.indrasudirman.setoranmurojaahapp.tools.PasswordMD5WithSalt.byteArrayToHexString;
import static id.indrasudirman.setoranmurojaahapp.tools.PasswordMD5WithSalt.digest;
import static id.indrasudirman.setoranmurojaahapp.tools.PasswordMD5WithSalt.getSalt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

import id.indrasudirman.setoranmurojaahapp.MainActivity;
import id.indrasudirman.setoranmurojaahapp.R;
import id.indrasudirman.setoranmurojaahapp.SplashScreen;
import id.indrasudirman.setoranmurojaahapp.databinding.LayoutBottomsheetEditAccountBinding;
import id.indrasudirman.setoranmurojaahapp.helper.SQLiteHelper;
import id.indrasudirman.setoranmurojaahapp.model.User;

public class BottomSheetEditAccount extends BottomSheetDialogFragment {

    private LayoutBottomsheetEditAccountBinding layoutBottomsheetEditAccountBinding;
    private SendDataInterface mSendDataInterface;
    private SQLiteHelper sqLiteHelper;
    private User user;
    private SharedPreferences sharedPreferences;

    private String userName, userEmail;
    private String saltPwdDB, pwdSaltedDB;
    private Boolean allFieldValid = false;

    private static final String SHARED_PREF_NAME = "sharedPrefLogin";
    private static final String KEY_EMAIL = "email";

    private static final String TAG = BottomSheetEditAccount.class.getSimpleName();

    private View view;

    //Default Constructor
    public BottomSheetEditAccount(SendDataInterface sendDataInterface) {
        this.mSendDataInterface = sendDataInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBottomsheetEditAccountBinding = LayoutBottomsheetEditAccountBinding.inflate(inflater, container, false);
        view = layoutBottomsheetEditAccountBinding.getRoot();

        sqLiteHelper = new SQLiteHelper(getActivity());
        user = new User();

        getDetailAccountFromDatabase();

        layoutBottomsheetEditAccountBinding.saveEditUserAccount.setOnClickListener(view1 -> {
            saveEditAccount();
        });


        return view;
    }

    private void getDetailAccountFromDatabase() {
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String userEmail = (sharedPreferences.getString(KEY_EMAIL, "").trim());

        String userName = sqLiteHelper.getUserName(userEmail);
        layoutBottomsheetEditAccountBinding.textInputEditTextName.setText(userName);
        layoutBottomsheetEditAccountBinding.textInputEditTextEmail.setText(userEmail);
    }

    public interface SendDataInterface{
        void userName(String userName, String userEmail);
//        void userEmail(String userEmail);
    }

    private void saveEditAccount() {
        try {
            checkFieldToEdit();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (allFieldValid) {

            sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
            String userEmail = (sharedPreferences.getString(KEY_EMAIL, "").trim());

            user.setName(layoutBottomsheetEditAccountBinding.textInputEditTextName.getText().toString().trim());
            Log.d(TAG, "User name is " + user.getName());
            user.setEmail(layoutBottomsheetEditAccountBinding.textInputEditTextEmail.getText().toString().trim());
            Log.d(TAG, "User email is " + user.getEmail());
            user.setSalt(getSaltPwdDB());
            Log.d(TAG, "User salt is " + user.getSalt());
            user.setPassword(getPwdSaltedDB());
            Log.d(TAG, "User password is " + user.getPassword());
            sqLiteHelper.updateUser(userEmail, user.getName(), user.getEmail(), user.getSalt(), user.getPassword());
            //Snackbar to show success message that record saved successfully
            Snackbar.make(layoutBottomsheetEditAccountBinding.coordinatorLayoutMain, "Edit data berhasil", Snackbar.LENGTH_LONG).show();

            layoutBottomsheetEditAccountBinding.textInputEditTextName.setText("");
            layoutBottomsheetEditAccountBinding.textInputEditTextEmail.setText("");
            layoutBottomsheetEditAccountBinding.textInputEditTextPassword.setText("");
            layoutBottomsheetEditAccountBinding.textInputEditTextPasswordConfirm.setText("");

            mSendDataInterface.userName(user.getName(), user.getEmail());

            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(KEY_EMAIL, user.getEmail()).apply();


        }
        allFieldValid = false;
        allFieldValid = false;
        new Handler(Looper.getMainLooper()).postDelayed(BottomSheetEditAccount.this::dismiss, 4400);


    }

    public void checkFieldToEdit() throws NoSuchAlgorithmException {
        int length = layoutBottomsheetEditAccountBinding.textInputEditTextPassword.length();
        int lengthPass = layoutBottomsheetEditAccountBinding.textInputEditTextPasswordConfirm.length();
        char [] passwordUserChar = new char[length];
        char [] passwordUserCharConfirm = new char[lengthPass];

        userName = layoutBottomsheetEditAccountBinding.textInputEditTextName.getText().toString().trim();
        userEmail = layoutBottomsheetEditAccountBinding.textInputEditTextEmail.getText().toString().trim();
        //Getting value from all EditText and storing into variables

        Objects.requireNonNull(layoutBottomsheetEditAccountBinding.textInputEditTextPassword.getText()).getChars(0, length, passwordUserChar,0);
        Objects.requireNonNull(layoutBottomsheetEditAccountBinding.textInputEditTextPasswordConfirm.getText()).getChars(0, lengthPass, passwordUserCharConfirm, 0);

        if (userName.isEmpty()){
            layoutBottomsheetEditAccountBinding.textInputLayoutName.setError("Nama tidak boleh kosong!");
        } else if (userName.isEmpty() && layoutBottomsheetEditAccountBinding.textInputEditTextPassword.length() == 0 && layoutBottomsheetEditAccountBinding.textInputEditTextPasswordConfirm.length()== 0) {
            layoutBottomsheetEditAccountBinding.textInputLayoutName.setError(null);
            layoutBottomsheetEditAccountBinding.textInputLayoutEmail.setError("Email tidak boleh kosong!");
            layoutBottomsheetEditAccountBinding.textInputLayoutPassword.setError("Password tidak boleh kosong!");
            layoutBottomsheetEditAccountBinding.textInputLayoutPasswordConfirm.setError("Konfirmasi Password tidak boleh kosong!");
        } else if (validateUserName()){
            layoutBottomsheetEditAccountBinding.textInputLayoutName.setError(null);
            layoutBottomsheetEditAccountBinding.textInputLayoutEmail.setError(null);
            if (Arrays.equals(passwordUserChar, passwordUserCharConfirm)) {
                if (passwordUserChar.length <= 5 && passwordUserCharConfirm.length <= 5) {
                    layoutBottomsheetEditAccountBinding.textInputLayoutPassword.setError("Password terlalu pendek!");
                    layoutBottomsheetEditAccountBinding.textInputLayoutPasswordConfirm.setError("Password terlalu pendek!");
                } else {
                    layoutBottomsheetEditAccountBinding.textInputLayoutPassword.setError(null);
                    layoutBottomsheetEditAccountBinding.textInputLayoutPasswordConfirm.setError(null);
                    byte [] salt = getSalt();
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
                layoutBottomsheetEditAccountBinding.textInputLayoutPassword.setError("Password tidak sama!");
                layoutBottomsheetEditAccountBinding.textInputLayoutPasswordConfirm.setError("Password tidak sama!");
            }
        }



    }
    private boolean validateUserName() {
        if (!checkUserNameValid(layoutBottomsheetEditAccountBinding.textInputEditTextEmail)) {
            layoutBottomsheetEditAccountBinding.textInputLayoutEmail.setError("Isi email valid!");
            return false;
        } else {
            return true;
        }

    }

    private boolean checkUserNameValid(TextInputEditText textInputEditTextUsername) {
        CharSequence charSequence = textInputEditTextUsername.getText().toString().trim();
        return (!TextUtils.isEmpty(charSequence) && Patterns.EMAIL_ADDRESS.matcher(charSequence).matches());
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
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }
}
