package tabrelationlibrary.buildins.commonnavigator.abs

import android.content.Context
import android.database.DataSetObservable
import android.database.DataSetObserver

abstract class CommonNavigatorAdapter {

    private val mDataSetObservable = DataSetObservable()

    abstract val count: Int

    abstract fun getTitleView(context: Context, index: Int): IPagerTitleView

    abstract fun getIndicator(context: Context): IPagerIndicator

    fun getTitleWeight(): Float {
        return 1f
    }

    fun registerDataSetObserver(observer: DataSetObserver) {
        mDataSetObservable.registerObserver(observer)
    }

    fun unregisterDataSetObserver(observer: DataSetObserver) {
        mDataSetObservable.unregisterObserver(observer)
    }

    fun notifyDataSetChanged() {
        mDataSetObservable.notifyChanged()
    }

    fun notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated()
    }
}
