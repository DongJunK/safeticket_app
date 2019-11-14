package com.example.safeticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {
    LinearLayout userInfoLayout;
    ImageView imageIcon;
    TextView infoTypeText;
    TextView issueNumberText;
    TextView issuerText;
    TextView issueDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        userInfoLayout = (LinearLayout) findViewById(R.id.userInfoLayout);
        imageIcon = (ImageView) findViewById(R.id.imageIcon);
        infoTypeText = (TextView) findViewById(R.id.infoTypeText);
        issueNumberText = (TextView) findViewById(R.id.issueNumberText);
        issuerText = (TextView) findViewById(R.id.issuerText);
        issueDateText = (TextView) findViewById(R.id.issueDateText);

        Bundle extras = getIntent().getExtras();

        String infoType = extras.getString("infoType");
        infoTypeText.setText(infoType);
        issueNumberText.setText(extras.getString("issueNumber"));
        issuerText.setText(extras.getString("issuer"));
        issueDateText.setText(extras.getString("issueDate"));

        if(infoType.equals("주민등록증"))
        {
            userInfoLayout.setBackgroundResource(R.drawable.ticket_bg_gray);
            userInfoLayout.getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP);
            imageIcon.setImageResource(R.drawable.student_ico);
            imageIcon.setColorFilter(Color.WHITE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
