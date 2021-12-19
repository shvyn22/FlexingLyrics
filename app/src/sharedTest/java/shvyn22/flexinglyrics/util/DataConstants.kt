package shvyn22.flexinglyrics.util

import shvyn22.flexinglyrics.data.remote.AlbumInfo
import shvyn22.flexinglyrics.data.remote.ArtistInfo
import shvyn22.flexinglyrics.data.remote.Track

val track1 = Track(
    track = "Radioactive",
    idTrack = 7515475,
    artist = "Imagine Dragons",
    idArtist = 2640,
    album = "Night Visions",
    idAlbum = 503511,
    hasLyrics = true,
    lyrics = null
)

const val TRACK_1_LYRICS =
    "I'm waking up to ash and dust\nI wipe my brow and I sweat my rust\nI'm breathing in the chemicals\n\nI'm breaking in, and shaping up\nThen checking out on the prison bus\nThis is it, the apocalypse, whoa\n\nI'm waking up, I feel it in my bones\nEnough to make my systems blow\nWelcome to the new age, to the new age\nWelcome to the new age, to the new age\n\nWhoa, whoa, I'm radioactive, radioactive\n\nWhoa, whoa, I'm radioactive, radioactive\n\nI raise my flag, dye my clothes\nIt's a revolution I suppose\nWe're painted red\n To fit right in, whoa\n\nI'm breaking in, and shaping up\nThen checking out on the prison bus\nThis is it, the apocalypse, whoa\n\nI'm waking up, I feel it in my bones\nEnough to make my systems blow\nWelcome to the new age, to the new age\nWelcome to the new age, to the new age\n\nWhoa, whoa, I'm radioactive, radioactive\n\nWhoa, whoa, I'm radioactive, radioactive\n\nAll systems go,\n the sun hasn't died\nDeep in my bones,\n Straight from inside\n\nI'm waking up, I feel it in my bones\nEnough to make my systems blow\nWelcome to the new age, to the new age\nWelcome to the new age, to the new age\n\nWhoa, whoa, I'm radioactive, radioactive\n\nWhoa, whoa, I'm radioactive, radioactive"

val track2 = Track(
    track = "Radioactive",
    idTrack = 11541289,
    artist = "Imagine Dragons",
    idArtist = 2640,
    album = "Night Visions (Deluxe)",
    idAlbum = 800069,
    hasLyrics = true,
    lyrics = null
)

val track3 = Track(
    track = "Believer",
    idTrack = 5543867,
    artist = "Imagine Dragons",
    idArtist = 2640,
    album = "Believer",
    idAlbum = 363426,
    hasLyrics = true,
    lyrics = null
)

val track4 = Track(
    track = "Bleeding Out",
    idTrack = 7515479,
    artist = "Imagine Dragons",
    idArtist = 2640,
    album = "Night Visions (Deluxe)",
    idAlbum = 800069,
    hasLyrics = true,
    lyrics = null
)

val tracks = listOf(track1, track2, track3)

val trackInfo1 = AlbumInfo.TrackInfo(
    idTrack = 7515475,
    track = "Imagine Dragons",
    hasLyrics = true
)

val trackInfo2 = AlbumInfo.TrackInfo(
    idTrack = 7515483,
    track = "Underdog",
    hasLyrics = true
)

val trackInfo3 = AlbumInfo.TrackInfo(
    idTrack = 7515479,
    track = "Bleeding Out",
    hasLyrics = true
)

val trackInfos = listOf(trackInfo2, trackInfo3)

val artistInfo1 = ArtistInfo(
    idArtist = 2640,
    artist = "Imagine Dragons",
    gender = "",
    country = "us",
    youtube = "",
    instagram = "",
    twitter = "",
    facebook = "",
    website = "",
    cover = "https://api.happi.dev/v1/music/cover/2640/artist"
)

val artistInfoDefault = ArtistInfo(
    idArtist = 0,
    artist = "",
    gender = "",
    country = "",
    youtube = "",
    instagram = "",
    twitter = "",
    facebook = "",
    website = "",
    cover = ""
)

val albumInfo1 = AlbumInfo(
    idAlbum = 503511,
    album = "Night Visions",
    cover = "https://api.happi.dev/v1/music/cover/503511",
    tracks = trackInfos
)

val albumInfoDefault = AlbumInfo(
    idAlbum = 0,
    album = "",
    cover = "",
    tracks = emptyList()
)