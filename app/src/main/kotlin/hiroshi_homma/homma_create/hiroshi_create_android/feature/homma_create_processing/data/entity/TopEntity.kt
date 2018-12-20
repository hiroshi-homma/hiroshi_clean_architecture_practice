package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.entity

import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.modeldata.Top

data class TopEntity(private val id: Int, private val poster: String) {
    fun toTop() = Top(id, poster)
}
