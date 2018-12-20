package hiroshi_homma.homma_create.hiroshi_create_android.core.platform.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import hiroshi_homma.homma_create.hiroshi_create_android.R.id
import hiroshi_homma.homma_create.hiroshi_create_android.R.layout
import hiroshi_homma.homma_create.hiroshi_create_android.core.extension.inTransaction
import hiroshi_homma.homma_create.hiroshi_create_android.core.platform.fragment.BaseFragment
import kotlinx.android.synthetic.main.toolbar.toolbar

abstract class BaseDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_layout)
        setSupportActionBar(toolbar)
        addFragment(savedInstanceState)
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
