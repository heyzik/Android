package com.example.musicapplication.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapplication.SongListActivity.AddSongAdapter
import com.example.musicapplication.DataBase.SongDb
import com.example.musicapplication.DataBase.SongInstance
import com.example.musicapplication.R
import com.example.musicapplication.SongActivity.SongActivity
import com.example.musicapplication.SongListActivity.SongList
import com.example.musicapplication.databinding.FragmentTemplateBinding

class BrowseFragment : Fragment(), SongAdapter.Listener, AddSongAdapter.Listener {
    private lateinit var binding: FragmentTemplateBinding
    private val songAdapter = SongAdapter(this)
    private var songList = ArrayList<SongInstance>()

    var songs = arrayListOf(
        SongInstance(0, R.drawable.song, "First song", "First author", R.raw.fairytale),
        SongInstance(1, R.drawable.song, "Second song", "Second author", R.raw.arcade),
        SongInstance(2, R.drawable.song, "Third song", "Third author", R.raw.come_alive),
        SongInstance(3, R.drawable.song, "Fourth song", "Fourth author", R.raw.my_type),
        SongInstance(4, R.drawable.song, "Fifth song", "Fifth author", R.raw.heat_waves)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTemplateBinding.inflate(layoutInflater)
        init()
        return binding.root
    }

    private fun init() {
        fragmentData()
        search()
    }

    companion object {
        @JvmStatic
        fun newInstance() = BrowseFragment()
    }

    private fun fragmentData() = with(binding) {
        songsRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        songsRecyclerView.adapter = songAdapter

        val db = SongDb.getDb(requireContext())
        db.getDao().getAllSongInBrowse().asLiveData().observe(requireActivity()) {
            songAdapter.addSongs(it as ArrayList<SongInstance>)
            songList = it
            if (it.isEmpty()) {
                content.visibility = View.GONE
                firstEmptyView.visibility = View.VISIBLE
            }
            else {
                content.visibility = View.VISIBLE
                firstEmptyView.visibility = View.GONE
            }
        }

        buttonExpandSongs.setOnClickListener {
            startActivity(Intent(activity, SongList::class.java).apply {
                putExtra("expand", "browse")
            })
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

        if (filteredList.isEmpty()) songAdapter.clear()
        else songAdapter.editSongList(filteredList)
    }

    override fun onClick(song: SongInstance) {
        val data = arrayListOf(song.id, 1)
        startActivity(Intent(activity, SongActivity::class.java).apply {
            putExtra("songId", data)
        })
    }
}