package com.wangzi.todayinhistory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
                val selTab = remember { mutableIntStateOf(0) }
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            listOf("首页", "日历", "收藏").forEachIndexed { i, s ->
                                NavigationBarItem(selected = selTab.intValue == i, onClick = { selTab.intValue = i }, icon = { Text(if (i==0) "🏠" else if (i==1) "📅" else "⭐") }, label = { Text(s) })
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
