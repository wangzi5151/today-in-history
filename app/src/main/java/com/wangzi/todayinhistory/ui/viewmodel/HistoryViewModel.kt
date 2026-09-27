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
    val loading: Boolean = false,
    val error: String? = null,
    val month: Int = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1,
    val day: Int = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH),
    val favs: Set<String> = emptySet()
)
class HistoryViewModel(repository: HistoryRepository) : ViewModel() {
    private val repo = repository
    private val _ui = MutableStateFlow(UiState())
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
            val remote = repo.fetchFromApi(month, day)
            val all = (remote + local)
                .distinctBy { "${it.y}-${it.t}" }
                .sortedByDescending { it.y }
            _ui.value = _ui.value.copy(events = all, loading = false, favs = repo.getFavorites())
        }
    }
    fun toggleFav(event: HistoricalEvent) {
        val key = "${event.m}-${event.d}-${event.y}-${event.t}"
        val added = repo.toggleFavorite(key)
        _ui.value = _ui.value.copy(favs = repo.getFavorites())
    }
    fun isFav(event: HistoricalEvent): Boolean {
        val key = "${event.m}-${event.d}-${event.y}-${event.t}"
        return repo.isFavorite(key)
    }
}
