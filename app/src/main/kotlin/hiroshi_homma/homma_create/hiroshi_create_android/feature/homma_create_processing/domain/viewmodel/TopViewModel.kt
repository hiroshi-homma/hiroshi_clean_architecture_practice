package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.viewmodel

import android.arch.lifecycle.MutableLiveData
import hiroshi_homma.homma_create.hiroshi_create_android.core.interactor.UseCase.None
import hiroshi_homma.homma_create.hiroshi_create_android.core.platform.viewmodel.BaseViewModel
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.modeldata.Top
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.viewmodeldata.TopView
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.corutine.GetTop
import javax.inject.Inject

class TopViewModel
@Inject constructor(private val getTop: GetTop) : BaseViewModel() {

    var topdata: MutableLiveData<List<TopView>> = MutableLiveData()

    fun loadMovies() = getTop(None()) { it.either(::handleFailure, ::handleMovieList) }

    private fun handleMovieList(tops: List<Top>) {
        this.topdata.value = tops.map { TopView(it.id, it.poster) }
    }
}