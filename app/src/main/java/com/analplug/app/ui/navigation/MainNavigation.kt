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
import com.analplug.app.data.repository.WearSessionRepository
import com.analplug.app.ui.screens.dashboard.DashboardRoute
import com.analplug.app.ui.screens.history.HistoryRoute
import com.analplug.app.ui.screens.plugs.PlugsScreen
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
                        plugRepository = plugRepository
                    )
                }
                composable(Screen.History.route) {
                    HistoryRoute(wearSessionRepository = wearSessionRepository)
                }
                composable(Screen.Profile.route) {
                    ProfileRoute(userProfileRepository = userProfileRepository)
                }
            }
        }
    }
}
