package com.example.safeticket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.safeticket.Interfaces.RequestToServer;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;

public class SelectLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private SessionCallback callback;      //콜백 선언
    //유저프로필
    String token = "";
    String name = "";
    TextView signUpText; // 회원가입 버튼
    Button emailLoginButton; // 이메일 로그인 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectlogin);
        signUpText = (TextView)findViewById(R.id.signUpText);
        emailLoginButton = (Button)findViewById(R.id.emailLoginButton);



        signUpText.setOnClickListener(this);
        emailLoginButton.setOnClickListener(this);

        // 자동로그인 가능하면 MainActivity로 전환
        if(checkAutoLogin()){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }

        /*
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

        getAppKeyHash();
        */
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.signUpText:
                intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.emailLoginButton:
                intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            redirectSignupActivity();  // 세션 연결성공 시 redirectSignupActivity() 호출
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
            setContentView(R.layout.activity_login); // 세션 연결이 실패했을때
        }
        // 로그인화면을 다시 불러옴
    }

    protected void redirectSignupActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //자동로그인 확인
    boolean checkAutoLogin(){
        // 저장된 값 호출
        SharedPreferences loginInfo = getSharedPreferences("loginInfo",MODE_PRIVATE);
        String email = loginInfo.getString("email",""); // 이메일 호출, default ""반환
        String pwd = loginInfo.getString("password","");// 비밀번호 호출, default ""반환

        // 이메일, 패스워드가 저장되어 있고 로그인 성공하면 true 반환
        if(!email.equals("") && !pwd.equals("") && logInCheck(email,pwd)){
            return true;
        } else {
            return false;
        }
    }

    public boolean logInCheck(String email, String pwd){
        JSONObject res_obj;
        RequestToServer reqToServer = new RequestToServer(); // Request to Server Class
        JSONObject req_json = new JSONObject(); // req body json
        boolean responseMsg = false; // response Message

        try {
            req_json.put("email" , email);
            req_json.put("password", pwd);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (req_json.length() > 0) {
                try {
                    //reqToserver execute / params 0 = GET OR POST / 1 = call function / 2 = request json
                    res_obj = new JSONObject(reqToServer.execute("POST", "users/login", String.valueOf(req_json)).get());
                    try {
                        responseMsg = res_obj.getBoolean("result");

                    } catch (JSONException e) {
                        System.out.println(e.toString());
                    }
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        }catch(Exception e){
            responseMsg = false;
        }
        return responseMsg;
    }


}
