package com.unipi.mpsp21043.emarket.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.unipi.mpsp21043.emarket.R
import com.unipi.mpsp21043.emarket.adapters.ViewPagerMainAdapter
import com.unipi.mpsp21043.emarket.database.FirestoreHelper
import com.unipi.mpsp21043.emarket.databinding.ActivityMainBinding
import com.unipi.mpsp21043.emarket.models.User
import com.unipi.mpsp21043.emarket.service.MyFirebaseMessagingService
import com.unipi.mpsp21043.emarket.utils.Constants
import com.unipi.mpsp21043.emarket.utils.IntentUtils
import com.unipi.mpsp21043.emarket.utils.SnackBarSuccessClass
import java.util.*


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()

        init()
    }

    private fun setupUI() {
        setUpTabs()
        setupActionBar()
        setupNavDrawer()

        binding.toolbar.actionBarImgBtnMyCart.setOnClickListener { IntentUtils().goToListCartItemsActivity(this) }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar.root)
        val actionBar = supportActionBar
        actionBar?.let {
            it.setDisplayShowCustomEnabled(true)
            it.setCustomView(R.layout.toolbar_activity_main)
        }
    }

    private fun setupNavDrawer() {
        binding.apply {
            val toggle = ActionBarDrawerToggle(
                this@MainActivity, drawerLayout, toolbar.root,
                R.string.nav_drawer_close, R.string.nav_drawer_close
            )
            drawerLayout.addDrawerListener(toggle)
            // Change drawer arrow icon
            toggle.drawerArrowDrawable.color =
                ContextCompat.getColor(this@MainActivity, R.color.colorTabSelected)
            // Set navigation arrow icon
            toggle.setHomeAsUpIndicator(R.drawable.ic_list)
            toggle.syncState()

            if (FirestoreHelper().getCurrentUserID().isEmpty()) {
                navView.inflateHeaderView(R.layout.nav_drawer_header_not_signed_in)
                navView.getHeaderView(0)
                    .findViewById<MaterialButton>(R.id.btn_NavView_Sign_In)
                    .setOnClickListener{ goToSignInActivity(this@MainActivity) }
            }
            else {
                navView.inflateHeaderView(R.layout.nav_drawer_header_signed_in)
                FirestoreHelper().getUserDetails(this@MainActivity)
            }
            navView.setNavigationItemSelectedListener(this@MainActivity)
            navView.setCheckedItem(R.id.nav_drawer_item_products)
        }
    }

    private fun init() {
        if (FirestoreHelper().getCurrentUserID() != "") {
            FirestoreHelper().getUserFCMRegistrationToken(this)
        }

        if (intent.hasExtra(Constants.EXTRA_SHOW_ORDER_PLACED_SNACKBAR)
            && intent.getBooleanExtra(Constants.EXTRA_SHOW_ORDER_PLACED_SNACKBAR, false)) {
            SnackBarSuccessClass
                .make(binding.root, getString(R.string.txt_order_placed_successfully))
                .show()
        }
    }

    private fun setUpTabs() {
        val adapter = ViewPagerMainAdapter(supportFragmentManager, lifecycle)

        binding.apply {
            viewPagerHomeActivity.adapter = adapter

            TabLayoutMediator(tabs, viewPagerHomeActivity){tab, position ->
                when (position) {
                    0 -> tab.setIcon(R.drawable.ic_food_stand)
                    1 -> tab.setIcon(R.drawable.ic_heart)
                    2 -> tab.setIcon(R.drawable.ic_user_circle)
                }
            }.attach()

            tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    when (tab.position) {
                        0 -> {
                            toolbar.textViewActionBarHeader.text = getString(R.string.txt_store)
                            navView.setCheckedItem(R.id.nav_drawer_item_products)
                        }
                        1 -> {
                            toolbar.textViewActionBarHeader.text = getString(R.string.txt_favorite_products)
                            navView.setCheckedItem(R.id.nav_drawer_item_favorites)
                        }
                        2 -> {
                            toolbar.textViewActionBarHeader.text = getString(R.string.txt_my_account)
                            navView.setCheckedItem(R.id.nav_drawer_item_profile)
                        }
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        when (item.itemId) {
            R.id.nav_drawer_item_products -> binding.tabs.getTabAt(0)?.select()
            R.id.nav_drawer_item_favorites -> binding.tabs.getTabAt(2)?.select()
            R.id.nav_drawer_item_cart -> {
                IntentUtils().goToListCartItemsActivity(this)
                return false
            }
            R.id.nav_drawer_item_profile -> binding.tabs.getTabAt(3)?.select()
            R.id.nav_drawer_item_orders -> {
                IntentUtils().goToListOrdersActivity(this)
                return false
            }
            R.id.nav_drawer_item_exit -> ActivityCompat.finishAffinity(this)
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_Layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun userDetailsSuccess(userInfo: User) {
        binding.apply {
            navView.getHeaderView(0).apply {
                findViewById<TextView>(R.id.navDrawer_SignedIn_Full_Name)
                    .text = userInfo.fullName
                findViewById<TextView>(R.id.navDrawer_SignedIn_Email)
                    .text = userInfo.email
            }
        }
    }

    fun userFcmRegistrationTokenSuccess(token: String) {
        MyFirebaseMessagingService.addTokenToFirestore(token)
        Log.e(Constants.TAG, "New token: $token")
    }

    override fun getOnBackInvokedDispatcher(): OnBackInvokedDispatcher {
        doubleBackToExit()
        return super.getOnBackInvokedDispatcher()
    }
}