package tabrelationlibrary.buildins.commonnavigator.indicators

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

import tabrelationlibrary.FragmentContainerHelper
import tabrelationlibrary.buildins.UIUtil
import tabrelationlibrary.buildins.commonnavigator.abs.IPagerIndicator
import tabrelationlibrary.buildins.commonnavigator.model.PositionData

class WrapPagerIndicator(context: Context) : View(context), IPagerIndicator {
    private var verticalPadding: Int = 0
    var horizontalPadding: Int = 0
    var fillColor: Int = 0
    private var mRoundRadius: Float = 0.toFloat()
    var startInterpolator: Interpolator? = LinearInterpolator()
        set(startInterpolator) {
            field = startInterpolator
            if (this.startInterpolator == null) {
                field = LinearInterpolator()
            }
        }
    var endInterpolator: Interpolator? = LinearInterpolator()
        set(endInterpolator) {
            field = endInterpolator
            if (this.endInterpolator == null) {
                field = LinearInterpolator()
            }
        }

    private var mPositionDataList: List<PositionData>? = null
    var paint: Paint? = null
        private set

    private val mRect = RectF()
    private var mRoundRadiusSet: Boolean = false

    var roundRadius: Float
        get() = mRoundRadius
        set(roundRadius) {
            mRoundRadius = roundRadius
            mRoundRadiusSet = true
        }

    init {
        init(context)
    }

    private fun init(context: Context) {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint!!.style = Paint.Style.FILL
        verticalPadding = UIUtil.dip2px(context, 2.0)
        horizontalPadding = UIUtil.dip2px(context, 4.0)
    }

    override fun onDraw(canvas: Canvas) {
        paint!!.color = fillColor
        canvas.drawRoundRect(mRect, mRoundRadius, mRoundRadius, paint!!)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (mPositionDataList == null || mPositionDataList!!.isEmpty()) {
            return
        }

        val current = FragmentContainerHelper.getImitativePositionData(mPositionDataList!!, position)
        val next = FragmentContainerHelper.getImitativePositionData(mPositionDataList!!, position + 1)

        mRect.left = current.mContentLeft - horizontalPadding + (next.mContentLeft - current.mContentLeft) * endInterpolator!!.getInterpolation(positionOffset)
        mRect.top = (current.mContentTop - verticalPadding).toFloat()
        mRect.right = current.mContentRight.toFloat() + horizontalPadding.toFloat() + (next.mContentRight - current.mContentRight) * startInterpolator!!.getInterpolation(positionOffset)
        mRect.bottom = (current.mContentBottom + verticalPadding).toFloat()

        if (!mRoundRadiusSet) {
            mRoundRadius = mRect.height() / 4
        }

        invalidate()
    }

    override fun onPageSelected(position: Int) {}

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPositionDataProvide(dataList: List<PositionData>) {
        mPositionDataList = dataList
    }
}
