package com.wangzi.todayinhistory.model
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.Context
class HistoryRepository(context: Context) {
    private val gson = Gson()
    private val prefs = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    private val assetEvents: List<HistoricalEvent>
    init {
        val json = context.assets.open("fallback_events.json").bufferedReader().use { it.readText() }
        assetEvents = gson.fromJson(json, object : TypeToken<List<HistoricalEvent>>(){}.type)
    }
    fun getFallbackEvents(): List<HistoricalEvent> = assetEvents
    fun getEventsFor(month: Int, day: Int): List<HistoricalEvent> =
        assetEvents.filter { it.m == month && it.d == day }
    suspend fun fetchFromApi(month: Int, day: Int): List<HistoricalEvent> {
        val url = "https://api.wikimedia.org/feed/v1/wikipedia/zh/onthisday/events/${String.format("%02d", month)}/${String.format("%02d", day)}"
        val conn = java.net.URL(url).openConnection() as java.net.HttpURLConnection
        return try {
            conn.setRequestProperty("User-Agent", "TodayInHistory/1.0")
            conn.connectTimeout = 15000
            conn.readTimeout = 15000
            val json = conn.inputStream.bufferedReader().use { it.readText() }
            val cls = object : TypeToken<List<ApiEvent>>(){}.type
            val list: List<ApiEvent> = gson.fromJson(json, cls)
            list.map { it.toDomain() }
        } catch (e: Exception) { emptyList() } finally { conn.disconnect() }
    }
    fun getFavorites(): Set<String> = prefs.getStringSet("favs", setOf()) ?: setOf()
    fun toggleFavorite(key: String): Boolean {
        val favs = prefs.getStringSet("favs", setOf())?.toMutableSet() ?: mutableSetOf()
        if (favs.contains(key)) favs.remove(key) else favs.add(key)
        prefs.edit().putStringSet("favs", favs).apply()
        return favs.contains(key)
    }
    fun isFavorite(key: String): Boolean = prefs.getStringSet("favs", setOf())?.contains(key) ?: false
    private data class ApiEvent(val text: String, val year: Int?) {
        fun toDomain(): HistoricalEvent = HistoricalEvent(m = 0, d = 0, y = year ?: 0, t = text, e = "", c = "事件")
    }
}
