package org.michaelbel.tjgram.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appbar.*
import org.michaelbel.tjgram.R
import org.michaelbel.tjgram.data.UserConfig
import org.michaelbel.tjgram.data.enums.NEW
import org.michaelbel.tjgram.ui.main.MainFragment
import org.michaelbel.tjgram.ui.profile.ProfileFragment
import org.michaelbel.tjgram.utils.DeviceUtil
import org.michaelbel.tjgram.utils.ViewUtil

class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener, MainFragment.Listener {

    companion object {
        const val REQUEST_CODE_NEW_ENTRY = 201
        const val NEW_ENTRY_RESULT = "new_entry_created"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_NEW_ENTRY) {
            if (data == null) {
                return
            }

            val isEntryCreated = data.getBooleanExtra(NEW_ENTRY_RESULT, false)
            if (isEntryCreated) {
                // TODO Переключиться на mainFragment и обновить адаптер
                Toast.makeText(this, R.string.msg_posted_successfully, Toast.LENGTH_LONG).show()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun setTheme(resid: Int) {
        super.setTheme(R.style.AppTheme)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= 21) {
            appbar.stateListAnimator = null
        }
        ViewCompat.setElevation(appbar, DeviceUtil.dp(this, 1.5F).toFloat())
        setSupportActionBar(toolbar)

        bottom_navigation.setOnNavigationItemSelectedListener(this)
        if (savedInstanceState == null) {
            bottom_navigation.selectedItemId = R.id.navigation_main
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val menu = bottom_navigation.menu
        menu.findItem(R.id.navigation_main).setIcon(if (menuItem.itemId == R.id.navigation_main) R.drawable.ic_view_dashboard else R.drawable.ic_view_dashboard_outline)
        menu.findItem(R.id.navigation_user).setIcon(if (menuItem.itemId == R.id.navigation_user) R.drawable.ic_account else R.drawable.ic_account_outline)

        when {
            menuItem.itemId == R.id.navigation_main -> {
                setActionBar(R.string.app_name, SCROLL_FLAG_SCROLL or SCROLL_FLAG_ENTER_ALWAYS)
                replaceFragment(MainFragment.newInstance(NEW))
            }
            menuItem.itemId == R.id.navigation_add -> {
                // FIXME 2 item не должен перехватывать фокус.
                //bottom_navigation.selectedItemId = bottom_navigation.selectedItemId ?!
                if (!UserConfig.isAuthorized(this)) {
                    showLoginSnack()
                } else {
                    startActivityForResult(Intent(this@MainActivity, NewPostActivity::class.java), REQUEST_CODE_NEW_ENTRY)
                }
                return false
            }
            menuItem.itemId == R.id.navigation_user -> {
                setActionBar(R.string.profile, 0)
                replaceFragment(ProfileFragment.newInstance())
            }
        }

        return true
    }

    override fun showLoginSnack() {
        val snack = Snackbar.make(main_coordinator, R.string.msg_login_first, Snackbar.LENGTH_LONG)

        val params = snack.view.layoutParams as CoordinatorLayout.LayoutParams
        val margin = DeviceUtil.dp(this, 4F)
        params.setMargins(margin, 0, margin, bottom_navigation.height + margin)
        snack.view.layoutParams = params

        snack.setAction(R.string.action_go) { bottom_navigation.selectedItemId = R.id.navigation_user }
        snack.show()
    }

    private fun setActionBar(textRes: Int, flags: Int) {
        supportActionBar!!.setTitle(textRes)
        ViewUtil.setScrollFlags(toolbar, flags)
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(R.id.fragment_view, fragment)
        transaction.commit()
    }
}