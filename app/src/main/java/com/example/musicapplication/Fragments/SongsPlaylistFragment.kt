package com.example.musicapplication.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapplication.AddSongActivity.AddSongAdapter
import com.example.musicapplication.DataBase.SongDb
import com.example.musicapplication.DataBase.SongInstance
import com.example.musicapplication.SongActivity.SongActivity
import com.example.musicapplication.SongAdapter
import com.example.musicapplication.databinding.FragmentSongsPlaylistBinding


class SongsPlaylistFragment : Fragment(),  SongAdapter.Listener, AddSongAdapter.Listener {
    private lateinit var binding: FragmentSongsPlaylistBinding
    private val songAdapter = SongAdapter(this)
    private var songList = ArrayList<SongInstance>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongsPlaylistBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    private fun init() {
        fragmentData()
        search()
    }

    companion object {

        @JvmStatic
        fun newInstance() = SongsPlaylistFragment()
    }

    private fun fragmentData() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = songAdapter
        }

        val db = SongDb.getDb(requireContext())
        db.getDao().getAllSong().asLiveData().observe(requireActivity()) {
            songAdapter.addSongs(it as ArrayList<SongInstance>)
            songList = it
        }
    }

    private fun search() {
        val searchView = binding.searchView
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : OnQueryTextListener
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
            songAdapter.clear()
            binding.noSongsFound.visibility = View.VISIBLE
        }
        else {
            songAdapter.editSongList(filteredList)
            binding.noSongsFound.visibility = View.GONE
        }
    }

    override fun onClick(song: SongInstance) {
        startActivity(Intent(activity, SongActivity::class.java).apply {
            putExtra("songId", song.id)
        })
    }

    override fun onClickRemove(song: SongInstance) {
        val db = SongDb.getDb(requireContext())
        Thread {
            db.getDao().deleteSong(song)
        }.start()
    }
}