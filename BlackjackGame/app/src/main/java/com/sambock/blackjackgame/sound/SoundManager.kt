package com.sambock.blackjackgame.sound

import android.content.Context
import android.media.MediaPlayer
import com.sambock.blackjackgame.R

class SoundManager(private val context: Context) {
    private var dealSound: MediaPlayer? = null
    private var winSound: MediaPlayer? = null
    private var loseSound: MediaPlayer? = null
    private var soundEnabled = true

    init {
        // Initialize sound effects
        dealSound = MediaPlayer.create(context, R.raw.card_deal)
        winSound = MediaPlayer.create(context, R.raw.win)
        loseSound = MediaPlayer.create(context, R.raw.lose)
    }

    fun setSoundEnabled(enabled: Boolean) {
        soundEnabled = enabled
    }

    fun playDealSound() {
        if (soundEnabled) {
            dealSound?.start()
        }
    }

    fun playWinSound() {
        if (soundEnabled) {
            winSound?.start()
        }
    }

    fun playLoseSound() {
        if (soundEnabled) {
            loseSound?.start()
        }
    }

    fun release() {
        dealSound?.release()
        winSound?.release()
        loseSound?.release()
        
        dealSound = null
        winSound = null
        loseSound = null
    }
}