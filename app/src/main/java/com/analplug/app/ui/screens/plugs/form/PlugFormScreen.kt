package com.analplug.app.ui.screens.plugs.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
    existingPlug: Plug? = null,
    allTags: List<Tag>,
    onSave: (Plug, List<Long>) -> Unit,
    onAddTag: (String) -> Unit
) {
    var name by remember { mutableStateOf(existingPlug?.name ?: "") }
    var material by remember { mutableStateOf(existingPlug?.material ?: "") }
    var shape by remember { mutableStateOf(existingPlug?.shape ?: "") }
    var size by remember { mutableStateOf(existingPlug?.size ?: "") }
    var description by remember { mutableStateOf(existingPlug?.description ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (existingPlug == null) "Add Plug" else "Edit Plug") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = material,
                onValueChange = { material = it },
                label = { Text("Material") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = shape,
                onValueChange = { shape = it },
                label = { Text("Shape") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = size,
                onValueChange = { size = it },
                label = { Text("Size") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Button(
                onClick = {
                    val plug = (existingPlug ?: Plug(name = name)).copy(
                        name = name,
                        material = material.ifBlank { null },
                        shape = shape.ifBlank { null },
                        size = size.ifBlank { null },
                        description = description.ifBlank { null }
                    )
                    onSave(plug, emptyList())
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}
