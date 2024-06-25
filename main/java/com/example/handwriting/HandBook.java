package com.example.handwritingg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class HandBook extends AppCompatActivity {
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activitiy_handbook);

            ImageView imageView = findViewById(R.id.imageView);


 // Glide를 사용하여 GIF를 가져오는 코드
        // Glide는 안드로이드용 이미지 로딩 라이브러리로, 이미지 및 GIF를 표기 할수 있게 해줌
            Glide.with(this)
                    .asGif()
                    .load(R.drawable.avisualimage)
                    .into(imageView);

        }

        //뒤로 가기 버튼
        public void back(View view) {
            Intent it = new Intent(this, StartMain.class);
            startActivity(it);
            finish();
        }
    }
