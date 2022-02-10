package shvyn22.flexinglyrics.repository.remote

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import shvyn22.flexinglyrics.FlexingLyrics.Companion.ERROR_FETCHING_DATA
import shvyn22.flexinglyrics.api.ApiInterface
import shvyn22.flexinglyrics.data.remote.AlbumInfo
import shvyn22.flexinglyrics.data.remote.ArtistInfo
import shvyn22.flexinglyrics.data.remote.Track
import shvyn22.flexinglyrics.util.Resource
import java.lang.IllegalArgumentException

class RemoteRepositoryImpl(
    private val api: ApiInterface
) : RemoteRepository {

    override fun searchTracks(
        query: String?
    ): Observable<Resource<List<Track>>> = Observable.create { sub ->
        sub.onNext(Resource.Loading())
        if (query == null) sub.onNext(Resource.Idle())
        else {
            try {
                api.searchTracks(query)
                    .execute()
                    .body()
                    ?.let { response ->
                        if (response.success) sub.onNext(Resource.Success(response.result))
                        else sub.onNext(Resource.Error(ERROR_FETCHING_DATA))
                    } ?: throw IllegalArgumentException()
            } catch (e: Exception) {
                Log.d("DEBUG_TAG", e.message.toString())
                sub.onNext(Resource.Error(ERROR_FETCHING_DATA))
            }
        }
    }

    override fun getArtistInfo(
        artistId: Int
    ): Observable<Resource<ArtistInfo>> = Observable.create { sub ->
        sub.onNext(Resource.Loading())
        try {
            api.getArtistInfo(artistId)
                .execute()
                .body()
                ?.let { response ->
                    if (response.success) sub.onNext(Resource.Success(response.result))
                    else sub.onNext(Resource.Error(ERROR_FETCHING_DATA))
                } ?: throw IllegalArgumentException()
        } catch (e: Exception) {
            sub.onNext(Resource.Error(ERROR_FETCHING_DATA))
        }
    }

    override fun getAlbumInfo(
        artistId: Int,
        albumId: Int
    ): Observable<Resource<AlbumInfo>> = Observable.create { sub ->
        sub.onNext(Resource.Loading())
        try {
            api.getAlbumInfo(artistId, albumId)
                .execute()
                .body()
                ?.let { response ->
                    if (response.success) sub.onNext(Resource.Success(response.result))
                    else sub.onNext(Resource.Error(ERROR_FETCHING_DATA))
                } ?: throw IllegalArgumentException()
        } catch (e: Exception) {
            sub.onNext(Resource.Error(ERROR_FETCHING_DATA))
        }
    }

    override fun getTrack(
        artistId: Int,
        albumId: Int,
        trackId: Int
    ): Observable<Resource<Track>> = Observable.create { sub ->
        sub.onNext(Resource.Loading())
        try {
            api.getTrackInfo(artistId, albumId, trackId)
                .execute()
                .body()
                ?.let { response ->
                    if (response.success) sub.onNext(Resource.Success(response.result))
                    else sub.onNext(Resource.Error(ERROR_FETCHING_DATA))
                } ?: throw IllegalArgumentException()
        } catch (e: Exception) {
            sub.onNext(Resource.Error(ERROR_FETCHING_DATA))
        }
    }
}