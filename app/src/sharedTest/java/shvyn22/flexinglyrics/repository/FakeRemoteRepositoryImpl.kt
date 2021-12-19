package shvyn22.flexinglyrics.repository

import kotlinx.coroutines.flow.flow
import shvyn22.flexinglyrics.FlexingLyrics
import shvyn22.flexinglyrics.api.ApiInterface
import shvyn22.flexinglyrics.api.FakeApiImpl
import shvyn22.flexinglyrics.repository.remote.RemoteRepository
import shvyn22.flexinglyrics.util.Resource

class FakeRemoteRepositoryImpl(
    private val api: ApiInterface = FakeApiImpl()
) : RemoteRepository {

    override suspend fun searchTracks(query: String?) = flow {
        emit(Resource.Loading())
        if (query == null) emit(Resource.Idle())
        else {
            try {
                val response = api.searchTracks(query)
                if (response.success) emit(Resource.Success(response.result))
                else emit(Resource.Error(FlexingLyrics.ERROR_FETCHING_DATA))
            } catch (e: Exception) {
                emit(Resource.Error(FlexingLyrics.ERROR_FETCHING_DATA))
            }
        }
    }

    override suspend fun getArtistInfo(artistId: Int) = flow {
        emit(Resource.Loading())
        try {
            val response = api.getArtistInfo(artistId)
            if (response.success) emit(Resource.Success(response.result))
            else emit(Resource.Error(FlexingLyrics.ERROR_FETCHING_DATA))
        } catch (e: Exception) {
            emit(Resource.Error(FlexingLyrics.ERROR_FETCHING_DATA))
        }
    }

    override suspend fun getAlbumInfo(artistId: Int, albumId: Int) = flow {
        emit(Resource.Loading())
        try {
            val response = api.getAlbumInfo(artistId, albumId)
            if (response.success) emit(Resource.Success(response.result))
            else emit(Resource.Error(FlexingLyrics.ERROR_FETCHING_DATA))
        } catch (e: Exception) {
            emit(Resource.Error(FlexingLyrics.ERROR_FETCHING_DATA))
        }
    }

    override suspend fun getTrack(artistId: Int, albumId: Int, trackId: Int) = flow {
        emit(Resource.Loading())
        try {
            val response = api.getTrackInfo(artistId, albumId, trackId)
            if (response.success) emit(Resource.Success(response.result))
            else emit(Resource.Error(FlexingLyrics.ERROR_FETCHING_DATA))
        } catch (e: Exception) {
            emit(Resource.Error(FlexingLyrics.ERROR_FETCHING_DATA))
        }
    }
}