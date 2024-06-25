package com.example.handwritingg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mainstart);

        // 시스템 바 영역 패딩 설정
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // "i" 버튼 클릭 이벤트 처리
        Button infoButton = findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다이얼로그 생성
                AlertDialog.Builder builder = new AlertDialog.Builder(StartMain.this);
                builder.setTitle("앱 정보");
                builder.setMessage("제작자: 돼지와 함께 춤을\n\n" +
                        "앱 이름: 한글 교정 앱\n" +
                        "출시일: 2024.05.30\n" +
                        "개발 언어: Java\n" +
                        "개발 환경: Android Studio Iguana | 2023.2.1\n" +
                        "이메일: Dancingwithapig@yeongsung.ac.kr");

                // 확인 버튼 추가 및 클릭 리스너 설정
                builder.setPositiveButton("확인", (dialog, which) -> {
                    dialog.dismiss();
                });

                // 다이얼로그 표시
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // 시작 버튼 클릭 이벤트 처리
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartMain.this, StartSub.class);
                startActivity(intent);
            }
        });
    }

    public void handbook(View view) {
        // PaintView.class로의 이동을 위한 Intent 생성
        Intent it = new Intent(this, HandBook.class);
        startActivity(it); // PaintView.class로 이동
        finish();
    }
}