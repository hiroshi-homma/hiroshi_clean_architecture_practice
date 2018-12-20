package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing

import hiroshi_homma.homma_create.hiroshi_create_android.UnitTest
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.repository.SampleRepository.Network
import hiroshi_homma.homma_create.hiroshi_create_android.core.exception.Failure.NetworkConnection
import hiroshi_homma.homma_create.hiroshi_create_android.core.exception.Failure.ServerError
import hiroshi_homma.homma_create.hiroshi_create_android.core.extension.empty
import hiroshi_homma.homma_create.hiroshi_create_android.core.functional.Either
import hiroshi_homma.homma_create.hiroshi_create_android.core.functional.Either.Right
import hiroshi_homma.homma_create.hiroshi_create_android.core.platform.NetworkHandler
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.modeldata.Top
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.modeldata.Details
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.entity.DetailsEntity
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.entity.TopEntity
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.repository.SampleRepository
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.api.SampleService
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import retrofit2.Call
import retrofit2.Response

class SampleRepositoryTest : UnitTest() {

    private lateinit var networkRepository: SampleRepository.Network

    @Mock private lateinit var networkHandler: NetworkHandler
    @Mock private lateinit var service: SampleService

    @Mock private lateinit var moviesCall: Call<List<TopEntity>>
    @Mock private lateinit var moviesResponse: Response<List<TopEntity>>
    @Mock private lateinit var detailsCall: Call<DetailsEntity>
    @Mock private lateinit var detailsResponse: Response<DetailsEntity>

    @Before fun setUp() {
        networkRepository = Network(networkHandler, service)
    }

    @Test fun `should return empty list by default`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { moviesResponse.body() }.willReturn(null)
        given { moviesResponse.isSuccessful }.willReturn(true)
        given { moviesCall.execute() }.willReturn(moviesResponse)
        given { service.top() }.willReturn(moviesCall)

        val movies = networkRepository.top()

        movies shouldEqual Right(emptyList<Top>())
        verify(service).top()
    }

    @Test fun `should get movie list from service`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { moviesResponse.body() }.willReturn(listOf(TopEntity(1, "poster")))
        given { moviesResponse.isSuccessful }.willReturn(true)
        given { moviesCall.execute() }.willReturn(moviesResponse)
        given { service.top() }.willReturn(moviesCall)

        val movies = networkRepository.top()

        movies shouldEqual Right(listOf(Top(1, "poster")))
        verify(service).top()
    }

    @Test fun `movies service should return network failure when no connection`() {
        given { networkHandler.isConnected }.willReturn(false)

        val movies = networkRepository.top()

        movies shouldBeInstanceOf Either::class.java
        movies.isLeft shouldEqual true
        movies.either({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test fun `movies service should return network failure when undefined connection`() {
        given { networkHandler.isConnected }.willReturn(null)

        val movies = networkRepository.top()

        movies shouldBeInstanceOf Either::class.java
        movies.isLeft shouldEqual true
        movies.either({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test fun `movies service should return server error if no successful response`() {
        given { networkHandler.isConnected }.willReturn(true)

        val movies = networkRepository.top()

        movies shouldBeInstanceOf Either::class.java
        movies.isLeft shouldEqual true
        movies.either({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }

    @Test fun `movies request should catch exceptions`() {
        given { networkHandler.isConnected }.willReturn(true)

        val movies = networkRepository.top()

        movies shouldBeInstanceOf Either::class.java
        movies.isLeft shouldEqual true
        movies.either({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }

    @Test fun `should return empty movie details by default`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { detailsResponse.body() }.willReturn(null)
        given { detailsResponse.isSuccessful }.willReturn(true)
        given { detailsCall.execute() }.willReturn(detailsResponse)
        given { service.Details(1) }.willReturn(detailsCall)

        val movieDetails = networkRepository.Details(1)

        movieDetails shouldEqual Right(Details.empty())
        verify(service).Details(1)
    }

    @Test fun `should get movie details from service`() {
        given { networkHandler.isConnected }.willReturn(true)
        given { detailsResponse.body() }.willReturn(
                DetailsEntity(8, "title", String.empty(), String.empty(),
                        String.empty(), String.empty(), 0, String.empty()))
        given { detailsResponse.isSuccessful }.willReturn(true)
        given { detailsCall.execute() }.willReturn(detailsResponse)
        given { service.Details(1) }.willReturn(detailsCall)

        val movieDetails = networkRepository.Details(1)

        movieDetails shouldEqual Right(Details(8, "title", String.empty(), String.empty(),
                String.empty(), String.empty(), 0, String.empty()))
        verify(service).Details(1)
    }

    @Test fun `movie details service should return network failure when no connection`() {
        given { networkHandler.isConnected }.willReturn(false)

        val movieDetails = networkRepository.Details(1)

        movieDetails shouldBeInstanceOf Either::class.java
        movieDetails.isLeft shouldEqual true
        movieDetails.either({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test fun `movie details service should return network failure when undefined connection`() {
        given { networkHandler.isConnected }.willReturn(null)

        val movieDetails = networkRepository.Details(1)

        movieDetails shouldBeInstanceOf Either::class.java
        movieDetails.isLeft shouldEqual true
        movieDetails.either({ failure -> failure shouldBeInstanceOf NetworkConnection::class.java }, {})
        verifyZeroInteractions(service)
    }

    @Test fun `movie details service should return server error if no successful response`() {
        given { networkHandler.isConnected }.willReturn(true)

        val movieDetails = networkRepository.Details(1)

        movieDetails shouldBeInstanceOf Either::class.java
        movieDetails.isLeft shouldEqual true
        movieDetails.either({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }

    @Test fun `movie details request should catch exceptions`() {
        given { networkHandler.isConnected }.willReturn(true)

        val movieDetails = networkRepository.Details(1)

        movieDetails shouldBeInstanceOf Either::class.java
        movieDetails.isLeft shouldEqual true
        movieDetails.either({ failure -> failure shouldBeInstanceOf ServerError::class.java }, {})
    }
}