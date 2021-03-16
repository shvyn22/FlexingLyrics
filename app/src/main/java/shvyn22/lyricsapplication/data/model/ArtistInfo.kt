package shvyn22.lyricsapplication.data.model

import com.google.gson.annotations.SerializedName

data class ArtistInfo(
    @SerializedName("id_artist")
    val idArtist: Int,

    val artist: String,

    val gender: String,

    val country: String,

    val youtube: String,

    val instagram: String,

    val twitter: String,

    val facebook: String,

    val website: String,

    val cover: String,
)
