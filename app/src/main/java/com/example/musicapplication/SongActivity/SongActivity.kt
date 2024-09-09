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
    private var originalSong: SongInstance? = null
    private var repeatId: Int = 0
    private var originalSongId: Int? = null
    private var songFrom: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar()

        val dataFromFragment = intent.getIntegerArrayListExtra("songId")
        originalSongId = dataFromFragment!!.first()
        songFrom = dataFromFragment.last()
        getData()
    }

    private fun toolbar() = with(binding) {
        songActivityInclude.buttonBack.setOnClickListener {
            finish()
            mediaPlayer!!.stop()
        }
        songActivityInclude.appTitle.visibility = View.GONE
    }

    private fun songFunctionality(song: SongInstance) {
        originalSong = song
        activityData(song, false)

        playAndPause()
        nextSong()
        previousSong()
        repeatSong()
        mediaPlayer()
    }

    private fun getData() {
        val db = SongDb.getDb(this)
        if (songFrom == 0) {
            db.getDao().getSongByIdFromHome(originalSongId!!).asLiveData().observe(this@SongActivity) {
                songFunctionality(it)
            }
        }
        else {
            db.getDao().getSongByIdFromBrowse(originalSongId!!).asLiveData().observe(this@SongActivity) {
                songFunctionality(it)
            }
        }
    }

    private fun activityData(songInstance: SongInstance, songState: Boolean = true) {
        if (songState) mediaPlayer!!.stop()
        mediaPlayer = MediaPlayer.create(this, songInstance.song)
        originalSongId = songInstance.id
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
        if (songFrom == 0) {
            if (nextSong) {
                db.getDao().getNextSongFromHome(originalSongId!!).asLiveData()
                    .observe(this@SongActivity) { songInstance ->
                        if (songInstance == null) activityData(originalSong!!)
                        else activityData(songInstance)
                    }
            } else {
                db.getDao().getPreviousSongFromHome(originalSongId!!).asLiveData()
                    .observe(this@SongActivity) { songInstance ->
                        if (songInstance != null) activityData(songInstance)
                    }
            }
        }
        else {
            if (nextSong) {
                db.getDao().getNextSongFromBrowse(originalSongId!!).asLiveData()
                    .observe(this@SongActivity) { songInstance ->
                        if (songInstance == null) activityData(originalSong!!)
                        else activityData(songInstance)
                    }
            } else {
                db.getDao().getPreviousSongFromBrowse(originalSongId!!).asLiveData()
                    .observe(this@SongActivity) { songInstance ->
                        if (songInstance != null) activityData(songInstance)
                    }
            }
        }
    }

    private fun playAndPause() = with(binding) {
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

    private fun nextSong(state: Boolean = true) = with(binding) {
        if (state) {
            buttonNext.setOnClickListener {
                getSong(true)
                buttonPlayAndPause.setImageResource(R.drawable.ic_pause)
            }
        }
        else {
            getSong(true)
            buttonPlayAndPause.setImageResource(R.drawable.ic_pause)
        }
    }

    private fun previousSong() = with(binding) {
        buttonPrevious.setOnClickListener {
            getSong(false)
            buttonPlayAndPause.setImageResource(R.drawable.ic_pause)
        }
    }

    private fun repeatSong() = with(binding) {
        val repeatStates = listOf(
            R.drawable.ic_repeat,
            R.drawable.ic_repeat_on,
        )

        buttonRepeat.setOnClickListener {
            repeatId += 1
            if (repeatId == repeatStates.size) repeatId = 0
            it.setBackgroundResource(repeatStates[repeatId])
            mediaPlayer!!.isLooping = repeatId == 1
        }

        mediaPlayer!!.isLooping = repeatId == 1
    }

    private fun mediaPlayer() = with(binding) {
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


