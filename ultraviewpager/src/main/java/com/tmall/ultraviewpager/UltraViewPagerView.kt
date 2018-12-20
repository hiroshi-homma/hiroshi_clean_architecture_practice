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
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

import com.tmall.ultraviewpager.transformer.UltraVerticalTransformer

/**
 * Created by mikeafc on 15/11/25.
 */
class UltraViewPagerView : ViewPager, UltraViewPagerAdapter.UltraViewPagerCenterListener {

    private var pagerAdapter: UltraViewPagerAdapter? = null

    //Internal state to schedule a new measurement pass.
    private var needsMeasurePage: Boolean = false

    private var multiScrRatio = java.lang.Float.NaN
    private var enableLoop: Boolean = false
    private var autoMeasureHeight: Boolean = false
    private var itemRatio = java.lang.Double.NaN
    var constrainLength: Int = 0
        private set

    private var itemMarginLeft: Int = 0
    private var itemMarginTop: Int = 0
    private var itemMarginRight: Int = 0
    private var itemMarginBottom: Int = 0

    var ratio = java.lang.Float.NaN

    var scrollMode: UltraViewPager.ScrollMode = UltraViewPager.ScrollMode.HORIZONTAL
        set(scrollMode) {
            field = scrollMode
            if (scrollMode == UltraViewPager.ScrollMode.VERTICAL)
                setPageTransformer(false, UltraVerticalTransformer())
        }

    val nextItem: Int
        get() {
            if (pagerAdapter!!.count != 0) {
                val next = super.getCurrentItem() + 1
                return next % pagerAdapter!!.realCount
            }
            return 0
        }

    /**
     * Get the currently selected page.
     */
    val currentItemFake: Int
        get() = super.getCurrentItem()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        clipChildren = false
        overScrollMode = View.OVER_SCROLL_NEVER
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        onMeasurePage(widthMeasureSpec, heightMeasureSpec)
    }

    protected fun onMeasurePage(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        @Suppress("NAME_SHADOWING")
        var heightMeasureSpec = heightMeasureSpec
        if (pagerAdapter == null) {
            return
        }

        Log.d("check_position",currentItem.toString())
        var child: View? = pagerAdapter!!.getViewAtPosition(currentItem)
        if (child == null) {
            child = getChildAt(0)
        }
        if (child == null) {
            return
        }
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view.paddingLeft != itemMarginLeft ||
                    view.paddingTop != itemMarginTop ||
                    view.paddingRight != itemMarginRight ||
                    view.paddingBottom != itemMarginBottom) {
                view.setPadding(itemMarginLeft, itemMarginTop, itemMarginRight, itemMarginBottom)
            }
        }

        val lp = child.layoutParams
        val childWidthSpec = ViewGroup.getChildMeasureSpec(widthMeasureSpec, 0, lp.width)
        val childHeightSpec = ViewGroup.getChildMeasureSpec(heightMeasureSpec, 0, lp.height)

        var childWidth = ((View.MeasureSpec.getSize(childWidthSpec) - paddingLeft - paddingRight) * pagerAdapter!!.getPageWidth(currentItem)).toInt()
        var childHeight = View.MeasureSpec.getSize(childHeightSpec) - paddingTop - paddingBottom

        if (!needsMeasurePage || childWidth == 0 && childHeight == 0) {
            return
        }

        if (!java.lang.Double.isNaN(itemRatio)) {
            val itemHeight = (childWidth / itemRatio).toInt()
            var i = 0
            val childCount = childCount
            while (i < childCount) {
                val view = getChildAt(i)
                view.measure(View.MeasureSpec.makeMeasureSpec(childWidth, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(itemHeight, View.MeasureSpec.EXACTLY))
                i++
            }
        } else {
            var i = 0
            val childCount = childCount
            while (i < childCount) {
                val view = getChildAt(i)
                if (pagerAdapter!!.getPageWidth(currentItem) != 1f) {
                    view.measure(View.MeasureSpec.makeMeasureSpec(childWidth, View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                } else {
                    view.measure(childWidthSpec, childHeightSpec)
                }
                i++
            }
        }

        val isHorizontalScroll = this.scrollMode == UltraViewPager.ScrollMode.HORIZONTAL

        childWidth = itemMarginLeft + child.measuredWidth + itemMarginRight
        childHeight = itemMarginTop + child.measuredHeight + itemMarginBottom

        if (!java.lang.Float.isNaN(ratio)) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((measuredWidth / ratio).toInt(), View.MeasureSpec.EXACTLY)
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
            var i = 0
            val childCount = childCount
            while (i < childCount) {
                val view = getChildAt(i)
                view.measure(View.MeasureSpec.makeMeasureSpec(childWidth, View.MeasureSpec.EXACTLY), heightMeasureSpec)
                i++
            }
        } else {
            if (autoMeasureHeight) {
                if (isHorizontalScroll) {
                    constrainLength = View.MeasureSpec.makeMeasureSpec(childHeight, View.MeasureSpec.EXACTLY)
                    setMeasuredDimension(measuredWidth, childHeight)
                } else {
                    constrainLength = View.MeasureSpec.makeMeasureSpec(childWidth, View.MeasureSpec.EXACTLY)
                    setMeasuredDimension(childWidth, measuredHeight)
                }

                needsMeasurePage = childHeight == itemMarginTop + itemMarginBottom
            }
        }

        if (!pagerAdapter!!.isEnableMultiScr) {
            return
        }

        val pageLength = if (isHorizontalScroll) measuredWidth else measuredHeight

        val childLength = if (isHorizontalScroll) child.measuredWidth else child.measuredHeight

        // Check that the measurement was successful
        if (childLength > 0) {
            needsMeasurePage = false
            val difference = pageLength - childLength
            if (pageMargin == 0) {
                pageMargin = -difference
            }
            val offscreen = Math.ceil((pageLength.toFloat() / childLength.toFloat()).toDouble()).toInt() + 1
            offscreenPageLimit = offscreen
            requestLayout()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Schedule a new measurement pass as the dimensions have changed
        needsMeasurePage = true
    }

    override fun setAdapter(adapter: PagerAdapter?) {
        if (adapter != null) {
            if (pagerAdapter == null || pagerAdapter!!.adapter !== adapter) {
                pagerAdapter = UltraViewPagerAdapter(adapter)
                pagerAdapter!!.setCenterListener(this)
                pagerAdapter!!.isEnableLoop = enableLoop
                pagerAdapter!!.setMultiScrRatio(multiScrRatio)
                needsMeasurePage = true
                constrainLength = 0
                super.setAdapter(pagerAdapter)
            }
        } else {
            super.setAdapter(adapter)
        }
    }

    override fun setCurrentItem(item: Int) {
        setCurrentItem(item, false)
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        @Suppress("NAME_SHADOWING")
        var item = item
        if (pagerAdapter!!.count != 0 && pagerAdapter!!.isEnableLoop) {
            item = pagerAdapter!!.count / 2 + item % pagerAdapter!!.realCount
        }
        super.setCurrentItem(item, smoothScroll)
    }

    override fun getCurrentItem(): Int {
        if (pagerAdapter != null && pagerAdapter!!.count != 0) {
            val position = super.getCurrentItem()
            return position % pagerAdapter!!.realCount
        }
        return super.getCurrentItem()
    }

    /**
     * Set the currently selected page.
     *
     * @param item         Item index to select
     * @param smoothScroll True to smoothly scroll to the new item, false to transition immediately
     */
    fun setCurrentItemFake(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(item, smoothScroll)
    }

    fun setMultiScreen(ratio: Float) {
        multiScrRatio = ratio
        if (pagerAdapter != null) {
            pagerAdapter!!.setMultiScrRatio(ratio)
            needsMeasurePage = true
        }
        val pageMargin = (1 - ratio) * resources.displayMetrics.widthPixels
        if (this.scrollMode == UltraViewPager.ScrollMode.VERTICAL) {
            setPageMargin(pageMargin.toInt())
        } else {
            setPageMargin((-pageMargin).toInt())
        }
    }

    fun setEnableLoop(status: Boolean) {
        enableLoop = status
        if (pagerAdapter != null) {
            pagerAdapter!!.isEnableLoop = enableLoop
        }
    }

    fun setItemRatio(itemRatio: Double) {
        this.itemRatio = itemRatio
    }

    fun setAutoMeasureHeight(autoMeasureHeight: Boolean) {
        this.autoMeasureHeight = autoMeasureHeight
    }

    fun setItemMargin(left: Int, top: Int, right: Int, bottom: Int) {
        itemMarginLeft = left
        itemMarginTop = top
        itemMarginRight = right
        itemMarginBottom = bottom
    }

    override fun center() {
        currentItem = 0
    }

    override fun resetPosition() {
        currentItem = currentItem
    }

    private fun swapTouchEvent(event: MotionEvent): MotionEvent {
        val width = width.toFloat()
        val height = height.toFloat()

        val swappedX = event.y / height * width
        val swappedY = event.x / width * height

        event.setLocation(swappedX, swappedY)

        return event
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (this.scrollMode == UltraViewPager.ScrollMode.VERTICAL) {
            val intercept = super.onInterceptTouchEvent(swapTouchEvent(ev))
            //If not intercept, touch event should not be swapped.
            swapTouchEvent(ev)
            return intercept
        }
        return super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return if (this.scrollMode == UltraViewPager.ScrollMode.VERTICAL) super.onTouchEvent(swapTouchEvent(ev)) else super.onTouchEvent(ev)
    }
}
