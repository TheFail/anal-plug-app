package com.analplug.app.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.analplug.app.data.entity.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profile: UserProfile?,
    onSave: (UserProfile) -> Unit
) {
    var name by remember(profile) { mutableStateOf(profile?.name ?: "") }
    var ageText by remember(profile) { mutableStateOf(profile?.age?.toString() ?: "") }
    var sex by remember(profile) { mutableStateOf(profile?.sex ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                actions = {
                    androidx.compose.material3.IconButton(
                        onClick = {
                            val age = ageText.toIntOrNull()
                            onSave(
                                UserProfile(
                                    id = 1,
                                    name = name.trim(),
                                    age = age,
                                    sex = sex.ifBlank { null }
                                )
                            )
                        }
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Personal info",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "Stored only on this device.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = ageText,
                onValueChange = { ageText = it.filter { ch -> ch.isDigit() } },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = sex,
                onValueChange = { sex = it },
                label = { Text("Sex") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                supportingText = { Text("e.g. Male, Female, Other — free text") }
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    val age = ageText.toIntOrNull()
                    onSave(
                        UserProfile(
                            id = 1,
                            name = name.trim(),
                            age = age,
                            sex = sex.ifBlank { null }
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save profile")
            }
        }
    }
}
