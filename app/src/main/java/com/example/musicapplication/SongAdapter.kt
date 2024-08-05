package com.example.musicapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapplication.DataBase.SongInstance
import com.example.musicapplication.databinding.SongInstanceBinding

class SongAdapter(private val listener: Listener): RecyclerView.Adapter<SongAdapter.SongHolder>() {
    private var songList = ArrayList<SongInstance>()

    class SongHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = SongInstanceBinding.bind(item)
        fun bind(song: SongInstance, listener: Listener) = with(binding) {
            songLogo.setImageResource(song.image)
            songTitle.text = song.title
            songAuthor.text = song.author

            holder.setOnClickListener {
                listener.onClick(song)
            }

            removeSongButton.setOnClickListener {
                listener.onClickRemove(song)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_instance, parent, false)
        return SongHolder(view)
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        holder.bind(songList[position], listener)

    }

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<SongInstance>) {
        songList.clear()
        songList = songs
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun editSongList(filteredList: ArrayList<SongInstance>) {
        songList = filteredList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        songList.clear()
        notifyDataSetChanged()
    }

    fun removeSong(song: SongInstance) {
        songList.remove(song)
    }

    interface Listener {
        fun onClick(song: SongInstance) {}
        fun onClickRemove(song: SongInstance) {}
    }

}