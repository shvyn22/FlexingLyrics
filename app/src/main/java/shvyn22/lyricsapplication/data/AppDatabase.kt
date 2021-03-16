package shvyn22.lyricsapplication.data

import androidx.room.Database
import androidx.room.RoomDatabase
import shvyn22.lyricsapplication.data.dao.HistoryDao
import shvyn22.lyricsapplication.data.dao.LibraryDao
import shvyn22.lyricsapplication.data.model.HistoryItem
import shvyn22.lyricsapplication.data.model.LibraryItem

@Database(entities = [HistoryItem::class, LibraryItem::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao() : HistoryDao

    abstract fun libraryDao() : LibraryDao
}