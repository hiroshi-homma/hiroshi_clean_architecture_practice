package hiroshi_homma.homma_create.hiroshi_create_android.core.navigation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import hiroshi_homma.homma_create.hiroshi_create_android.AndroidApplication
import hiroshi_homma.homma_create.hiroshi_create_android.core.di.ApplicationComponent
import javax.inject.Inject

class RouteActivity : AppCompatActivity() {

    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }

    @Inject internal lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        navigator.showMain(this)
    }
}
