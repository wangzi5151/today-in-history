package com.wangzi.todayinhistory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.wangzi.todayinhistory.ui.screens.*
import com.wangzi.todayinhistory.ui.theme.*
import com.wangzi.todayinhistory.ui.viewmodel.HistoryViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodayInHistoryTheme {
                val repo = (application as TodayInHistoryApp).repository
                val vm = remember { HistoryViewModel(repo) }
                val nav = rememberNavController()
                val backStack by nav.currentBackStackEntryAsState()
                val currentRoute = backStack?.destination?.route
                val tabs = listOf(
                    Triple("home", "首页", "🏠"),
                    Triple("calendar", "日历", "📅"),
                    Triple("favorites", "收藏", "⭐")
                )
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            tabs.forEach { (route, label, emoji) ->
                                NavigationBarItem(
                                    selected = currentRoute == route,
                                    onClick = {
                                        nav.navigate(route) {
                                            popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Text(emoji) },
                                    label = { Text(label) }
                                )
                            }
                        }
                    }
                ) { p ->
                    NavHost(nav, startDestination = "home", modifier = Modifier.padding(p)) {
                        composable("home") { HomeScreen(vm) }
                        composable("calendar") { CalendarScreen(vm) }
                        composable("favorites") { FavoritesScreen(vm) }
                    }
                }
            }
        }
    }
}
