package hiroshi_homma.homma_create.hiroshi_create_android

import android.app.Application
import hiroshi_homma.homma_create.hiroshi_create_android.core.di.ApplicationComponent
import hiroshi_homma.homma_create.hiroshi_create_android.core.di.ApplicationModule
import hiroshi_homma.homma_create.hiroshi_create_android.core.di.DaggerApplicationComponent
import io.realm.Realm
import io.realm.RealmConfiguration

//import com.squareup.leakcanary.LeakCanary

class AndroidApplication : Application() {

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        this.injectMembers()


        Realm.init(this)
        instance = this
        Realm.setDefaultConfiguration(buildRealmConfiguration())

        // メモリリークを検出するライブラリ
//        this.initializeLeakDetection()
    }

    private fun injectMembers() = appComponent.inject(this)

    private fun initializeLeakDetection() {
        // メモリリークを検出するライブラリ
//        if (BuildConfig.DEBUG) LeakCanary.install(this)
    }
    private fun buildRealmConfiguration(): RealmConfiguration {
        return RealmConfiguration.Builder()
                .name("hiroshi.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build()
    }

    companion object {
        val TAG = AndroidApplication::class.java
                .simpleName

        @get:Synchronized
        var instance: AndroidApplication? = null
            private set
    }

}
