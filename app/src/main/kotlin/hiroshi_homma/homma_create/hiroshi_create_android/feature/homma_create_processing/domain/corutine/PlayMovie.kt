package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.corutine

import android.content.Context
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.corutine.PlayMovie.Params
import hiroshi_homma.homma_create.hiroshi_create_android.core.exception.Failure
import hiroshi_homma.homma_create.hiroshi_create_android.core.functional.Either
import hiroshi_homma.homma_create.hiroshi_create_android.core.functional.Either.Right
import hiroshi_homma.homma_create.hiroshi_create_android.core.interactor.UseCase
import hiroshi_homma.homma_create.hiroshi_create_android.core.interactor.UseCase.None
import hiroshi_homma.homma_create.hiroshi_create_android.core.navigation.Navigator
import javax.inject.Inject

class PlayMovie
@Inject constructor(private val context: Context,
                    private val navigator: Navigator) : UseCase<None, Params>() {

    override suspend fun run(params: Params): Either<Failure, None> {
        navigator.openVideo(context, params.url)
        return Right(None())
    }

    data class Params(val url: String)
}
