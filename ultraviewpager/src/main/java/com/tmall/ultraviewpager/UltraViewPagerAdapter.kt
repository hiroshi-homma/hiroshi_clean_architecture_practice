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

import android.database.DataSetObserver
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

/**
 * Created by mikeafc on 15/11/25.
 */
class UltraViewPagerAdapter(val adapter: PagerAdapter) : PagerAdapter() {
    var isEnableLoop: Boolean = false
        set(status) {
            field = status
            notifyDataSetChanged()
            if (!status) {
                centerListener!!.resetPosition()
            } else {
            }
        }
    private var multiScrRatio = java.lang.Float.NaN
    private var hasCentered: Boolean = false //ensure that the first item is in the middle when enabling loop-mode
    private var scrWidth: Int = 0
    private var infiniteRatio: Int = 0
    private var centerListener: UltraViewPagerCenterListener? = null

    private val viewArray = SparseArray<View>()

    val realCount: Int
        get() = adapter.count

    val isEnableMultiScr: Boolean
        get() = !java.lang.Float.isNaN(multiScrRatio) && multiScrRatio < 1f

    interface UltraViewPagerCenterListener {
        fun center()

        fun resetPosition()
    }

    init {
        infiniteRatio = INFINITE_RATIO
    }

    override fun getCount(): Int {
        return if (isEnableLoop) {
            if (adapter.count == 0) {
                0
            } else {
                adapter.count * infiniteRatio
            }
        } else {
            adapter.count
        }
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var realPosition = position
        if (isEnableLoop && adapter.count != 0) {
            realPosition = position % adapter.count
        }

        val item = adapter.instantiateItem(container, realPosition)
        var childView: View? = null
        if (item is View)
            childView = item
        if (item is RecyclerView.ViewHolder)
            childView = item.itemView

        val childCount = container.childCount
        for (i in 0 until childCount) {
            val child = container.getChildAt(i)
            if (isViewFromObject(child, item)) {
                viewArray.put(realPosition, child)
                break
            }
        }
        Log.d("viewArray_size",viewArray.size().toString())

        if (isEnableMultiScr) {
            if (scrWidth == 0) {
                scrWidth = container.resources.displayMetrics.widthPixels
            }
            val relativeLayout = RelativeLayout(container.context)

            if (childView!!.layoutParams != null) {
                val layoutParams = RelativeLayout.LayoutParams(
                        (scrWidth * multiScrRatio).toInt(),
                        ViewGroup.LayoutParams.MATCH_PARENT)

                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
                childView.layoutParams = layoutParams
            }

            container.removeView(childView)
            relativeLayout.addView(childView)

            container.addView(relativeLayout)
            return relativeLayout
        }

        return item
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        var realPosition = position

        if (isEnableLoop && adapter.count != 0)
            realPosition = position % adapter.count

        if (isEnableMultiScr && `object` is RelativeLayout) {
            val child = `object`.getChildAt(0)
            `object`.removeAllViews()
            adapter.destroyItem(container, realPosition, child)
        } else {
            adapter.destroyItem(container, realPosition, `object`)
        }

//        viewArray.remove(realPosition)
    }

    fun getViewAtPosition(position: Int): View {
        Log.d("check_viewArray",viewArray.toString())
        return viewArray.get(position)
    }

    override fun finishUpdate(container: ViewGroup) {
        // only need to set the center position  when the loop is enabled
        if (!hasCentered) {
            if (adapter.count in 1..(count - 1)) {
                centerListener!!.center()
            }
        }
        hasCentered = true
        adapter.finishUpdate(container)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return adapter.isViewFromObject(view, `object`)
    }

    override fun restoreState(bundle: Parcelable?, classLoader: ClassLoader?) {
        adapter.restoreState(bundle, classLoader)
    }

    override fun saveState(): Parcelable? {
        return adapter.saveState()
    }

    override fun startUpdate(container: ViewGroup) {
        adapter.startUpdate(container)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val virtualPosition = position % adapter.count
        return adapter.getPageTitle(virtualPosition)
    }

    override fun getPageWidth(position: Int): Float {
        return adapter.getPageWidth(position)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        adapter.setPrimaryItem(container, position, `object`)
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver) {
        adapter.unregisterDataSetObserver(observer)
    }

    override fun registerDataSetObserver(observer: DataSetObserver) {
        adapter.registerDataSetObserver(observer)
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
        adapter.notifyDataSetChanged()
    }

    override fun getItemPosition(`object`: Any): Int {
        return adapter.getItemPosition(`object`)
    }

    fun setMultiScrRatio(ratio: Float) {
        multiScrRatio = ratio
    }

    fun setCenterListener(listener: UltraViewPagerCenterListener) {
        centerListener = listener
    }

    fun setInfiniteRatio(infiniteRatio: Int) {
        this.infiniteRatio = infiniteRatio
    }

    companion object {

        private val INFINITE_RATIO = 400
    }
}
