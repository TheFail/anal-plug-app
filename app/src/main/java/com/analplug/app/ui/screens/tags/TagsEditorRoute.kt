package com.analplug.app.ui.screens.tags

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.analplug.app.data.repository.TagRepository
import kotlinx.coroutines.launch

@Composable
fun TagsEditorRoute(
    tagRepository: TagRepository,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val tags by tagRepository.getAllTags().collectAsState(initial = emptyList())

    TagsEditorScreen(
        tags = tags,
        onAddTag = { name ->
            scope.launch { tagRepository.addTag(name) }
        },
        onDeleteTag = { tag ->
            scope.launch { tagRepository.deleteTag(tag) }
        },
        onBack = onBack
    )
}
