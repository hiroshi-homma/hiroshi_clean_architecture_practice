package tabrelationlibrary.buildins.commonnavigator.indicators

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

import tabrelationlibrary.FragmentContainerHelper
import tabrelationlibrary.buildins.ArgbEvaluatorHolder
import tabrelationlibrary.buildins.UIUtil
import tabrelationlibrary.buildins.commonnavigator.abs.IPagerIndicator
import tabrelationlibrary.buildins.commonnavigator.model.PositionData

import java.util.Arrays

class LinePagerIndicator(context: Context) : View(context), IPagerIndicator {

    var mode: Int = 0
        set(mode) = if (mode == MODE_EXACTLY || mode == MODE_MATCH_EDGE || mode == MODE_WRAP_CONTENT) {
            field = mode
        } else {
            throw IllegalArgumentException("mode $mode not supported.")
        }

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

    var yOffset: Float = 0.toFloat()
    var lineHeight: Float = 0.toFloat()
    var xOffset: Float = 0.toFloat()
    var lineWidth: Float = 0.toFloat()
    var roundRadius: Float = 0.toFloat()

    var paint: Paint? = null
        private set
    private var mPositionDataList: List<PositionData>? = null
    var colors: List<Int>? = null
        private set

    private val mLineRect = RectF()

    init {
        init(context)
    }

    private fun init(context: Context) {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint!!.style = Paint.Style.FILL
        lineHeight = UIUtil.dip2px(context, 3.0).toFloat()
        lineWidth = UIUtil.dip2px(context, 10.0).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(mLineRect, roundRadius, roundRadius, paint!!)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (mPositionDataList == null || mPositionDataList!!.isEmpty()) {
            return
        }

        if (colors != null && colors!!.size > 0) {
            val currentColor = colors!![Math.abs(position) % colors!!.size]
            val nextColor = colors!![Math.abs(position + 1) % colors!!.size]
            val color = ArgbEvaluatorHolder.eval(positionOffset, currentColor, nextColor)
            paint!!.color = color
        }

        val current = FragmentContainerHelper.getImitativePositionData(mPositionDataList!!, position)
        val next = FragmentContainerHelper.getImitativePositionData(mPositionDataList!!, position + 1)

        val leftX: Float
        val nextLeftX: Float
        val rightX: Float
        val nextRightX: Float
        if (mode == MODE_MATCH_EDGE) {
            leftX = current.mLeft + xOffset
            nextLeftX = next.mLeft + xOffset
            rightX = current.mRight - xOffset
            nextRightX = next.mRight - xOffset
        } else if (mode == MODE_WRAP_CONTENT) {
            leftX = current.mContentLeft + xOffset
            nextLeftX = next.mContentLeft + xOffset
            rightX = current.mContentRight - xOffset
            nextRightX = next.mContentRight - xOffset
        } else {
            leftX = current.mLeft + (current.width() - lineWidth) / 2
            nextLeftX = next.mLeft + (next.width() - lineWidth) / 2
            rightX = current.mLeft + (current.width() + lineWidth) / 2
            nextRightX = next.mLeft + (next.width() + lineWidth) / 2
        }

        mLineRect.left = leftX + (nextLeftX - leftX) * startInterpolator!!.getInterpolation(positionOffset)
        mLineRect.right = rightX + (nextRightX - rightX) * endInterpolator!!.getInterpolation(positionOffset)
        mLineRect.top = height.toFloat() - lineHeight - yOffset
        mLineRect.bottom = height - yOffset

        invalidate()
    }

    override fun onPageSelected(position: Int) {}

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPositionDataProvide(dataList: List<PositionData>) {
        mPositionDataList = dataList
    }

//    fun setColors(vararg colors: Int) {
//        this.colors = Arrays.asList(*colors)
//    }

    companion object {
        val MODE_MATCH_EDGE = 0
        val MODE_WRAP_CONTENT = 1
        val MODE_EXACTLY = 2
    }
}
