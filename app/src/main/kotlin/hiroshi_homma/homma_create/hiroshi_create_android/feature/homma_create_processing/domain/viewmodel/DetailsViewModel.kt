package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.viewmodel

import android.arch.lifecycle.MutableLiveData
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.corutine.GetDetails.Params
import hiroshi_homma.homma_create.hiroshi_create_android.core.platform.viewmodel.BaseViewModel
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.modeldata.Details
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.viewmodeldata.DetailsView
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.corutine.GetDetails
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.corutine.PlayMovie
import javax.inject.Inject

class DetailsViewModel
@Inject constructor(private val getDetails: GetDetails,
                    private val playMovie: PlayMovie) : BaseViewModel() {

    var details: MutableLiveData<DetailsView> = MutableLiveData()

    fun loadMovieDetails(movieId: Int) =
            getDetails(Params(movieId)) { it.either(::handleFailure, ::handleMovieDetails) }

    fun playMovie(url: String) = playMovie(PlayMovie.Params(url))

    private fun handleMovieDetails(movie: Details) {
        this.details.value = DetailsView(movie.id, movie.title, movie.poster,
                movie.summary, movie.cast, movie.director, movie.year, movie.trailer)
    }
}