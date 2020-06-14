package com.romain.cellarv1.outils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.romain.cellarv1.R;


public class CurvedBottomNavigationView extends BottomNavigationView {
    private Path mPath;
    private Paint mPaint;

    /** the CURVE_CIRCLE_RADIUS represent the radius of the fab button */
    private int CURVE_CIRCLE_RADIUS=56 ;


    // the coordinates of the first curve
    private Point mFirstCurveStartPoint = new Point();
    private Point mFirstCurveEndPoint = new Point();
    private Point mFirstCurveControlPoint1 = new Point();
    private Point mFirstCurveControlPoint2 = new Point();

    //the coordinates of the second curve
    @SuppressWarnings("FieldCanBeLocal")
    private Point mSecondCurveStartPoint = new Point();
    private Point mSecondCurveEndPoint = new Point();
    private Point mSecondCurveControlPoint1 = new Point();
    private Point mSecondCurveControlPoint2 = new Point();

    private int mNavigationBarWidth;
    private int mNavigationBarHeight;


    public CurvedBottomNavigationView(Context context) {
        super(context);
        // CURVE_CIRCLE_RADIUS=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, context.getResources().getDisplayMetrics());

        init();
    }

    public CurvedBottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurvedBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // Couleur green_ultra_dark
        mPaint.setColor(getResources().getColor(R.color.green_ultra_dark));
        //mPaint.setColor(Color.rgb(35, 45, 48));
        setBackgroundColor(Color.TRANSPARENT);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // get width and height of navigation bar
        // Navigation bar bounds (width & height)
        mNavigationBarWidth = getWidth();
        mNavigationBarHeight = getHeight();


        // the coordinates (x,y) of the start point before curve
        mFirstCurveStartPoint.set((mNavigationBarWidth / 2) - (90) - (CURVE_CIRCLE_RADIUS / 1), 60);
        // the coordinates (x,y) of the end point after curve
        mFirstCurveEndPoint.set(mNavigationBarWidth / 2, 0);
        // same thing for the second curve
        mSecondCurveStartPoint = mFirstCurveEndPoint;
        mSecondCurveEndPoint.set((mNavigationBarWidth / 2) + (90) + (CURVE_CIRCLE_RADIUS / 1), 60);

        // the coordinates (x,y)  of the 1st control point on a cubic curve
        mFirstCurveControlPoint1.set(mFirstCurveStartPoint.x + (CURVE_CIRCLE_RADIUS + (CURVE_CIRCLE_RADIUS / 3)), mFirstCurveStartPoint.y - 0);
        // the coordinates (x,y)  of the 2nd control point on a cubic curve
        mFirstCurveControlPoint2.set(mFirstCurveEndPoint.x - (CURVE_CIRCLE_RADIUS + (CURVE_CIRCLE_RADIUS / 3)), mFirstCurveEndPoint.y - 0);

        mSecondCurveControlPoint1.set(
                mSecondCurveStartPoint.x + CURVE_CIRCLE_RADIUS,
                //mSecondCurveStartPoint.x + (CURVE_CIRCLE_RADIUS * 2) - CURVE_CIRCLE_RADIUS,
                mSecondCurveStartPoint.y - 0);

        mSecondCurveControlPoint2.set(
                mSecondCurveEndPoint.x - CURVE_CIRCLE_RADIUS,
                //mSecondCurveEndPoint.x - (CURVE_CIRCLE_RADIUS + (CURVE_CIRCLE_RADIUS / 3)),
                mSecondCurveEndPoint.y + 0);

        mPath.reset();
        mPath.moveTo(0, 60);
        mPath.lineTo(mFirstCurveStartPoint.x, mFirstCurveStartPoint.y);

        mPath.cubicTo(mFirstCurveControlPoint1.x, mFirstCurveControlPoint1.y,
                mFirstCurveControlPoint2.x, mFirstCurveControlPoint2.y,
                mFirstCurveEndPoint.x, mFirstCurveEndPoint.y);

        mPath.cubicTo(mSecondCurveControlPoint1.x, mSecondCurveControlPoint1.y,
                mSecondCurveControlPoint2.x, mSecondCurveControlPoint2.y,
                mSecondCurveEndPoint.x, mSecondCurveEndPoint.y);

        mPath.lineTo(mNavigationBarWidth, 60);
        mPath.lineTo(mNavigationBarWidth, mNavigationBarHeight);
        mPath.lineTo(0, mNavigationBarHeight);
        mPath.close();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

}
