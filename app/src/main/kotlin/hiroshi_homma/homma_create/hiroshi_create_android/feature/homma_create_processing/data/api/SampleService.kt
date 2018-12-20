package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.api

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampleService
@Inject constructor(retrofit: Retrofit) : SampleApi {
    private val sampleApi by lazy { retrofit.create(SampleApi::class.java) }

    override fun top() = sampleApi.top()
    override fun details(movieId: Int) = sampleApi.details(movieId)
}
