package shvyn22.flexinglyrics.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import shvyn22.flexinglyrics.data.local.model.LibraryItem

@Dao
interface LibraryDao {

    @Query("SELECT * FROM Library WHERE track LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%'")
    fun getAll(query: String): Observable<List<LibraryItem>>

    @Query("SELECT EXISTS (SELECT 1 FROM Library WHERE idTrack = :id)")
    fun exists(id: Int): Observable<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(libraryItem: LibraryItem): Completable

    @Query("DELETE FROM Library WHERE idTrack = :id ")
    fun delete(id: Int): Completable

    @Query("DELETE From Library")
    fun deleteAll(): Completable
}