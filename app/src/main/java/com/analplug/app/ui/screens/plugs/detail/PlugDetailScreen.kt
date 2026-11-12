package com.analplug.app.ui.screens.plugs.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.analplug.app.data.entity.WearSession
import com.analplug.app.data.relation.PlugWithHistory
import com.analplug.app.data.relation.PlugWithTags
import com.analplug.app.data.repository.WearSessionRepository
import com.analplug.app.ui.session.SessionControlRoute
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlugDetailScreen(
    plugWithTags: PlugWithTags?,
    plugWithHistory: PlugWithHistory?,
    wearSessionRepository: WearSessionRepository,
    onBack: () -> Unit,
    onEdit: () -> Unit = {}
) {
    val plug = plugWithTags?.plug
    val scope = rememberCoroutineScope()
    var sessionToDelete by remember { mutableStateOf<WearSession?>(null) }

    if (plug == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Plug", style = MaterialTheme.typography.titleMedium, maxLines = 1) },
                    modifier = Modifier.height(40.dp),
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    colors = TopAppBarDefaults.topAppBarColors(),
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Plug not found", style = MaterialTheme.typography.titleMedium)
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        plug.shortName,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1
                    )
                },
                modifier = Modifier.height(40.dp),
                windowInsets = WindowInsets(0, 0, 0, 0),
                colors = TopAppBarDefaults.topAppBarColors(),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SessionControlRoute(
                plugId = plug.id,
                plugName = plug.shortName,
                wearSessionRepository = wearSessionRepository
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow("Material", plug.material)
                    InfoRow("Shape", plug.shape)
                    InfoRow("Size", plug.size)
                    InfoRow("Vibration", if (plug.hasVibration) "Yes" else "No")
                    InfoRow("Remote app", if (plug.hasRemoteApp) "Yes" else "No")
                }
            }

            if (!plug.description.isNullOrBlank()) {
                Text("Description", style = MaterialTheme.typography.titleMedium)
                Text(text = plug.description, style = MaterialTheme.typography.bodyLarge)
            }

            val tags = plugWithTags?.tags.orEmpty()
            if (tags.isNotEmpty()) {
                Text("Tags", style = MaterialTheme.typography.titleMedium)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tags.forEach { tag ->
                        AssistChip(
                            onClick = {},
                            label = { Text(tag.name) }
                        )
                    }
                }
            }

            val sessions = plugWithHistory?.sessions.orEmpty()
            Text("Wear history", style = MaterialTheme.typography.titleMedium)
            if (sessions.isEmpty()) {
                Text(
                    "No sessions yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    "${sessions.size} session${if (sessions.size != 1) "s" else ""}",
                    style = MaterialTheme.typography.bodyMedium
                )
                sessions.forEach { session ->
                    SessionRow(
                        session = session,
                        onDeleteClick = { sessionToDelete = session }
                    )
                }
            }
        }
    }

    sessionToDelete?.let { session ->
        AlertDialog(
            onDismissRequest = { sessionToDelete = null },
            title = { Text("Delete session?") },
            text = { Text("This will permanently remove this wear session.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            wearSessionRepository.deleteSession(session)
                        }
                        sessionToDelete = null
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { sessionToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SessionRow(
    session: WearSession,
    onDeleteClick: () -> Unit
) {
    val date = SimpleDateFormat("MMM d, yyyy · HH:mm", Locale.getDefault())
        .format(Date(session.startTime))
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(date, style = MaterialTheme.typography.bodyMedium)
                session.notes?.takeIf { it.isNotBlank() }?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete session",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String?) {
    if (value.isNullOrBlank()) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.End)
    }
}
