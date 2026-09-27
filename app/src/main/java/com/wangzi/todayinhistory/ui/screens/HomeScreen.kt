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
import com.wangzi.todayinhistory.ui.components.CategoryTabs
import com.wangzi.todayinhistory.ui.components.DateSelector
import com.wangzi.todayinhistory.ui.components.EventCard
import com.wangzi.todayinhistory.ui.theme.*
import com.wangzi.todayinhistory.ui.viewmodel.HistoryViewModel

@Composable
fun HomeScreen(vm: HistoryViewModel) {
    val ui by vm.ui.collectAsState()
    val context = LocalContext.current
    var category by remember { mutableStateOf("全部") }
    Column(modifier = Modifier.padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("历史上的今天", style = MaterialTheme.typography.headlineMedium)
            Text("${ui.month}月${ui.day}日", style = MaterialTheme.typography.bodyLarge, color = TextSec)
        }
        Spacer(Modifier.height(8.dp))
        DateSelector(ui.month, ui.day, onMonth = { m -> vm.load(m, ui.day) }, onDay = { d -> vm.load(ui.month, d) })
        Spacer(Modifier.height(4.dp))
        CategoryTabs(category) { category = it }
        Spacer(Modifier.height(4.dp))

        val events: List<HistoricalEvent> =
            if (category == "全部") ui.events else ui.events.filter { it.c == category }

        when {
            ui.loading -> CircularProgressIndicator(color = Primary)
            events.isEmpty() -> Column {
                Text("该日期暂无记录", color = TextSec, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(4.dp))
                Text("可点击上方 ◀ ▶ 切换日期，或检查网络后重试", color = TextSec, style = MaterialTheme.typography.bodySmall)
            }
            else -> LazyColumn(
                modifier = Modifier.fillMaxHeight(0.75f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(events) { ev ->
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
