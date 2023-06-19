package com.tlu.audiobasedlearning

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alan.alansdk.button.AlanButton

class MediaPlayerActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var handler: Handler? = null
    private var playButton: Button? = null
    private var pauseButton: Button? = null
    private var audioCurrentProgress: TextView? = null
    private var audioLength: TextView? = null
    private var nowPlaying: TextView? = null
    private var seekBar: SeekBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mediaplayer)

        ActivityBase.currentActivity = this

        val fileuri = intent.getStringExtra("file_uri")

        mediaPlayer = if (fileuri == null) {
            MediaPlayer.create(this, R.raw.cocktails)
        } else {
            MediaPlayer.create(this, Uri.parse(fileuri))
        }

        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)
        audioCurrentProgress = findViewById(R.id.audioCurrentProgress)
        audioLength = findViewById(R.id.audioLength)
        nowPlaying = findViewById(R.id.nowPlayingText)
        seekBar = findViewById(R.id.audioSeekBar)
        handler = Handler(Looper.getMainLooper())

        pauseButton!!.isEnabled = false
        nowPlaying!!.text = if (fileuri == null) {
            resources.getResourceEntryName(R.raw.cocktails)
        } else {
            Helpers.resolveFileNameFromUri(contentResolver, Uri.parse(fileuri))
        }
        seekBar!!.isClickable = false
        seekBar!!.max = mediaPlayer!!.duration

        audioLength!!.text = millisecondsToTimestamp(mediaPlayer!!.duration)

        setListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.release()
    }

    override fun onRestart() {
        super.onRestart()
        ActivityBase.currentActivity = this
        val mainAlanButton: AlanButton = ActivityBase.mainActivity.findViewById(R.id.alan_button)
        AlanAI.setVisualState(mainAlanButton, getString(R.string.mediaplayer_screen))
    }

    private fun setListeners() {
        playButton!!.setOnClickListener {
            pauseButton!!.isEnabled = true
            playButton!!.isEnabled = false

            mediaPlayer!!.start()

            seekBar!!.progress = mediaPlayer!!.currentPosition
            audioCurrentProgress!!.text = millisecondsToTimestamp(mediaPlayer!!.currentPosition)

            handler!!.postDelayed(updateAudio, 100)
        }

        pauseButton!!.setOnClickListener {
            pauseButton!!.isEnabled = false
            playButton!!.isEnabled = true

            mediaPlayer!!.pause()
        }

        seekBar!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser) return

                mediaPlayer!!.seekTo(progress)
                audioCurrentProgress!!.text = millisecondsToTimestamp(mediaPlayer!!.currentPosition)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun millisecondsToTimestamp(time: Int): String {
        val minutes = time / 1000 / 60
        val seconds = time / 1000 % 60
        val secondsPadded = seconds.toString().padStart(2, '0')

        return "$minutes:$secondsPadded"
    }

    private val updateAudio = object : Runnable {
        override fun run() {
            seekBar!!.progress = mediaPlayer!!.currentPosition
            audioCurrentProgress!!.text = millisecondsToTimestamp(mediaPlayer!!.currentPosition)

            if (mediaPlayer!!.isPlaying) {
                handler!!.postDelayed(this, 100)
            }
        }

    }
}