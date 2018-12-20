package hiroshi_homma.homma_create.hiroshi_create_android.core.platform

import android.content.Context
import hiroshi_homma.homma_create.hiroshi_create_android.core.extension.networkInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkHandler
@Inject constructor(context: Context) {
    val isConnected = context.networkInfo?.isConnected
}