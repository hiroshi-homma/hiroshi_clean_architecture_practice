package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.presentation.viewcontroller

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import hiroshi_homma.homma_create.hiroshi_create_android.R
import hiroshi_homma.homma_create.hiroshi_create_android.core.extension.inflate
import hiroshi_homma.homma_create.hiroshi_create_android.core.extension.loadFromUrl
import hiroshi_homma.homma_create.hiroshi_create_android.core.navigation.Navigator
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.viewmodeldata.TopView
import kotlinx.android.synthetic.main.row_movie.view.*
import javax.inject.Inject
import kotlin.properties.Delegates

class TopRecyclerViewAdapter
@Inject constructor() : RecyclerView.Adapter<TopRecyclerViewAdapter.ViewHolder>() {

    internal var collection: List<TopView> by Delegates.observable(emptyList()) {
        _, _, _ -> notifyDataSetChanged()
    }

    internal var clickListener: (TopView, Navigator.Extras) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(parent.inflate(R.layout.row_movie))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
            viewHolder.bind(collection[position], clickListener)

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(topView: TopView, clickListener: (TopView, Navigator.Extras) -> Unit) {
            itemView.moviePoster.loadFromUrl(topView.poster)
            itemView.setOnClickListener {
                clickListener(topView, Navigator.Extras(itemView.moviePoster))
            }
        }
    }
}
