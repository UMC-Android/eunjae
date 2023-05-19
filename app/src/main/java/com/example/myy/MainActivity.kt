package com.example.myy

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.activity.ComponentActivity
import com.example.myy.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding

    private var started = false
    private var totalSeconds = 0
    private val SET_TIME = 30
    private val RESET = 31

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SET_TIME -> {
                    binding.textTimer.text = formatTime(msg.arg1)
                    if (msg.arg1 >= 30) {
                        stop()
                    }
                }

                RESET -> {
                    binding.textTimer.text = "00:00"
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonStart.setOnClickListener { start() }
        binding.buttonPause.setOnClickListener { pause() }
        binding.buttonStop.setOnClickListener { stop() }
    }

    private fun start() {
        if (!started) {
            started = true
            Thread {
                while (started) {
                    Thread.sleep(1000)
                    totalSeconds++
                    val msg = Message.obtain()
                    msg.what = SET_TIME
                    msg.arg1 = totalSeconds
                    handler.sendMessage(msg)
                }
            }.start()
        }
    }

    private fun pause() {
        started = false
    }

    private fun stop() {
        started = false
        totalSeconds = 0
        handler.sendEmptyMessage(RESET)
    }

    private fun formatTime(time: Int): String {
        val minute = String.format("%02d", time / 60)
        val second = String.format("%02d", time % 60)
        return "$minute:$second"
    }
}