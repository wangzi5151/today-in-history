package com.wangzi.todayinhistory.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wangzi.todayinhistory.model.HistoricalEvent
import com.wangzi.todayinhistory.ui.components.EventCard
import com.wangzi.todayinhistory.ui.theme.*
import com.wangzi.todayinhistory.ui.viewmodel.HistoryViewModel
@Composable
fun FavoritesScreen(vm: HistoryViewModel) {
    val ui by vm.ui.collectAsState()
    val favEvents = ui.events.filter { vm.isFav(it) }
    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf("首页", "日历", "收藏").forEachIndexed { i, s ->
                    NavigationBarItem(selected = false, onClick = {}, icon = { Text(if (i==0) "🏠" else if (i==1) "📅" else "⭐") }, label = { Text(s) })
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(12.dp)) {
            Text("我的收藏 (${favEvents.size})", style = MaterialTheme.typography.headlineMedium)
            if (favEvents.isEmpty()) Text("还没有收藏", color = TextSec)
            else LazyColumn { items(favEvents) { ev -> EventCard(ev, true, {}, {}) } }
        }
    }
}
