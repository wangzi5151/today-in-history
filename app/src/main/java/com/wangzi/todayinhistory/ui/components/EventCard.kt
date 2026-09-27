package com.wangzi.todayinhistory.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.wangzi.todayinhistory.model.HistoricalEvent
import com.wangzi.todayinhistory.ui.theme.*

@Composable
fun EventCard(event: HistoricalEvent, isFav: Boolean, onFav: () -> Unit, onShare: () -> Unit) {
    val categoryColor = when (event.c) {
        "事件" -> Primary
        "出生" -> Accent
        "逝世" -> Warning
        "节日" -> Success
        else -> Primary
    }
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Card, contentColor = Text)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val yearText = when {
                    event.y < 0 -> "前${-event.y}"
                    event.y == 0 -> "——"
                    else -> event.y.toString()
                }
                Text(yearText, style = MaterialTheme.typography.headlineMedium, color = categoryColor, modifier = Modifier.weight(1f))
                IconButton(onClick = onFav) {
                    Icon(
                        if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "收藏",
                        tint = if (isFav) Primary else TextSec
                    )
                }
                IconButton(onClick = onShare) {
                    Icon(Icons.Default.Share, contentDescription = "分享", tint = TextSec)
                }
            }
            Text(event.t, style = MaterialTheme.typography.titleLarge, color = Text, maxLines = 2, overflow = TextOverflow.Ellipsis)
            if (event.e.isNotEmpty()) {
                Text(event.e, style = MaterialTheme.typography.bodySmall, color = TextSec, maxLines = 3, overflow = TextOverflow.Ellipsis)
            }
            Text(event.c, style = MaterialTheme.typography.bodySmall, color = categoryColor, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
fun DateSelector(month: Int, day: Int, onMonth: (Int) -> Unit, onDay: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        OutlinedButton(onClick = { if (month > 1) onMonth(month - 1) }) { Text("◀") }
        Text("$month/$day", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(horizontal = 16.dp))
        OutlinedButton(onClick = { if (month < 12) onMonth(month + 1) }) { Text("▶") }
    }
}

@Composable
fun CategoryTabs(selected: String, onSelect: (String) -> Unit) {
    val cats = listOf("全部", "事件", "出生", "逝世", "节日")
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        cats.forEach { c ->
            FilterChip(selected = c == selected, onClick = { onSelect(c) }, label = { Text(c) })
        }
    }
}
