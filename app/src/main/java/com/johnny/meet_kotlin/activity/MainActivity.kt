package com.johnny.meet_kotlin.activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.johnny.base.BaseUIActivity
import com.johnny.base.manager.MediaPlayerManager
import com.johnny.base.utils.i
import com.johnny.meet_kotlin.R

class MainActivity : BaseUIActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mediaPlayer = MediaPlayerManager()
        mediaPlayer.onProgressListener = object : MediaPlayerManager.OnProgressListener {
            override fun onProgressListener(currentPosition: Int, percent: Int) {
                i(msg = "currentPosition = $currentPosition percent = $percent")
            }
        }
        mediaPlayer.startPlay(resources.openRawResourceFd(R.raw.music))
    }
}
