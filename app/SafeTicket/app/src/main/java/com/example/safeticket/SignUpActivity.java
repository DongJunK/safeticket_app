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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    ImageButton backButton; // 뒤로가기 버튼
    TextView idCreateMsgText; // 아이디 생성 가능 여부 메세지 텍스트뷰
    TextView pwdErrorMsgText; // 패스워드 불일치 메세지
    EditText idEdit; // 아이디 입력 텍스트 박스
    EditText pwdEdit; // 비밀번호 입력 텍스트 박스
    EditText pwdCheckEdit; //비밀번호 입력 확인 텍스트 박스
    EditText nameEdit; // 이름 입력 텍스트 박스
    EditText phoneNumEdit; // 휴대폰번호 입력 텍스트 박스
    Button signUpButton; // 회원가입 버튼
    Button idExistCheckButton; // 아이디 중복 확인 버튼
    String existIdMsg = "이미 계정이 있습니다";
    String notExistIdMsg = "사용가능한 아이디입니다";
    String pwdLengthMsg = "비밀번호는 7자리 이상 가능합니다";
    String notEquelPwdMsg = "비밀번호가 일치하지 않습니다";
    String notEmailFormatMsg = "이메일 형식이 아닙니다";
    String emailLongMsg = "이메일은 40자리 미만 가능합니다";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // xml과 연결 설정
        backButton = (ImageButton)findViewById(R.id.backButton);
        idCreateMsgText = (TextView)findViewById(R.id.idCreateMsgText);
        pwdErrorMsgText = (TextView)findViewById(R.id.pwdErrorMsgText);
        idEdit = (EditText)findViewById(R.id.idEdit);
        pwdEdit = (EditText)findViewById(R.id.pwdEdit);
        pwdCheckEdit = (EditText)findViewById(R.id.pwdCheckEdit);
        nameEdit = (EditText)findViewById(R.id.nameEdit);
        phoneNumEdit = (EditText)findViewById(R.id.phoneNumEdit);
        signUpButton = (Button)findViewById(R.id.signUpButton);
        idExistCheckButton = (Button)findViewById(R.id.idExistCheckButton);


        // 각 버튼 클릭 이벤트 등록
        backButton.setOnClickListener(this);
        idExistCheckButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    // 휴대폰 뒤로가기 버튼
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // 클릭 이벤트
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.backButton:
                intent = new Intent(getApplicationContext(), SelectLoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.idExistCheckButton:
                // ID 생성 가능한지 확인
                checkID();
                break;
            case R.id.signUpButton:
                // 비밀번호 유효성 체크
                if(!checkPwd(pwdEdit,pwdCheckEdit)){
                    idCreateMsgText.setVisibility(View.INVISIBLE);
                    pwdEdit.requestFocus(); // 비밀번호 포커스
                    break;
                }
                if(!checkID()){
                    idEdit.requestFocus();
                    break;
                }
                // 회원가입 가능하면 성공
                if(checkSingUp()){
                    saveLoginInfo(idEdit.getText().toString(), pwdEdit.getText().toString());
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showSuccessOrFailMsg(idCreateMsgText,existIdMsg+"\n다시 중복체크 해주세요",false);
                }
                break;
        }
    }

    // 아이디 생성 가능한지 확인
    boolean checkID(){
        if(40 < idEdit.getText().toString().length()){
            showSuccessOrFailMsg(idCreateMsgText,emailLongMsg,false);
            return false;
        }
        if(!checkEmailFormat(idEdit.getText().toString())){
            showSuccessOrFailMsg(idCreateMsgText,notEmailFormatMsg,false);
            return false;
        }
        if(!checkExistID()){
            showSuccessOrFailMsg(idCreateMsgText,existIdMsg,false);
            return false;
        }
        showSuccessOrFailMsg(idCreateMsgText,notExistIdMsg,true);
        return true;
    }

    //특수 문자 or 공백 포함여부와 이메일 포멧인지 확인
    public boolean checkEmailFormat(String str)
    {
        int dotCount = 0;
        int atCount =0;
        for(int i=0; i < str.length(); i++)
        {
            char indexChar = str.charAt(i);
            // .이면서 3개보다 많지 않을 경우
            if(indexChar=='.' && dotCount < 3){
                ++dotCount;
                continue;
            }
            //@이면서 2개보다 많지 않을 경우
            if(indexChar=='@' && atCount < 2){
                ++atCount;
                continue;
            }
            // 알파벳일 경우
            if(( 0<=indexChar-'a' && indexChar-'a'<26 )|| ( 0<=indexChar-'A' && indexChar-'A'<26 )|| ( '0' <= indexChar && indexChar <= '9' )){
                continue;
            }
            System.out.println("str.charAt(i) : "+str.charAt(i));
            return false;
        }

        // @와 .이 1개씩 없을 경우
        if((0<dotCount && dotCount <= 2) || atCount != 1){
            System.out.println(dotCount+" "+atCount);
            return false;
        }

        return true;
    }


    // 아이디 존재 여부 확인
    boolean checkExistID(){
        JSONObject res_obj; // 응답 json
        RequestToServer reqToServer = new RequestToServer(); // 서버 요청 클래스
        JSONObject req_json = new JSONObject(); // 요청 json
        boolean responseMsg = false; // 요청 결과 값
        try {
            req_json.put("email" , idEdit.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (req_json.length() > 0) {
            try {
                //reqToserver execute / params 0 = GET OR POST / 1 = call function / 2 = request json
                res_obj = new JSONObject(reqToServer.execute("POST", "users/email",String.valueOf(req_json)).get());
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

    // 가입 가능한지 확인
    boolean checkSingUp(){
        JSONObject res_obj; // 응답 json
        RequestToServer reqToServer = new RequestToServer(); // 서버 요청 클래스
        JSONObject req_json = new JSONObject(); // 요청 json
        boolean responseMsg = false; // 요청 결과 값

        try {
            req_json.put("email" , idEdit.getText().toString());
            req_json.put("password" , pwdEdit.getText().toString());
            req_json.put("name" , nameEdit.getText().toString());
            req_json.put("phone_num" , phoneNumEdit.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (req_json.length() > 0) {
            try {
                //reqToserver execute / params 0 = GET OR POST / 1 = call function / 2 = request json
                res_obj = new JSONObject(reqToServer.execute("POST", "users/join",String.valueOf(req_json)).get());
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

    // 성공 or 실패 메세지 textview 출력
    void showSuccessOrFailMsg(TextView messageText, String message, boolean success) {
        if(success){ // 성공 메세지
            messageText.setTextColor(Color.parseColor("#8ec96d"));
            messageText.setText(message);
        } else { // 실패 메세지
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

    // 비밀번호 유효성 체크
    boolean checkPwd(EditText pwd, EditText pwdCheck){
        // 비밀번호가 동일하면 false 리턴
        if(!pwd.getText().toString().equals(pwdCheck.getText().toString())){
            showSuccessOrFailMsg(pwdErrorMsgText,notEquelPwdMsg,false);
            return false;
        }
        // 비밀번호가 7자리가 안되면 false 리턴
        if(pwd.getText().toString().length() < 7){
            showSuccessOrFailMsg(pwdErrorMsgText,pwdLengthMsg,false);
            return false;
        }

        return true;

    }

    // 로그인 정보 저장
    void saveLoginInfo(String email, String password){
        SharedPreferences loginInfo = getSharedPreferences("loginInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = loginInfo.edit();

        editor.putString("email",email);
        editor.putString("password",password);

        editor.commit();
    }
}
