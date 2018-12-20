package tabrelationlibrary.buildins.commonnavigator.abs

interface IPagerTitleView {
    fun onSelected(index: Int, totalCount: Int)

    fun onDeselected(index: Int, totalCount: Int)

    fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean)

    fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean)
}
