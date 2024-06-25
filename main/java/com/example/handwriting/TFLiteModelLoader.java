package com.example.handwritingg;

import android.content.Context;
import org.tensorflow.lite.Interpreter;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.io.IOException;

public class TFLiteModelLoader {
    //interpreter 객체 선언
    private Interpreter interpreter;

    //생성자 = 모델 파일을 로드
    public TFLiteModelLoader(Context context) throws IOException {
        MappedByteBuffer model = loadModelFile(context, "model1.tflite");
        //inertpretet 객체를 통해 모델의 추론을 수행 할수 있게 해줌
        interpreter = new Interpreter(model);
    }

    //모델 파일 로드 메서드
    private MappedByteBuffer loadModelFile(Context context, String modelName) throws IOException {
        //예외 처리를 통해서 FileInputStream 을 생성 , 파일을 자동으로 닫아 메모리 누수 방지
        try (FileInputStream inputStream = new FileInputStream(context.getAssets().openFd(modelName).getFileDescriptor())) {
            //파일을 가져옴
            FileChannel fileChannel = inputStream.getChannel();
            //모델 파일의 시작 오프셋(데이터가 저장된 위치)을 가져옴
            long startOffset = context.getAssets().openFd(modelName).getStartOffset();
            //모델 파일의 길이를 가져옴
            long declaredLength = context.getAssets().openFd(modelName).getDeclaredLength();
            //파일 채널을 읽기 전용 모드로 매핑 MappedByteBuffer 객체를 반환
            //안정성, 선능, 일관성을 위해 읽기 전용 사용
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        }
    }

    //추론을 수행하는 메서드
    public float[] doInference(float[][][][] inputData) {
        //추론 결과를 저장할 배열 생성 ,[1] = 배치 크기(한번에 처리하는 데이터 수) ,[19] = 모델 출력 크기
        float[][] outputData = new float[1][19];
        //추론 수행 후 데이터 저장
        interpreter.run(inputData, outputData);
        //추론 후 결과 출력
        return outputData[0];
    }
}