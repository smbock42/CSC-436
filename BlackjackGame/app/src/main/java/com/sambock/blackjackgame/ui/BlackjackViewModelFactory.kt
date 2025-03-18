package com.sambock.blackjackgame.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sambock.blackjackgame.data.ChipDataStore

class BlackjackViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlackjackViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BlackjackViewModel(ChipDataStore(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}