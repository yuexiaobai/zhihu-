package com.ipcmessenger.example.yuejianchao.zhihu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.ipcmessenger.example.yuejianchao.zhihu.R;

/**
 * Created by yuejianchao on 16-2-25.
 */
public class CircleImageView extends View {
    //图片
    private Bitmap mSrc;
    //宽度
    private int mWidth;
    //高度
    private int mHeight;

    public CircleImageView(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
        TypedArray a=context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleImageView,defStyle,0);
        int n=a.getIndexCount();
        for(int i=0;i<n;i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.CircleImageView_src:
                    mSrc = BitmapFactory.decodeResource(getResources(),
                            a.getResourceId(attr, 0));
                    break;
            }
        }
        a.recycle();
    }

    public CircleImageView(Context context,AttributeSet attrs){
        this(context, attrs, 0);
    }

    public CircleImageView(Context context){
        this(context,null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specMode=MeasureSpec.getMode(widthMeasureSpec);
        int specSize=MeasureSpec.getSize(widthMeasureSpec);
        if(specMode==MeasureSpec.EXACTLY){
            mWidth=specSize;
        }else{
            // 由图片决定的宽
            int desireByImg = getPaddingLeft() + getPaddingRight()
                    + mSrc.getWidth();
            if(specMode==MeasureSpec.AT_MOST){
                mWidth=Math.min(desireByImg,specSize);
            }else {
                mWidth=desireByImg;
            }
        }

        specMode=MeasureSpec.getMode(heightMeasureSpec);
        specSize=MeasureSpec.getSize(heightMeasureSpec);
        if(specMode==MeasureSpec.EXACTLY){
            mHeight=specSize;
        }else{
            int desireByImg=getPaddingBottom()+getPaddingTop()+mSrc.getHeight();
            if(specMode==MeasureSpec.AT_MOST){
                mHeight=Math.min(desireByImg,specSize);
            }else{
                mHeight=desireByImg;
            }
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int min=Math.min(mWidth, mHeight);
        mSrc = Bitmap.createScaledBitmap(mSrc, min, min, false);
        canvas.drawBitmap(createCircleImage(mSrc, min), 0, 0, null);
    }

    /**
     * 根据原图和变长绘制圆形图片
     *
     * @param source
     * @param min
     * @return
     */
    private Bitmap createCircleImage(Bitmap source, int min)
    {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /**
         * 首先绘制圆形
         */
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        /**
         * 使用SRC_IN，参考上面的说明
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 绘制图片
         */
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }
}
