package com.analplug.app.ui.screens.plugs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.analplug.app.data.repository.PlugRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlugsScreen(
    plugRepository: PlugRepository
) {
    val plugs by plugRepository.getAllPlugsWithTags().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Plugs") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: navigate to form */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add plug")
            }
        }
    ) { padding ->
        if (plugs.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No plugs yet", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Tap + to add your first plug",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(plugs) { plugWithTags ->
                    Text(plugWithTags.plug.shortName, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
