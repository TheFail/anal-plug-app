package com.analplug.app.ui.screens.plugs.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.analplug.app.data.entity.Plug
import com.analplug.app.data.repository.PlugRepository
import com.analplug.app.data.repository.TagRepository
import kotlinx.coroutines.launch

@Composable
fun PlugFormRoute(
    plugId: Long? = null,
    plugRepository: PlugRepository,
    tagRepository: TagRepository,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val allTags by tagRepository.getAllTags().collectAsState(initial = emptyList())

    PlugFormScreen(
        initialPlug = null,
        allTags = allTags,
        selectedTagIds = emptySet(),
        onSave = { plug, selectedTagIds ->
            scope.launch {
                val savedId = plugRepository.savePlug(plug)
                plugRepository.setTagsForPlug(savedId, selectedTagIds.toList())
                onSaved()
            }
        },
        onAddTag = { name ->
            scope.launch { tagRepository.addTag(name) }
        },
        onCancel = onCancel
    )
}
