package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.api

import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.entity.DetailsEntity
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.entity.TopEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

internal interface SampleApi {
    companion object {
        private const val PARAM_ID = "movieId"
        private const val TOP = "movies.json"
        private const val DETAILS = "movie_0{$PARAM_ID}.json"
    }

    @GET(TOP) fun top(): Call<List<TopEntity>>
    @GET(DETAILS) fun details(@Path(PARAM_ID) movieId: Int): Call<DetailsEntity>
}
