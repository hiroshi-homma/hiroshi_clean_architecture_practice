package tabrelationlibrary.buildins.commonnavigator.abs


import tabrelationlibrary.buildins.commonnavigator.model.PositionData

interface IPagerIndicator {
    fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)

    fun onPageSelected(position: Int)

    fun onPageScrollStateChanged(state: Int)

    fun onPositionDataProvide(dataList: List<PositionData>)
}
