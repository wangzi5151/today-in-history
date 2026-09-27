package com.wangzi.todayinhistory.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
@Composable
fun CategoryTabs(selected: String, onSelect: (String) -> Unit) {
    val categories = listOf(
        ("全部", "查看所有历史事件"),
        ("事件", "重要历史事件"),
        ("出生", "历史人物生日"),
        ("逝世", "历史人物逝世"),
        ("节日", "节日和纪念日")
    )
    Row(
        horizontalArrangement = Arrangement.SpacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categories.forEach { (name, desc) ->
            FilterChip(
                selected = selected == name,
                onClick = { onSelect(name) },
                label = {
                    Text(name, style = MaterialTheme.typography.bodyMedium)
                },
                icon = {
                    Icon(
                        if (selected == name) Icons.Default.CheckCircleElse Icons.Default.Warning,
                        contentDescription = null
                    )
                }
            )
        }
    }
}