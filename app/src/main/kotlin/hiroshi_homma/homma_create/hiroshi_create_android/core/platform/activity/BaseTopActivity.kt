package hiroshi_homma.homma_create.hiroshi_create_android.core.platform.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import hiroshi_homma.homma_create.hiroshi_create_android.R
import hiroshi_homma.homma_create.hiroshi_create_android.R.id
import hiroshi_homma.homma_create.hiroshi_create_android.R.layout
import hiroshi_homma.homma_create.hiroshi_create_android.core.extension.inTransaction
import hiroshi_homma.homma_create.hiroshi_create_android.core.platform.fragment.BaseFragment
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.top_activity_layout.*

abstract class BaseTopActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.top_activity_layout)
        setSupportActionBar(toolbar)
        addFragment(savedInstanceState)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        (supportFragmentManager.findFragmentById(
                id.fragmentContainer) as BaseFragment).onBackPressed()
        super.onBackPressed()
    }

    private fun addFragment(savedInstanceState: Bundle?) =
            savedInstanceState ?: supportFragmentManager.inTransaction { add(
                    id.fragmentContainer, fragment()) }

    abstract fun fragment(): BaseFragment
}