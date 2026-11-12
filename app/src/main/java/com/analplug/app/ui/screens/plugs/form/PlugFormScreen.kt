package com.analplug.app.ui.screens.plugs.form

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.analplug.app.data.entity.Plug
import com.analplug.app.data.entity.Tag
import com.analplug.app.ui.util.PhotoStorage
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PlugFormScreen(
    initialPlug: Plug? = null,
    allTags: List<Tag> = emptyList(),
    selectedTagIds: Set<Long> = emptySet(),
    onSave: (Plug, Set<Long>) -> Unit,
    onAddTag: (String) -> Unit = {},
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    var shortName by remember { mutableStateOf(initialPlug?.shortName ?: "") }
    var material by remember { mutableStateOf(initialPlug?.material ?: "") }
    var shape by remember { mutableStateOf(initialPlug?.shape ?: "") }
    var size by remember { mutableStateOf(initialPlug?.size ?: "") }
    var description by remember { mutableStateOf(initialPlug?.description ?: "") }
    var hasVibration by remember { mutableStateOf(initialPlug?.hasVibration ?: false) }
    var hasRemoteApp by remember { mutableStateOf(initialPlug?.hasRemoteApp ?: false) }
    var photoPath by remember { mutableStateOf(initialPlug?.photoPath) }
    var selected by remember { mutableStateOf(selectedTagIds) }
    var newTagName by remember { mutableStateOf("") }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            val saved = PhotoStorage.copyToAppStorage(context, it)
            if (saved != null) {
                photoPath = saved
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (initialPlug == null) "Add Plug" else "Edit Plug",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1
                    )
                },
                modifier = Modifier.height(40.dp),
                windowInsets = WindowInsets(0, 0, 0, 0),
                colors = TopAppBarDefaults.topAppBarColors(),
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val plug = Plug(
                                id = initialPlug?.id ?: 0L,
                                shortName = shortName.trim(),
                                photoPath = photoPath,
                                material = material.ifBlank { null },
                                shape = shape.ifBlank { null },
                                size = size.ifBlank { null },
                                hasVibration = hasVibration,
                                hasRemoteApp = hasRemoteApp,
                                description = description.ifBlank { null }
                            )
                            onSave(plug, selected)
                        },
                        enabled = shortName.isNotBlank()
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Photo", style = MaterialTheme.typography.titleMedium)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentAlignment = Alignment.Center
            ) {
                if (photoPath != null && File(photoPath!!).exists()) {
                    Image(
                        painter = rememberAsyncImagePainter(File(photoPath!!)),
                        contentDescription = "Plug photo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("No photo selected", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = {
                        photoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = null)
                    Spacer(Modifier.size(8.dp))
                    Text("Choose Photo")
                }
                if (photoPath != null) {
                    OutlinedButton(onClick = { photoPath = null }) {
                        Text("Remove")
                    }
                }
            }

            OutlinedTextField(
                value = shortName,
                onValueChange = { shortName = it },
                label = { Text("Short name *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = material,
                onValueChange = { material = it },
                label = { Text("Material") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = shape,
                onValueChange = { shape = it },
                label = { Text("Shape") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = size,
                onValueChange = { size = it },
                label = { Text("Size") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            RowWithCheckbox("Vibration", hasVibration) { hasVibration = it }
            RowWithCheckbox("Remote app", hasRemoteApp) { hasRemoteApp = it }

            // Tags section
            Text("Tags", style = MaterialTheme.typography.titleMedium)
            if (allTags.isEmpty()) {
                Text(
                    "No tags yet. Add one below.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    allTags.forEach { tag ->
                        FilterChip(
                            selected = selected.contains(tag.id),
                            onClick = {
                                selected = if (selected.contains(tag.id)) {
                                    selected - tag.id
                                } else {
                                    selected + tag.id
                                }
                            },
                            label = { Text(tag.name) }
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = newTagName,
                    onValueChange = { newTagName = it },
                    label = { Text("New tag") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Button(
                    onClick = {
                        val name = newTagName.trim()
                        if (name.isNotBlank()) {
                            onAddTag(name)
                            newTagName = ""
                        }
                    },
                    enabled = newTagName.isNotBlank()
                ) {
                    Text("Add")
                }
            }

            Button(
                onClick = {
                    val plug = Plug(
                        id = initialPlug?.id ?: 0L,
                        shortName = shortName.trim(),
                        photoPath = photoPath,
                        material = material.ifBlank { null },
                        shape = shape.ifBlank { null },
                        size = size.ifBlank { null },
                        hasVibration = hasVibration,
                        hasRemoteApp = hasRemoteApp,
                        description = description.ifBlank { null }
                    )
                    onSave(plug, selected)
                },
                enabled = shortName.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (initialPlug == null) "Create Plug" else "Save Changes")
            }
        }
    }
}

@Composable
private fun RowWithCheckbox(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(label)
    }
}
