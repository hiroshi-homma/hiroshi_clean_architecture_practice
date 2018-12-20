package hiroshi_homma.homma_create.hiroshi_create_android.core.interactor

import hiroshi_homma.homma_create.hiroshi_create_android.core.exception.Failure
import hiroshi_homma.homma_create.hiroshi_create_android.core.functional.Either
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI

@Suppress("DEPRECATION")
abstract class UseCase<out Type, in Params> where Type : Any {

    abstract suspend fun run(params: Params): Either<Failure, Type>

    operator fun invoke(params: Params, onResult: (Either<Failure, Type>) -> Unit = {}) {
        val job =
                GlobalScope.async(
                        CommonPool,
                        CoroutineStart.DEFAULT,
                        null,
                        { run(params) }
                )
        launch(UI) { onResult(job.await()) }
    }

    class None
}
