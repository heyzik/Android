package com.example.musicapplication.SongActivity

import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.example.musicapplication.DataBase.SongDb
import com.example.musicapplication.DataBase.SongInstance
import com.example.musicapplication.R
import com.example.musicapplication.databinding.ActivitySongBinding


class SongActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySongBinding
    private lateinit var runnable: Runnable
    private var handler = Handler()
    private var mediaPlayer : MediaPlayer? = null
    private var songId: Int? = null
    private var repeatId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar()

        songId = intent.getIntExtra("songId", 0)
        val db = SongDb.getDb(this)
        db.getDao().getSongById(songId!!).asLiveData().observe(this@SongActivity) {
            activityData(it, false)

            playAndPause()
            nextSong()
            previousSong()

            repeatSong()

            mediaPlayer()
        }
    }

    private fun toolbar() {
        binding.apply {
            songActivityInclude.buttonBack.setOnClickListener {
                finish()
                mediaPlayer!!.stop()
            }

            songActivityInclude.songListButton.visibility = View.GONE
        }
    }

    private fun activityData(songInstance: SongInstance, songState: Boolean = true) {
        if (songState) { mediaPlayer!!.pause() }
        mediaPlayer = MediaPlayer.create(this, songInstance.song)
        songId = songInstance.id
        repeatSong()

        binding.apply {
            songImage.setImageResource(songInstance.image)
            songName.text = songInstance.title
            songCreater.text = songInstance.author


            mediaPlayer!!.setOnCompletionListener {
                if (repeatId != 1) {
                    buttonPlayAndPause.setImageResource(R.drawable.ic_play)
                    progressBar.progress = 0
                    nextSong(false)
                }
            }
        }

        if (songState) { mediaPlayer!!.start() }
    }

    private fun getSong(nextSong: Boolean) {
        val db = SongDb.getDb(this)
        if (nextSong) {
            db.getDao().getNextSong(songId!!).asLiveData()
                .observe(this@SongActivity) { songInstance ->
                    activityData(songInstance)
                }
        } else {
            db.getDao().getPreviousSong(songId!!).asLiveData()
                .observe(this@SongActivity) { songInstance ->
                    activityData(songInstance)
                }
        }
    }

    private fun playAndPause() {
        binding.apply {
            buttonPlayAndPause.setOnClickListener {
                if (!mediaPlayer!!.isPlaying) {
                    mediaPlayer!!.start()
                    buttonPlayAndPause.setImageResource(R.drawable.ic_pause)
                }
                else {
                    mediaPlayer!!.pause()
                    buttonPlayAndPause.setImageResource(R.drawable.ic_play)
                }
            }
        }
    }

    private fun nextSong(state: Boolean = true) {
        if (state) {
            binding.buttonNext.setOnClickListener {
                getSong(true)
                binding.buttonPlayAndPause.setImageResource(R.drawable.ic_pause)
            }
        }
        else {
            getSong(true)
            binding.buttonPlayAndPause.setImageResource(R.drawable.ic_pause)
        }
    }

    private fun previousSong() {
        binding.buttonPrevious.setOnClickListener {
            getSong(false)
            binding.buttonPlayAndPause.setImageResource(R.drawable.ic_pause)
        }
    }

    private fun repeatSong() {
        val repeatStates = listOf(
            R.drawable.ic_repeat,
            R.drawable.ic_repeat_on,
        )

        binding.apply {
            buttonRepeat.setOnClickListener {
                repeatId += 1
                if (repeatId == repeatStates.size) repeatId = 0
                it.setBackgroundResource(repeatStates[repeatId])
                mediaPlayer!!.isLooping = repeatId == 1
            }
        }

        mediaPlayer!!.isLooping = repeatId == 1
    }

    private fun mediaPlayer() {
        binding.apply {
            progressBar.progress = 0
            progressBar.max = mediaPlayer!!.duration
            progressBar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        position: Int,
                        changed: Boolean
                    ) {
                        if (changed) {
                            mediaPlayer!!.seekTo(position)
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                }
            )

            runnable = Runnable {
                progressBar.progress = mediaPlayer!!.currentPosition
                handler.postDelayed(runnable, 1000)
            }
            handler.postDelayed(runnable, 1000)
        }
    }
}


