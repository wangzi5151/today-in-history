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
import com.wangzi.todayinhistory.ui.components.*
import com.wangzi.todayinhistory.ui.theme.*
import com.wangzi.todayinhistory.ui.viewmodel.HistoryViewModel
@Composable
fun HomeScreen(vm: HistoryViewModel) {
    val ui by vm.ui.collectAsState()
    val context = LocalContext.current
    Column(modifier = Modifier.padding(16.dp)) {
        Text("历史上的今天", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))
        DateSelector(ui.month, ui.day, onMonth = { vm.load(it, ui.day) }, onDay = { vm.load(ui.month, it) })
        Spacer(Modifier.height(8.dp))
        CategoryTabs("全部") {}
        Spacer(Modifier.height(8.dp))
        if (ui.loading) CircularProgressIndicator(color = Primary)
        else if (ui.events.isEmpty()) Text("暂无数据", color = TextSec)
        else LazyColumn {
            val dayEvents: List<HistoricalEvent> = ui.events.filter { it.m == ui.month && it.d == ui.day }
            items(dayEvents) { ev ->
                EventCard(
                    ev, vm.isFav(ev),
                    onFav = { vm.toggleFav(ev) },
                    onShare = {
                        val txt = "${ev.y}年 ${ev.t}"
                        val share = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(android.content.Intent.EXTRA_TEXT, txt)
                        }
                        context.startActivity(android.content.Intent.createChooser(share, "分享"))
                    }
                )
            }
        }
    }
}
