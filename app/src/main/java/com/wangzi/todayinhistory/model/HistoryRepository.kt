package com.wangzi.todayinhistory.model
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HistoryRepository(private val context: Context) {
    private val gson = Gson()
    private val prefs = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    private val monthCache = HashMap<Int, List<HistoricalEvent>>()

    // ---------- 离线数据（按月按需加载，共 3 万余条） ----------
    private fun loadMonth(month: Int): List<HistoricalEvent> {
        monthCache[month]?.let { return it }
        val name = "fallback_%02d.json".format(month)
        val list = try {
            context.assets.open(name).bufferedReader().use { it.readText() }
                .let { json -> gson.fromJson<List<HistoricalEvent>>(json, object : TypeToken<List<HistoricalEvent>>() {}.type) }
                ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
        monthCache[month] = list
        return list
    }

    suspend fun getEventsFor(month: Int, day: Int): List<HistoricalEvent> = withContext(Dispatchers.IO) {
        loadMonth(month).filter { it.d == day }
    }

    // ---------- 在线数据：优先国内可访问的 60s API，失败再用维基百科 ----------
    suspend fun fetchFromApi(month: Int, day: Int): List<HistoricalEvent> = withContext(Dispatchers.IO) {
        val from60s = fetchFrom60s(month, day)
        if (from60s.isNotEmpty()) from60s else fetchFromWikimedia(month, day)
    }

    private fun httpGet(url: String): String? {
        return try {
            val conn = java.net.URL(url).openConnection() as java.net.HttpURLConnection
            try {
                conn.setRequestProperty("User-Agent", "TodayInHistory/1.0")
                conn.setRequestProperty("Accept", "application/json")
                conn.connectTimeout = 10000
                conn.readTimeout = 10000
                if (conn.responseCode !in 200..299) return null
                conn.inputStream.bufferedReader().use { it.readText() }
            } finally {
                conn.disconnect()
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun fetchFrom60s(month: Int, day: Int): List<HistoricalEvent> {
        val url = "https://60s.viki.moe/v2/today_in_history?date=%04d-%02d-%02d".format(2000, month, day)
        val json = httpGet(url) ?: return emptyList()
        return try {
            val resp = gson.fromJson(json, SixtyResponse::class.java)
            resp?.data?.items?.map { it.toDomain(month, day) } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun fetchFromWikimedia(month: Int, day: Int): List<HistoricalEvent> {
        val url = "https://api.wikimedia.org/feed/v1/wikipedia/zh/onthisday/events/" +
                String.format("%02d", month) + "/" + String.format("%02d", day)
        val json = httpGet(url) ?: return emptyList()
        return try {
            val resp = gson.fromJson(json, ApiResponse::class.java)
            resp?.events?.map { it.toDomain(month, day) } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ---------- 收藏（保存完整事件，跨日期可用） ----------
    private fun keyOf(e: HistoricalEvent) = "${e.m}-${e.d}-${e.y}-${e.t}"

    fun getFavoriteEvents(): List<HistoricalEvent> {
        val json = prefs.getString("fav_events", "[]") ?: "[]"
        return try {
            gson.fromJson<List<HistoricalEvent>>(json, object : TypeToken<List<HistoricalEvent>>() {}.type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun isFavorite(event: HistoricalEvent): Boolean =
        getFavoriteEvents().any { keyOf(it) == keyOf(event) }

    fun toggleFavorite(event: HistoricalEvent): Boolean {
        val list = getFavoriteEvents().toMutableList()
        val k = keyOf(event)
        val existing = list.firstOrNull { keyOf(it) == k }
        val added = existing == null
        if (added) list.add(event) else list.remove(existing)
        prefs.edit().putString("fav_events", gson.toJson(list)).apply()
        return added
    }

    // ---------- 响应模型 ----------
    private data class SixtyResponse(val code: Int?, val data: SixtyData?)
    private data class SixtyData(val items: List<SixtyItem>?)
    private data class SixtyItem(
        val title: String?,
        val year: String?,
        val description: String?,
        val event_type: String?
    ) {
        fun toDomain(m: Int, d: Int): HistoricalEvent = HistoricalEvent(
            m = m, d = d,
            y = year?.trim()?.toIntOrNull() ?: 0,
            t = title ?: "",
            e = description ?: "",
            c = when (event_type) {
                "birth" -> "出生"
                "death" -> "逝世"
                else -> "事件"
            }
        )
    }

    private data class ApiResponse(val events: List<ApiEvent>?)
    private data class ApiEvent(val text: String?, val year: Int?) {
        fun toDomain(month: Int, day: Int): HistoricalEvent =
            HistoricalEvent(m = month, d = day, y = year ?: 0, t = text ?: "", e = "", c = "事件")
    }
}
