package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.corutine

import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.corutine.GetDetails.Params
import hiroshi_homma.homma_create.hiroshi_create_android.core.interactor.UseCase
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.repository.SampleRepository
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.modeldata.Details
import javax.inject.Inject

class GetDetails
@Inject constructor(private val sampleRepository: SampleRepository) : UseCase<Details, Params>() {
    override suspend fun run(params: Params) = sampleRepository.details(params.id)
    data class Params(val id: Int)
}
