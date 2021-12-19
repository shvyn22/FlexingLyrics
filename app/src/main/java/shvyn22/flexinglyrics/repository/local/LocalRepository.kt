package shvyn22.flexinglyrics.repository.local

import shvyn22.flexinglyrics.data.remote.Track

interface LocalRepository {

    suspend fun add(track: Track)

    suspend fun remove(id: Int)

    suspend fun removeAll()
}