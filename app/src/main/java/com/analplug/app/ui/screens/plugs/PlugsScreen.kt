package com.analplug.app.ui.screens.plugs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.analplug.app.data.repository.PlugRepository

@Composable
fun PlugsScreen(
    plugRepository: PlugRepository,
    onAddClick: () -> Unit = {},
    onPlugClick: (Long) -> Unit = {},
    onTagsClick: () -> Unit = {}
) {
    val plugs by plugRepository.getAllPlugsWithTags().collectAsState(initial = emptyList())

    PlugListScreen(
        plugs = plugs,
        onAddClick = onAddClick,
        onPlugClick = onPlugClick,
        onTagsClick = onTagsClick
    )
}
