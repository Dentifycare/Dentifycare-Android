package com.dentify.dentifycare.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavArgument
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dentify.dentifycare.R
import com.dentify.dentifycare.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val role = intent.getStringExtra("ROLE")
        setupNavigation(role)
    }

    private fun setupNavigation(role: String?) {
        val navView: BottomNavigationView = binding.navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(
            if (role == "Patient") R.navigation.navigation_patient else R.navigation.navigation_co_ass
        )

        val name = intent.getStringExtra("NAME")

        val bundle = Bundle().apply {
            putString("NAME", name)
            putString("ROLE", role)
        }

        navGraph.addArgument("NAME", NavArgument.Builder().setDefaultValue(name).build())
        navGraph.addArgument("ROLE", NavArgument.Builder().setDefaultValue(role).build())

        navController.setGraph(navGraph, bundle)
        navView.setupWithNavController(navController)
    }
}