package com.wangzi.todayinhistory.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val DarkColorScheme = darkColorScheme(
    primary = Primary, onPrimary = Text, background = Bg, surface = Surface, onSurface = Text
)
@Composable
fun TodayInHistoryTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DarkColorScheme, typography = Typography, content = content)
}
