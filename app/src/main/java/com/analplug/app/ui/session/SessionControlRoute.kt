package com.analplug.app.ui.session

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.analplug.app.data.entity.WearSession
import com.analplug.app.data.repository.WearSessionRepository
import kotlinx.coroutines.launch

@Composable
fun SessionControlRoute(
    plugId: Long,
    plugName: String,
    wearSessionRepository: WearSessionRepository
) {
    val scope = rememberCoroutineScope()
    val sessions by wearSessionRepository.getSessionsForPlug(plugId)
        .collectAsState(initial = emptyList())

    val ongoing = sessions.firstOrNull { it.endTime == null }

    SessionControlCard(
        plugName = plugName,
        ongoingSession = ongoing,
        onStart = {
            scope.launch {
                wearSessionRepository.saveSession(
                    WearSession(
                        plugId = plugId,
                        startTime = System.currentTimeMillis()
                    )
                )
            }
        },
        onStop = {
            ongoing?.let { session ->
                scope.launch {
                    wearSessionRepository.saveSession(
                        session.copy(endTime = System.currentTimeMillis())
                    )
                }
            }
        }
    )
}
