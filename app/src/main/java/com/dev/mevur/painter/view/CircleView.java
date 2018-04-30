package com.dev.mevur.painter.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Mevur on 04/30/18.
 */

public class CircleView extends View {
    private Path path;
    private Path dist;
    private float length;
    private Paint basePaint;
    private Paint topPaint;
    private Paint textPaint;
    private RectF rect;
    private float animationValue;
    private PathMeasure pathMeasure;
    private ValueAnimator animator;

    private float angels = 359;
    private int baseColor = Color.parseColor("#EEEEEE");
    private int topColor = Color.GREEN;
    private String centralText = "";

    private int strokeWidth = 10;

    private int[] colors = {Color.RED, Color.YELLOW, Color.GREEN};

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }


    public CircleView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //the base layer
        canvas.drawArc(rect, -90, 360, false, basePaint);
        //the top layer with animation
        dist.reset();
        dist.lineTo(0, 0);
        float stop = animationValue * length;
        float start = 0;
        pathMeasure.getSegment(start, stop, dist, true);
        canvas.drawPath(dist, topPaint);
        if (animationValue == 1) {
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float top = fontMetrics.top;
            float bottom = fontMetrics.bottom;
            //place the text to the center of view
            int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);

            canvas.drawText(centralText, rect.centerX(), baseLineY, textPaint);
        }
    }

    /**
     * init view
     *
     * @param width  the width of the view
     * @param height the height of the view
     */
    private void init(int width, int height) {
        //init the base painter
        basePaint = new Paint();
        //抗锯齿
        basePaint.setAntiAlias(true);
        //中空图案
        basePaint.setStyle(Paint.Style.STROKE);
        basePaint.setStrokeWidth(strokeWidth);
        basePaint.setColor(baseColor);
        //init the top painter
        topPaint = new Paint();
        //抗锯齿
        topPaint.setAntiAlias(true);
        //中空图案
        topPaint.setStyle(Paint.Style.STROKE);
        topPaint.setStrokeWidth(strokeWidth);
        topPaint.setStrokeCap(Paint.Cap.ROUND);

        float[] positions = {0.6f, 0.8f, 1f};
        SweepGradient sweepGradient = new SweepGradient(width / 2, height / 2, colors, positions);
        Matrix matrix = new Matrix();
        matrix.setRotate(270, width / 2, height / 2);
        sweepGradient.setLocalMatrix(matrix);
        topPaint.setShader(sweepGradient);
        //init the position of circle
        int size = width < height ? width : height;
        size -= strokeWidth * 2;
        float left = (width - size) / 2;
        float top = (height - size) / 2;
        System.out.println(left + " " + top + " " + size);
        rect = new RectF(left, top, size + left, size + top);

        //init path
        path = new Path();
        dist = new Path();
        path.arcTo(rect, -90, angels);
        pathMeasure = new PathMeasure();
        pathMeasure.setPath(path, false);
        length = pathMeasure.getLength();
        //create animation
        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(2000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animationValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
        //text painter
        textPaint = new Paint();
        textPaint.setColor(topColor);
        textPaint.setTextSize(size / 10);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setAngels(float angels) {
        this.angels = angels;
    }

    public void setBaseColor(int baseColor) {
        this.baseColor = baseColor;
    }

    public void setTopColor(int topColor) {
        this.topColor = topColor;
    }

    public void setCentralText(String centralText) {
        this.centralText = centralText;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    /**
     * replay the animation
     */
    public void load() {
        animator.start();
    }
}
