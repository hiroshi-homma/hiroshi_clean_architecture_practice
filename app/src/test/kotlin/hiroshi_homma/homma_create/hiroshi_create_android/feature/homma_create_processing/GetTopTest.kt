package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing

import hiroshi_homma.homma_create.hiroshi_create_android.UnitTest
import hiroshi_homma.homma_create.hiroshi_create_android.core.functional.Either.Right
import hiroshi_homma.homma_create.hiroshi_create_android.core.interactor.UseCase
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.modeldata.Top
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.repository.SampleRepository
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.corutine.GetTop
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class GetTopTest : UnitTest() {

    private lateinit var getTop: GetTop

    @Mock private lateinit var sampleRepository: SampleRepository

    @Before fun setUp() {
        getTop = GetTop(sampleRepository)
        given { sampleRepository.top() }.willReturn(Right(listOf(Top.empty())))
    }

    @Test fun `should get data from repository`() {
        runBlocking { getTop.run(UseCase.None()) }

        verify(sampleRepository).top()
        verifyNoMoreInteractions(sampleRepository)
    }
}
