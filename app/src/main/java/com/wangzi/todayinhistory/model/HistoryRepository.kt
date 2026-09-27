package com.wangzi.todayinhistory.model
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HistoryRepository(context: Context) {
    private val gson = Gson()
    private val prefs = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    private val assetEvents: List<HistoricalEvent>

    init {
        val json = context.assets.open("fallback_events.json").bufferedReader().use { it.readText() }
        assetEvents = try {
            gson.fromJson(json, object : TypeToken<List<HistoricalEvent>>() {}.type)
                ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getFallbackEvents(): List<HistoricalEvent> = assetEvents

    fun getEventsFor(month: Int, day: Int): List<HistoricalEvent> =
        assetEvents.filter { it.m == month && it.d == day }

    suspend fun fetchFromApi(month: Int, day: Int): List<HistoricalEvent> = withContext(Dispatchers.IO) {
        val url = "https://api.wikimedia.org/feed/v1/wikipedia/zh/onthisday/events/" +
                String.format("%02d", month) + "/" + String.format("%02d", day)
        try {
            val conn = java.net.URL(url).openConnection() as java.net.HttpURLConnection
            try {
                conn.setRequestProperty("User-Agent", "TodayInHistory/1.0")
                conn.setRequestProperty("Accept", "application/json")
                conn.connectTimeout = 15000
                conn.readTimeout = 15000
                if (conn.responseCode !in 200..299) return@withContext emptyList()
                val json = conn.inputStream.bufferedReader().use { it.readText() }
                val resp = gson.fromJson(json, ApiResponse::class.java)
                resp?.events?.map { it.toDomain(month, day) } ?: emptyList()
            } finally {
                conn.disconnect()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getFavorites(): Set<String> = prefs.getStringSet("favs", setOf()) ?: setOf()

    fun toggleFavorite(key: String): Boolean {
        val favs = prefs.getStringSet("favs", setOf())?.toMutableSet() ?: mutableSetOf()
        if (favs.contains(key)) favs.remove(key) else favs.add(key)
        prefs.edit().putStringSet("favs", favs).apply()
        return favs.contains(key)
    }

    fun isFavorite(key: String): Boolean =
        prefs.getStringSet("favs", setOf())?.contains(key) ?: false

    private data class ApiResponse(val events: List<ApiEvent>?)
    private data class ApiEvent(val text: String?, val year: Int?) {
        fun toDomain(month: Int, day: Int): HistoricalEvent =
            HistoricalEvent(m = month, d = day, y = year ?: 0, t = text ?: "", e = "", c = "事件")
    }
}
