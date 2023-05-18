package com.example.Myy

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.Myy.ui.theme.Workbook7Theme
import kotlinx.android.synthetic.main.activity_main.buttonPause
import kotlinx.android.synthetic.main.activity_main.buttonStart
import kotlinx.android.synthetic.main.activity_main.buttonStop
import kotlinx.android.synthetic.main.activity_main.textTimer
import kotlin.concurrent.thread

class MainActivity : ComponentActivity() {


    private var started = false
    private var totalSeconds = 0
    private val SET_TIME = 30
    private val RESET = 31

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SET_TIME -> {
                    textTimer.text = formatTime(msg.arg1)
                    if (msg.arg1 >= 30) {
                        stop()
                    }
                }

                RESET -> {
                    textTimer.text = "00:00"
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStart.setOnClickListener { start() }
        buttonPause.setOnClickListener { pause() }
        buttonStop.setOnClickListener { stop() }
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