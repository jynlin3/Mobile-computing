package com.example.hellotoast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class SecondScreen extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.second_layout);

        Intent intent = getIntent();

        String previousActivity = intent.getExtras().getString("callingActivity");

        TextView msg = (TextView)findViewById(R.id.textview_callingactivity);

        msg.append(" "+previousActivity);
    }

    public void onSendUserName(View view) {
        EditText userNameET = (EditText) findViewById(R.id.editText_username);
        String username = String.valueOf(userNameET.getText());

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        setResult(RESULT_OK, intent);
        finish();
    }
}
