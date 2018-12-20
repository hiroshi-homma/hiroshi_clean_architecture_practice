/*
 *
 *  MIT License
 *
 *  Copyright (c) 2017 Alibaba Group
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 *
 */

package com.tmall.ultraviewpager

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.Gravity.LEFT
import android.view.Gravity.RIGHT
import android.view.View

/**
 * Created by mikeafc on 15/11/25.
 */
class UltraViewPagerIndicator : View, ViewPager.OnPageChangeListener, IUltraIndicatorBuilder {

    private var viewPager: UltraViewPagerView? = null
    private var pageChangeListener: ViewPager.OnPageChangeListener? = null
    private var scrollState: Int = 0

    //attr for custom
    private var radius: Int = 0
    private var indicatorPadding: Int = 0
    private val animateIndicator: Boolean = false
    private var gravity: Int = 0
    private var orientation: UltraViewPager.Orientation = UltraViewPager.Orientation.HORIZONTAL

    private var marginLeft: Int = 0
    private var marginTop: Int = 0
    private var marginRight: Int = 0
    private var marginBottom: Int = 0
    //for circle
    private var focusColor: Int = 0
    private var normalColor: Int = 0
    //for custom icon
    private var focusBitmap: Bitmap? = null
    private var normalBitmap: Bitmap? = null

    //paint
    private var paintStroke: Paint? = null
    private var paintFill: Paint? = null

    internal var pageOffset: Float = 0.toFloat()
    internal var defaultRadius: Float = 0.toFloat()

    private var indicatorBuildListener: UltraViewPagerIndicatorListener? = null

    private val isDrawResIndicator: Boolean
        get() = focusBitmap != null && normalBitmap != null

    private val itemWidth: Float
        get() {
            if (isDrawResIndicator) {
                return Math.max(focusBitmap!!.width, normalBitmap!!.width).toFloat()
            }
            return if (radius == 0) defaultRadius else radius.toFloat()
        }

    private val itemHeight: Float
        get() {
            if (isDrawResIndicator) {
                return Math.max(focusBitmap!!.height, normalBitmap!!.height).toFloat()
            }
            return if (radius == 0) defaultRadius else radius.toFloat()
        }

    interface UltraViewPagerIndicatorListener {
        fun build()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        paintStroke = Paint(Paint.ANTI_ALIAS_FLAG)
        paintStroke!!.style = Paint.Style.STROKE
        paintFill = Paint(Paint.ANTI_ALIAS_FLAG)
        paintFill!!.style = Paint.Style.FILL
        defaultRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_RADIUS.toFloat(), resources.displayMetrics)
    }

    fun setViewPager(viewPager: UltraViewPagerView) {
        this.viewPager = viewPager
//        this.viewPager!!.setOnPageChangeListener(this)
    }

    @SuppressLint("RtlHardcoded")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (viewPager == null || viewPager!!.adapter == null)
            return

        val count = (viewPager!!.adapter as UltraViewPagerAdapter).realCount
        if (count == 0)
            return

        val longSize: Int
        val shortSize: Int

        val longPaddingBefore: Int
        val longPaddingAfter: Int
        val shortPaddingBefore: Int
        val shortPaddingAfter: Int
        if (orientation === UltraViewPager.Orientation.HORIZONTAL) {
            longSize = viewPager!!.width
            shortSize = viewPager!!.height
            longPaddingBefore = paddingLeft + marginLeft
            longPaddingAfter = paddingRight + marginRight
            shortPaddingBefore = paddingTop + marginTop
            shortPaddingAfter = paintStroke!!.strokeWidth.toInt() + paddingBottom + marginBottom
        } else {
            longSize = viewPager!!.height
            shortSize = viewPager!!.width
            longPaddingBefore = paddingTop + marginTop
            longPaddingAfter = paintStroke!!.strokeWidth.toInt() + paddingBottom + marginBottom
            shortPaddingBefore = paddingLeft + marginLeft
            shortPaddingAfter = paddingRight + marginRight
        }

        val itemWidth = itemWidth
        val widthRatio = if (isDrawResIndicator) 1 else 2 //bitmap resource X1 : circle  X2
        if (indicatorPadding == 0) {
            indicatorPadding = itemWidth.toInt()
        }

        var shortOffset = shortPaddingBefore.toFloat()
        var longOffset = longPaddingBefore.toFloat()

        val indicatorLength = (count - 1) * (itemWidth * widthRatio + indicatorPadding)

        val horizontalGravityMask = gravity and Gravity.HORIZONTAL_GRAVITY_MASK
        val verticalGravityMask = gravity and Gravity.VERTICAL_GRAVITY_MASK
        when (horizontalGravityMask) {
            Gravity.CENTER_HORIZONTAL -> longOffset = (longSize.toFloat() - longPaddingBefore.toFloat() - longPaddingAfter.toFloat() - indicatorLength) / 2.0f
            RIGHT -> {
                if (orientation === UltraViewPager.Orientation.HORIZONTAL) {
                    longOffset = longSize.toFloat() - longPaddingAfter.toFloat() - indicatorLength - itemWidth
                }
                if (orientation === UltraViewPager.Orientation.VERTICAL) {
                    shortOffset = shortSize.toFloat() - shortPaddingAfter.toFloat() - itemWidth
                }
            }
            LEFT -> longOffset += itemWidth
            else -> {
            }
        }

        when (verticalGravityMask) {
            Gravity.CENTER_VERTICAL -> shortOffset = (shortSize.toFloat() - shortPaddingAfter.toFloat() - shortPaddingBefore.toFloat() - itemWidth) / 2
            Gravity.BOTTOM -> {
                if (orientation === UltraViewPager.Orientation.HORIZONTAL) {
                    shortOffset = shortSize.toFloat() - shortPaddingAfter.toFloat() - itemHeight
                }
                if (orientation === UltraViewPager.Orientation.VERTICAL) {
                    longOffset = longSize.toFloat() - longPaddingAfter.toFloat() - indicatorLength
                }
            }
            Gravity.TOP -> shortOffset += itemWidth
            else -> {
            }
        }

        if (horizontalGravityMask == Gravity.CENTER_HORIZONTAL && verticalGravityMask == Gravity.CENTER_VERTICAL) {
            shortOffset = (shortSize.toFloat() - shortPaddingAfter.toFloat() - shortPaddingBefore.toFloat() - itemWidth) / 2
        }

        var dX: Float
        var dY: Float

        var pageFillRadius = radius.toFloat()
        if (paintStroke!!.strokeWidth > 0) {
            pageFillRadius -= paintStroke!!.strokeWidth / 2.0f
        }

        //Draw stroked circles
        for (iLoop in 0 until count) {
            val drawLong = longOffset + iLoop * (itemWidth * widthRatio + indicatorPadding)

            if (orientation === UltraViewPager.Orientation.HORIZONTAL) {
                dX = drawLong
                dY = shortOffset
            } else {
                dX = shortOffset
                dY = drawLong
            }

            if (isDrawResIndicator) {
                if (iLoop == viewPager!!.currentItem)
                    continue
                canvas.drawBitmap(normalBitmap!!, dX, dY, paintFill)
            } else {
                // Only paint fill if not completely transparent
                if (paintFill!!.alpha > 0) {
                    paintFill!!.color = normalColor
                    canvas.drawCircle(dX, dY, pageFillRadius, paintFill!!)
                }

                // Only paint stroke if a stroke width was non-zero
                if (pageFillRadius != radius.toFloat()) {
                    canvas.drawCircle(dX, dY, radius.toFloat(), paintStroke!!)
                }
            }
        }

        //Draw the filled circle according to the current scroll
        var cx = viewPager!!.currentItem * (itemWidth * widthRatio + indicatorPadding)
        if (animateIndicator)
            cx += pageOffset * itemWidth
        if (orientation === UltraViewPager.Orientation.HORIZONTAL) {
            dX = longOffset + cx
            dY = shortOffset
        } else {
            dX = shortOffset
            dY = longOffset + cx
        }

        if (isDrawResIndicator) {
            canvas.drawBitmap(focusBitmap!!, dX, dY, paintStroke)
        } else {
            paintFill!!.color = focusColor
            canvas.drawCircle(dX, dY, radius.toFloat(), paintFill!!)
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        pageOffset = positionOffset
        invalidate()

        if (pageChangeListener != null) {
            pageChangeListener!!.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }
    }

    override fun onPageSelected(position: Int) {
        if (scrollState == ViewPager.SCROLL_STATE_IDLE) {
            invalidate()
        }

        if (pageChangeListener != null) {
            pageChangeListener!!.onPageSelected(position)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        scrollState = state

        if (pageChangeListener != null) {
            pageChangeListener!!.onPageScrollStateChanged(state)
        }
    }

    override fun setOrientation(orientation: UltraViewPager.Orientation): IUltraIndicatorBuilder {
        this.orientation = orientation
        return this
    }

    override fun setRadius(radius: Int): IUltraIndicatorBuilder {
        this.radius = radius
        return this
    }

    override fun setIndicatorPadding(indicatorPadding: Int): IUltraIndicatorBuilder {
        this.indicatorPadding = indicatorPadding
        return this
    }

    override fun setFocusColor(focusColor: Int): IUltraIndicatorBuilder {
        this.focusColor = focusColor
        return this
    }

    override fun setNormalColor(normalColor: Int): IUltraIndicatorBuilder {
        this.normalColor = normalColor
        return this
    }

    override fun setStrokeColor(strokeColor: Int): IUltraIndicatorBuilder {
        paintStroke!!.color = strokeColor
        return this
    }

    override fun setStrokeWidth(strokeWidth: Int): IUltraIndicatorBuilder {
        paintStroke!!.strokeWidth = strokeWidth.toFloat()
        return this
    }

    override fun setGravity(gravity: Int): IUltraIndicatorBuilder {
        this.gravity = gravity
        return this
    }

    override fun setFocusResId(focusResId: Int): IUltraIndicatorBuilder {
        try {
            focusBitmap = BitmapFactory.decodeResource(resources, focusResId)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return this
    }

    override fun setNormalResId(normalResId: Int): IUltraIndicatorBuilder {
        try {
            normalBitmap = BitmapFactory.decodeResource(resources, normalResId)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return this
    }

    override fun setFocusIcon(bitmap: Bitmap): IUltraIndicatorBuilder {
        focusBitmap = bitmap
        return this
    }

    override fun setNormalIcon(bitmap: Bitmap): IUltraIndicatorBuilder {
        normalBitmap = bitmap
        return this
    }

    override fun setMargin(left: Int, top: Int, right: Int, bottom: Int): IUltraIndicatorBuilder {
        marginLeft = left
        marginTop = top
        marginRight = right
        marginBottom = bottom
        return this
    }

    override fun build() {
        if (indicatorBuildListener != null) {
            indicatorBuildListener!!.build()
        }
    }

    fun setPageChangeListener(pageChangeListener: ViewPager.OnPageChangeListener) {
        this.pageChangeListener = pageChangeListener
    }

    fun setIndicatorBuildListener(listener: UltraViewPagerIndicatorListener) {
        this.indicatorBuildListener = listener
    }

    companion object {

        //default
        private val DEFAULT_RADIUS = 3
    }
}
