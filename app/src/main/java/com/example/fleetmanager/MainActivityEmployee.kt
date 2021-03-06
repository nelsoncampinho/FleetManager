package com.example.fleetmanager

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.fleetmanager.uiEmployee.dashboard.DashboardEmployeeFragment
import com.example.fleetmanager.uiEmployee.garage.GarageEmployeeFragment
import com.example.fleetmanager.uiEmployee.profile.ProfileEmployeeFragment
import com.example.fleetmanager.uiEmployee.trips.TripsEmployeeFragment
import com.example.fleetmanager.uiManagement.employees.EmployeesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
//import com.example.fleetmanager.uiEmployee.dashboard.

class MainActivityEmployee : AppCompatActivity() {
    private lateinit var bottomNavigationView : BottomNavigationView
    private var isPlay: Boolean = false
    private lateinit var playStopTrip : FloatingActionButton
    private lateinit var sharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_employee)

        sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )

        playStopTrip = findViewById(R.id.playStopTrip)

        val TAG = "MainActivityEmployee"

        val garageFragment = GarageEmployeeFragment()
        val dashboardFragment = DashboardEmployeeFragment()
        val profileFragment = ProfileEmployeeFragment()
        val tripsFragment = TripsEmployeeFragment()

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationViewEmployee)
        bottomNavigationView.background = null
        setCurrentFragment(dashboardFragment)


        isPlay = sharedPref.getBoolean("isPlay", false)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_dashboard_employee -> {
                    setCurrentFragment(dashboardFragment)
                    Log.i("aa", "Home Selected")
                    badgeClear(R.id.navigation_dashboard_employee)
                }
                R.id.navigation_garage_employee -> {
                    setCurrentFragment(garageFragment)
                    Log.i(TAG, "garage Selected")
                    badgeClear(R.id.navigation_garage_employee)
                }
                R.id.navigation_trips_employee -> {
                    setCurrentFragment(tripsFragment)
                    Log.i(TAG, "trips Selected")
                    badgeClear(R.id.navigation_trips_employee)
                }
                R.id.navigation_profile_employee -> {
                    setCurrentFragment(profileFragment)
                    Log.i(TAG, "profile Selected")
                    badgeClear(R.id.navigation_profile_employee)
                }
            }
            true
        }



    }


    private fun badgeClear(id: Int) {
        val badgeDrawable = bottomNavigationView.getBadge(id)
        if (badgeDrawable != null) {
            badgeDrawable.isVisible = false
            badgeDrawable.clearNumber()
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        if(intent.getStringExtra("DESTINATION") != null){

            val destination = intent.getStringExtra("DESTINATION")
            val origin_lat = intent.getDoubleExtra("ORI_LAT", 0.0)
            val origin_lng = intent.getDoubleExtra("ORI_LNG", 0.0)

            var bundles = Bundle()
            bundles.putString("DESTINATION", destination)
            bundles.putDouble("ORIGIN_LAT", origin_lat)
            bundles.putDouble("ORIGIN_LNG", origin_lng)


            fragment.arguments = bundles
            playStopTrip.setImageDrawable(resources.getDrawable(R.drawable.ic_stop))
    supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl_wrapper_employee, fragment)
                commit()
            }
        }else{
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl_wrapper_employee, fragment)
                commit()
            }
        }



    fun playStopTrip(view: View) {

        if(isPlay){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                playStopTrip.setImageDrawable(resources.getDrawable(R.drawable.ic_stop, theme))

            } else {
                playStopTrip.setImageDrawable(resources.getDrawable(R.drawable.ic_stop))
            }
            val i = Intent(this, StartTripActivity::class.java)
            startActivity(i)

            with(sharedPref.edit()) {
                putBoolean("isPlay", false)
                commit()
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                playStopTrip.setImageDrawable(resources.getDrawable(R.drawable.ic_play, theme))
            } else {
                playStopTrip.setImageDrawable(resources.getDrawable(R.drawable.ic_play))
            }

            with(sharedPref.edit()) {
                putBoolean("isPlay", true)
                commit()
            }

            with(sharedPref.edit()) {
                putBoolean("active", false)
                commit()
            }

            val i = Intent(this, MainActivityEmployee::class.java)
            startActivity(i)
            finish()


        }
    }

    fun replaceFragmentChat(){
        var bundle = Bundle()
        val chat_fragment = EmployeesFragment()
        bundle.putBoolean("admin",false)
        chat_fragment.arguments = bundle
        setCurrentFragment(chat_fragment)
    }

}