package com.wangzi.todayinhistory.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun DateSelector(month: Int, day: Int, onMonth: (Int) -> Unit, onDay: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpacedBy(8.dp)
    ) {
        OutlinedButton(onClick = { if (month > 1) onMonth(month - 1) }) {
            Text("◀", style = MaterialTheme.typography.titleSmall)
        }
        Text(
            "$month/$day",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        OutlinedButton(onClick = { if (month < 12) onMonth(month + 1) }) {
            Text("▶", style = MaterialTheme.typography.titleSmall)
        }
    }
}