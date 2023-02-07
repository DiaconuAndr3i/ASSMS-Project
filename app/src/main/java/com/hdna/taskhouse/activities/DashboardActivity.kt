package com.hdna.taskhouse.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hdna.taskhouse.R
import com.hdna.taskhouse.fragments.*

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val homeFragment = HomeFragment()
        val calendarFragment = CalendarFragment()
        val addFragment = AddFragment()
        val extraFragment = ExtraFragment()
        val settingsFragment = SettingsFragment()

        setCurrentFragment(homeFragment)
        var bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomBar)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.myHome ->setCurrentFragment(homeFragment)
                R.id.myCalendar ->setCurrentFragment(calendarFragment)
                R.id.myAdd ->setCurrentFragment(addFragment)
                R.id.myUpdates ->setCurrentFragment(extraFragment)
                R.id.mySettings ->setCurrentFragment(settingsFragment)
            }
            true
        }


    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
    }


}