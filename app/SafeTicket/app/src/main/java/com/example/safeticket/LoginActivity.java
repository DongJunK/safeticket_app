package com.example.safeticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView findIdPwdText = (TextView) findViewById(R.id.findIdPwdText);
        findIdPwdText.setOnClickListener(this);

        Button signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), FindActivity.class);
        switch (v.getId()){
            case R.id.findIdPwdText:
                break;
            case R.id.signUpButton:
                intent.setClass(getApplicationContext(), SignUpActivity.class);
                break;
            case R.id.loginButton:
                intent.setClass(getApplicationContext(), MainActivity.class);
                break;
        }
        startActivity(intent);
        finish();
    }
}
