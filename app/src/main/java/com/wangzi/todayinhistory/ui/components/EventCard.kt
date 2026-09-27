package com.wangzi.todayinhistory.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

fun categoryColorOf(c: String) = when (c) {
    "出生" -> Accent
    "逝世" -> Warning
    "节日" -> Success
    else -> Primary
}

fun yearTextOf(y: Int) = when {
    y < 0 -> "公元前${-y}年"
    y == 0 -> "——"
    else -> "${y}年"
}

@Composable
fun EventCard(
    event: HistoricalEvent,
    isFav: Boolean,
    onFav: () -> Unit,
    onShare: () -> Unit,
    onClick: () -> Unit
) {
    val color = categoryColorOf(event.c)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Card, contentColor = Text)
    ) {
        Row(Modifier.height(IntrinsicSize.Min)) {
            Box(Modifier.width(4.dp).fillMaxHeight().background(color))
            Column(Modifier.padding(12.dp).weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        yearTextOf(event.y),
                        style = MaterialTheme.typography.titleLarge,
                        color = color,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onFav, modifier = Modifier.size(36.dp)) {
                        Icon(
                            if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "收藏",
                            tint = if (isFav) Primary else TextSec
                        )
                    }
                    IconButton(onClick = onShare, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.Share, contentDescription = "分享", tint = TextSec)
                    }
                }
                Text(
                    event.t,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Text,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(event.c, style = MaterialTheme.typography.bodySmall, color = color)
            }
        }
    }
}

@Composable
fun EventDetailDialog(
    event: HistoricalEvent,
    isFav: Boolean,
    onFav: () -> Unit,
    onShare: () -> Unit,
    onDismiss: () -> Unit
) {
    val color = categoryColorOf(event.c)
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Surface,
        titleContentColor = Text,
        textContentColor = Text,
        title = {
            Text(yearTextOf(event.y), style = MaterialTheme.typography.headlineMedium, color = color)
        },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                Text(event.t, style = MaterialTheme.typography.bodyLarge, color = Text)
                Spacer(Modifier.height(8.dp))
                Text("【${event.c}】", style = MaterialTheme.typography.bodyMedium, color = color)
            }
        },
        confirmButton = {
            TextButton(onClick = onFav) {
                Text(if (isFav) "取消收藏" else "收藏", color = Primary)
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = onShare) { Text("分享", color = TextSec) }
                TextButton(onClick = onDismiss) { Text("关闭", color = TextSec) }
            }
        }
    )
}

@Composable
fun DateSelector(month: Int, day: Int, onMonth: (Int) -> Unit, onDay: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        OutlinedButton(
            onClick = { if (month > 1) onMonth(month - 1) },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Primary)
        ) { Text("◀") }
        Text(
            "$month/$day",
            style = MaterialTheme.typography.titleLarge,
            color = Text,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        OutlinedButton(
            onClick = { if (month < 12) onMonth(month + 1) },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Primary)
        ) { Text("▶") }
    }
}

@Composable
fun CategoryTabs(selected: String, onSelect: (String) -> Unit) {
    val cats = listOf("全部", "事件", "出生", "逝世")
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        cats.forEach { c ->
            FilterChip(
                selected = c == selected,
                onClick = { onSelect(c) },
                label = { Text(c) },
                colors = FilterChipDefaults.filterChipColors(
                    labelColor = TextSec,
                    selectedLabelColor = Text,
                    selectedContainerColor = Primary
                )
            )
        }
    }
}
