package com.example.hellotoast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // Declare member variables here:

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView userNameMsg = (TextView) findViewById(R.id.username_msg);

        String nameSentBack = data.getStringExtra("username");
        String title = getString(R.string.the_username_was);

        userNameMsg.setText(title + " "+ nameSentBack);
    }

    public void onGetNameClicked(View view) {
        Intent intent = new Intent(this, SecondScreen.class);
        final int result = 1;
        intent.putExtra("callingActivity", "MainActivity");
        startActivityForResult(intent, result);

    }
}
