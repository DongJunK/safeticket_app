package com.example.safeticket;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class RegisterUserInfoDialog {
    private Context context;

    public RegisterUserInfoDialog(Context context) { this.context = context; }

    public void callFunction()
    {
        final Dialog dlg = new Dialog(context);

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.register_user_dialog);
        dlg.show();

        final Button driverLicenseButton = (Button) dlg.findViewById(R.id.driverLicenseButton);
        final Button registrationCardButton = (Button) dlg.findViewById(R.id.registrationCardButton);

        driverLicenseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                driverLicenseButton.setBackgroundColor(Color.rgb(39, 0, 85));
                Intent intent = new Intent(context, RegisterDriverLicenseActivity.class);
                context.startActivity(intent);
                ((Activity)context).finish();
                dlg.dismiss();
            }
        });

        registrationCardButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                registrationCardButton.setBackgroundColor(Color.rgb(39, 0, 85));
                Intent intent = new Intent(context, RegisterRegistrationActivity.class);
                context.startActivity(intent);
                ((Activity)context).finish();
                dlg.dismiss();
            }
        });
    }
}
