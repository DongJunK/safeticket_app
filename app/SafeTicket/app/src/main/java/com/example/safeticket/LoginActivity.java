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
import android.widget.ImageButton;
import android.widget.ImageView;
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
    TextView logInFailMessageText;
    Button logInButton;
    ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        idEdit = (EditText)findViewById(R.id.idEdit);
        pwdEdit = (EditText)findViewById(R.id.pwdEdit);
        logInFailMessageText = (TextView) findViewById(R.id.logInFailMessageText);
        findIdPwdText = (TextView) findViewById(R.id.findIdPwdText);
        logInButton = (Button) findViewById(R.id.logInButton);
        signUpText = (TextView) findViewById(R.id.signUpText);
        backButton = (ImageButton) findViewById(R.id.backButton);

        signUpText.setOnClickListener(this);
        logInButton.setOnClickListener(this);
        findIdPwdText.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.findIdPwdText:
                intent = new Intent(getApplicationContext(),FindActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.signUpText:
                intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.logInButton:
                // 로그인 성공시 true, 실패 false
                String email = idEdit.getText().toString();
                String password = pwdEdit.getText().toString();

                boolean result = logInCheck(email, password);

                if(result){
                    // 로그인 정보 저장
                    JSONObject req_json = new JSONObject();
                    String name = "";

                    try {
                        //request body
                        req_json.put("email",email);
                        req_json.put("password",password);

                        // get response
                        JSONObject response = postRequest(req_json,"users/");
                        // jsonString으로된 유저 정보 result에 저장
                        String infoResult = response.getString("info");

                        //string to json
                        JSONObject userInfo = new JSONObject(infoResult);

                        // user name phoneNum setting
                        name = userInfo.getString("name");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // jsonString으로된 유저 정보 result에 저장

                    saveLoginInfo(email, password, name);

                    //메인 엑티비티로 전환
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    loginFailError();
                }
                break;
            case R.id.backButton:
                intent = new Intent(getApplicationContext(),SelectLoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public JSONObject postRequest(JSONObject req_json,String url){
        JSONObject res_obj;
        RequestToServer reqToServer = new RequestToServer(); // 서버 요청 클래스
        JSONObject result = null;

        if (req_json.length() > 0) {
            try {
                //reqToserver execute / params 0 = GET OR POST / 1 = call function / 2 = request json
                res_obj = new JSONObject(reqToServer.execute("POST", url, String.valueOf(req_json)).get());

                result = res_obj;


            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),SelectLoginActivity.class);
        startActivity(intent);
        finish();
    }


    public boolean logInCheck(String email, String pwd){
        JSONObject res_obj;
        RequestToServer reqToServer = new RequestToServer(); // 서버 요청 클래스
        JSONObject req_json = new JSONObject(); // req body json
        boolean responseMsg = false; // res message

        try {
            req_json.put("email" ,email);
            req_json.put("password", pwd);

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

    //login 실패시 실패 메세지 깜빡임 출력
    void loginFailError(){
        logInFailMessageText.setVisibility(View.VISIBLE);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);  // 생성자 : 애니메이션 duration 간격 설정
        anim.setDuration(50); // 깜빡임 동작 시간 milliseconds
        anim.setStartOffset(50);  // 반복 횟수
        anim.setRepeatCount(1); // 시작 전 시간 간격 milliseconds
        logInFailMessageText.startAnimation(anim); // 깜빡임 시작
    }
    void saveLoginInfo(String email, String password, String name){
        SharedPreferences loginInfo = getSharedPreferences("loginInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = loginInfo.edit();

        editor.putString("email",email);
        editor.putString("password",password);
        editor.putString("name", name);

        editor.commit();

    }
}
