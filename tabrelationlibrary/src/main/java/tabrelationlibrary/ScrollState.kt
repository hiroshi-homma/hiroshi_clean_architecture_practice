package tabrelationlibrary

interface ScrollState {
    companion object {
        val SCROLL_STATE_IDLE = 0
        val SCROLL_STATE_DRAGGING = 1
        val SCROLL_STATE_SETTLING = 2
    }
}
