package shvyn22.flexinglyrics.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
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
