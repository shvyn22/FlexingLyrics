package shvyn22.flexinglyrics.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import shvyn22.flexinglyrics.data.local.model.HistoryItem

@Dao
interface HistoryDao {

    @Query("SELECT * FROM History ORDER BY id DESC")
    fun getAll(): Observable<List<HistoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(historyItem: HistoryItem): Completable

    @Query("DELETE FROM History WHERE idTrack = :id ")
    fun delete(id: Int): Completable

    @Query("DELETE From History")
    fun deleteAll(): Completable
}