package com.analplug.app.ui.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.WindowInsets
import com.analplug.app.data.relation.WearSessionWithPlug
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    sessions: List<WearSessionWithPlug>,
    onSessionClick: (Long) -> Unit = {},
    onEditNotes: (Long) -> Unit = {}
) {
    var selectedStart by remember { mutableStateOf<Long?>(null) }
    var selectedEnd by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy", Locale.getDefault()) }

    val filteredSessions = remember(sessions, selectedStart, selectedEnd) {
        if (selectedStart == null && selectedEnd == null) {
            sessions
        } else {
            sessions.filter { item ->
                val t = item.session.startTime
                val afterStart = selectedStart == null || t >= selectedStart!!
                val beforeEnd = selectedEnd == null || t <= selectedEnd!!
                afterStart && beforeEnd
            }
        }
    }

    val rangeLabel = when {
        selectedStart == null && selectedEnd == null -> "All dates"
        selectedStart != null && selectedEnd != null ->
            "${dateFormat.format(Date(selectedStart!!))} – ${dateFormat.format(Date(selectedEnd!!))}"
        selectedStart != null -> "From ${dateFormat.format(Date(selectedStart!!))}"
        else -> "Until ${dateFormat.format(Date(selectedEnd!!))}"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "History",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1
                    )
                },
                modifier = Modifier.height(40.dp),
                windowInsets = WindowInsets(0, 0, 0, 0),
                colors = TopAppBarDefaults.topAppBarColors(),
                actions = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "Pick date")
                    }
                    if (selectedStart != null || selectedEnd != null) {
                        IconButton(onClick = {
                            selectedStart = null
                            selectedEnd = null
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear filter")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Compact date header (2 lines)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = rangeLabel,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1
                        )
                        Text(
                            text = "${filteredSessions.size} session${if (filteredSessions.size != 1) "s" else ""}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1
                        )
                    }
                    IconButton(
                        onClick = { showDatePicker = true },
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "Change date")
                    }
                }
            }

            if (filteredSessions.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        if (sessions.isEmpty()) "No sessions yet" else "No sessions in this period",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredSessions, key = { it.session.id }) { item ->
                        SessionCard(item = item)
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        DateRangePickerDialog(
            onDismiss = { showDatePicker = false },
            onConfirm = { start, end ->
                selectedStart = start
                selectedEnd = end
                showDatePicker = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateRangePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (start: Long?, end: Long?) -> Unit
) {
    val state = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(state.selectedStartDateMillis, state.selectedEndDateMillis)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DateRangePicker(
            state = state,
            modifier = Modifier.padding(horizontal = 8.dp),
            title = {
                Text(
                    "Select date or period",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            },
            headline = {
                // Compact 2-line headline
                val start = state.selectedStartDateMillis
                val end = state.selectedEndDateMillis
                val fmt = SimpleDateFormat("MMM d", Locale.getDefault())
                val text = when {
                    start != null && end != null -> "${fmt.format(Date(start))} – ${fmt.format(Date(end))}"
                    start != null -> fmt.format(Date(start))
                    else -> "Pick a day or range"
                }
                Text(
                    text,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                )
            },
            showModeToggle = false
        )
    }
}

@Composable
private fun SessionCard(item: WearSessionWithPlug) {
    val dateFormat = SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault())
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(item.plug.shortName, style = MaterialTheme.typography.titleMedium)
            Text(
                dateFormat.format(Date(item.session.startTime)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            item.session.notes?.let { notes ->
                if (notes.isNotBlank()) {
                    Text(
                        notes,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
