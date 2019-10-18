package com.triton.voxit.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.triton.voxit.R;

public class FacebookPasswordPage extends Activity {

    Intent i;
    EditText email_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebook_password);

        initUI();

    }//end of oncreate

    private void initUI(){
        email_edit = (EditText)findViewById(R.id.email_edit);
        i = getIntent();
        String email = i.getStringExtra("currentemail");
        Log.e("currentemail",email);
        disableEditText(email);
    }

    private void disableEditText(String email) {
        email_edit.setText(email);
        email_edit.setEnabled(false);
        email_edit.setFocusable(false);
        email_edit.setFocusableInTouchMode(false);
    }
}
