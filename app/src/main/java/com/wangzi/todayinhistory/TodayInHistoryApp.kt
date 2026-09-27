package com.wangzi.todayinhistory
import android.app.Application
import com.wangzi.todayinhistory.model.HistoryRepository
class TodayInHistoryApp : Application() {
    val repository by lazy { HistoryRepository(this) }
}
