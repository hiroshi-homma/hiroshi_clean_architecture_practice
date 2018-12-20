package hiroshi_homma.homma_create.hiroshi_create_android.core.di

import hiroshi_homma.homma_create.hiroshi_create_android.AndroidApplication
import hiroshi_homma.homma_create.hiroshi_create_android.core.di.viewmodel.ViewModelModule
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.presentation.fragment.DetailsFragment
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.presentation.fragment.TopFragment
import hiroshi_homma.homma_create.hiroshi_create_android.core.navigation.RouteActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent {
    // Application initilaze
    fun inject(application: AndroidApplication)
    // Activity initilaze
    fun inject(routeActivity: RouteActivity)
    // Fragment initilaze
    fun inject(topFragment: TopFragment)
    fun inject(detailsFragment: DetailsFragment)
}
