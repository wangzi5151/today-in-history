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
import com.wangzi.todayinhistory.ui.components.CategoryTabs
import com.wangzi.todayinhistory.ui.components.DateSelector
import com.wangzi.todayinhistory.ui.components.EventCard
import com.wangzi.todayinhistory.ui.theme.*
import com.wangzi.todayinhistory.ui.viewmodel.HistoryViewModel

@Composable
fun HomeScreen(vm: HistoryViewModel) {
    val ui by vm.ui.collectAsState()
    val context = LocalContext.current
    Column(modifier = Modifier.padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("历史上的今天", style = MaterialTheme.typography.headlineMedium)
            Text("${ui.month}月${ui.day}日", style = MaterialTheme.typography.bodyLarge, color = TextSec)
        }
        Spacer(Modifier.height(8.dp))
        DateSelector(ui.month, ui.day, onMonth = { m -> vm.load(m, ui.day) }, onDay = { d -> vm.load(ui.month, d) })
        Spacer(Modifier.height(4.dp))
        CategoryTabs("全部") {}
        Spacer(Modifier.height(4.dp))
        val dayEvents: List<com.wangzi.todayinhistory.model.HistoricalEvent> =
            ui.events.filter { it.m == ui.month && it.d == ui.day }
        when {
            ui.loading -> CircularProgressIndicator(color = Primary)
            dayEvents.isEmpty() -> Text("暂无数据", color = TextSec)
            else -> LazyColumn(
                modifier = Modifier.fillMaxHeight(0.7f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(dayEvents) { ev ->
                    EventCard(ev, vm.isFav(ev), onFav = { vm.toggleFav(ev) }, onShare = {
                        val shareTxt = "${ev.y}年 ${ev.t}"
                        val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(android.content.Intent.EXTRA_TEXT, shareTxt)
                        }
                        context.startActivity(android.content.Intent.createChooser(intent, "分享"))
                    })
                }
            }
        }
    }
}
