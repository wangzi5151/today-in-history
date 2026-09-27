package com.wangzi.todayinhistory.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.wangzi.todayinhistory.model.HistoricalEvent
import com.wangzi.todayinhistory.ui.components.EventCard
import com.wangzi.todayinhistory.ui.viewmodel.HistoryViewModel
@Composable
fun HomeScreen(vm: HistoryViewModel) {
    val ui by vm.ui.collectAsState()
    val context = LocalContext.current
    // 今日历史事件过滤：只显示当月当日
    val todayEvents = remember(ui.events) {
        ui.events.filter { it.m == ui.month && it.d == ui.day }
    }
    Column(modifier = Modifier.padding(16.dp)) {
        // 顶部时间展示
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("历史上的今天", style = MaterialTheme.typography.headlineMedium)
            Text("${ui.month}月${ui.day}日", style = MaterialTheme.typography.bodyLarge, color = TextSec)
        }
        Spacer(Modifier.height(8.dp))
        // 日期选择器
        DateSelector(ui.month, ui.day, onMonth = { vm.load(it, ui.day) }, onDay = { vm.load(ui.month, it) })
        Spacer(Modifier.height(4.dp))
        // 分类标签
        CategoryTabs("全部") {}
        Spacer(Modifier.height(4.dp))
        // 内容展示区
        if (ui.loading) {
            CircularProgressIndicator(color = Primary)
        } else if (todayEvents.isEmpty()) {
            // 今日无大事提示 - 显示该月的一个随机历史事件
            val otherEvents = ui.events.filter { it.m == ui.month && it.d != ui.day }
            if (otherEvents.isNotEmpty()) {
                val randEvent = otherEvents.random()
                EventCard(randEvent, vm.isFav(randEvent),
                    onFav = { vm.toggleFav(randEvent) },
                    onShare = { shareText("${randEvent.y}年 ${randEvent.t}") }
                )
            } else {
                Text("暂无数据", color = TextSec)
            }
        } else {
            // 使用网格展示今日事件
            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.7f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(todayEvents) { ev ->
                    EventCard(ev, vm.isFav(ev),
                        onFav = { vm.toggleFav(ev) },
                        onShare = { shareText("${ev.y}年 ${ev.t}") }
                    )
                }
            }
        }
    }
}

@Composable
fun shareText(text: String) {
    val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(android.content.Intent.EXTRA_TEXT, text)
    }
    try {
        androidx.compose.ui.platform.LocalContext.current.startActivity(android.content.Intent.createChooser(intent, "分享"))
    } catch (e: Exception) {
        // 静默处理，避免崩溃
    }
}