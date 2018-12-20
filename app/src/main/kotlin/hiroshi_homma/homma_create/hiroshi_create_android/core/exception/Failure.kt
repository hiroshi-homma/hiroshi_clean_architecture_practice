package hiroshi_homma.homma_create.hiroshi_create_android.core.exception

sealed class Failure {
    class NetworkConnection: Failure()
    class ServerError: Failure()

    abstract class productFailure: Failure()
}
