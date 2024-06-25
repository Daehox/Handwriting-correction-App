package com.example.handwritingg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.opencv.android.OpenCVLoader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView;

public class MainActivity2 extends AppCompatActivity {

    // 사용할 객체 선언
    private PaintView paintView;
    private Button clearButton, resultButton;
    private TFLiteModelLoader2 modelLoader2;
    private TextView resultTextView;
    private List<String> hangulList;

    // 이미지 크기에 대한 상수와 픽셀 설정
    private static final int IMAGE_SIZE = 28;
    private static final int PIXEL_COUNT = IMAGE_SIZE * IMAGE_SIZE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // OpenCV 초기화
        if (!OpenCVLoader.initDebug()) {
            resultTextView.setText("OpenCV 초기화 실패");
            return;
        }

        // 스피너 설정
        // 한글 자음, 모음 교본 이미지 중 원하는 이미지를 선택 가능
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.spinner_items2)) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextSize(25); // 스피너에 표시되는 텍스트 크기 설정
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextSize(15); // 드롭다운 목록에 표시되는 텍스트 크기 설정
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // 객체 초기화
        paintView = findViewById(R.id.paintView);
        clearButton = findViewById(R.id.clearButton);
        resultButton = findViewById(R.id.resultButton);
        resultTextView = findViewById(R.id.resultTextView);

        // 모델을 연결하기 위한 예외 처리 추가
        try {
            modelLoader2 = new TFLiteModelLoader2(this);
            hangulList = loadHangulList(); // 파일에서 한글 리스트를 읽어와 저장
        } catch (IOException e) {
            e.printStackTrace();
            resultTextView.setText("모델 로드 중 오류 발생");
        }

        // Clear 버튼 클릭 이벤트 설정
        clearButton.setOnClickListener(v -> paintView.clearCanvas());

        // Result 버튼 클릭 이벤트 설정
        // TensorFlow Lite 모델이 이미지를 처리하기 위해서는 float 형식의 데이터가 필요함
        resultButton.setOnClickListener(v -> {
            float[] inputData = paintView.getPixelData(); // PaintView에서 픽셀 데이터를 가져옴
            performInference(inputData); // 추론 수행
        });

        // Spinner 항목 변경 시 이벤트 처리
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                // 스피너 항목에 따라 배경 설정
                switch (selectedItem) {
                    case "ㅏ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_001);
                        break;
                    case "ㅑ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_002);
                        break;
                    case "ㅓ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_003);
                        break;
                    case "ㅕ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_004);
                        break;
                    case "ㅗ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_005);
                        break;
                    case "ㅛ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_006);
                        break;
                    case "ㅜ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_007);
                        break;
                    case "ㅠ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_008);
                        break;
                    case "ㅡ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_009);
                        break;
                    case "ㅣ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_010);
                        break;
                    case "ㅐ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_011);
                        break;
                    case "ㅒ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_012);
                        break;
                    case "ㅔ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_013);
                        break;
                    case "ㅖ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_014);
                        break;
                    case "ㅘ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_015);
                        break;
                    case "ㅙ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_016);
                        break;
                    case "ㅚ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_017);
                        break;
                    case "ㅝ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_018);
                        break;
                    case "ㅞ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_019);
                        break;
                    case "ㅟ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_020);
                        break;
                    case "ㅢ":
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.vowel_021);
                        break;
                    default:
                        paintView.clearCanvas();
                        paintView.setBackgroundResource(R.drawable.grid); // 기본 배경
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무것도 선택되지 않을 때 처리 기능 추가 가능
            }
        });
    }

    // 한글 리스트 로드 메서드
    private List<String> loadHangulList() throws IOException {
        List<String> list = new ArrayList<>();
        // 입력 스트림을 사용하여 raw 리소스 파일을 열음 > hangul 파일이 읽힘
        try (InputStream inputStream = getResources().openRawResource(R.raw.hangul2);
             // 입력 스트림을 버퍼 리더로 감싸서 데이터를 효율적으로 사용 가능
             // InputStream을 통해서 문자 스트림으로 변환
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // trim을 사용해 앞뒤 공백 제거
                list.add(line.trim());
            }
        }
        return list;
    }

    // 추론 수행 메서드
    private void performInference(final float[] inputData) {
        new Thread(() -> {
            try {
                // 4차원 배열로 선언
                // TensorFlow Lite 모델이 요구하는 입력 형식이 4차원 배열 [batch_size, height, width, channels] 이기 때문
                float[][][][] transformedInput = transformInputData(inputData);
                // 변환된 입력 데이터를 사용하여 모델 추론 수행 => float 배열로 반환됨
                final float[] results = modelLoader2.doInference(transformedInput);
                // getPredictedClass를 통해 results 배열에서 가장 높은 확률을 가지는 요소의 인덱스를 찾아서 반환 => 예측된 한글 문자 반환
                final int predictedClass = getPredictedClass(results);
                final float similarityScore = results[predictedClass] * 100; // 퍼센트로 설정
                // 예측된 클래스에 해당하는 한글 문자를 리스트에 가져옴, predictedHangul을 사용해 predictedClass 안에서 해당 한글 문자를 가져옴
                final String predictedHangul = hangulList.get(predictedClass);

                // UI 업데이트
                runOnUiThread(() -> resultTextView.setText(
                        String.format("예측된 글자: %s\n정확도: %.2f%%", predictedHangul, similarityScore)));
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> resultTextView.setText("추론 중 오류: " + e.getMessage()));
            }
        }).start();
    }

    // 예측 클래스 변환 메서드
    // 추론 결과 배열에서 가장 높은 확률 값을 가지는 클래스 인덱스를 반환
    private int getPredictedClass(float[] results) {
        int maxIndex = 0; // 최대 확률 값을 가지는 인덱스 초기화
        for (int i = 1; i < results.length; i++) {
            // 현재 인덱스의 값이 최대 인덱스의 값보다 크면 최대 인덱스를 현재 인덱스로 갱신
            if (results[i] > results[maxIndex]) {
                maxIndex = i;
            }
        }
        // 최대 확률 값을 가지는 인덱스를 반환
        return maxIndex;
    }

    // 입력 데이터 변환 메서드
    // 평탄화된 1차원 배열을 4차원 배열 [batch_size, height, width, channels]로 변환
    private float[][][][] transformInputData(float[] flatInput) {
        // TensorFlow Lite 모델은 입력 데이터를 4차원 배열 형식으로 받기 때문에 변환이 필요
        // batch_size = 한 번에 처리할 입력 데이터 개수, height, width = 이미지 높이와 너비 설정, channels = 채널 수를 의미, 흑백은 1, 컬러일 경우 3
        float[][][][] input = new float[1][IMAGE_SIZE][IMAGE_SIZE][1];
        for (int i = 0; i < IMAGE_SIZE; i++) {
            for (int j = 0; j < IMAGE_SIZE; j++) {
                // 평탄화된 1차원 배열을 4차원 배열로 변환
                input[0][i][j][0] = flatInput[i * IMAGE_SIZE + j];
            }
        }
        // 변환된 4차원 배열 반환
        return input;
    }

    // 뒤로 가기 버튼
    public void back(View view) {
        Intent it = new Intent(this, StartMain.class);
        startActivity(it);
        finish();
    }
}
