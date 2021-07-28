package com.app.weather.presentation.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.app.weather.R
import com.app.weather.databinding.ActivityMainBinding
import com.app.weather.presentation.core.BaseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        setTransparentStatusBar()
        setupNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.container_fragment)

        setupWithNavController(
            binding.toolbar,
            navController,
            AppBarConfiguration(setOf(R.id.dashboardFragment))
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.destinationId = destination.id
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuItemSearch -> {
                findNavController(R.id.container_fragment).navigate(R.id.searchFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            findNavController(R.id.container_fragment),
            AppBarConfiguration(setOf(R.id.dashboardFragment))
        )
    }

    fun setToolbarTitle(s: String?) {
        s?.let { binding.textViewToolbarTitle.text = s }
    }
}
