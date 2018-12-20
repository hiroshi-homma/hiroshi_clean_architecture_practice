package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.modeldata

import hiroshi_homma.homma_create.hiroshi_create_android.core.extension.empty

data class Top(val id: Int, val poster: String) {
    companion object {
        fun empty() = Top(0, String.empty())
    }
}
