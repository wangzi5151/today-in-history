package com.wangzi.todayinhistory.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Surface,
    onPrimaryContainer = Text,
    secondary = Accent,
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Primary,
    onTertiary = Color(0xFFFFFFFF),
    background = Bg,
    onBackground = Text,
    surface = Surface,
    onSurface = Text,
    surfaceVariant = Card,
    onSurfaceVariant = TextSec,
    outline = TextSec,
    error = Warning,
    onError = Color(0xFFFFFFFF)
)

@Composable
fun TodayInHistoryTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DarkColorScheme, typography = Typography, content = content)
}
