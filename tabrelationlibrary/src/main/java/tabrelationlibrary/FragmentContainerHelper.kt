package tabrelationlibrary

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.os.Build
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator

import tabrelationlibrary.buildins.commonnavigator.model.PositionData

import java.util.ArrayList

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
class FragmentContainerHelper {
    private val mTabRelationLibrary = ArrayList<TabRelationLibrary>()
    private var mScrollAnimator: ValueAnimator? = null
    private var mLastSelectedIndex: Int = 0
    private var mDuration = 150
    private var mInterpolator: Interpolator = AccelerateDecelerateInterpolator()

    private val mAnimatorListener = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            dispatchPageScrollStateChanged(ScrollState.SCROLL_STATE_IDLE)
            mScrollAnimator = null
        }
    }

    private val mAnimatorUpdateListener = ValueAnimator.AnimatorUpdateListener { animation ->
        val positionOffsetSum = animation.animatedValue as Float
        var position = positionOffsetSum.toInt()
        var positionOffset = positionOffsetSum - position
        if (positionOffsetSum < 0) {
            position = position - 1
            positionOffset = 1.0f + positionOffset
        }
        dispatchPageScrolled(position, positionOffset, 0)
    }

    constructor() {}

    constructor(tabRelationLibrary: TabRelationLibrary) {
        mTabRelationLibrary.add(tabRelationLibrary)
    }

    @JvmOverloads
    fun handlePageSelected(selectedIndex: Int, smooth: Boolean = true) {
        if (mLastSelectedIndex == selectedIndex) {
            return
        }
        if (smooth) {
            if (mScrollAnimator == null || !mScrollAnimator!!.isRunning) {
                dispatchPageScrollStateChanged(ScrollState.SCROLL_STATE_SETTLING)
            }
            dispatchPageSelected(selectedIndex)
            var currentPositionOffsetSum = mLastSelectedIndex.toFloat()
            if (mScrollAnimator != null) {
                currentPositionOffsetSum = mScrollAnimator!!.animatedValue as Float
                mScrollAnimator!!.cancel()
                mScrollAnimator = null
            }
            mScrollAnimator = ValueAnimator()
            mScrollAnimator!!.setFloatValues(currentPositionOffsetSum, selectedIndex.toFloat())
            mScrollAnimator!!.addUpdateListener(mAnimatorUpdateListener)
            mScrollAnimator!!.addListener(mAnimatorListener)
            mScrollAnimator!!.interpolator = mInterpolator
            mScrollAnimator!!.duration = mDuration.toLong()
            mScrollAnimator!!.start()
        } else {
            dispatchPageSelected(selectedIndex)
            if (mScrollAnimator != null && mScrollAnimator!!.isRunning) {
                dispatchPageScrolled(mLastSelectedIndex, 0.0f, 0)
            }
            dispatchPageScrollStateChanged(ScrollState.SCROLL_STATE_IDLE)
            dispatchPageScrolled(selectedIndex, 0.0f, 0)
        }
        mLastSelectedIndex = selectedIndex
    }

    fun setDuration(duration: Int) {
        mDuration = duration
    }

    fun setInterpolator(interpolator: Interpolator?) {
        if (interpolator == null) {
            mInterpolator = AccelerateDecelerateInterpolator()
        } else {
            mInterpolator = interpolator
        }
    }

    fun attachTabRelationLibrary(tabRelationLibrary: TabRelationLibrary) {
        mTabRelationLibrary.add(tabRelationLibrary)
    }

    private fun dispatchPageSelected(pageIndex: Int) {
        for (tabRelationLibrary in mTabRelationLibrary) {
            tabRelationLibrary.onPageSelected(pageIndex)
        }
    }

    private fun dispatchPageScrollStateChanged(state: Int) {
        for (tabRelationLibrary in mTabRelationLibrary) {
            tabRelationLibrary.onPageScrollStateChanged(state)
        }
    }

    private fun dispatchPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        for (tabRelationLibrary in mTabRelationLibrary) {
            tabRelationLibrary.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }
    }

    companion object {

        fun getImitativePositionData(positionDataList: List<PositionData>, index: Int): PositionData {
            if (index >= 0 && index <= positionDataList.size - 1) {
                return positionDataList[index]
            } else {
                val result = PositionData()
                val referenceData: PositionData
                val offset: Int
                if (index < 0) {
                    offset = index
                    referenceData = positionDataList[0]
                } else {
                    offset = index - positionDataList.size + 1
                    referenceData = positionDataList[positionDataList.size - 1]
                }
                result.mLeft = referenceData.mLeft + offset * referenceData.width()
                result.mTop = referenceData.mTop
                result.mRight = referenceData.mRight + offset * referenceData.width()
                result.mBottom = referenceData.mBottom
                result.mContentLeft = referenceData.mContentLeft + offset * referenceData.width()
                result.mContentTop = referenceData.mContentTop
                result.mContentRight = referenceData.mContentRight + offset * referenceData.width()
                result.mContentBottom = referenceData.mContentBottom
                return result
            }
        }
    }
}
