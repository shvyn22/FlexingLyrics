package shvyn22.lyricsapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import shvyn22.lyricsapplication.data.model.HistoryItem

@Dao
interface HistoryDao {

    @Query("SELECT * FROM History ORDER BY id DESC")
    fun getAll() : Flow<List<HistoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historyItem: HistoryItem)

    @Query("DELETE FROM History WHERE idTrack = :id ")
    suspend fun delete(id: Int)

    @Query("DELETE From History")
    suspend fun deleteAll()
}