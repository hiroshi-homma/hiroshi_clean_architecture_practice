package tabrelationlibrary

import android.support.v4.view.ViewPager

object ViewPagerHelper {
    fun bind(tabrelationlibrary: TabRelationLibrary, viewPager: ViewPager) {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                tabrelationlibrary.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                tabrelationlibrary.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                tabrelationlibrary.onPageScrollStateChanged(state)
            }
        })
    }
}
