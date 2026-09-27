package com.wangzi.todayinhistory.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wangzi.todayinhistory.ui.components.EventCard
import com.wangzi.todayinhistory.ui.theme.*
import com.wangzi.todayinhistory.ui.viewmodel.HistoryViewModel

@Composable
fun FavoritesScreen(vm: HistoryViewModel) {
    val ui by vm.ui.collectAsState()
    val favEvents = ui.events.filter { vm.isFav(it) }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("我的收藏 (${favEvents.size})", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        if (favEvents.isEmpty()) {
            Text("还没有收藏，点击卡片上的 ♡ 收藏事件", color = TextSec,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(favEvents) { ev ->
                    EventCard(ev, true, onFav = { vm.toggleFav(ev) }, onShare = {})
                }
            }
        }
    }
}
