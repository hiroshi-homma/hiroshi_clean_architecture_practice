package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.presentation.activity

import android.content.Context
import android.content.Intent
import hiroshi_homma.homma_create.hiroshi_create_android.core.platform.activity.BaseDetailActivity
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.viewmodeldata.TopView
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.presentation.fragment.DetailsFragment

class DetailsActivity : BaseDetailActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_MOVIE = "com.fernandocejas.INTENT_PARAM_MOVIE"

        fun callingIntent(context: Context, top: TopView): Intent {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_MOVIE, top)
            return intent
        }
    }

    override fun fragment() = DetailsFragment.forMovie(intent.getParcelableExtra(INTENT_EXTRA_PARAM_MOVIE))
}
