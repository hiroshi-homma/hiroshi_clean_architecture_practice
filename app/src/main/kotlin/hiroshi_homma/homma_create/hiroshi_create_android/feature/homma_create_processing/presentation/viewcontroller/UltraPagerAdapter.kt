/*
 *
 *  MIT License
 *
 *  Copyright (c) 2017 Alibaba Group
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 *
 */

package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.presentation.viewcontroller

import android.annotation.SuppressLint
import android.support.v4.app.FragmentActivity
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import hiroshi_homma.homma_create.hiroshi_create_android.R
import hiroshi_homma.homma_create.hiroshi_create_android.core.navigation.Navigator
import kotlinx.android.synthetic.main.layout_child.view.*

/**
 * Created by mikeafc on 15/11/26.
 */
class UltraPagerAdapter(val activity: FragmentActivity?,
                        private val topRecyclerViewAdapter: TopRecyclerViewAdapter,
                        val navigator: Navigator) : PagerAdapter() {

    override fun getCount(): Int {
        return 15
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "test_"+position.toString()
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val linearLayout =
                LayoutInflater
                        .from(container.context)
                        .inflate(R.layout.layout_child, null) as LinearLayout
        linearLayout.id = R.id.item_id
        linearLayout.title.text = "test"+(position+1)

        val recycler = linearLayout.movieList
        recycler.layoutManager =
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recycler.adapter = topRecyclerViewAdapter

        topRecyclerViewAdapter.clickListener = {
            movie, navigationExtras ->
                navigator.showMovieDetails(activity!!, movie, navigationExtras)
        }
        container.addView(linearLayout)

        return linearLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as LinearLayout
        container.removeView(view)
    }
}
