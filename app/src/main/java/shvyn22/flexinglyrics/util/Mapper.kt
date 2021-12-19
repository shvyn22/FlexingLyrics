package shvyn22.flexinglyrics.util

import shvyn22.flexinglyrics.data.remote.AlbumInfo
import shvyn22.flexinglyrics.data.local.model.HistoryItem
import shvyn22.flexinglyrics.data.local.model.LibraryItem
import shvyn22.flexinglyrics.data.remote.Track

fun fromHistoryItemToTrack(item: HistoryItem) = Track(
    track = item.track,
    idTrack = item.idTrack,
    artist = item.artist,
    idArtist = item.idArtist,
    album = item.album,
    idAlbum = item.idAlbum,
    hasLyrics = false,
    lyrics = null
)

fun fromTrackToHistoryItem(track: Track) = HistoryItem(
    idTrack = track.idTrack,
    track = track.track,
    idArtist = track.idArtist,
    artist = track.artist,
    idAlbum = track.idAlbum,
    album = track.album,
    hasLyrics = track.hasLyrics
)

fun fromLibraryItemToTrack(item: LibraryItem) = Track(
    track = item.track,
    idTrack = item.idTrack,
    artist = item.artist,
    idArtist = item.idArtist,
    album = item.album,
    idAlbum = item.idAlbum,
    hasLyrics = false,
    lyrics = null
)

fun fromTrackToLibraryItem(track: Track) = LibraryItem(
    idTrack = track.idTrack,
    track = track.track,
    idArtist = track.idArtist,
    artist = track.artist,
    idAlbum = track.idAlbum,
    album = track.album,
    hasLyrics = track.hasLyrics
)

fun fromTrackInfoToTrack(item: AlbumInfo.TrackInfo, track: Track) = Track(
    idTrack = item.idTrack,
    track = item.track,
    idArtist = track.idArtist,
    artist = track.artist,
    album = track.album,
    idAlbum = track.idAlbum,
    hasLyrics = item.hasLyrics,
    lyrics = null
)