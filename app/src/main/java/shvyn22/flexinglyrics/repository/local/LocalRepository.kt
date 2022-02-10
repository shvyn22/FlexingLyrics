package shvyn22.flexinglyrics.repository.local

import shvyn22.flexinglyrics.data.remote.Track

interface LocalRepository {

    fun add(track: Track)

    fun remove(id: Int)

    fun removeAll()
}