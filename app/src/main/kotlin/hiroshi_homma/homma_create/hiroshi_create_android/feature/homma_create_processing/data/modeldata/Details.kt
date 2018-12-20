package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.modeldata

import hiroshi_homma.homma_create.hiroshi_create_android.core.extension.empty

data class Details(val id: Int,
                   val title: String,
                   val poster: String,
                   val summary: String,
                   val cast: String,
                   val director: String,
                   val year: Int,
                   val trailer: String) {

    companion object {
        fun empty() = Details(0, String.empty(), String.empty(), String.empty(),
                String.empty(), String.empty(), 0, String.empty())
    }
}


