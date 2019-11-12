package com.example.safeticket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {
    TextView infoTypeText;
    TextView issueNumberText;
    TextView issuerText;
    TextView issueDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        infoTypeText = (TextView) findViewById(R.id.infoTypeText);
        issueNumberText = (TextView) findViewById(R.id.issueNumberText);
        issuerText = (TextView) findViewById(R.id.issuerText);
        issueDateText = (TextView) findViewById(R.id.issueDateText);

        Bundle extras = getIntent().getExtras();

        infoTypeText.setText(extras.getString("infoType"));
        issueNumberText.setText(extras.getString("issueNumber"));
        issuerText.setText(extras.getString("issuer"));
        issueDateText.setText(extras.getString("issueDate"));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
