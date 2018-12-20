package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.corutine

import hiroshi_homma.homma_create.hiroshi_create_android.core.interactor.UseCase
import hiroshi_homma.homma_create.hiroshi_create_android.core.interactor.UseCase.None
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.repository.SampleRepository
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.modeldata.Top
import javax.inject.Inject

class GetTop
@Inject constructor(private val sampleRepository: SampleRepository) : UseCase<List<Top>, None>() {
    override suspend fun run(params: None) = sampleRepository.top()
}
