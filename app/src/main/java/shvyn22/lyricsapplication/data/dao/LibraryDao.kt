package shvyn22.lyricsapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import shvyn22.lyricsapplication.data.model.LibraryItem

@Dao
interface LibraryDao {

    @Query("SELECT * FROM Library WHERE track LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%'")
    fun getAll(query: String) : Flow<List<LibraryItem>>

    @Query("SELECT EXISTS (SELECT 1 FROM Library WHERE idTrack = :id)")
    suspend fun exists(id: Int) : Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(libraryItem: LibraryItem)

    @Query("DELETE FROM Library WHERE idTrack = :id ")
    suspend fun delete(id: Int)

    @Query("DELETE From Library")
    suspend fun deleteAll()
}