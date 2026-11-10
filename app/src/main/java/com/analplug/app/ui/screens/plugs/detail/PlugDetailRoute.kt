package com.analplug.app.ui.screens.plugs.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.analplug.app.data.repository.PlugRepository
import com.analplug.app.data.repository.WearSessionRepository

@Composable
fun PlugDetailRoute(
    plugId: Long,
    plugRepository: PlugRepository,
    wearSessionRepository: WearSessionRepository,
    onBack: () -> Unit,
    onEdit: (Long) -> Unit = {}
) {
    val plugWithTags by plugRepository.getPlugWithTags(plugId).collectAsState(initial = null)
    val plugWithHistory by plugRepository.getPlugWithHistory(plugId).collectAsState(initial = null)

    PlugDetailScreen(
        plugWithTags = plugWithTags,
        plugWithHistory = plugWithHistory,
        wearSessionRepository = wearSessionRepository,
        onBack = onBack,
        onEdit = { onEdit(plugId) }
    )
}
