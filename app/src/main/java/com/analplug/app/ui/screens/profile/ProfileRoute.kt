package com.analplug.app.ui.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.analplug.app.data.repository.UserProfileRepository
import kotlinx.coroutines.launch

@Composable
fun ProfileRoute(
    userProfileRepository: UserProfileRepository
) {
    val scope = rememberCoroutineScope()
    val profile by userProfileRepository.getProfile().collectAsState(initial = null)

    ProfileScreen(
        profile = profile,
        onSave = { updated ->
            scope.launch {
                userProfileRepository.saveProfile(updated)
            }
        }
    )
}
