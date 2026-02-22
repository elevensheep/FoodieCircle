package com.foodicircle.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.foodicircle.ui.friends.FriendListScreen
import com.foodicircle.ui.groups.GroupListScreen
import com.foodicircle.ui.map.MapScreen
import com.foodicircle.ui.theme.YellowMain
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainApp()
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Friends : Screen("friends", "친구", Icons.Default.Person)
    object Groups : Screen("groups", "그룹", Icons.Default.Group) // Using Group icon? Or Circle shape
    object Map : Screen("map", "지도", Icons.Default.Place)
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Friends,
        Screen.Groups,
        Screen.Map
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = YellowMain,
                            selectedTextColor = YellowMain,
                            indicatorColor = MaterialTheme.colorScheme.surface // Remove background blob if desired
                        ),
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
            navController,
            startDestination = Screen.Friends.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.Friends.route) { FriendListScreen() }
            composable(Screen.Groups.route) { GroupListScreen() }
            composable(Screen.Map.route) { MapScreen() }
        }
    }
}
