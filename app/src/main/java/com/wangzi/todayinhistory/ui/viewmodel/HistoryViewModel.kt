package com.wangzi.todayinhistory.ui.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wangzi.todayinhistory.model.HistoricalEvent
import com.wangzi.todayinhistory.model.HistoryRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UiState(
    val events: List<HistoricalEvent> = emptyList(),
    val favorites: List<HistoricalEvent> = emptyList(),
    val loading: Boolean = false,
    val refreshing: Boolean = false,
    val error: String? = null,
    val month: Int = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1,
    val day: Int = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH)
)

class HistoryViewModel(private val repo: HistoryRepository) : ViewModel() {
    private val _ui = MutableStateFlow(UiState(favorites = repo.getFavoriteEvents()))
    val ui: StateFlow<UiState> = _ui
    private var loadJob: Job? = null

    init { loadForToday() }

    fun loadForToday() {
        val cal = java.util.Calendar.getInstance()
        load(cal.get(java.util.Calendar.MONTH) + 1, cal.get(java.util.Calendar.DAY_OF_MONTH))
    }

    fun load(month: Int, day: Int) {
        loadJob?.cancel()
        _ui.value = _ui.value.copy(
            month = month,
            day = day,
            events = emptyList(),
            loading = true,
            refreshing = false,
            error = null
        )
        loadJob = viewModelScope.launch {
            // 1) 本地优先：离线资源 + 上次联网缓存立即展示。
            val local = repo.getEventsFor(month, day)
            if (!isCurrent(month, day)) return@launch
            _ui.value = _ui.value.copy(
                events = local,
                loading = false,
                refreshing = true,
                favorites = repo.getFavoriteEvents()
            )

            // 2) 网络增量刷新：成功则合并缓存，失败/超时则保留本地数据。
            val remote = try {
                repo.refreshEvents(month, day)
            } catch (e: Exception) {
                null
            }
            if (!isCurrent(month, day)) return@launch
            _ui.value = _ui.value.copy(
                events = (remote ?: local).sortedByDescending { it.y },
                loading = false,
                refreshing = false,
                error = if (remote == null) "网络不可用，已显示本地数据" else null,
                favorites = repo.getFavoriteEvents()
            )
        }
    }

    private fun isCurrent(month: Int, day: Int): Boolean =
        _ui.value.month == month && _ui.value.day == day

    fun toggleFav(event: HistoricalEvent) {
        repo.toggleFavorite(event)
        _ui.value = _ui.value.copy(favorites = repo.getFavoriteEvents())
    }

    fun isFav(event: HistoricalEvent): Boolean = repo.isFavorite(event)
}
