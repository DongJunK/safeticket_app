package com.example.safeticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.safeticket.Interfaces.RequestToServer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class ReadQrCodeActivity extends AppCompatActivity {
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readqrcode);
        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        qrScan.setPrompt("티켓 QrCode를 찍어주세요!");
        qrScan.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                // todo
            } else {
                scanQrCode(result);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    void scanQrCode(IntentResult result){
        try {
            JSONObject ticketInfo = new JSONObject(result.getContents());
            //enrollTicket(ticketInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    boolean enrollTicket(JSONObject ticketInfo){

        JSONObject res_obj; // 응답 json
        RequestToServer reqToServer = new RequestToServer(); // 서버 요청 클래스
        JSONObject req_json = new JSONObject(); // 요청 json
        boolean result = false; // 요청 결과 값

        try {
            req_json.put("authorization",ticketInfo.getString("token"));
            req_json.put("email" , ticketInfo.getString("email"));
            req_json.put("venue" , ticketInfo.getString("venue"));
            req_json.put("event_name" , ticketInfo.getString("event_name"));
            req_json.put("evnet_date" , ticketInfo.getString("event_date"));
            req_json.put("event_time" , ticketInfo.getString("event_time"));
            req_json.put("ticket_issuer",ticketInfo.getString("ticket_issuer"));
            req_json.put("payment_time",ticketInfo.getString("payment_time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (req_json.length() > 0) {
            try {
                //reqToserver execute / params 0 = GET OR POST / 1 = call function / 2 = request json
                res_obj = new JSONObject(reqToServer.execute("POST", "users/join",String.valueOf(req_json)).get());
                try {
                    result = res_obj.getBoolean("result");

                } catch (JSONException e) {
                    System.out.println(e.toString());
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        return result;
    }


}
