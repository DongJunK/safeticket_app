package com.example.safeticket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.safeticket.Interfaces.RequestToServer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;

public class TicketActivity extends AppCompatActivity {
    private String ticketCode;
    String email;
    TextView eventNameText;
    TextView eventDateText;
    TextView eventTimeText;
    TextView venueText;
    ImageView ticketQrCodeView;
    SharedPreferences loginInfo;
    TextView ticketIssuerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        eventNameText = (TextView) findViewById(R.id.eventNameText);
        eventDateText = (TextView) findViewById(R.id.eventDateText);
        eventTimeText = (TextView) findViewById(R.id.eventTimeText);
        venueText = (TextView) findViewById(R.id.venueText);
        ticketQrCodeView = (ImageView) findViewById(R.id.ticketQrCodeView);
        ticketIssuerText = (TextView) findViewById(R.id.ticketIssuerText);

        loginInfo = getSharedPreferences("loginInfo",MODE_PRIVATE);
        email = loginInfo.getString("email","");

        Bundle extras = getIntent().getExtras();
        ticketCode = extras.getString("TicketCode");

        setTicketInfo(); // Ticket info setting at TextView
        createQrCode(); // create ticket Qr Code

        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    // 티켓 요청
    String reqTicektInfo() {
        JSONObject res_obj; // 응답 json
        RequestToServer reqToServer = new RequestToServer(); // 서버 요청 클래스
        JSONObject req_json = new JSONObject(); // 요청 json
        String result = null; // 요청 결과 값

        try {
            //reqToserver execute / params 0 = GET OR POST / 1 = call function / 2 = request json
            res_obj = new JSONObject(reqToServer.execute("GET", "ticket/info?ticket_code=" + ticketCode, String.valueOf(req_json)).get());

            try {
                result = res_obj.getString("info");
                System.out.println(result);

            } catch (JSONException e) {
                System.out.println(e.toString());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return result;
    }
    void setTicketInfo(){
        String res_obj = reqTicektInfo();
        try {
            JSONObject obj = new JSONObject(res_obj);
            eventNameText.setText(obj.getString("event_name"));
            eventDateText.setText(obj.getString("event_date"));
            eventTimeText.setText(obj.getString("event_time"));
            venueText.setText(obj.getString("venue"));
            ticketIssuerText.setText(obj.getString("ticket_issuer"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void createQrCode(){
        JSONObject qrcode_obj = new JSONObject();
        try {
            qrcode_obj.put("email",email);
            qrcode_obj.put("ticket_code",ticketCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(qrcode_obj.toString(), BarcodeFormat.QR_CODE,300,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ticketQrCodeView.setImageBitmap(bitmap);
        }catch (Exception e){}
    }
}