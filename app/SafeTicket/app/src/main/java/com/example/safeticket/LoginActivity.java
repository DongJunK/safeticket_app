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
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.security.MessageDigest;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private SessionCallback callback;      //콜백 선언
    //유저프로필
    String token = "";
    String name = "";
    TextView findIdPwdText;
    EditText idEdit;
    EditText pwdEdit;
    TextView signUpButton;
    TextView logInFailMessage;
    Button logInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        idEdit = (EditText)findViewById(R.id.idEdit);
        pwdEdit = (EditText)findViewById(R.id.pwdEdit);
        findIdPwdText = (TextView) findViewById(R.id.findIdPwdText);
        findIdPwdText.setOnClickListener(this);

        signUpButton = (TextView) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this);

        logInButton = (Button) findViewById(R.id.logInButton);
        logInButton.setOnClickListener(this);

        logInFailMessage = (TextView) findViewById(R.id.logInFailMessage);
        /*
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

        getAppKeyHash();

         */
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

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), FindActivity.class);
        switch (v.getId()){
            case R.id.findIdPwdText:
                break;
            case R.id.signUpButton:
                intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.logInButton:
                // 로그인 성공시 true, 실패 false
                boolean result = logInCheck();

                if(result){
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }else {
                    logInFailMessage.setVisibility(View.VISIBLE);
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);  // 생성자 : 애니메이션 duration 간격 설정
                    anim.setDuration(50); // 깜빡임 동작 시간 milliseconds
                    anim.setStartOffset(50);  // 반복 횟수
                    anim.setRepeatCount(1); // 시작 전 시간 간격 milliseconds
                    logInFailMessage.startAnimation(anim); // 깜빡임 시작
                }
                break;
        }
        //startActivity(intent);
        //finish();
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
