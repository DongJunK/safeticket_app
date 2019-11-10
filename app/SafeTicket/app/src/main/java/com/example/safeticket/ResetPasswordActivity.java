package com.example.safeticket;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.safeticket.Interfaces.RequestToServer;

import org.json.JSONException;
import org.json.JSONObject;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText pwdEdit;
    EditText pwdCheckEdit;
    TextView pwdText;
    TextView pwdCheckText;
    TextView failMessageText;
    Button okButton;
    String id; // 입력받은 아이디
    String name; // 입력받은 이름

    String pwdLengthMsg = "비밀번호는 7자리 이상 가능합니다";
    String notEquelPwdMsg = "비밀번호가 일치하지 않습니다";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);

        pwdEdit = (EditText)findViewById(R.id.pwdEdit);
        pwdCheckEdit = (EditText)findViewById(R.id.pwdCheckEdit);
        pwdText = (TextView)findViewById(R.id.pwdText);
        pwdCheckText = (TextView)findViewById(R.id.pwdCheckText);
        okButton = (Button)findViewById(R.id.okButton);
        failMessageText = (TextView)findViewById(R.id.failMessageText);

        Bundle extras = getIntent().getExtras();
        id = extras.getString("id");
        name = extras.getString("name");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPwd(pwdEdit,pwdCheckEdit)) {
                    resetPassword(id, name, pwdEdit.getText().toString());
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    // 비밀번호 유효성 체크
    boolean checkPwd(EditText pwd, EditText pwdCheck){
        // 비밀번호가 동일하지 않으면 false 리턴
        if(!pwd.getText().toString().equals(pwdCheck.getText().toString())){
            showFailMsg(failMessageText,notEquelPwdMsg);
            return false;
        }
        // 비밀번호가 7자리가 안되면 false 리턴
        if(pwd.getText().toString().length() < 7){
            showFailMsg(failMessageText,pwdLengthMsg);
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

    public boolean resetPassword(String email, String name,String password){
        JSONObject res_obj;
        RequestToServer reqToServer = new RequestToServer(); // 서버 요청 클래스
        JSONObject req_json = new JSONObject(); // req body json
        boolean responseMsg = false; // res message

        try {
            req_json.put("email" ,email);
            req_json.put("name", name);
            req_json.put("password",password);

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
}
