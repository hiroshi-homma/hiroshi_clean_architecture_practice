package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.entity

import hiroshi_homma.homma_create.hiroshi_create_android.core.extension.empty
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.modeldata.Details

data class DetailsEntity(private val id: Int,
                         private val title: String,
                         private val poster: String,
                         private val summary: String,
                         private val cast: String,
                         private val director: String,
                         private val year: Int,
                         private val trailer: String) {

    companion object {
        fun empty() = DetailsEntity(0, String.empty(), String.empty(), String.empty(),
                String.empty(), String.empty(), 0, String.empty())
    }

    fun toDetails() = Details(id, title, poster, summary, cast, director, year, trailer)
}
