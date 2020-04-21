package com.johnny.base.manager

import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import com.johnny.base.WeakRefHandler

/**
 * 功能：
 * 1.开始播放
 * 2.暂停播放
 * 3.停止播放
 * 4.继续播放
 * 5.监听实时播放进度并对外提供回调
 *
 * @author Johnny
 */
class MediaPlayerManager {

    companion object {
        const val MEDIA_STATUS_PLAYING = 0
        const val MEDIA_STATUS_PAUSED = 1
        const val MEDIA_STATUS_STOP = 2
        const val WHAT_GET_PROGRESS = 1000
    }

    inner class HandlerCallback : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            when (msg.what) {
                WHAT_GET_PROGRESS -> {
                    val currentProgress = getCurrentPosition()
                    val percent = currentProgress.toFloat() / getDuration().toFloat() * 100
                    onProgressListener?.onProgressListener(currentProgress, percent.toInt())
                    msg.target.sendEmptyMessageDelayed(WHAT_GET_PROGRESS, 1000)
                }
            }
            return true
        }
    }

    private val mMediaPlayer = MediaPlayer()
    var mCurrentMediaStatus = MEDIA_STATUS_STOP
    var onProgressListener: OnProgressListener? = null
    private val mHandler = WeakRefHandler(HandlerCallback())

    fun startPlay(afd: AssetFileDescriptor) {
        try {
            this.mMediaPlayer.reset()
            this.mMediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            this.mMediaPlayer.prepare()
            this.mMediaPlayer.start()
            this.mCurrentMediaStatus = MEDIA_STATUS_PLAYING
            this.mHandler.sendEmptyMessage(WHAT_GET_PROGRESS)
        } catch (e: Exception) {
            // ignore
        }
    }

    fun pausePlay() {
        if (this.mMediaPlayer.isPlaying) {
            this.mMediaPlayer.pause()
            this.mCurrentMediaStatus = MEDIA_STATUS_PAUSED
            this.mHandler.removeMessages(WHAT_GET_PROGRESS)
        }
    }

    fun continuePlay() {
        this.mMediaPlayer.start()
        this.mCurrentMediaStatus = MEDIA_STATUS_PLAYING
        this.mHandler.sendEmptyMessage(WHAT_GET_PROGRESS)
    }

    fun stopPlay() {
        this.mMediaPlayer.stop()
        this.mCurrentMediaStatus = MEDIA_STATUS_STOP
        this.mHandler.removeMessages(WHAT_GET_PROGRESS)
    }

    fun setLooping(isLooping: Boolean) {
        this.mMediaPlayer.isLooping = isLooping
    }

    fun seekTo(ms: Int) {
        this.mMediaPlayer.seekTo(ms)
    }

    fun getCurrentPosition(): Int = this.mMediaPlayer.currentPosition

    fun getDuration(): Int = this.mMediaPlayer.duration

    fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener) {
        this.mMediaPlayer.setOnCompletionListener(listener)
    }

    fun setOnErrorListener(listener: MediaPlayer.OnErrorListener) {
        this.mMediaPlayer.setOnErrorListener(listener)
    }

    interface OnProgressListener {
        fun onProgressListener(currentPosition: Int, percent: Int)
    }
}