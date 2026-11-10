package com.analplug.app.ui.screens.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.analplug.app.data.repository.WearSessionRepository

@Composable
fun HistoryRoute(
    wearSessionRepository: WearSessionRepository
) {
    val sessions by wearSessionRepository.getAllSessionsWithPlug()
        .collectAsState(initial = emptyList())

    HistoryScreen(
        sessions = sessions
    )
}
