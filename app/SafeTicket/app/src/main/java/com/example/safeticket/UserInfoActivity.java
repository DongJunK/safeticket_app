package com.example.safeticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    EditText pwdEdit;
    EditText pwdCheckEdit;
    TextView idText;
    EditText nameEdit;
    EditText phoneNumEdit;
    Button modifiedButton;
    Button withdrawalButton;
    ImageButton backButton;
    String email;
    String password;

    TextView errorMsgText;
    String pwdLengthMsg = "비밀번호는 7자리 이상 가능합니다";
    String notEquelPwdMsg = "비밀번호가 일치하지 않습니다";
    String pwdFailMsg = "비밀번호를 정확히 입력해주세요";
    String reqFailMsg = "다시 시도해주세요";
    SharedPreferences loginInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        pwdEdit = (EditText)findViewById(R.id.pwdEdit);
        pwdCheckEdit = (EditText)findViewById(R.id.pwdCheckEdit);
        idText = (TextView)findViewById(R.id.idText);
        nameEdit = (EditText)findViewById(R.id.nameEdit);
        phoneNumEdit = (EditText)findViewById(R.id.phoneNumEdit);
        modifiedButton = (Button)findViewById(R.id.modifiedButton);
        withdrawalButton = (Button)findViewById(R.id.withdrawalButton);
        backButton = (ImageButton) findViewById(R.id.backButton);
        errorMsgText = (TextView)findViewById(R.id.errorMsgText);

        loginInfo = getSharedPreferences("loginInfo",MODE_PRIVATE);
        email = loginInfo.getString("email",""); // 이메일 호출, default ""반환
        password = loginInfo.getString("password","");// 비밀번호 호출, default ""반환

        setUserInfo();


        backButton.setOnClickListener(this);
        modifiedButton.setOnClickListener(this);
        withdrawalButton.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.modifiedButton: // 회원정보 수정
                if(checkPwd(pwdEdit, pwdCheckEdit)){
                    if(modifiedRequest()){
                        // 자동로그인정보 변경
                        saveLoginInfo(email,pwdEdit.getText().toString());

                        intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        // 실패 에러메세지 출력
                        showFailMsg(errorMsgText,reqFailMsg);
                    }
                }
                break;
            case R.id.withdrawalButton: // 회원탈퇴
                // 비밀번호를 일치해야 삭제 요청 보냄.
                if(!pwdEdit.getText().toString().equals(pwdCheckEdit.getText().toString())){
                    showFailMsg(errorMsgText,notEquelPwdMsg);
                } else {
                    if(withdrawalRequest()){
                        removeLoginInfo();
                        intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // 실패 에러메세지 출력
                        showFailMsg(errorMsgText,reqFailMsg);
                    }
                }

                break;
            case R.id.backButton:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    boolean modifiedRequest(){
        JSONObject req_json = new JSONObject();
        boolean result = false;


        idText.setText(email); // user id setting

        try {
            //request body
            req_json.put("email",email);
            req_json.put("previous_password",password);
            req_json.put("password",pwdEdit.getText().toString());
            req_json.put("name",nameEdit.getText().toString());
            req_json.put("phone_num",phoneNumEdit.getText().toString());

            // get response
            JSONObject response = postRequest(req_json,"users/update");

            result = response.getBoolean("result");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    boolean withdrawalRequest(){
        JSONObject req_json = new JSONObject();
        boolean result = false;


        idText.setText(email); // user id setting

        try {
            //request body
            req_json.put("email",email);
            req_json.put("password",pwdEdit.getText().toString());

            // get response
            JSONObject response = postRequest(req_json,"users/withdrawal");

            result = response.getBoolean("result");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 유저 정보 세팅
    void setUserInfo(){

        JSONObject req_json = new JSONObject();


        idText.setText(email); // user id setting

        try {
            //request body
            req_json.put("email",email);
            req_json.put("password",password);

            // get response
            JSONObject response = postRequest(req_json,"users/");
            // jsonString으로된 유저 정보 result에 저장
            String result = response.getString("info");

            //string to json
            JSONObject userInfo = new JSONObject(result);

            // user name phoneNum setting
            nameEdit.setText(userInfo.getString("name"));
            phoneNumEdit.setText(userInfo.getString("phone_num"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject postRequest(JSONObject req_json,String method){
        JSONObject res_obj;
        RequestToServer reqToServer = new RequestToServer(); // 서버 요청 클래스
        JSONObject result = null;

        if (req_json.length() > 0) {
            try {
                //reqToserver execute / params 0 = GET OR POST / 1 = call function / 2 = request json
                res_obj = new JSONObject(reqToServer.execute("POST", method,String.valueOf(req_json)).get());

                result = res_obj;


            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }

    // 비밀번호 유효성 체크
    boolean checkPwd(EditText pwd, EditText pwdCheck){
        // 비밀번호가 동일하지 않으면 false 리턴
        if(!pwd.getText().toString().equals(pwdCheck.getText().toString())){
            showFailMsg(errorMsgText,notEquelPwdMsg);
            return false;
        }
        // 비밀번호가 7자리가 안되면 false 리턴
        if(pwd.getText().toString().length() < 7){
            showFailMsg(errorMsgText,pwdLengthMsg);
            return false;
        }

        return true;
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

    //자동로그인 정보 변경
    void saveLoginInfo(String email, String password){
        SharedPreferences.Editor editor = loginInfo.edit();

        editor.putString("email",email);
        editor.putString("password",password);

        editor.commit();
    }
    //자동로그인 정보 삭제
    void removeLoginInfo(){
        SharedPreferences.Editor editor = loginInfo.edit();

        editor.remove("email");
        editor.remove("password");
        editor.commit();
    }

}
