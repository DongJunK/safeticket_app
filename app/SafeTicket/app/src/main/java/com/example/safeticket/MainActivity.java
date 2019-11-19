package com.example.safeticket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safeticket.Interfaces.RequestToServer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private BackPressCloseHandler bpcHandler;
    private String attendeeId;
    private ArrayList<UserInfo> userInfoList = new ArrayList<UserInfo>();
    private ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;

    FloatingActionButton menuButton, registerButton, myPageButton, scannerButton, logoutButton;
    TextView nameText;
    SharedPreferences loginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bpcHandler = new BackPressCloseHandler(this);

        nameText = (TextView) findViewById(R.id.nameText);
        loginInfo = getSharedPreferences("loginInfo",MODE_PRIVATE);
        String name = loginInfo.getString("name",""); // 이메일 호출, default ""반환
        nameText.setText(name + "님, 안녕하세요");
        attendeeId = loginInfo.getString("email", "");

        RecyclerView userInfoView = findViewById(R.id.userInfoView);
        this.initList();
        getTicketList();

        UserInfoAdapter userInfoAdapter = new UserInfoAdapter(userInfoList, name, this);

        userInfoView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        userInfoView.setAdapter(userInfoAdapter);
        userInfoView.addItemDecoration(new RecyclerDecoration(30));

        RecyclerView ticketView = findViewById(R.id.ticketView);

        TicketAdapter ticketAdapter = new TicketAdapter(ticketList, this);
        ticketView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ticketView.setAdapter(ticketAdapter);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(ticketView.getContext(), new LinearLayoutManager(this).getOrientation());
        ticketView.addItemDecoration(dividerItemDecoration);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        menuButton = (FloatingActionButton) findViewById(R.id.menuButton);
        registerButton = (FloatingActionButton) findViewById(R.id.registerButton);
        myPageButton = (FloatingActionButton) findViewById(R.id.myPageButton);
        scannerButton = (FloatingActionButton) findViewById(R.id.scannerButton);
        logoutButton = (FloatingActionButton) findViewById(R.id.logoutButton);

        menuButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        myPageButton.setOnClickListener(this);
        scannerButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
    }

    private void initList() {
        UserInfo test = new UserInfo("운전면허증", "12-399-4444", "운전", "2017-11-12");
        userInfoList.add(test);
        test = new UserInfo("주민등록증", "940416-1", "주민등록증", "2018-04-12");
        userInfoList.add(test);
    }

    private void anim()
    {
        if (isFabOpen) {
            registerButton.startAnimation(fab_close);
            myPageButton.startAnimation(fab_close);
            scannerButton.startAnimation(fab_close);
            logoutButton.startAnimation(fab_close);

            registerButton.setClickable(false);
            myPageButton.setClickable(false);
            scannerButton.setClickable(false);
            logoutButton.setClickable(false);

            isFabOpen = false;
        } else {
            registerButton.startAnimation(fab_open);
            myPageButton.startAnimation(fab_open);
            scannerButton.startAnimation(fab_open);
            logoutButton.startAnimation(fab_open);

            registerButton.setClickable(true);
            myPageButton.setClickable(true);
            scannerButton.setClickable(true);
            logoutButton.setClickable(true);

            isFabOpen = true;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        switch (id){
            case R.id.menuButton:
                anim();
                break;
            case R.id.registerButton:
                anim();
                RegisterUserInfoDialog dlg = new RegisterUserInfoDialog(MainActivity.this);
                dlg.callFunction();
                break;
            case R.id.myPageButton:
                anim();
                intent = new Intent(this, UserInfoActivity.class);
                break;
            case R.id.scannerButton:
                anim();
                intent = new Intent(this, ReadQrCodeActivity.class);
                break;
            case R.id.logoutButton:
                anim();
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {

                    }
                });
                SharedPreferences.Editor editor = loginInfo.edit();
                intent = new Intent(this, SelectLoginActivity.class);
                editor.clear();
                editor.commit();
                break;
        }

        if(intent != null)
        {
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        bpcHandler.onBackPressed();
    }

    public JSONArray getTicketList(){
        JSONObject obj = null;
        RequestToServer reqToServer = new RequestToServer(); // Request to Server Class
        JSONArray ticketArray; // response JSON

        try {
            //reqToserver execute / params 0 = GET OR POST / 1 = call function / 2 = request json
            obj = new JSONObject(reqToServer.execute("GET", "ticket/list?attendee_id=" + attendeeId).get());

            try {
                ticketArray = new JSONArray(obj.get("list").toString());

                for (int i = 0; i < ticketArray.length(); i++) {
                    JSONObject ticketObject = ticketArray.getJSONObject(i);

                    String ticketCode = ticketObject.getString("TicketCode");
                    String eventName = ticketObject.getString("EventName");
                    String eventDate = ticketObject.getString("EventDate");
                    Ticket ticket = new Ticket(ticketCode, eventName, eventDate);

                    ticketList.add(ticket);
                }
                return ticketArray;
            } catch (JSONException e) {
                System.out.println(e.toString());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return null;
    }
}
