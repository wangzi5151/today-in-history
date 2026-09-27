package com.wangzi.todayinhistory.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import com.wangzi.todayinhistory.model.HistoricalEvent
import com.wangzi.todayinhistory.ui.viewmodel.HistoryViewModel
@Composable
fun EventCard(event: HistoricalEvent, isFav: Boolean, onFav: () -> Unit, onShare: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = com.wangzi.todayinhistory.ui.theme.Card)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text(event.y.toString(), style = MaterialTheme.typography.headlineMedium, modifier = Modifier.weight(1f))
                IconButton(onClick = onFav) { Icon(if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder, "fav", tint = if (isFav) com.wangzi.todayinhistory.ui.theme.Primary else com.wangzi.todayinhistory.ui.theme.TextSec) }
                IconButton(onClick = onShare) { Icon(Icons.Default.Share, "share", tint = com.wangzi.todayinhistory.ui.theme.TextSec) }
            }
            Text(event.t, style = MaterialTheme.typography.titleLarge, color = com.wangzi.todayinhistory.ui.theme.Text)
            if (event.e.isNotEmpty()) Text(event.e, color = com.wangzi.todayinhistory.ui.theme.TextSec)
            Text(event.c, color = com.wangzi.todayinhistory.ui.theme.Primary, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
@Composable
fun DateSelector(month: Int, day: Int, onMonth: (Int) -> Unit, onDay: (Int) -> Unit) {
    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center) {
        OutlinedButton(onClick = { if (month > 1) onMonth(month - 1) }) { Text("\u25C0") }
        Text("$month/$day", color = com.wangzi.todayinhistory.ui.theme.Text, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(horizontal = 16.dp))
        OutlinedButton(onClick = { if (month < 12) onMonth(month + 1) }) { Text("\u25B6") }
    }
}
@Composable
fun CategoryTabs(cat: String, onCat: (String) -> Unit) {
    val cats = listOf("全部", "事件", "出生", "逝世", "节日")
    Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(6.dp)) {
        cats.forEach { c -> FilterChip(selected = c == cat, onClick = { onCat(c) }, label = { Text(c) }) }
    }
}
