@file:JvmName("AndroidAssertions")
@file:JvmMultifileClass
@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package hiroshi_homma.homma_create.hiroshi_create_android

import android.support.v7.app.AppCompatActivity
import org.amshove.kluent.shouldEqual
import org.robolectric.Robolectric
import org.robolectric.Shadows
import kotlin.reflect.KClass

infix fun KClass<out AppCompatActivity>.shouldNavigateTo(nextActivity: KClass<out AppCompatActivity>) = {
    val originActivity = Robolectric.buildActivity(this.java).get()
    val shadowActivity = Shadows.shadowOf(originActivity)
    val nextIntent = shadowActivity.peekNextStartedActivity()

    nextIntent.component.className shouldEqual nextActivity.java.canonicalName
}
