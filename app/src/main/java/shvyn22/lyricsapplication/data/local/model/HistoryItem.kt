package shvyn22.lyricsapplication.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "History")
data class HistoryItem(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

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
