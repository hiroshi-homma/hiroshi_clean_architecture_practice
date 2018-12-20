package tabrelationlibrary.buildins.commonnavigator.titles

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextUtils
import android.view.Gravity
import android.widget.TextView

import tabrelationlibrary.buildins.UIUtil
import tabrelationlibrary.buildins.commonnavigator.abs.IMeasurablePagerTitleView

open class SimplePagerTitleView(context: Context) : android.support.v7.widget.AppCompatTextView(context, null), IMeasurablePagerTitleView {
    var selectedColor: Int = 0
    var normalColor: Int = 0

    override val contentLeft: Int
        get() {
            val bound = Rect()
            paint.getTextBounds(text.toString(), 0, text.length, bound)
            val contentWidth = bound.width()
            return left + width / 2 - contentWidth / 2
        }

    override val contentTop: Int
        get() {
            val metrics = paint.fontMetrics
            val contentHeight = metrics.bottom - metrics.top
            return (height / 2 - contentHeight / 2).toInt()
        }

    override val contentRight: Int
        get() {
            val bound = Rect()
            paint.getTextBounds(text.toString(), 0, text.length, bound)
            val contentWidth = bound.width()
            return left + width / 2 + contentWidth / 2
        }

    override val contentBottom: Int
        get() {
            val metrics = paint.fontMetrics
            val contentHeight = metrics.bottom - metrics.top
            return (height / 2 + contentHeight / 2).toInt()
        }

    init {
        init(context)
    }

    private fun init(context: Context) {
        gravity = Gravity.CENTER
        val padding = UIUtil.dip2px(context, 10.0)
        setPadding(padding, 0, padding, 0)
        setSingleLine()
        ellipsize = TextUtils.TruncateAt.END
    }

    override fun onSelected(index: Int, totalCount: Int) {
        setTextColor(selectedColor)
    }

    override fun onDeselected(index: Int, totalCount: Int) {
        setTextColor(normalColor)
    }

    override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {}

    override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {}
}
