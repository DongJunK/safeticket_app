package com.example.safeticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.safeticket.Interfaces.RequestToServer;
import com.google.zxing.qrcode.QRCodeReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private BackPressCloseHandler bpcHandler;
    private String attendeeId = "owen1994";

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

    public JSONArray getTicketList(View v){
        JSONObject obj = new JSONObject();
        RequestToServer reqToServer = new RequestToServer(); // Request to Server Class
        JSONObject req_json = new JSONObject();
        JSONArray responseMsg; // response JSON

        try {
            req_json.put("attendee_id" , attendeeId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (req_json.length() > 0) {
            try {
                //reqToserver execute / params 0 = GET OR POST / 1 = call function / 2 = request json
                obj = new JSONObject(reqToServer.execute("GET", "ticket/list",String.valueOf(req_json)).get());
                try {
                    responseMsg = obj.getJSONArray("list");
                    return responseMsg;

                } catch (JSONException e) {
                    System.out.println(e.toString());
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        return null;
    }
}
