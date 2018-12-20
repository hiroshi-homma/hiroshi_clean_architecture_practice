package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.presentation.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.annotation.StringRes
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.SparseIntArray
import android.view.View
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.CompoundButton
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import com.tmall.ultraviewpager.UltraViewPager
import hiroshi_homma.homma_create.hiroshi_create_android.core.platform.fragment.BaseFragment
import hiroshi_homma.homma_create.hiroshi_create_android.R
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.utils.exception.TopFailure.ListNotAvailable
import hiroshi_homma.homma_create.hiroshi_create_android.core.exception.Failure
import hiroshi_homma.homma_create.hiroshi_create_android.core.exception.Failure.NetworkConnection
import hiroshi_homma.homma_create.hiroshi_create_android.core.exception.Failure.ServerError
import hiroshi_homma.homma_create.hiroshi_create_android.core.extension.appContext
import hiroshi_homma.homma_create.hiroshi_create_android.core.extension.failure
import hiroshi_homma.homma_create.hiroshi_create_android.core.extension.observe
import hiroshi_homma.homma_create.hiroshi_create_android.core.extension.viewModel
import hiroshi_homma.homma_create.hiroshi_create_android.core.navigation.Navigator
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.data.viewmodeldata.TopView
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.domain.viewmodel.TopViewModel
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.presentation.viewcontroller.TopRecyclerViewAdapter
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.presentation.viewcontroller.UltraPagerAdapter
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.utils.HommaUtil
import kotlinx.android.synthetic.main.fragment_top.*
import tabrelationlibrary.ViewPagerHelper
import tabrelationlibrary.buildins.commonnavigator.CommonNavigator
import tabrelationlibrary.buildins.commonnavigator.abs.CommonNavigatorAdapter
import tabrelationlibrary.buildins.commonnavigator.abs.IPagerIndicator
import tabrelationlibrary.buildins.commonnavigator.abs.IPagerTitleView
import tabrelationlibrary.buildins.commonnavigator.indicators.WrapPagerIndicator
import tabrelationlibrary.buildins.commonnavigator.titles.SimplePagerTitleView
import javax.inject.Inject

class TopFragment : BaseFragment() , AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener{
    private var adapter: PagerAdapter? = null
    private var loopCheckBox: CheckBox? = null
    private var autoScrollCheckBox: CheckBox? = null
//    private var gravity_hor: Int = 0
//    private var gravity_ver: Int = 0
    private var orientation: UltraViewPager.Orientation? = null
    private var commonNavigator: CommonNavigator? = null

    @Inject lateinit var navigator: Navigator
    @Inject lateinit var topRecyclerViewAdapter: TopRecyclerViewAdapter
    private lateinit var topViewModel: TopViewModel

    override fun layoutId() = R.layout.fragment_top

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        topViewModel = viewModel(viewModelFactory) {
            observe(topdata, ::renderMoviesList)
            failure(failure, ::handleFailure)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
        loadMoviesList()
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (buttonView === loopCheckBox) {
            ultra_viewpager!!.setInfiniteLoop(isChecked)
        }
        if (buttonView === autoScrollCheckBox) {
            if (isChecked) {
                val special = SparseIntArray()
                special.put(0, 2000)
                special.put(1, 2000)
                ultra_viewpager!!.setAutoScroll(2000, special)
            } else
                ultra_viewpager!!.disableAutoScroll()
        }
    }

    override fun onClick(v: View) {
        ultra_viewpager!!.indicator!!.build()
    }

    private fun loadMoviesList() {
        showProgress()
        topViewModel.loadMovies()
    }

    private fun renderMoviesList(tops: List<TopView>?) {
        topRecyclerViewAdapter.collection = tops.orEmpty()
        hideProgress()
    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is NetworkConnection -> renderFailure(R.string.failure_network_connection)
            is ServerError -> renderFailure(R.string.failure_server_error)
            is ListNotAvailable -> renderFailure(R.string.failure_movies_list_unavailable)
        }
    }

    private fun renderFailure(@StringRes message: Int) {
        hideProgress()
        notifyWithAction(message, R.string.action_refresh, ::loadMoviesList)
    }

    private fun initializeView() {
        initUrtraViewPager()
        initTabRelationLibrary()
        initSeekBar()
        initUI()
    }

    private fun initUrtraViewPager(){
        ultra_viewpager!!.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL)
        adapter = UltraPagerAdapter(activity,topRecyclerViewAdapter,navigator)
        ultra_viewpager!!.adapter = adapter
        ultra_viewpager!!.getViewPager()!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onPageSelected(position: Int) {
                when {
                    12 < position -> commonNavigator!!.scrollPivotX = -8.0f
                    position < 3 -> commonNavigator!!.scrollPivotX = 8.0f
                    else -> commonNavigator!!.scrollPivotX = 0.5f
                }
                HommaUtil.setTabNumber(position,appContext)
                HommaUtil.setSwipeFlag(true,appContext)
//                tv0.text = "選択タブ:"+realm_command.tabs_read(realm)[seekbar.progress]!!.name
                RangeSeekBar.setValue(position.toFloat())
//                RangeSeekBar.progress = position
            }
        })
        ultra_viewpager!!.setInfiniteRatio(100)
        orientation = UltraViewPager.Orientation.HORIZONTAL
    }

    private fun initTabRelationLibrary() {
        commonNavigator = CommonNavigator(appContext)
        commonNavigator!!.adapter = object : CommonNavigatorAdapter() {
            override val count: Int get() =15

            @SuppressLint("SetTextI18n")
            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = SimplePagerTitleView(context)
                simplePagerTitleView.textSize = 14f
                simplePagerTitleView.text = "test"+(index+1)
                simplePagerTitleView.normalColor = resources.getColor(R.color.theme_F9F9F9,null)
                simplePagerTitleView.selectedColor = resources.getColor(R.color.theme_F9F9F9,null)

                simplePagerTitleView.setOnClickListener {
                    HommaUtil.setTabNumber(index,appContext)
                    ultra_viewpager!!.setCurrentItem(index,true)
                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = WrapPagerIndicator(context)
                indicator.fillColor = resources.getColor(R.color.theme_018786,null)
                return indicator
            }
        }
        tab_relation_library.navigator = commonNavigator
        tab_relation_library.onPageSelected(HommaUtil.getTabNumber(appContext))
        ultra_viewpager!!.currentItem = HommaUtil.getTabNumber(appContext)
        ViewPagerHelper.bind(tab_relation_library, ultra_viewpager!!.getViewPager()!!)
    }

    private fun initSeekBar(){
        RangeSeekBar.setRange(0.0f, 14.0f)
        RangeSeekBar.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onRangeChanged(view: RangeSeekBar, leftValue: Float, rightValue: Float, isFromUser: Boolean) {
                //                seekbar1.setIndicatorText((int)leftValue+"");
                RangeSeekBar.setIndicatorText("test"+(leftValue.toInt()+1))
                if(!HommaUtil.getSwipeFlag(appContext)) {
                    tab_relation_library.onPageScrolled(
                            leftValue.toInt(),
                            0.0f,
                            0)
                }
                HommaUtil.setTabNumber(leftValue.toInt(),appContext)
            }

            override fun onStartTrackingTouch(view: RangeSeekBar, isLeft: Boolean) {
                //do what you want!!
                commonNavigator!!.scrollPivotX = 0.5f
                HommaUtil.setSwipeFlag(false,appContext)
            }

            override fun onStopTrackingTouch(view: RangeSeekBar, isLeft: Boolean) {
                //do what you want!!

                ultra_viewpager!!.setCurrentItem(
                        HommaUtil.getTabNumber(appContext),
                        true
                )
                HommaUtil.setSwipeFlag(false,appContext)
            }
        })
    }

    private fun initUI() {
        // TODO: InfiniteLoopのバグが修正できたら
        // FIXME: InfiniteLoop処理にバグあり。(きちんとpositionの値を取得できていない。)
//        loopCheckBox = loop
//        loopCheckBox!!.setOnCheckedChangeListener(this)

        autoScrollCheckBox = autoscroll
        autoScrollCheckBox!!.setOnCheckedChangeListener(this)
    }
}
