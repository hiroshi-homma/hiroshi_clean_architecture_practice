package tabrelationlibrary.buildins.commonnavigator.indicators

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator

import tabrelationlibrary.FragmentContainerHelper
import tabrelationlibrary.buildins.ArgbEvaluatorHolder
import tabrelationlibrary.buildins.UIUtil
import tabrelationlibrary.buildins.commonnavigator.abs.IPagerIndicator
import tabrelationlibrary.buildins.commonnavigator.model.PositionData

import java.util.Arrays

class BezierPagerIndicator(context: Context) : View(context), IPagerIndicator {
    private var mPositionDataList: List<PositionData>? = null

    private var mLeftCircleRadius: Float = 0.toFloat()
    private var mLeftCircleX: Float = 0.toFloat()
    private var mRightCircleRadius: Float = 0.toFloat()
    private var mRightCircleX: Float = 0.toFloat()

    private var mYOffset: Float = 0.toFloat()
    private var mMaxCircleRadius: Float = 0.toFloat()
    private var mMinCircleRadius: Float = 0.toFloat()

    private var mPaint: Paint? = null
    private val mPath = Path()

    private val mColors: List<Int>? = null
    private val mStartInterpolator = AccelerateInterpolator()
    private val mEndInterpolator = DecelerateInterpolator()

    init {
        init(context)
    }

    private fun init(context: Context) {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.style = Paint.Style.FILL
        mMaxCircleRadius = UIUtil.dip2px(context, 3.5).toFloat()
        mMinCircleRadius = UIUtil.dip2px(context, 2.0).toFloat()
        mYOffset = UIUtil.dip2px(context, 1.5).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(mLeftCircleX, height.toFloat() - mYOffset - mMaxCircleRadius, mLeftCircleRadius, mPaint!!)
        canvas.drawCircle(mRightCircleX, height.toFloat() - mYOffset - mMaxCircleRadius, mRightCircleRadius, mPaint!!)
        drawBezierCurve(canvas)
    }

    private fun drawBezierCurve(canvas: Canvas) {
        mPath.reset()
        val y = height.toFloat() - mYOffset - mMaxCircleRadius
        mPath.moveTo(mRightCircleX, y)
        mPath.lineTo(mRightCircleX, y - mRightCircleRadius)
        mPath.quadTo(mRightCircleX + (mLeftCircleX - mRightCircleX) / 2.0f, y, mLeftCircleX, y - mLeftCircleRadius)
        mPath.lineTo(mLeftCircleX, y + mLeftCircleRadius)
        mPath.quadTo(mRightCircleX + (mLeftCircleX - mRightCircleX) / 2.0f, y, mRightCircleX, y + mRightCircleRadius)
        mPath.close()  // 闭合
        canvas.drawPath(mPath, mPaint!!)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (mPositionDataList == null || mPositionDataList!!.isEmpty()) {
            return
        }

        if (mColors != null && mColors.size > 0) {
            val currentColor = mColors[Math.abs(position) % mColors.size]
            val nextColor = mColors[Math.abs(position + 1) % mColors.size]
            val color = ArgbEvaluatorHolder.eval(positionOffset, currentColor, nextColor)
            mPaint!!.color = color
        }

        val current = FragmentContainerHelper.getImitativePositionData(mPositionDataList!!, position)
        val next = FragmentContainerHelper.getImitativePositionData(mPositionDataList!!, position + 1)

        val leftX = (current.mLeft + (current.mRight - current.mLeft) / 2).toFloat()
        val rightX = (next.mLeft + (next.mRight - next.mLeft) / 2).toFloat()

        mLeftCircleX = leftX + (rightX - leftX) * mStartInterpolator.getInterpolation(positionOffset)
        mRightCircleX = leftX + (rightX - leftX) * mEndInterpolator.getInterpolation(positionOffset)
        mLeftCircleRadius = mMaxCircleRadius + (mMinCircleRadius - mMaxCircleRadius) * mEndInterpolator.getInterpolation(positionOffset)
        mRightCircleRadius = mMinCircleRadius + (mMaxCircleRadius - mMinCircleRadius) * mStartInterpolator.getInterpolation(positionOffset)

        invalidate()
    }

    override fun onPageSelected(position: Int) {}

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPositionDataProvide(dataList: List<PositionData>) {
        mPositionDataList = dataList
    }

    //    public float getMaxCircleRadius() {
    //        return mMaxCircleRadius;
    //    }
    //
    //    public void setMaxCircleRadius(float maxCircleRadius) {
    //        mMaxCircleRadius = maxCircleRadius;
    //    }
    //
    //    public float getMinCircleRadius() {
    //        return mMinCircleRadius;
    //    }
    //
    //    public void setMinCircleRadius(float minCircleRadius) {
    //        mMinCircleRadius = minCircleRadius;
    //    }
    //
    //    public float getYOffset() {
    //        return mYOffset;
    //    }
    //
    //    public void setYOffset(float yOffset) {
    //        mYOffset = yOffset;
    //    }
    //
    //    public void setColors(Integer... colors) {
    //        mColors = Arrays.asList(colors);
    //    }
    //
    //    public void setStartInterpolator(Interpolator startInterpolator) {
    //        mStartInterpolator = startInterpolator;
    //        if (mStartInterpolator == null) {
    //            mStartInterpolator = new AccelerateInterpolator();
    //        }
    //    }
    //
    //    public void setEndInterpolator(Interpolator endInterpolator) {
    //        mEndInterpolator = endInterpolator;
    //        if (mEndInterpolator == null) {
    //            mEndInterpolator = new DecelerateInterpolator();
    //        }
    //    }
}
