package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing

import hiroshi_homma.homma_create.hiroshi_create_android.UnitTest
import hiroshi_homma.homma_create.hiroshi_create_android.core.functional.Either.Right
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.modeldata.Details
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.repository.SampleRepository
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.corutine.GetDetails
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class GetDetailsTest : UnitTest() {

    private val MOVIE_ID = 1

    private lateinit var getDetails: GetDetails

    @Mock private lateinit var sampleRepository: SampleRepository

    @Before fun setUp() {
        getDetails = GetDetails(sampleRepository)
        given { sampleRepository.Details(MOVIE_ID) }.willReturn(Right(Details.empty()))
    }

    @Test fun `should get data from repository`() {
        runBlocking { getDetails.run(GetDetails.Params(MOVIE_ID)) }

        verify(sampleRepository).Details(MOVIE_ID)
        verifyNoMoreInteractions(sampleRepository)
    }
}
