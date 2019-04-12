package com.office.pc25245.waterview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by pc252 on 2019/4/11.
 */

public class WaterView extends View {
    private final int fillColor = 0xff999999;// 填充颜色
    private Paint paint;
    private int width = 100, height = 300;// 默认宽高
    /* 两个圆心的最大距离 */
    private int distance = 60;
    /* 当前两个圆心的距离 */
    private float currentDis = 0;
    private float bigRadius = 20;// 大圆半径
    private float smallRadius = 10;// 小圆半径
    private float controlX1, controlX2, controlY1, controlY2;// 两个控制点的坐标
    private float leftX, leftY, rightX, rightY;// 大圆两边的两个点的坐标
    private float leftX2, leftY2, rightX2, rightY2; // 小圆两边的两个坐标
    DrawFilter drawFilter;
    Path path;
    /* 由属性动画控制，范围为0-1 */
    float fraction = 0;// 比例值
    /* 大圆半径变化的比例 */
    private final int bigPercent = 8;
    /* 小圆半径变化的比例 */
    private final int smallPercent = 20;
    // 动画的执行时间
    private long duration = 3000;
    private static int ScreenWidth = 0;
    private static int ScreenHeoght = 0;

    public WaterView(Context context) {
        super(context);
        init();
    }

    public WaterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#a0aaa0"));
        drawFilter = new PaintFlagsDrawFilter(Paint.ANTI_ALIAS_FLAG, Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        path = new Path();
        canvas.translate(ScreenWidth / 2, ScreenHeoght / 2);
        canvas.setDrawFilter(drawFilter);
        // 当前的两个圆心的距离
        currentDis = distance * fraction;
        // 计算当前大圆的半径
        float bigRadius = this.bigRadius - currentDis / bigPercent;
        Log.d("zhangrui", "currentDis=" + currentDis + "//bigPercent=" + bigPercent);
        float smallRadius = 0;
        if (currentDis > 5) {// 距离大于5才改变小圆的半径
            smallRadius = this.smallRadius - currentDis / smallPercent;
        }
        // 大圆两边的两个点坐标
        leftX = -bigRadius;// 大圆当前的半径
        leftY = rightY = 0;
        rightX = bigRadius;// 大圆当前的半径
        // 小圆两边的两个点坐标
        leftX2 = -smallRadius;// 小圆当前的半径
        leftY2 = rightY2 = currentDis;
        rightX2 = -leftX2; // 小圆当前的半径
        // 两个控制点
        controlX1 = -smallRadius;// x坐标取小圆当前的半径大小
        controlY1 = currentDis / 2;// y坐标取两个圆距离的一半
        controlX2 = smallRadius;// x坐标取小圆当前的半径大小
        controlY2 = currentDis / 2;// y坐标取两个圆距离的一半
        path.moveTo(leftX, leftY);
        path.lineTo(rightX, rightY);
        // 用二阶贝塞尔曲线画右边的曲线，参数的第一个点是右边的一个控制点
        path.quadTo(controlX2, controlY2, rightX2, rightY2);
        path.lineTo(leftX2, leftY2);
        // 用二阶贝塞尔曲线画左边边的曲线，参数的第一个点是左边的一个控制点
        path.quadTo(controlX1, controlY1, leftX, leftY);
        // 画大圆
        canvas.drawCircle(0, 0, bigRadius, paint);
        // 画小圆
        canvas.drawCircle(0, currentDis, smallRadius, paint);
        // 画path
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ScreenWidth = w;
        ScreenHeoght = h;
        Log.d("zhangrui", "w=" + w + "//h=" + h);
    }

    /*** 执行属性动画，实现水滴的效果 */
    public void perforAnim() {
        ValueAnimator valAnimator = ObjectAnimator.ofFloat(0, 1);
        valAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fraction = (float) animation.getAnimatedValue();
                postInvalidate();
            }

        });
        valAnimator.setDuration(duration);
        valAnimator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float TranslateXer = Math.abs(event.getX() - ScreenWidth / 2);
                float TranslateYer = Math.abs(event.getY() - ScreenHeoght / 2);
                fraction = (float) Math.sqrt(Math.pow(TranslateXer, 2) + Math.pow(TranslateYer, 2)) / distance;
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                fraction = 0;
                postInvalidate();
                break;
        }
        return /*super.onTouchEvent(event)*/true;
    }
}
