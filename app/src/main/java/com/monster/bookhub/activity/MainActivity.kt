package com.monster.bookhub.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.monster.bookhub.R
import com.monster.bookhub.databinding.ActivityMainBinding
import com.monster.bookhub.fragment.AboutAppFragment
import com.monster.bookhub.fragment.DashboardFragment
import com.monster.bookhub.fragment.FavouritesFragment
import com.monster.bookhub.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()

        openDashboard()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener {
            previousMenuItem?.isChecked = false

            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when (it.itemId) {
                R.id.dashboard -> {
                    openDashboard()
                    binding.drawerLayout.closeDrawers()
                }
                R.id.favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FavouritesFragment())
                        .commit()
                    supportActionBar?.title = "Favourites"
                    binding.drawerLayout.closeDrawers()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, ProfileFragment())
                        .commit()
                    supportActionBar?.title = "Profile"
                    binding.drawerLayout.closeDrawers()
                }
                R.id.aboutApp -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, AboutAppFragment())
                        .commit()
                    supportActionBar?.title = "About App"
                    binding.drawerLayout.closeDrawers()
                }
            }
            true
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                binding.drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openDashboard() {
        val fragment = DashboardFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragment)
            .commit()
        supportActionBar?.title = "MyBooks"
        binding.navigationView.setCheckedItem(R.id.dashboard)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        if (frag !is DashboardFragment) {
            openDashboard()
        } else {
            super.onBackPressed()
        }
    }
}