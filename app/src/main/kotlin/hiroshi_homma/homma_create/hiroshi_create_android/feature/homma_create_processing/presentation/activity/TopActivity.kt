package hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.presentation.activity

import android.content.Context
import android.content.Intent
import android.support.v4.view.GravityCompat
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import hiroshi_homma.homma_create.hiroshi_create_android.R
import hiroshi_homma.homma_create.hiroshi_create_android.core.platform.activity.BaseTopActivity
import hiroshi_homma.homma_create.hiroshi_create_android.feature.homma_create_processing.presentation.fragment.TopFragment
import kotlinx.android.synthetic.main.top_activity_layout.*

class TopActivity : BaseTopActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, TopActivity::class.java)
    }

    override fun fragment() = TopFragment()


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings1 -> {
                Toast.makeText(applicationContext, "action_settings1", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_settings2 -> {
                Toast.makeText(applicationContext, "action_settings2", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_settings3 -> {
                Toast.makeText(applicationContext, "action_settings3", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_settings4 -> {
                Toast.makeText(applicationContext, "action_settings4", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_settings5 -> {
                Toast.makeText(applicationContext, "action_settings5", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_settings6 -> {
                Toast.makeText(applicationContext, "action_settings6", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_settings7 -> {
                Toast.makeText(applicationContext, "action_settings7", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_settings8 -> {
                Toast.makeText(applicationContext, "action_settings8", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                Toast.makeText(applicationContext, "nav_camera", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_gallery -> {
                Toast.makeText(applicationContext, "nav_gallery", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_slideshow -> {
                Toast.makeText(applicationContext, "nav_slideshow", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_manage -> {
                Toast.makeText(applicationContext, "nav_manage", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_share -> {
                Toast.makeText(applicationContext, "nav_share", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_send -> {
                Toast.makeText(applicationContext, "nav_send", Toast.LENGTH_SHORT).show()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

}
