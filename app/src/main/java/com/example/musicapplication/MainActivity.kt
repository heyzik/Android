package com.example.musicapplication

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.marginLeft
import com.example.musicapplication.Fragments.AlbumsPlaylistFragment
import com.example.musicapplication.Fragments.SongsPlaylistFragment
import com.example.musicapplication.AddSongActivity.SongListActivity
import com.example.musicapplication.DataBase.SongDb
import com.example.musicapplication.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.util.Locale


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

    private fun toolbar() {
        binding.apply {
            mainActivityInclude.buttonBack.visibility = View.GONE
            val appTitle = mainActivityInclude.appTitle.layoutParams as ViewGroup.MarginLayoutParams
            appTitle.setMargins(0,0,0,0)
            mainActivityInclude.appTitle.layoutParams = appTitle

            mainActivityInclude.songListButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, SongListActivity::class.java))
            }
        }
    }

    private fun viewPager() {
        val fragmentList = listOf(
            SongsPlaylistFragment.newInstance(),
            AlbumsPlaylistFragment.newInstance()
        )

        val fragListTitles = listOf(
            "Songs",
            "Albums"
        )

        val adapter = ViewPagerAdapter(this, fragmentList)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) {
                tab, pos -> tab.text = fragListTitles[pos]
        }.attach()
    }
}