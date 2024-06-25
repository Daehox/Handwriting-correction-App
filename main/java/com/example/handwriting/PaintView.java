package com.example.handwritingg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class PaintView extends View {

    //이미지 크기에 대한 상수와 픽셀 선언
    private static final int IMAGE_SIZE = 28;
    private static final int PIXEL_COUNT = IMAGE_SIZE * IMAGE_SIZE;

    //페인트뷰에서 사용할 객체 선언
    private Path path;
    private Paint paint;
    //경로 동기화를 위한 락 객체
    //하나의 path 객체에 대해 하나의 스레드만 접근 가능 하도록 함
    //
    private final Object pathLock = new Object();

    //생성자를 통해 초기 설정 지정
    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //새로운 path 경로 선언
        path = new Path(); 

        //새로운 paint 선언 , 이미지를 부드럽게 해줌 , 스타일 밑 크기 지정 코드
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(35);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //pathLook 객체를 사용하여 동기화 수행
        //여러 스레드가 동시에 path에 접근을 방지
        synchronized (pathLock) {
            canvas.drawPath(path, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //사용자가 터치한 x,y 좌표를 가져옴
        float x = event.getX();
        float y = event.getY();

        // pathLock 객체를 사용하여 동기화 블록 시작
        synchronized (pathLock) {
            // 터치 이벤트의 종류에 따라 처리
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                //터치가 시작될 때의 동작 => 경로의 시작 위치를 {x,y}로 설정
                    path.moveTo(x, y);
                    return true; //이벤트가 처리 되었음을 알림
                case MotionEvent.ACTION_MOVE:
                //터치가 움직일 때의 동작 => 현재 위치 (x,y)까지 경로를 확장 
                    path.lineTo(x, y);
                    break;
                default:
                    return false;
            }
        }
        //뷰를 다시 그리도록 설정
        invalidate();
        return true;
    }

    //캔버스 초기화 메서드
    public void clearCanvas() {
        synchronized (pathLock) {
            path.reset();
        }
        invalidate();
    }

    //페인트뷰의 내용을 비트맵으로 변환하여, OpenCV를 사용해 전처리 한 후 float 배열로 변환
    public float[] getPixelData() {
        //뷰의 너비와 높이를 가져옴
        int width = getWidth();
        int height = getHeight();
        // 가장 작은 값 선택하여 최소 크기 지정
        int minDimension = Math.min(width, height);

        //최소 크기로 비트맵을 생성, 구성은 Config.ARGB_8888 로 설정
        Bitmap bitmap = Bitmap.createBitmap(minDimension, minDimension, Bitmap.Config.ARGB_8888);
        //비트맵에 그리기 위한 캔버스 설정
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);

        //새로운 mat 객체 생성, Mat = OPenCV에서 사용되는 기본 이미지 저장 클래스
        Mat mat = new Mat();
        //비트 Mat을 객체로 변환
        Utils.bitmapToMat(bitmap, mat);

        //객체 색상 변환
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
        //이진화 처리 => 픽셀 값을 이진화 하여, 이미지의 경계를 명확히
        //이진화를 통해 흑백 이미지로 변환 ,THRESH_OTSU + THRESH_BINARY_INV 사용
        Imgproc.threshold(mat, mat, 0, 255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);
        //Mat 객체 크기 조절
        Mat resizedMat = new Mat();
        Imgproc.resize(mat, resizedMat, new Size(IMAGE_SIZE, IMAGE_SIZE));

        //Mat 객체를 1차원 float 배열로 변환
        float[] inputData = new float[PIXEL_COUNT];
        for (int i = 0; i < IMAGE_SIZE; i++) {
            for (int j = 0; j < IMAGE_SIZE; j++) {
                //Mat 객체의 값을 0-1 범위로 변환하여 배열에 저장
                //픽셀 값을 255.0 으로 나누어 0~1사이의 범위로 정규화 => 이를 통해서 이미지 데이터를 머신러닝 모델에 입력할수 있는 형태로 변환
                inputData[i * IMAGE_SIZE + j] = (float) resizedMat.get(i, j)[0] / 255.0f;
            }
        }

        //비트맵을 재활용 하여 메모리를 해제
        bitmap.recycle();
        //mat 객체가 할당하는 메모리를 해제
        mat.release();
        //크키가 조정 된 mat 객체 메모리 해제
        resizedMat.release();

        return inputData;
    }
}