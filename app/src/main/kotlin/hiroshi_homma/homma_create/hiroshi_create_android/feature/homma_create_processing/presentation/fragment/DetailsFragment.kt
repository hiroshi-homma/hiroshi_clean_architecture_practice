package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.presentation.fragment

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import hiroshi_homma.homma_create.hiroshi_create_android.core.platform.fragment.BaseFragment
import hiroshi_homma.homma_create.hiroshi_create_android.R
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.utils.exception.TopFailure.NonExistentMovie
import hiroshi_homma.homma_create.hiroshi_create_android.core.exception.Failure
import hiroshi_homma.homma_create.hiroshi_create_android.core.exception.Failure.NetworkConnection
import hiroshi_homma.homma_create.hiroshi_create_android.core.exception.Failure.ServerError
import hiroshi_homma.homma_create.hiroshi_create_android.core.extension.close
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.presentation.curlView.CurlPage
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.presentation.curlView.CurlView
import hiroshi_homma.homma_create.hiroshi_create_android.core.extension.failure
import hiroshi_homma.homma_create.hiroshi_create_android.core.extension.isVisible
import hiroshi_homma.homma_create.hiroshi_create_android.core.extension.loadFromUrl
import hiroshi_homma.homma_create.hiroshi_create_android.core.extension.loadUrlAndPostponeEnterTransition
import hiroshi_homma.homma_create.hiroshi_create_android.core.extension.observe
import hiroshi_homma.homma_create.hiroshi_create_android.core.extension.viewModel
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.utils.DetailsAnimator
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.viewmodel.DetailsViewModel
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.viewmodeldata.TopView
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.viewmodeldata.DetailsView
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.toolbar.toolbar
import javax.inject.Inject

class DetailsFragment : BaseFragment() {

    companion object {
        private const val PARAM_MOVIE = "param_movie"

        fun forMovie(top: TopView): DetailsFragment {
            val movieDetailsFragment = DetailsFragment()
            val arguments = Bundle()
            arguments.putParcelable(PARAM_MOVIE, top)
            movieDetailsFragment.arguments = arguments

            return movieDetailsFragment
        }
    }

    @Inject lateinit var detailsAnimator: DetailsAnimator

    private lateinit var detailsViewModel: DetailsViewModel

    override fun layoutId() = R.layout.fragment_details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        activity?.let { detailsAnimator.postponeEnterTransition(it) }

        detailsViewModel = viewModel(viewModelFactory) {
            observe(details, ::renderMovieDetails)
            failure(failure, ::handleFailure)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (firstTimeCreated(savedInstanceState)) {
            detailsViewModel.loadMovieDetails((arguments?.get(PARAM_MOVIE) as TopView).id)
        } else {
            detailsAnimator.scaleUpView(moviePlay)
            detailsAnimator.cancelTransition(moviePoster)
            moviePoster.loadFromUrl((arguments!![PARAM_MOVIE] as TopView).poster)
        }

    }

    override fun onBackPressed() {
        curl_page.visibility = GONE
        detailsAnimator.fadeInvisible(scrollView, curl)
        detailsAnimator.fadeInvisible(scrollView, movieDetails)
        if (moviePlay.isVisible())
            detailsAnimator.scaleDownView(moviePlay)
        else
            detailsAnimator.cancelTransition(moviePoster)
    }

    override fun onPause() {
        super.onPause()
        curl!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        curl!!.onResume()
    }

    private fun renderMovieDetails(movie: DetailsView?) {
        movie?.let {
            with(movie) {
                activity?.let {
                    moviePoster.loadUrlAndPostponeEnterTransition(poster, it)
                    it.toolbar.title = title
                }
                movieSummary.text = summary
                movieCast.text = cast
                movieDirector.text = director
                movieYear.text = year.toString()
                moviePlay.setOnClickListener { detailsViewModel.playMovie(trailer) }
                curl!!.setPageProvider(PageProvider())
                curl!!.setSizeChangedObserver(SizeChangedObserver())
                curl!!.setBackgroundColor(-0x1)
            }
        }
        detailsAnimator.fadeVisible(scrollView, movieDetails)
        detailsAnimator.scaleUpView(moviePlay)
    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is NetworkConnection -> { notify(R.string.failure_network_connection); close() }
            is ServerError -> { notify(R.string.failure_server_error); close() }
            is NonExistentMovie -> { notify(R.string.failure_movie_non_existent); close() }
        }
    }

    /**
     * Bitmap provider.
     */
    private inner class PageProvider : CurlView.PageProvider {

        // Bitmap resources.
        private val mBitmapIds = intArrayOf(R.drawable.obama, R.drawable.road_rage, R.drawable.taipei_101, R.drawable.world)

        override val pageCount: Int
            get() = 405

        private fun loadBitmap(width: Int, height: Int, index: Int): Bitmap {
            val b = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888)
            b.eraseColor(-0x1)
            val c = Canvas(b)
            @Suppress("DEPRECATION")
            val d = resources.getDrawable(mBitmapIds[index])

            val margin = 0
            val border = 3
            val r = Rect(margin, margin, width - margin, height - margin)

            var imageWidth = r.width() - border * 2
            var imageHeight = imageWidth * d.intrinsicHeight / d.intrinsicWidth
            if (imageHeight > r.height() - border * 2) {
                imageHeight = r.height() - border * 2
                imageWidth = imageHeight * d.intrinsicWidth / d.intrinsicHeight
            }

            r.left += (r.width() - imageWidth) / 2 - border
            r.right = r.left + imageWidth + border + border
            r.top += (r.height() - imageHeight) / 2 - border
            r.bottom = r.top + imageHeight + border + border

            val p = Paint()
            p.color = -0x3f3f40
            c.drawRect(r, p)
            r.left += border
            r.right -= border
            r.top += border
            r.bottom -= border

            d.bounds = r
            d.draw(c)

            return b
        }

        override fun updatePage(page: CurlPage, width: Int, height: Int, index: Int) {
//            scrollView.isNestedScrollingEnabled = false
            when (index) {
                // First case is image on front side, solid colored back.
                0 -> {
                    val front = loadBitmap(width, height, 0)
                    page.setTexture(front, CurlPage.SIDE_FRONT)
//                    page.setColor(Color.rgb(255, 255, 255), CurlPage.SIDE_BACK)
                }
                // Second case is image on back side, solid colored front.
                1 -> {
                    val back = loadBitmap(width, height, 2)
                    page.setTexture(back, CurlPage.SIDE_BACK)
//                    page.setColor(Color.rgb(255, 255, 255), CurlPage.SIDE_FRONT)
                }
                // Third case is images on both sides.
                2 -> {
                    val front = loadBitmap(width, height, 1)
                    val back = loadBitmap(width, height, 3)
                    page.setTexture(front, CurlPage.SIDE_FRONT)
                    page.setTexture(back, CurlPage.SIDE_BACK)
                }
                // Fourth case is images on both sides - plus they are blend against
                // separate colors.
                3 -> {
                    val front = loadBitmap(width, height, 2)
                    val back = loadBitmap(width, height, 1)
                    page.setTexture(front, CurlPage.SIDE_FRONT)
                    page.setTexture(back, CurlPage.SIDE_BACK)
//                    page.setColor(Color.argb(127, 170, 130, 255),
//                            CurlPage.SIDE_FRONT)
//                    page.setColor(Color.rgb(255, 190, 150), CurlPage.SIDE_BACK)
                }
                // Fifth case is same image is assigned to front and back. In this
                // scenario only one texture is used and shared for both sides.
                4 -> {
                    val front = loadBitmap(width, height, 0)
                    page.setTexture(front, CurlPage.SIDE_BOTH)
//                    page.setColor(Color.argb(127, 255, 255, 255),
//                            CurlPage.SIDE_BACK)
                }
            }
//            scrollView.isNestedScrollingEnabled = true
        }

    }

    /**
     * CurlView size changed observer.
     *
     */
    private inner class SizeChangedObserver : CurlView.SizeChangedObserver {
        override fun onSizeChanged(width: Int, height: Int) {
            if (width > height) {
                curl!!.setViewMode(CurlView.SHOW_TWO_PAGES)
                curl!!.setMargins(.0f, .09f, .0f, .0f)
            } else {
                curl!!.setViewMode(CurlView.SHOW_ONE_PAGE)
                curl!!.setMargins(.0f, .09f, .0f, .0f)
            }
        }
    }

}
