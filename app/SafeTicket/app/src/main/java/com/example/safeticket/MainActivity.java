package com.example.safeticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private BackPressCloseHandler bpcHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bpcHandler = new BackPressCloseHandler(this);

        Button registerUserInfoButton = (Button) findViewById(R.id.registerUserInfoButton);
        registerUserInfoButton.setOnClickListener(this);

        Button userButton = (Button) findViewById(R.id.userButton);
        userButton.setOnClickListener(this);

        Button ticketButton = (Button) findViewById(R.id.ticketButton);
        ticketButton.setOnClickListener(this);

        Button userInfoButton = (Button) findViewById(R.id.userInfoButton);
        userInfoButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
        switch (v.getId()){
            case R.id.registerUserInfoButton:
                RegisterUserInfoDialog customDialog = new RegisterUserInfoDialog(MainActivity.this);
                customDialog.callFunction();
                return;
            case R.id.userButton:
                break;
            case R.id.ticketButton:
                intent.setClass(getApplicationContext(), TicketActivity.class);
                break;
            case R.id.userInfoButton:
                intent.setClass(getApplicationContext(), UserInfoActivity.class);
                break;
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        bpcHandler.onBackPressed();
    }
}
