package shvyn22.lyricsapplication.data.remote

import com.google.gson.annotations.SerializedName

data class ArtistInfo(
    @SerializedName("id_artist")
    val idArtist: Int,

    @SerializedName("artist")
    val artist: String,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("country")
    val country: String,

    @SerializedName("youtube")
    val youtube: String,

    @SerializedName("instagram")
    val instagram: String,

    @SerializedName("twitter")
    val twitter: String,

    @SerializedName("facebook")
    val facebook: String,

    @SerializedName("website")
    val website: String,

    @SerializedName("cover")
    val cover: String
)
