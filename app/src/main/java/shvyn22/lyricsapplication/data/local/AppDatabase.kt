package shvyn22.lyricsapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import shvyn22.lyricsapplication.data.local.dao.HistoryDao
import shvyn22.lyricsapplication.data.local.dao.LibraryDao
import shvyn22.lyricsapplication.data.local.model.HistoryItem
import shvyn22.lyricsapplication.data.local.model.LibraryItem

@Database(entities = [HistoryItem::class, LibraryItem::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    abstract fun libraryDao(): LibraryDao
}