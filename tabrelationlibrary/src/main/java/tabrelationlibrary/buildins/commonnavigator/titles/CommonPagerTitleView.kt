package tabrelationlibrary.buildins.commonnavigator.titles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

import tabrelationlibrary.buildins.commonnavigator.abs.IMeasurablePagerTitleView

class CommonPagerTitleView(context: Context) : FrameLayout(context), IMeasurablePagerTitleView {
    var onPagerTitleChangeListener: OnPagerTitleChangeListener? = null
    var contentPositionDataProvider: ContentPositionDataProvider? = null

    override val contentLeft: Int
        get() = if (contentPositionDataProvider != null) {
            contentPositionDataProvider!!.contentLeft
        } else left

    override val contentTop: Int
        get() = if (contentPositionDataProvider != null) {
            contentPositionDataProvider!!.contentTop
        } else top

    override val contentRight: Int
        get() = if (contentPositionDataProvider != null) {
            contentPositionDataProvider!!.contentRight
        } else right

    override val contentBottom: Int
        get() = if (contentPositionDataProvider != null) {
            contentPositionDataProvider!!.contentBottom
        } else bottom

    override fun onSelected(index: Int, totalCount: Int) {
        if (onPagerTitleChangeListener != null) {
            onPagerTitleChangeListener!!.onSelected(index, totalCount)
        }
    }

    override fun onDeselected(index: Int, totalCount: Int) {
        if (onPagerTitleChangeListener != null) {
            onPagerTitleChangeListener!!.onDeselected(index, totalCount)
        }
    }

    override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {
        if (onPagerTitleChangeListener != null) {
            onPagerTitleChangeListener!!.onLeave(index, totalCount, leavePercent, leftToRight)
        }
    }

    override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {
        if (onPagerTitleChangeListener != null) {
            onPagerTitleChangeListener!!.onEnter(index, totalCount, enterPercent, leftToRight)
        }
    }

    @JvmOverloads
    fun setContentView(contentView: View?, lp: FrameLayout.LayoutParams? = null) {
        var layoutparam = lp
        removeAllViews()
        if (contentView != null) {
            if (layoutparam == null) {
                layoutparam = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
            addView(contentView, layoutparam)
        }
    }

    fun setContentView(layoutId: Int) {
        val child = LayoutInflater.from(context).inflate(layoutId, null)
        setContentView(child, null)
    }

    interface OnPagerTitleChangeListener {
        fun onSelected(index: Int, totalCount: Int)

        fun onDeselected(index: Int, totalCount: Int)

        fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean)

        fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean)
    }

    interface ContentPositionDataProvider {
        val contentLeft: Int

        val contentTop: Int

        val contentRight: Int

        val contentBottom: Int
    }
}
