package com.tlu.audiobasedlearning

import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

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

        mediaPlayer = MediaPlayer.create(this, R.raw.cocktails)
        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)
        audioCurrentProgress = findViewById(R.id.audioCurrentProgress)
        audioLength = findViewById(R.id.audioLength)
        nowPlaying = findViewById(R.id.nowPlayingText)
        seekBar = findViewById(R.id.audioSeekBar)
        handler = Handler(Looper.getMainLooper())

        pauseButton!!.isEnabled = false
        nowPlaying!!.text = resources.getResourceEntryName(R.raw.cocktails)
        seekBar!!.isClickable = false
        seekBar!!.max = mediaPlayer!!.duration

        audioLength!!.text = millisecondsToTimestamp(mediaPlayer!!.duration)

        val metadata = MediaMetadataRetriever()
        val path = "android.resource://" + packageName + "/" + R.raw.cocktails
        metadata.setDataSource(this, Uri.parse(path))
        val title = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
        metadata.release()
        findViewById<TextView>(R.id.textView6).text = title

        setListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.release()
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