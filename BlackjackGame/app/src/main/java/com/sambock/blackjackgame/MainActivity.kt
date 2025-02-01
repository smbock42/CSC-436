package com.sambock.blackjackgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sambock.blackjackgame.game.BlackjackGame
import com.sambock.blackjackgame.ui.BlackjackScreen
import com.sambock.blackjackgame.ui.theme.BlackjackGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val game = BlackjackGame()

        setContent {
            BlackjackScreen(game)
        }

    }
}