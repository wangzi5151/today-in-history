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
import com.wangzi.todayinhistory.ui.viewmodel.HistoryViewModel
@Composable
fun FavoritesScreen(vm: HistoryViewModel) {
    val ui by vm.ui.collectAsState()
    val favEvents = ui.events.filter { vm.isFav(it) }
    Scaffold(
        bottomBar = {
            BottomAppBar {
                ListItem(
                    leading = Icon(Icons.Default.Favorite, tint = Primary),
                    title = { Text("收藏", style = MaterialTheme.typography.titleMedium) },
                    subtitle = { Text("(${favEvents.size})", style = MaterialTheme.typography.bodySmall) }
                )
               TrailingIcon(
                    icon = {
                        Icon(Icons.Default.Close, contentDescription = null) { /* 关闭按钮 */ }
                    }
                )
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(12.dp)) {
            Text("我的收藏 (${favEvents.size})", style = MaterialTheme.typography.headlineMedium)
            if (favEvents.isEmpty()) {
                // 显示鼓励性提示
                Text("还没有收藏", color = TextSec,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn { items(favEvents) { ev -> EventCard(ev, true, {}, {}) } }
            }
        }
    }
}