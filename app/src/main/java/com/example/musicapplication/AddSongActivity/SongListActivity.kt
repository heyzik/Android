package com.example.musicapplication.AddSongActivity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapplication.DataBase.SongDb
import com.example.musicapplication.DataBase.SongInstance
import com.example.musicapplication.SongAdapter
import com.example.musicapplication.R
import com.example.musicapplication.databinding.ActivitySongListBinding

class SongListActivity : AppCompatActivity(), SongAdapter.Listener, AddSongAdapter.Listener {
    private lateinit var binding: ActivitySongListBinding
    private val adapter = AddSongAdapter(this)
    private var songList = ArrayList<SongInstance>()

    private var songs = arrayListOf(
        SongInstance(0, R.drawable.song, "First song", "First author", R.raw.fairytale),
        SongInstance(1, R.drawable.song, "Second song", "Second author", R.raw.arcade),
        SongInstance(2, R.drawable.song, "Third song", "Third author", R.raw.come_alive),
        SongInstance(3, R.drawable.song, "Fourth song", "Fourth author", R.raw.my_type),
        SongInstance(4, R.drawable.song, "Fifth song", "Fifth author", R.raw.heat_waves)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySongListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar()
        init()
    }

    private fun init() {
        activityData()
        search()
    }

    private fun toolbar() {
        binding.apply {
            songListInclude.buttonBack.setOnClickListener {
                finish()
            }

            songListInclude.songListButton.visibility = View.GONE
        }
    }

    private fun search() {
        val searchView = binding.searchView
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun filterList(text: String?) {
        val filteredList: ArrayList<SongInstance> = ArrayList()
        for (song in songList) {
            if (song.title.lowercase().contains(text!!.lowercase())) {
                filteredList.add(song)
            }
        }

        if (filteredList.isEmpty()) {
            adapter.clear()
            binding.noSongsFound.visibility = View.VISIBLE
        }
        else {
            adapter.editSongList(filteredList)
            binding.noSongsFound.visibility = View.GONE
        }
    }

    private fun activityData() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@SongListActivity)
            recyclerView.adapter = adapter
        }

        for (song in songs) {
            songList.add(song)
            val db = SongDb.getDb(this@SongListActivity)
            db.getDao().getSongById(song.id!!).asLiveData().observe(this@SongListActivity) { it ->
                if (it != null) {
                    songList.remove(song)
                }
            }
        }
        adapter.addSongs(songList)
    }

    override fun onClick(song: SongInstance) {
        val db = SongDb.getDb(this)
        Thread {
            db.getDao().insertSong(song)
        }.start()
        adapter.removeSong(song)
    }
}