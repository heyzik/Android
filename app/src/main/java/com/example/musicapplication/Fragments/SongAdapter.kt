package com.example.musicapplication.Fragments

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapplication.DataBase.SongInstance
import com.example.musicapplication.R
import com.example.musicapplication.databinding.PreviewInstanceBinding

@SuppressLint("NotifyDataSetChanged")
class SongAdapter(private val listener: Listener): RecyclerView.Adapter<SongAdapter.SongHolder>() {
    private var songList = ArrayList<SongInstance>()

    class SongHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = PreviewInstanceBinding.bind(item)
        fun bind(song: SongInstance, listener: Listener) = with(binding) {
            songLogo.setImageResource(song.image)
            songTitle.text = song.title

            holder.setOnClickListener {
                listener.onClick(song)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.preview_instance, parent, false)
        return SongHolder(view)
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        holder.bind(songList[position], listener)
    }

    fun addSongs(songs: ArrayList<SongInstance>) {
        songList.clear()
        songList = songs
        notifyDataSetChanged()
    }

    fun editSongList(filteredList: ArrayList<SongInstance>) {
        songList = filteredList
        notifyDataSetChanged()
    }

    fun clear() {
        songList.clear()
        notifyDataSetChanged()
    }

    interface Listener {
        fun onClick(song: SongInstance) {}
    }

}