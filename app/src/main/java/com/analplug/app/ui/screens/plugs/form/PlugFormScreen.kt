package com.analplug.app.ui.screens.plugs.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.analplug.app.data.entity.Plug
import com.analplug.app.data.entity.Tag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlugFormScreen(
    initialPlug: Plug? = null,
    allTags: List<Tag> = emptyList(),
    selectedTagIds: Set<Long> = emptySet(),
    onSave: (Plug, Set<Long>) -> Unit,
    onAddTag: (String) -> Unit = {},
    onCancel: () -> Unit
) {
    var shortName by remember { mutableStateOf(initialPlug?.shortName ?: "") }
    var material by remember { mutableStateOf(initialPlug?.material ?: "") }
    var shape by remember { mutableStateOf(initialPlug?.shape ?: "") }
    var size by remember { mutableStateOf(initialPlug?.size ?: "") }
    var description by remember { mutableStateOf(initialPlug?.description ?: "") }
    var hasVibration by remember { mutableStateOf(initialPlug?.hasVibration ?: false) }
    var hasRemoteApp by remember { mutableStateOf(initialPlug?.hasRemoteApp ?: false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (initialPlug == null) "Add Plug" else "Edit Plug") },
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
                                material = material.ifBlank { null },
                                shape = shape.ifBlank { null },
                                size = size.ifBlank { null },
                                hasVibration = hasVibration,
                                hasRemoteApp = hasRemoteApp,
                                description = description.ifBlank { null }
                            )
                            onSave(plug, selectedTagIds)
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

            Button(
                onClick = {
                    val plug = Plug(
                        id = initialPlug?.id ?: 0L,
                        shortName = shortName.trim(),
                        material = material.ifBlank { null },
                        shape = shape.ifBlank { null },
                        size = size.ifBlank { null },
                        hasVibration = hasVibration,
                        hasRemoteApp = hasRemoteApp,
                        description = description.ifBlank { null }
                    )
                    onSave(plug, selectedTagIds)
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
    androidx.compose.foundation.layout.Row(
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(label)
    }
}
