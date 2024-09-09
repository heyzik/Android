package com.example.musicapplication.SongListActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapplication.DataBase.SongDb
import com.example.musicapplication.DataBase.SongInstance
import com.example.musicapplication.RemoveSongAdapter
import com.example.musicapplication.SongActivity.SongActivity
import com.example.musicapplication.databinding.ActivitySongListBinding

class SongList : AppCompatActivity(), RemoveSongAdapter.Listener, AddSongAdapter.Listener {
    private lateinit var binding: ActivitySongListBinding
    private val removeSongAdapter = RemoveSongAdapter(this)
    private val addSongAdapter = AddSongAdapter(this)
    private var songList = ArrayList<SongInstance>()
    private var from: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySongListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar()
        search()
        activityData()
    }

    private fun toolbar() = with(binding) {
        songListActivityInclude.appTitle.visibility = View.GONE
        songListActivityInclude.buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun activityData() = with(binding) {
        from = intent.getStringExtra("expand")
        val db = SongDb.getDb(this@SongList)
        if (from == "home") {
            recyclerView.layoutManager = LinearLayoutManager(this@SongList)
            recyclerView.adapter = removeSongAdapter

            db.getDao().getAllSong().asLiveData().observe(this@SongList) {
                removeSongAdapter.addSongs(it as ArrayList<SongInstance>)
                songList = it
            }
        }
        else {
            recyclerView.layoutManager = LinearLayoutManager(this@SongList)
            recyclerView.adapter = addSongAdapter

            db.getDao().getAllSongInBrowse().asLiveData().observe(this@SongList) {
                addSongAdapter.addSongs(it as ArrayList<SongInstance>)
                songList = it
            }
        }
    }

    private fun search() = with(binding) {
        val searchView = searchView
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
            if (song.title.lowercase().filterNot { it.isWhitespace() }.contains(text!!.lowercase())) {
                filteredList.add(song)
            }
        }

        if (from == "home") {
            if (filteredList.isEmpty()) removeSongAdapter.clear()
            else removeSongAdapter.editSongList(filteredList)
        }
        else {
            if (filteredList.isEmpty()) addSongAdapter.clear()
            else addSongAdapter.editSongList(filteredList)
        }
    }

    private fun startSongActivity(song: SongInstance, from: Int) {
        val data = arrayListOf(song.id, from)
        startActivity(Intent(this, SongActivity::class.java).apply {
            putExtra("songId", data)
        })
    }

    override fun onClickSong(song: SongInstance) {
        startSongActivity(song, 0)
    }

    override fun onClickShowSong(song: SongInstance) {
        startSongActivity(song, 1)
    }

    override fun onClickAddSong(song: SongInstance) {
        val db = SongDb.getDb(this@SongList)
        Thread {
            db.getDao().insertSongInHome(song)
            db.getDao().deleteSongFromBrowse(song)
        }.start()
        addSongAdapter.removeSong(song)
    }

    override fun onClickRemoveSong(song: SongInstance) {
        val db = SongDb.getDb(this@SongList)
        Thread {
            db.getDao().insertSongInBrowse(song)
            db.getDao().deleteSongFromHome(song)
        }.start()
        removeSongAdapter.removeSong(song)
    }
}