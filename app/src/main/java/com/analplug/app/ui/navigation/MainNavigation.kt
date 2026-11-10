package com.analplug.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.analplug.app.data.AppDatabase
import com.analplug.app.data.repository.PlugRepository
import com.analplug.app.data.repository.TagRepository
import com.analplug.app.data.repository.UserProfileRepository
import com.analplug.app.ui.screens.dashboard.DashboardRoute
import com.analplug.app.ui.screens.history.SessionNotesEditor
import com.analplug.app.ui.screens.plugs.form.PlugFormRoute
import com.analplug.app.data.repository.WearSessionRepository
import com.analplug.app.ui.screens.history.HistoryRoute
import com.analplug.app.ui.screens.plugs.PlugsScreen
import com.analplug.app.ui.screens.plugs.detail.PlugDetailRoute
import com.analplug.app.ui.screens.tags.TagsEditorRoute
import com.analplug.app.ui.screens.profile.ProfileRoute
import com.analplug.app.ui.theme.AnalPlugTheme

@Composable
fun MainNavigation(
    database: AppDatabase
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val plugRepository = PlugRepository(database.plugDao())
    val tagRepository = TagRepository(database.tagDao())
    val wearSessionRepository = WearSessionRepository(database.wearSessionDao())
    val userProfileRepository = UserProfileRepository(database.userProfileDao())

    AnalPlugTheme {
        Scaffold(
            bottomBar = {
                if (currentRoute != "plug_form" && !currentRoute.orEmpty().startsWith("plug_detail") && currentRoute != "tags_editor") {
                    NavigationBar {
                        bottomNavItems.forEach { screen ->
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        imageVector = if (currentRoute == screen.route) {
                                            screen.selectedIcon
                                        } else {
                                            screen.unselectedIcon
                                        },
                                        contentDescription = screen.title
                                    )
                                },
                                label = { Text(screen.title) },
                                selected = currentRoute == screen.route,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Dashboard.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Dashboard.route) {
                    DashboardRoute(
                        wearSessionRepository = wearSessionRepository,
                        plugRepository = plugRepository
                    )
                }
                composable(Screen.Plugs.route) {
                    PlugsScreen(
                        plugRepository = plugRepository,
                        onAddClick = { navController.navigate("plug_form") },
                        onPlugClick = { plugId -> navController.navigate("plug_detail/$plugId") },
                        onTagsClick = { navController.navigate("tags_editor") }
                    )
                }
                composable(Screen.History.route) {
                    HistoryRoute(
                        wearSessionRepository = wearSessionRepository,
                        onEditNotes = { sessionId -> navController.navigate("session_notes/$sessionId") }
                    )
                }
                composable(Screen.Profile.route) {
                    ProfileRoute(userProfileRepository = userProfileRepository)
                }
                composable("session_notes/{sessionId}") { backStackEntry ->
                    val sessionId = backStackEntry.arguments?.getString("sessionId")?.toLongOrNull() ?: 0L
                    SessionNotesEditor(
                        session = null,
                        plugName = "Session",
                        onSave = { notes -> navController.popBackStack() },
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("tags_editor") {
                    TagsEditorRoute(
                        tagRepository = tagRepository,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("plug_form") {
                    PlugFormRoute(
                        plugRepository = plugRepository,
                        tagRepository = tagRepository,
                        onSaved = { navController.popBackStack() },
                        onCancel = { navController.popBackStack() }
                    )
                }
                composable("plug_form?plugId={plugId}") { backStackEntry ->
                    val plugId = backStackEntry.arguments?.getString("plugId")?.toLongOrNull()
                    PlugFormRoute(
                        plugId = plugId,
                        plugRepository = plugRepository,
                        tagRepository = tagRepository,
                        onSaved = { navController.popBackStack() },
                        onCancel = { navController.popBackStack() }
                    )
                }
                composable("plug_detail/{plugId}") { backStackEntry ->
                    val plugId = backStackEntry.arguments?.getString("plugId")?.toLongOrNull() ?: 0L
                    PlugDetailRoute(
                        plugId = plugId,
                        plugRepository = plugRepository,
                        wearSessionRepository = wearSessionRepository,
                        onBack = { navController.popBackStack() },
                        onEdit = { id -> navController.navigate("plug_form?plugId=$id") }
                    )
                }
            }
        }
    }
}
