package shvyn22.lyricsapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "History")
data class HistoryItem(

    @PrimaryKey(autoGenerate = true) var id: Int = 0,

    val idTrack: Int,

    val track: String,

    val idArtist: Int,

    val artist: String,

    val idAlbum: Int,

    val album: String,

    val hasLyrics: Boolean
)
