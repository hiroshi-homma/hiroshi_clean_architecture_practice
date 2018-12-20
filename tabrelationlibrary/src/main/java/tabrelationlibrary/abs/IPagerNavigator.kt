package tabrelationlibrary.abs

interface IPagerNavigator {
    fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)
    fun onPageSelected(position: Int)
    fun onPageScrollStateChanged(state: Int)
    fun onAttachToTabRelationLibrary()
    fun onDetachFromTabRelationLibrary()
    fun notifyDataSetChanged()
}
