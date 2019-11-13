package com.example.safeticket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.safeticket.Interfaces.RequestToServer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BackPressCloseHandler bpcHandler;
    private String attendeeId = "owen1994";
    private ArrayList<UserInfo> userInfoList = new ArrayList<UserInfo>();
    private ArrayList<Ticket> ticketList = new ArrayList<Ticket>();

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

        RecyclerView userInfoView = findViewById(R.id.userInfoView);
        this.initList();
        getTicketList();

        UserInfoAdapter userInfoAdapter = new UserInfoAdapter(userInfoList, name);

        userInfoView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        userInfoView.setAdapter(userInfoAdapter);
        userInfoView.addItemDecoration(new RecyclerDecoration(30));

        RecyclerView ticketView = findViewById(R.id.ticketView);

        TicketAdapter ticketAdapter = new TicketAdapter(ticketList);
        ticketView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ticketView.setAdapter(ticketAdapter);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(ticketView.getContext(), new LinearLayoutManager(this).getOrientation());
        ticketView.addItemDecoration(dividerItemDecoration);
    }

    private void initList() {
        UserInfo test = new UserInfo("운전면허증", "123994444", "운전", "2019-11-12");
        userInfoList.add(test);
        test = new UserInfo("운전면허증", "343434", "운전", "2019-11-12");
        userInfoList.add(test);
        test = new UserInfo("운전면허증", "565656", "운전", "2019-11-12");
        userInfoList.add(test);
        test = new UserInfo("운전면허증", "787878", "운전", "2019-11-12");
        userInfoList.add(test);
        test = new UserInfo("운전면허증", "909090", "운전", "2019-11-12");
        userInfoList.add(test);
        test = new UserInfo("운전면허증", "321321", "운전", "2019-11-12");
        userInfoList.add(test);
        test = new UserInfo("운전면허증", "235", "운전", "2019-11-12");
        userInfoList.add(test);
        test = new UserInfo("운전면허증", "65656", "운전", "2019-11-12");
        userInfoList.add(test);
    }

//    @Override
//    public void onClick(View v) {
//        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
//        switch (v.getId()){
//            case R.id.registerUserInfoButton:
//                RegisterUserInfoDialog customDialog = new RegisterUserInfoDialog(MainActivity.this);
//                customDialog.callFunction();
//                return;
//            case R.id.userButton:
//                break;
//            case R.id.ticketButton:
//                intent.setClass(getApplicationContext(), TicketActivity.class);
//                break;
//            case R.id.userInfoButton:
//                intent.setClass(getApplicationContext(), UserInfoActivity.class);
//                break;
//        }
//        startActivity(intent);
//        finish();
//    }

    @Override
    public void onBackPressed() {
        bpcHandler.onBackPressed();
    }

    public JSONArray getTicketList(){
        JSONObject obj = new JSONObject();
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
