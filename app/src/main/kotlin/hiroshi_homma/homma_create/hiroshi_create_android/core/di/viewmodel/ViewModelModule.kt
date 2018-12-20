package hiroshi_homma.homma_create.hiroshi_create_android.core.di.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.viewmodel.DetailsViewModel
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.viewmodel.TopViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TopViewModel::class)
    abstract fun bindsTopViewModel(topViewModel: TopViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    abstract fun bindsDetailsViewModel(detailsViewModel: DetailsViewModel): ViewModel
}