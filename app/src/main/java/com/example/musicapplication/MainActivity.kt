package com.example.musicapplication

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapplication.Fragments.BrowseFragment
import com.example.musicapplication.Fragments.HomeFragment
import com.example.musicapplication.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar()
        viewPager()
    }

    private fun toolbar() = with(binding) {
        mainActivityInclude.buttonBack.visibility = View.GONE
        val appTitle = mainActivityInclude.appTitle.layoutParams as ViewGroup.MarginLayoutParams
        appTitle.setMargins(0,0,0,0)
        mainActivityInclude.appTitle.layoutParams = appTitle
    }

    private fun viewPager() = with(binding) {
        val fragmentList = listOf(
            HomeFragment.newInstance(),
            BrowseFragment.newInstance()
        )

        val fragListTitles = listOf(
            "Home",
            "Browse"
        )

        val adapter = ViewPagerAdapter(this@MainActivity, fragmentList)
        viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) {
                tab, pos -> tab.text = fragListTitles[pos]
        }.attach()
        viewPager.setUserInputEnabled(false)
    }
}