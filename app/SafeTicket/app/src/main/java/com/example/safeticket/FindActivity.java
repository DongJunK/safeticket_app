package com.example.safeticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.safeticket.Interfaces.RequestToServer;

import org.json.JSONException;
import org.json.JSONObject;


public class FindActivity extends AppCompatActivity implements View.OnClickListener {
    EditText idEdit;
    EditText nameEdit;
    Button okButton;
    ImageButton backButton;
    TextView failMessageText;
    String failMessage = "이메일과 이름을 확인하세요";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        backButton = (ImageButton) findViewById(R.id.backButton);
        idEdit = (EditText)findViewById(R.id.idEdit);
        nameEdit = (EditText)findViewById(R.id.nameEdit);
        okButton = (Button)findViewById(R.id.okButton);
        failMessageText = (TextView)findViewById(R.id.failMessageText);

        backButton.setOnClickListener(this);
        okButton.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.backButton:
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.okButton:
                String id = idEdit.getText().toString();
                String name = nameEdit.getText().toString();
                // idName 유효 체크
                if(idNameCheck(id,name)){
                    intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("name",name);
                    startActivity(intent);
                    finish();
                } else {
                    showFailMsg(failMessageText,failMessage);
                }
                break;
        }
    }
    public boolean idNameCheck(String email, String name){
        JSONObject res_obj;
        RequestToServer reqToServer = new RequestToServer(); // 서버 요청 클래스
        JSONObject req_json = new JSONObject(); // req body json
        boolean responseMsg = false; // res message

        try {
            req_json.put("email" ,email);
            req_json.put("name", name);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (req_json.length() > 0) {
            try {
                //reqToserver execute / params 0 = GET OR POST / 1 = call function / 2 = request json
                res_obj = new JSONObject(reqToServer.execute("POST", "users/update/check",String.valueOf(req_json)).get());
                try {
                    responseMsg = res_obj.getBoolean("result");

                } catch (JSONException e) {
                    System.out.println(e.toString());
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        return responseMsg;
    }
    // 실패 메세지 textview 출력
    void showFailMsg(TextView messageText, String message) {
        // 실패 메세지
        messageText.setText(message);
        messageText.setTextColor(Color.parseColor("#ff0000"));
        messageText.setVisibility(View.VISIBLE);
        Animation anim = new AlphaAnimation(0.0f, 1.0f); // 생성자 : 애니메이션 duration 간격 설정
        anim.setDuration(50); // 깜빡임 동작 시간 milliseconds
        anim.setStartOffset(50); // 시작 전 시간 간격 milliseconds
        anim.setRepeatCount(1); // 반복 횟수
        messageText.startAnimation(anim); // 깜빡임 시작
    }

}
