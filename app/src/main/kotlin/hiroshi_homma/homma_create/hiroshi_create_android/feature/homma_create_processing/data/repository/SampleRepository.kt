package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.repository

import hiroshi_homma.homma_create.hiroshi_create_android.core.exception.Failure
import hiroshi_homma.homma_create.hiroshi_create_android.core.exception.Failure.NetworkConnection
import hiroshi_homma.homma_create.hiroshi_create_android.core.exception.Failure.ServerError
import hiroshi_homma.homma_create.hiroshi_create_android.core.functional.Either
import hiroshi_homma.homma_create.hiroshi_create_android.core.functional.Either.Left
import hiroshi_homma.homma_create.hiroshi_create_android.core.functional.Either.Right
import hiroshi_homma.homma_create.hiroshi_create_android.core.platform.NetworkHandler
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.api.SampleService
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.entity.DetailsEntity
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.modeldata.Top
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.modeldata.Details
import retrofit2.Call
import javax.inject.Inject

interface SampleRepository {
    fun top(): Either<Failure, List<Top>>
    fun details(movieId: Int): Either<Failure, Details>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val service: SampleService) : SampleRepository {

        override fun top(): Either<Failure, List<Top>> {
            return when (networkHandler.isConnected) {
                true -> request(service.top(), { it.map { it.toTop() } }, emptyList())
                false, null -> Left(NetworkConnection())
            }
        }

        override fun details(movieId: Int): Either<Failure, Details> {
            return when (networkHandler.isConnected) {
                true -> request(service.details(movieId), { it.toDetails() }, DetailsEntity.empty())
                false, null -> Left(NetworkConnection())
            }
        }

        private fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
            return try {
                val response = call.execute()
                when (response.isSuccessful) {
                    true -> Right(transform((response.body() ?: default)))
                    false -> Left(ServerError())
                }
            } catch (exception: Throwable) {
                Left(ServerError())
            }
        }
    }
}
