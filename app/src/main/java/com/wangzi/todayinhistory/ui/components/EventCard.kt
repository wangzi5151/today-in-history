package com.wangzi.todayinhistory.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberClickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wangzi.todayinhistory.model.HistoricalEvent
import com.wangzi.todayinhistory.ui.theme.*
@Composable
fun EventCard(event: HistoricalEvent, isFav: Boolean, onFav: () -> Unit, onShare: () -> Unit) {
    // 根据分类确定颜色
    val categoryColor = when (event.c) {
        "事件" -> Primary
        "出生" -> Accent
        "逝世" -> Warning
        "节日" -> Success
        else -> Primary
    }
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
        elevation = CardElevation.value4,
        colors = CardDefaults.cardColors(
            containerColor = Card,
            contentColor = Text
        )
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
            // 顶部行：年份 + 收藏图标
            Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(event.y.toString(), style = MaterialTheme.typography.headlineSmall, color = categoryColor)
                FavoriteToggle(isFav, onFav)
            }
            // 事件标题
            Text(event.t, style = MaterialTheme.typography.titleLarge, color = Text, maxLines = 1, overflow = TextOverflow.Ellipsis)
            // 简介/描述
            if (event.e.isNotEmpty()) {
                Text(event.e, style = MaterialTheme.typography.bodySmall, color = TextSec, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            // 分类标签
            Text(event.c, style = MaterialTheme.typography.bodySmall, color = categoryColor, modifier = Modifier.padding(top = 2.dp))
            // 分享按钮
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onShare) {
                Icon(Icons.Default.Share, contentDescription = null, tint = TextSec)
            }
        }
    }
}
@Composable
fun FavoriteToggle(isFav: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            if (isFav) Icons.Default.Favorite Else Icons.Default.OutlineFavorite,
            contentDescription = null,
            tint = if (isFav) Primary else TextSec
        )
    }
}