package com.triton.voxit.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.triton.voxit.R;

public class AudioUpdatePage extends Activity {
    TextView msgTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_update_page);
        initUI();
    }

    private void initUI() {
        msgTv = (TextView) findViewById(R.id.msgTv);
       // String message = "Your audio has been sent for review. Voxit team will revert back to you. Enjoy listening to Voxit audio talks.";

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AudioUpdatePage.this, Dashboard.class)
                .putExtra("flag","BV")
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        finish();
    }
}
