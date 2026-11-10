package com.analplug.app.ui.screens.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import com.analplug.app.data.repository.PlugRepository
import com.analplug.app.data.repository.WearSessionRepository

@Composable
fun DashboardRoute(
    wearSessionRepository: WearSessionRepository,
    plugRepository: PlugRepository
) {
    val averageMillis by wearSessionRepository.getAverageDurationMillis()
        .collectAsState(initial = null)

    val allSessions by wearSessionRepository.getAllSessions()
        .collectAsState(initial = emptyList())

    val popularPlugs by produceState(initialValue = emptyList<PopularPlug>()) {
        val usage = wearSessionRepository.getMostUsedPlugIds(5)
        val plugs = usage.mapNotNull { u ->
            plugRepository.getById(u.plugId)?.let { plug ->
                PopularPlug(name = plug.shortName, count = u.count)
            }
        }
        value = plugs
    }

    val averageMinutes = averageMillis?.div(60000.0)

    DashboardScreen(
        averageDurationMinutes = averageMinutes,
        popularPlugs = popularPlugs,
        totalSessions = allSessions.size
    )
}
