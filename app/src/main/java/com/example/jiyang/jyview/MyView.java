package com.example.jiyang.jyview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by jiyang on 16-12-25.
 * 自定义View
 */

public class MyView extends View {
    private int[] mColors = {Color.BLUE, Color.DKGRAY, Color.CYAN, Color.RED, Color.GREEN};
    private Paint paint;    //画笔
    private ArrayList<ViewData> viewDatas;    //数据集
    private int w;          //View宽高
    private int h;
    private RectF rectF;    //矩形

    public MyView(Context context) {
        super(context);
        initPaint();    //设置画笔
    }

    //设置数据
    public void setData(ArrayList<ViewData> viewDatas) {
        this.viewDatas = viewDatas;
        initData();     //设置数据的百分度和角度
        invalidate();   //刷新UI
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    //初始化画笔
    private void initPaint() {
        paint = new Paint();
        //设置画笔默认颜色
        paint.setColor(Color.WHITE);
        //设置画笔模式：填充
        paint.setStyle(Paint.Style.FILL);
        //
        paint.setTextSize(30);
        //初始化区域
        rectF = new RectF();
    }

    //确定View大小
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;     //获取宽高
        this.h = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(w / 2, h / 2);             //将画布坐标原点移到中心位置
        float currentStartAngle = 0;                //起始角度
        float r = (float) (Math.min(w, h) / 2);     //饼状图半径(取宽高里最小的值)
        rectF.set(-r, -r, r, r);                    //设置将要用来画扇形的矩形的轮廓
        for (int i = 0; i < viewDatas.size(); i++) {
            ViewData viewData = viewDatas.get(i);
            paint.setColor(viewData.color);
            //绘制扇形(通过绘制圆弧)
            canvas.drawArc(rectF, currentStartAngle, viewData.angle, true, paint);
            //绘制扇形上文字
            float textAngle = currentStartAngle + viewData.angle / 2;    //计算文字位置角度
            paint.setColor(Color.BLACK);
            float x = (float) (r / 2 * Math.cos(textAngle * Math.PI / 180));    //计算文字位置坐标
            float y = (float) (r / 2 * Math.sin(textAngle * Math.PI / 180));
            paint.setColor(Color.YELLOW);        //文字颜色
            canvas.drawText(viewData.name, x, y, paint);    //绘制文字

            currentStartAngle += viewData.angle;     //改变起始角度
        }
    }

    private void initData() {
        if (null == viewDatas || viewDatas.size() == 0) {
            return;
        }

        float sumValue = 0;                 //数值和
        for (int i = 0; i < viewDatas.size(); i++) {
            ViewData viewData = viewDatas.get(i);
            sumValue += viewData.value;
            int j = i % mColors.length;     //设置颜色
            viewData.color = mColors[j];
        }

        for (ViewData data : viewDatas) {
            float percentage = data.value / sumValue;    //计算百分比
            float angle = percentage * 360;           //对应的角度
            data.percentage = percentage;
            data.angle = angle;
        }
    }

}