package com.wangzi.todayinhistory.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.wangzi.todayinhistory.model.HistoricalEvent
import com.wangzi.todayinhistory.ui.components.EventCard
import com.wangzi.todayinhistory.ui.components.EventDetailDialog
import com.wangzi.todayinhistory.ui.theme.*
import com.wangzi.todayinhistory.ui.viewmodel.HistoryViewModel

@Composable
fun FavoritesScreen(vm: HistoryViewModel) {
    val ui by vm.ui.collectAsState()
    val context = LocalContext.current
    var selected by remember { mutableStateOf<HistoricalEvent?>(null) }
    val favEvents = ui.favorites

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("我的收藏 (${favEvents.size})", style = MaterialTheme.typography.headlineMedium, color = Primary)
        Spacer(Modifier.height(8.dp))
        if (favEvents.isEmpty()) {
            Text(
                "还没有收藏。在首页点击卡片上的 ♡ 即可收藏。",
                color = TextSec,
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(favEvents) { ev ->
                    EventCard(
                        ev, true,
                        onFav = { vm.toggleFav(ev) },
                        onShare = {
                            val txt = "${ev.y}年 ${ev.t}"
                            val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(android.content.Intent.EXTRA_TEXT, txt)
                            }
                            context.startActivity(android.content.Intent.createChooser(intent, "分享"))
                        },
                        onClick = { selected = ev }
                    )
                }
            }
        }
    }

    selected?.let { ev ->
        EventDetailDialog(
            event = ev,
            isFav = true,
            onFav = { vm.toggleFav(ev) },
            onShare = {
                val txt = "${ev.y}年 ${ev.t}"
                val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(android.content.Intent.EXTRA_TEXT, txt)
                }
                context.startActivity(android.content.Intent.createChooser(intent, "分享"))
            },
            onDismiss = { selected = null }
        )
    }
}
