package com.example.safeticket;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

public class RegisterUserInfoDialog {
    private Context context;

    public RegisterUserInfoDialog(Context context) {
        this.context = context;
    }

    public void callFunction() {

        final Dialog dlg = new Dialog(context);

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.dialog_registeruserinfo);

        dlg.show();

        final Button backButton = (Button) dlg.findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
    }
}
