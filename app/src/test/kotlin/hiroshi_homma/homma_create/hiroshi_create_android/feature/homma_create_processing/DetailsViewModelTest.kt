package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing

import hiroshi_homma.homma_create.hiroshi_create_android.AndroidTest
import hiroshi_homma.homma_create.hiroshi_create_android.core.functional.Either.Right
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.modeldata.Details
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.corutine.GetDetails
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.viewmodel.DetailsViewModel
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.corutine.PlayMovie
import kotlinx.coroutines.experimental.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldEqualTo
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class DetailsViewModelTest : AndroidTest() {

    private lateinit var detailsViewModel: DetailsViewModel

    @Mock private lateinit var getDetails: GetDetails
    @Mock private lateinit var playMovie: PlayMovie

    @Before
    fun setUp() {
        detailsViewModel = DetailsViewModel(getDetails, playMovie)
    }

    @Test fun `loading movie details should update live data`() {
        val movieDetails = Details(0, "IronMan", "poster", "summary",
                "cast", "director", 2018, "trailer")
        given { runBlocking { getDetails.run(eq(any())) } }.willReturn(Right(movieDetails))

        detailsViewModel.details.observeForever {
            with(it!!) {
                id shouldEqualTo 0
                title shouldBeEqualTo "IronMan"
                poster shouldBeEqualTo "poster"
                summary shouldBeEqualTo "summary"
                cast shouldBeEqualTo "cast"
                director shouldBeEqualTo "director"
                year shouldEqualTo 2018
                trailer shouldBeEqualTo "trailer"
            }
        }

        runBlocking { detailsViewModel.loadMovieDetails(0) }
    }
}