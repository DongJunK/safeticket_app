package com.example.safeticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.transition.Visibility;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.safeticket.Interfaces.RequestToServer;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.security.MessageDigest;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView findIdPwdText; // 비밀번호 찾기 버튼
    EditText idEdit; //
    EditText pwdEdit;
    TextView signUpText;
    TextView logInFailMessage;
    Button logInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        idEdit = (EditText)findViewById(R.id.idEdit);
        pwdEdit = (EditText)findViewById(R.id.pwdEdit);
        logInFailMessage = (TextView) findViewById(R.id.logInFailMessage);
        findIdPwdText = (TextView) findViewById(R.id.findIdPwdText);
        logInButton = (Button) findViewById(R.id.logInButton);
        signUpText = (TextView) findViewById(R.id.signUpText);

        signUpText.setOnClickListener(this);
        logInButton.setOnClickListener(this);
        findIdPwdText.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.findIdPwdText:
                break;
            case R.id.signUpText:
                intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.logInButton:
                // 로그인 성공시 true, 실패 false
                boolean result = logInCheck();

                if(result){
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    logInFailMessage.setVisibility(View.VISIBLE);
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);  // 생성자 : 애니메이션 duration 간격 설정
                    anim.setDuration(50); // 깜빡임 동작 시간 milliseconds
                    anim.setStartOffset(50);  // 반복 횟수
                    anim.setRepeatCount(1); // 시작 전 시간 간격 milliseconds
                    logInFailMessage.startAnimation(anim); // 깜빡임 시작
                }
                break;
            case R.id.backButton:
                intent = new Intent(getApplicationContext(),SelectLoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),SelectLoginActivity.class);
        startActivity(intent);
        finish();
    }


    public boolean logInCheck(){
        JSONObject res_obj;
        RequestToServer reqToServer = new RequestToServer(); // Request to Server Class
        JSONObject req_json = new JSONObject(); // req body json
        boolean responseMsg = false; // response Message

        try {
            req_json.put("email" , idEdit.getText().toString());
            req_json.put("password", pwdEdit.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (req_json.length() > 0) {
            try {
                //reqToserver execute / params 0 = GET OR POST / 1 = call function / 2 = request json
                res_obj = new JSONObject(reqToServer.execute("POST", "users/login",String.valueOf(req_json)).get());
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
}
