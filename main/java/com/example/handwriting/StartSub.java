package com.example.handwritingg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class StartSub extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startsub);

        // MainActivity로 이동하는 버튼
        Button buttonToMainActivity = findViewById(R.id.buttonToMainActivity);
        buttonToMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartSub.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // MainActivity2로 이동하는 버튼
        Button buttonToMainActivity2 = findViewById(R.id.buttonToMainActivity2);
        buttonToMainActivity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartSub.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }
}