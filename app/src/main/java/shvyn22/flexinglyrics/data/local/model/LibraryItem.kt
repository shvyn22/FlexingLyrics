package shvyn22.flexinglyrics.data.local.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "Library")
data class LibraryItem(

    @PrimaryKey
    @ColumnInfo(name = "idTrack")
    val idTrack: Int,

    @ColumnInfo(name = "track")
    val track: String,

    @ColumnInfo(name = "idArtist")
    val idArtist: Int,

    @ColumnInfo(name = "artist")
    val artist: String,

    @ColumnInfo(name = "idAlbum")
    val idAlbum: Int,

    @ColumnInfo(name = "album")
    val album: String,

    @ColumnInfo(name = "hasLyrics")
    val hasLyrics: Boolean
)
