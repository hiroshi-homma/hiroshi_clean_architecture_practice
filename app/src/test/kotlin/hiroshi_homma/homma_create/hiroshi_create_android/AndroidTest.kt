package hiroshi_homma.homma_create.hiroshi_create_android

import android.app.Application
import android.content.Context
import hiroshi_homma.homma_create.hiroshi_create_android.core.platform.activity.BaseDetailActivity
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Base class for Android tests. Inherit from it to create test cases which contain android
 * framework dependencies or components.
 *
 * @see UnitTest
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = AndroidTest.ApplicationStub::class,
        sdk = [21])
abstract class AndroidTest {

    @Suppress("LeakingThis")
    @Rule @JvmField val injectMocks = InjectMocksRule.create(this@AndroidTest)

//    fun context(): Context = RuntimeEnvironment.application

    fun activityContext(): Context = mock(BaseDetailActivity::class.java)

    internal class ApplicationStub : Application()
}
