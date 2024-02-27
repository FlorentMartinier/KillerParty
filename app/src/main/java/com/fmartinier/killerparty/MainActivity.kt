package com.fmartinier.killerparty

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.fmartinier.killerparty.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissions()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController =
            findNavController(com.fmartinier.killerparty.R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_party,
                R.id.navigation_historic,
                R.id.navigation_challenges
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun checkPermissions() {
        val permissionsNeeded: List<String> =
            PERMISSIONS_NEEDED
                .filter {
                    ContextCompat.checkSelfPermission(
                        this,
                        it
                    ) != PackageManager.PERMISSION_GRANTED
                }
                .filter { !ActivityCompat.shouldShowRequestPermissionRationale(this, it) }

        requestPermissions(permissionsNeeded)
    }

    private fun requestPermissions(permissions: List<String>) {
        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissions.toTypedArray(),
                REQUEST_CODE
            )
        }
    }

    companion object {
        const val REQUEST_CODE = 100
        val PERMISSIONS_NEEDED = listOf(
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.READ_CONTACTS
        )
    }
}