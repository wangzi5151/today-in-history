package com.wangzi.todayinhistory.ui.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wangzi.todayinhistory.model.HistoricalEvent
import com.wangzi.todayinhistory.model.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UiState(
    val events: List<HistoricalEvent> = emptyList(),
    val favorites: List<HistoricalEvent> = emptyList(),
    val loading: Boolean = false,
    val month: Int = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1,
    val day: Int = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH)
)

class HistoryViewModel(private val repo: HistoryRepository) : ViewModel() {
    private val _ui = MutableStateFlow(UiState(favorites = repo.getFavoriteEvents()))
    val ui: StateFlow<UiState> = _ui

    init { loadForToday() }

    fun loadForToday() {
        val cal = java.util.Calendar.getInstance()
        load(cal.get(java.util.Calendar.MONTH) + 1, cal.get(java.util.Calendar.DAY_OF_MONTH))
    }

    fun load(month: Int, day: Int) {
        _ui.value = _ui.value.copy(month = month, day = day, loading = true)
        viewModelScope.launch {
            val local = repo.getEventsFor(month, day)
            _ui.value = _ui.value.copy(
                events = local.sortedByDescending { it.y },
                loading = false,
                favorites = repo.getFavoriteEvents()
            )
        }
    }

    fun toggleFav(event: HistoricalEvent) {
        repo.toggleFavorite(event)
        _ui.value = _ui.value.copy(favorites = repo.getFavoriteEvents())
    }

    fun isFav(event: HistoricalEvent): Boolean = repo.isFavorite(event)
}
