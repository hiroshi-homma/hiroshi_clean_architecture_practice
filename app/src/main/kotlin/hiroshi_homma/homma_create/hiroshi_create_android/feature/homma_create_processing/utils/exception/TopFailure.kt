package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.utils.exception

import hiroshi_homma.homma_create.hiroshi_create_android.core.exception.Failure.productFailure

class TopFailure {
    class ListNotAvailable: productFailure()
    class NonExistentMovie: productFailure()
}

