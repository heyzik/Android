package com.example.musicapplication.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.musicapplication.databinding.FragmentAlbumsPlaylistBinding

class AlbumsPlaylistFragment : Fragment() {
    private lateinit var binding: FragmentAlbumsPlaylistBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumsPlaylistBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = AlbumsPlaylistFragment()
    }
}