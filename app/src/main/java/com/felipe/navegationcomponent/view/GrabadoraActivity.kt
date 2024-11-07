package com.felipe.navegationcomponent.view

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.felipe.navegationcomponent.databinding.ActivityGrabadoraBinding
import java.io.File
import java.io.IOException


class GrabadoraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGrabadoraBinding
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var outputFilePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGrabadoraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermissions()

        // Configura los botones para iniciar, detener y reproducir la grabación
        binding.btnStartRecording.setOnClickListener {
            startRecording()
        }

        binding.btnStopRecording.setOnClickListener {
            stopRecording()
        }

        binding.btnPlayRecording.setOnClickListener {
            playRecording()
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 0
            )
        }
    }

    private fun startRecording() {
        outputFilePath = "${externalCacheDir?.absolutePath}/grabacion_audio.3gp"
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(outputFilePath)

            try {
                prepare()
                start()
                binding.tvStatus.text = "Recording..."
                binding.btnStartRecording.isEnabled = false
                binding.btnStopRecording.isEnabled = true
                binding.btnPlayRecording.isEnabled = false
                Toast.makeText(this@GrabadoraActivity, "Recording started", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this@GrabadoraActivity, "Recording failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        binding.tvStatus.text = "Recording stopped. Ready to play"
        binding.btnStartRecording.isEnabled = true
        binding.btnStopRecording.isEnabled = false
        binding.btnPlayRecording.isEnabled = true
        Toast.makeText(this, "Recording saved", Toast.LENGTH_SHORT).show()
    }

    private fun playRecording() {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(outputFilePath)
                prepare()
                start()
                binding.tvStatus.text = "Playing recording..."
                Toast.makeText(this@GrabadoraActivity, "Playing recording", Toast.LENGTH_SHORT).show()

                setOnCompletionListener {
                    binding.tvStatus.text = "Playback completed"
                    Toast.makeText(this@GrabadoraActivity, "Playback completed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this@GrabadoraActivity, "Playback failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaRecorder?.release()
        mediaPlayer?.release()
        mediaRecorder = null
        mediaPlayer = null
    }
}