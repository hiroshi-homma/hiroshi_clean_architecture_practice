package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing

import hiroshi_homma.homma_create.hiroshi_create_android.AndroidTest
import hiroshi_homma.homma_create.hiroshi_create_android.core.functional.Either.Right
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.modeldata.Top
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.corutine.GetTop
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.viewmodel.TopViewModel
import kotlinx.coroutines.experimental.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldEqualTo
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class TopViewModelTest : AndroidTest() {

    private lateinit var topViewModel: TopViewModel

    @Mock private lateinit var getTop: GetTop

    @Before
    fun setUp() {
        topViewModel = TopViewModel(getTop)
    }

    @Test fun `loading movies should update live data`() {
        val moviesList = listOf(Top(0, "IronMan"), Top(1, "Batman"))
        given { runBlocking { getTop.run(eq(any())) } }.willReturn(Right(moviesList))

        topViewModel.movies.observeForever {
            it!!.size shouldEqualTo 2
            it[0].id shouldEqualTo 0
            it[0].poster shouldBeEqualTo "IronMan"
            it[1].id shouldEqualTo 1
            it[1].poster shouldBeEqualTo "Batman"
        }

        runBlocking { topViewModel.loadMovies() }
    }
}