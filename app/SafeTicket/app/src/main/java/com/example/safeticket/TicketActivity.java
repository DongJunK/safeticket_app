package com.example.safeticket;

import android.content.Intent;
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
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;

public class TicketActivity extends AppCompatActivity {
    String tmpTicketCode = "EVN001";
    TextView eventNameText;
    TextView eventDateText;
    TextView eventTimeText;
    TextView venueText;
    ImageView ticketQrCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        eventNameText = (TextView) findViewById(R.id.eventNameText);
        eventDateText = (TextView) findViewById(R.id.eventDateText);
        eventTimeText = (TextView) findViewById(R.id.eventTimeText);
        venueText = (TextView) findViewById(R.id.venueText);
        ticketQrCodeView = (ImageView) findViewById(R.id.ticketQrCodeView);

        setTicketInfo();
        createQrCode();

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
    String reqTicektInfo(String ticket_code) {
        JSONObject res_obj; // 응답 json
        RequestToServer reqToServer = new RequestToServer(); // 서버 요청 클래스
        JSONObject req_json = new JSONObject(); // 요청 json
        String result = null; // 요청 결과 값

        try {
            //reqToserver execute / params 0 = GET OR POST / 1 = call function / 2 = request json
            res_obj = new JSONObject(reqToServer.execute("GET", "ticket/info?ticket_code=" + ticket_code, String.valueOf(req_json)).get());
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
        String res_obj = reqTicektInfo(tmpTicketCode);
        try {
            JSONObject obj = new JSONObject(res_obj);
            JSONObject ticketInfo = obj.getJSONObject("Record");
            eventNameText.setText(ticketInfo.getString("event_id"));
            eventDateText.setText(ticketInfo.getString("EventDate"));
            eventTimeText.setText(ticketInfo.getString("EventTime"));
            venueText.setText(ticketInfo.getString("Venue"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void createQrCode(){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(tmpTicketCode, BarcodeFormat.QR_CODE,300,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ticketQrCodeView.setImageBitmap(bitmap);
        }catch (Exception e){}
    }
}
