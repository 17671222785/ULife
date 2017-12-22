package com.andywang.ulife.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by parting_soul on 2016/10/4.
 * 自定义圆形图片控件
 */

public class CircleImageView extends android.support.v7.widget.AppCompatImageView {

    private static final String TAG = "CircleImageView";
    /**
     * 圆形控件的半径
     */
    private int mRadius;

    /**
     * 画笔
     */
    private Paint mPaint;

    public CircleImageView(Context context) {
        super(context);
        Log.i(TAG, "CircleImageView:  1");
        init();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG, "CircleImageView:  2");
        init();
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.i(TAG, "CircleImageView:  3");
        init();
    }

    /**
     * 初始化画笔
     */
    public void init() {
        mPaint = new Paint();
    }

    /**
     * 重写该方法，得到圆形控件的半径，并设置该半径大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //得到用户设置的宽度
        int width = getMeasuredWidth();
        //得到用户设置的高度
        int height = getMeasuredHeight();

        //高度和宽度取小的一方的一半为半径
        if (width < height) {
            mRadius = width / 2;
        } else {
            mRadius = height / 2;
        }

        //设置图片UI的宽和高
        setMeasuredDimension(mRadius * 2, mRadius * 2);
    }

    /**
     * 将drawable转化为Bitmap
     *
     * @param drawable
     * @return Bitmap 转化后的位图
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        //如果drawable可以向下造型为BitmapDrawable,直接返回BitmapDrawable的Bitmap
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        //得到drawable的宽度和高度
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        //创建一个与drawable大小相同的位图
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //将位图放入画布
        Canvas canvas = new Canvas(bitmap);

        //指定一个可以绘制的边框
        drawable.setBounds(0, 0, mRadius * 2, mRadius * 2);

        //绘制到画布上
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * 绘制图片UI,注意千万不能实现父类的onDraw
     *
     * @param canvas 画板
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //得到UI的绘制对象
        Bitmap bitmap = drawableToBitmap(getDrawable());
        //通过Bitmap和指定x,y方向的平铺方式构造出BitmapShader对象
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //设置渲染器
        mPaint.setShader(shader);
        //在画布上画出圆形控件
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
    }

}
