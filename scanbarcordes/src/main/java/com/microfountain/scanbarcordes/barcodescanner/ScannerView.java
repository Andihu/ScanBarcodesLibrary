package com.microfountain.scanbarcordes.barcodescanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.microfountain.scanbarcordes.R;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Copyright (C), 2015-2021
 * FileName: ScannerView
 * Author: hujian
 * Date: 2021/5/27 16:30
 * History:
 * <author> <time> <version> <desc>
 */
public class ScannerView extends View implements LifecycleObserver {

    boolean running = true;

    private int mCurrentY;

    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                if (mCurrentY < startY * 3) {
                    mCurrentY += 1;
                } else {
                    mCurrentY = startY;
                }
                invalidate();
            }
        }
    };
    private Bitmap bitmap;
    private int bitmapHeight;

    public ScannerView(Context context) {
        this(context, null);
    }

    public ScannerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ScannerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public ScannerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(10);
        linePaint.setColor(Color.BLUE);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_saoyisao_qrline);
        bitmapHeight = bitmap.getHeight();
    }

    private float mWidth;

    private float mHeight;

    private int startY;

    private Paint linePaint = new Paint();

    private List<RectF> mRect = new ArrayList<>();

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        startY = (int) (mHeight / 4);
        mCurrentY = startY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (running) {
            drawDrawable(canvas);
        }
    }


    private void drawDrawable(Canvas canvas) {
        canvas.save();
        canvas.translate(0, mCurrentY - bitmapHeight);
        Rect rect = new Rect(0, 0, (int) mWidth, 150);
        Rect dst = new Rect(0, 0, (int) mWidth, 150);
        canvas.drawBitmap(bitmap, null, dst, new Paint());
        canvas.restore();
    }

    void drawLine(Canvas canvas) {
        canvas.save();
        canvas.translate(0, mCurrentY);
        canvas.drawLine(0, 0, mWidth, 0, linePaint);
        canvas.restore();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void startMove() {
        running = true;
        new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(5);
                    handler.sendEmptyMessage(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void stop() {
        running = false;
    }
}
