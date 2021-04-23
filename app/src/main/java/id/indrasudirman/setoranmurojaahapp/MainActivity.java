package id.indrasudirman.setoranmurojaahapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;

import id.indrasudirman.setoranmurojaahapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    //View Binding Variable
    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //View Binding change findViewById
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mainBinding.getRoot();
        setContentView(view);

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