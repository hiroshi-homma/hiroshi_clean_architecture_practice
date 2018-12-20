package tabrelationlibrary.buildins.commonnavigator

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout

import tabrelationlibrary.NavigatorHelper
import tabrelationlibrary.R
import tabrelationlibrary.ScrollState
import tabrelationlibrary.abs.IPagerNavigator
import tabrelationlibrary.buildins.commonnavigator.abs.CommonNavigatorAdapter
import tabrelationlibrary.buildins.commonnavigator.abs.IMeasurablePagerTitleView
import tabrelationlibrary.buildins.commonnavigator.abs.IPagerIndicator
import tabrelationlibrary.buildins.commonnavigator.abs.IPagerTitleView
import tabrelationlibrary.buildins.commonnavigator.model.PositionData

import java.util.ArrayList

class CommonNavigator(context: Context) : FrameLayout(context), IPagerNavigator, NavigatorHelper.OnNavigatorScrollListener {
    private var mScrollView: HorizontalScrollView? = null
    private var mNavigatorHelper: NavigatorHelper? = null
    private var mIndicatorContainer: LinearLayout? = null
    private var titleContainer: LinearLayout? = null
    var pagerIndicator: IPagerIndicator? = null
    var adapter: CommonNavigatorAdapter? = null
    set(adapter) {
        if (this.adapter === adapter) {
            return
        }
        if (this.adapter != null) {
            this.adapter!!.unregisterDataSetObserver(mObserver)
        }
        field = adapter
        if (this.adapter != null) {
            this.adapter!!.registerDataSetObserver(mObserver)
            if (mNavigatorHelper != null) {
                mNavigatorHelper!!.totalCount = this.adapter!!.count
            }
            if (titleContainer != null) {
                this.adapter!!.notifyDataSetChanged()
            }
        } else {
            if (mNavigatorHelper != null) {
                mNavigatorHelper!!.totalCount = 0
            }
            init()
        }
    }

    var isAdjustMode: Boolean = false
    var isEnablePivotScroll: Boolean = false
    var scrollPivotX = 0.5f
    var isSmoothScroll = true
    var isFollowTouch = true
    var rightPadding: Int = 0
    var leftPadding: Int = 0
    var isIndicatorOnTop: Boolean = false
    var isSkimOver: Boolean = false
        set(skimOver) {
            field = skimOver
            mNavigatorHelper!!.setSkimOver(skimOver)
        }
    var isReselectWhenLayout = true

    private val mPositionDataList = ArrayList<PositionData>()

    private val mObserver = object : DataSetObserver() {

        override fun onChanged() {
            mNavigatorHelper!!.totalCount = adapter!!.count
            init()
        }

        override fun onInvalidated() {

        }
    }

    init {
        mNavigatorHelper = NavigatorHelper()
        mNavigatorHelper!!.setNavigatorScrollListener(this)
    }

    override fun notifyDataSetChanged() {
        if (adapter != null) {
            adapter!!.notifyDataSetChanged()
        }
    }

    private fun init() {
        removeAllViews()

        val root: View
        if (isAdjustMode) {
            root = LayoutInflater.from(context).inflate(R.layout.pager_navigator_layout_no_scroll, this)
        } else {
            root = LayoutInflater.from(context).inflate(R.layout.pager_navigator_layout, this)
        }

        mScrollView = root.findViewById(R.id.scroll_view)

        titleContainer = root.findViewById(R.id.title_container)
        titleContainer!!.setPadding(leftPadding, 0, rightPadding, 0)

        mIndicatorContainer = root.findViewById(R.id.indicator_container)
        if (isIndicatorOnTop) {
            mIndicatorContainer!!.parent.bringChildToFront(mIndicatorContainer)
        }

        initTitlesAndIndicator()
    }

    private fun initTitlesAndIndicator() {
        var i = 0
        val j = mNavigatorHelper!!.totalCount
        while (i < j) {
            val v = adapter!!.getTitleView(context, i)
            if (v is View) {
                val view = v as View
                val lp: LinearLayout.LayoutParams
                if (isAdjustMode) {
                    lp = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
                    lp.weight = adapter!!.getTitleWeight()
                } else {
                    lp = LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT)
                }
                titleContainer!!.addView(view, lp)
            }
            i++
        }
        if (adapter != null) {
            pagerIndicator = adapter!!.getIndicator(context)
            if (pagerIndicator is View) {
                val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                mIndicatorContainer!!.addView(pagerIndicator as View?, lp)
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (adapter != null) {
            preparePositionData()
            if (pagerIndicator != null) {
                pagerIndicator!!.onPositionDataProvide(mPositionDataList)
            }
            if (isReselectWhenLayout && mNavigatorHelper!!.scrollState == ScrollState.SCROLL_STATE_IDLE) {
                onPageSelected(mNavigatorHelper!!.currentIndex)
                onPageScrolled(mNavigatorHelper!!.currentIndex, 0.0f, 0)
            }
        }
    }

    private fun preparePositionData() {
        mPositionDataList.clear()
        var i = 0
        val j = mNavigatorHelper!!.totalCount
        while (i < j) {
            val data = PositionData()
            val v = titleContainer!!.getChildAt(i)
            if (v != null) {
                data.mLeft = v.left
                data.mTop = v.top
                data.mRight = v.right
                data.mBottom = v.bottom
                if (v is IMeasurablePagerTitleView) {
                    val view = v as IMeasurablePagerTitleView
                    data.mContentLeft = view.contentLeft
                    data.mContentTop = view.contentTop
                    data.mContentRight = view.contentRight
                    data.mContentBottom = view.contentBottom
                } else {
                    data.mContentLeft = data.mLeft
                    data.mContentTop = data.mTop
                    data.mContentRight = data.mRight
                    data.mContentBottom = data.mBottom
                }
            }
            mPositionDataList.add(data)
            i++
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (adapter != null) {

            mNavigatorHelper!!.onPageScrolled(position, positionOffset)
            if (pagerIndicator != null) {
                pagerIndicator!!.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            if (mScrollView != null && mPositionDataList.size > 0 && position >= 0 && position < mPositionDataList.size) {
                if (isFollowTouch) {
                    val currentPosition = Math.min(mPositionDataList.size - 1, position)
                    val nextPosition = Math.min(mPositionDataList.size - 1, position + 1)
                    val current = mPositionDataList[currentPosition]
                    val next = mPositionDataList[nextPosition]
                    val scrollTo = current.horizontalCenter() - mScrollView!!.width * scrollPivotX
                    val nextScrollTo = next.horizontalCenter() - mScrollView!!.width * scrollPivotX
                    mScrollView!!.scrollTo((scrollTo + (nextScrollTo - scrollTo) * positionOffset).toInt(), 0)
                }
            }
        }
    }

    override fun onPageSelected(position: Int) {
        if (adapter != null) {
            mNavigatorHelper!!.onPageSelected(position)
            if (pagerIndicator != null) {
                pagerIndicator!!.onPageSelected(position)
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        if (adapter != null) {
            mNavigatorHelper!!.onPageScrollStateChanged(state)
            if (pagerIndicator != null) {
                pagerIndicator!!.onPageScrollStateChanged(state)
            }
        }
    }

    override fun onAttachToTabRelationLibrary() {
        init()
    }

    override fun onDetachFromTabRelationLibrary() {}

    override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {
        if (titleContainer == null) {
            return
        }
        val v = titleContainer!!.getChildAt(index)
        if (v is IPagerTitleView) {
            (v as IPagerTitleView).onEnter(index, totalCount, enterPercent, leftToRight)
        }
    }

    override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {
        if (titleContainer == null) {
            return
        }
        val v = titleContainer!!.getChildAt(index)
        if (v is IPagerTitleView) {
            (v as IPagerTitleView).onLeave(index, totalCount, leavePercent, leftToRight)
        }
    }

    override fun onSelected(index: Int, totalCount: Int) {
        if (titleContainer == null) {
            return
        }
        val v = titleContainer!!.getChildAt(index)
        if (v is IPagerTitleView) {
            (v as IPagerTitleView).onSelected(index, totalCount)
        }
        if (!isAdjustMode && !isFollowTouch && mScrollView != null && mPositionDataList.size > 0) {
            val currentIndex = Math.min(mPositionDataList.size - 1, index)
            val current = mPositionDataList[currentIndex]
            if (isEnablePivotScroll) {
                val scrollTo = current.horizontalCenter() - mScrollView!!.width * scrollPivotX
                if (isSmoothScroll) {
                    mScrollView!!.smoothScrollTo(scrollTo.toInt(), 0)
                } else {
                    mScrollView!!.scrollTo(scrollTo.toInt(), 0)
                }
            } else {
                if (mScrollView!!.scrollX > current.mLeft) {
                    if (isSmoothScroll) {
                        mScrollView!!.smoothScrollTo(current.mLeft, 0)
                    } else {
                        mScrollView!!.scrollTo(current.mLeft, 0)
                    }
                } else if (mScrollView!!.scrollX + width < current.mRight) {
                    if (isSmoothScroll) {
                        mScrollView!!.smoothScrollTo(current.mRight - width, 0)
                    } else {
                        mScrollView!!.scrollTo(current.mRight - width, 0)
                    }
                }
            }
        }
    }

    override fun onDeselected(index: Int, totalCount: Int) {
        if (titleContainer == null) {
            return
        }
        val v = titleContainer!!.getChildAt(index)
        if (v is IPagerTitleView) {
            (v as IPagerTitleView).onDeselected(index, totalCount)
        }
    }

    fun getPagerTitleView(index: Int): IPagerTitleView? {
        return if (titleContainer == null) {
            null
        } else titleContainer!!.getChildAt(index) as IPagerTitleView
    }
}
