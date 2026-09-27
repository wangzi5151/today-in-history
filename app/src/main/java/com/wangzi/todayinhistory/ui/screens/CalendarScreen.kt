package com.wangzi.todayinhistory.ui.screens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wangzi.todayinhistory.ui.viewmodel.HistoryViewModel
@Composable
fun CalendarScreen(vm: HistoryViewModel) {
    val ui by vm.ui.collectAsState()
    var selectedM by remember { mutableIntStateOf(ui.month) }
    var selectedD by remember { mutableIntStateOf(ui.day) }
    Column(modifier = Modifier.padding(12.dp)) {
        Text("📅 选择日期", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = { if (selectedM > 1) selectedM = selectedM - 1 }) { Text("\u25C0") }
            Text("${selectedM}月", style = MaterialTheme.typography.titleLarge)
            OutlinedButton(onClick = { if (selectedM < 12) selectedM = selectedM + 1 }) { Text("\u25B6") }
        }
        Spacer(Modifier.height(16.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.fillMaxWidth()) {
            items((1..31).toList()) { day ->
                val has = vm.ui.value.events.any { it.d == day && it.m == selectedM }
                Box(modifier = Modifier.padding(3.dp).clickable { selectedD = day; vm.load(selectedM, day) }, contentAlignment = Alignment.Center) {
                    Text("$day", color = if (has) com.wangzi.todayinhistory.ui.theme.Primary else com.wangzi.todayinhistory.ui.theme.TextSec)
                }
            }
        }
    }
}
