package com.example.safeticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class RegisterDriverLicenseActivity extends AppCompatActivity implements View.OnClickListener{
    ImageButton backButton; // 뒤로가기 버튼
    EditText issueNumberEdit; // 아이디 입력 텍스트 박스
    EditText issuerEdit; // 비밀번호 입력 텍스트 박스
    EditText issueDateEdit; //비밀번호 입력 확인 텍스트 박스
    Button registerButton; // 회원가입 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_driver_license);

        backButton = (ImageButton) findViewById(R.id.backButton);
        issueNumberEdit = (EditText) findViewById(R.id.issueNumberEdit);
        issuerEdit = (EditText) findViewById(R.id.issuerEdit);
        issueDateEdit = (EditText) findViewById(R.id.issueDateEdit);
        registerButton = (Button) findViewById(R.id.registerButton);

        backButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch(v.getId())
        {
            case R.id.backButton:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                break;
            case R.id.registerButton:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                break;
        }

        if(intent != null)
        {
            startActivity(intent);
            finish();
        }
    }
}
