package com.example.safeticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.safeticket.Interfaces.RequestToServer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReadQrCodeActivity extends AppCompatActivity {
    private IntentIntegrator qrScan;
    IntentResult result;
    String loginId;
    SharedPreferences loginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readqrcode);
        qrScan = new IntentIntegrator(this);
        loginId = loginInfo.getString("email","");
        qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        qrScan.setPrompt("티켓 QrCode를 찍어주세요!");
        qrScan.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                scanQrCode();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    // qrCode 스캔
    void scanQrCode() {
        try {
            JSONObject qrCode_json = new JSONObject(result.getContents());
            if (!whetherEnrollOrScan(qrCode_json)) { // 티켓 등록 Qr코드인지 검표 Qr코드인지 확인
                if (enrollTicket(qrCode_json)) { // 티켓 등록
                    Toast.makeText(this, "등록되었습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }

            } else { // 검표 qr코드일 때
                JSONObject ticketInfo = scanTicket(qrCode_json.getString("ticket_code"));

                if (ticketInfo.getString("attendee_id").equals(qrCode_json.getString("email"))) {
                    qrScan.setPrompt(ticketInfo.getString("attendee_id") + "확인되었습니다");
                    Toast.makeText(this, "확인되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,ticketInfo.getString("attendee_id") + "티켓을 확인해주세요",Toast.LENGTH_LONG).show();
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    boolean enrollTicket(JSONObject ticketInfo) {

        JSONObject res_obj; // 응답 json
        RequestToServer reqToServer = new RequestToServer(); // 서버 요청 클래스
        JSONObject req_json = new JSONObject(); // 요청 json
        boolean result = false; // 요청 결과 값

        try {
            req_json.put("authorization", ticketInfo.getString("token"));
            req_json.put("attendee_id", ticketInfo.getString("email"));
            req_json.put("venue", ticketInfo.getString("venue"));
            req_json.put("event_name", ticketInfo.getString("event_name"));
            req_json.put("event_date", ticketInfo.getString("event_date"));
            req_json.put("event_time", ticketInfo.getString("event_time"));
            req_json.put("ticket_issuer", ticketInfo.getString("ticket_issuer"));
            req_json.put("payment_time", ticketInfo.getString("payment_time"));
            if(!ticketInfo.getString("email").equals(loginId)){
                return result;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (0 < req_json.length()) {
            try {
                //reqToserver execute / params 0 = GET OR POST / 1 = call function / 2 = request json
                res_obj = new JSONObject(reqToServer.execute("POST", "ticket/", String.valueOf(req_json)).get());
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

    //블록체인에 티켓 정보 요청
    JSONObject scanTicket(String ticketCode) {
        JSONObject res_obj; // 응답 json
        RequestToServer reqToServer = new RequestToServer(); // 서버 요청 클래스
        JSONObject req_json = new JSONObject(); // 요청 json
        JSONObject result = null; // 요청 결과 값

        try {
            //reqToserver execute / params 0 = GET OR POST / 1 = call function / 2 = request json
            res_obj = new JSONObject(reqToServer.execute("GET", "ticket/info?ticket_code=" + ticketCode, String.valueOf(req_json)).get());

            try {
                result = new JSONObject(res_obj.get("info").toString());
            } catch (JSONException e) {
                System.out.println(e.toString());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        Log.i("saficket","anstjaos "+result.toString());
        return result;
    }

    // 등록 QrCode 인지 검표용 QrCode인지 확인
    boolean whetherEnrollOrScan(JSONObject qrCode) {
        boolean check = true;
        String ticket_code = null;
        try {
            ticket_code = qrCode.getString("ticket_code");
        } catch (JSONException e) {
            e.printStackTrace();
            check = false;
        }
        if(ticket_code==null){
            check = false;
        }
        Log.e("safi",String.valueOf(check)+" "+ticket_code);
        return check;
    }
}
