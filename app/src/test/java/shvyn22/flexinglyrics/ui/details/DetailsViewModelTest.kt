package shvyn22.flexinglyrics.ui.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import shvyn22.flexinglyrics.repository.FakeHistoryRepositoryImpl
import shvyn22.flexinglyrics.repository.FakeLibraryRepositoryImpl
import shvyn22.flexinglyrics.repository.FakeRemoteRepositoryImpl
import shvyn22.flexinglyrics.repository.remote.RemoteRepository
import shvyn22.flexinglyrics.util.*

@ExperimentalCoroutinesApi
class DetailsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var remoteRepository: RemoteRepository
    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setup() {
        remoteRepository = FakeRemoteRepositoryImpl()
        viewModel = DetailsViewModel(
            remoteRepository,
            FakeLibraryRepositoryImpl(),
            FakeHistoryRepositoryImpl()
        )
        viewModel.init(track1)
    }

    @Test
    fun initViewModel_ArtistInfoReturnsValidValue() {
        val artistInfo = viewModel.artistInfo

        assertThat(
            artistInfo.getOrAwaitValue(),
            `is`(instanceOf(Resource.Loading::class.java))
        )

        assertThat(
            artistInfo.getOrAwaitValue(),
            `is`(instanceOf(Resource.Success::class.java))
        )
        assertThat(
            (artistInfo.getOrAwaitValue() as Resource.Success).data,
            `is`(artistInfo1)
        )
    }

    @Test
    fun initViewModel_AlbumInfoReturnsValidValue() {
        val albumInfo = viewModel.albumInfo

        assertThat(
            albumInfo.getOrAwaitValue(),
            `is`(instanceOf(Resource.Loading::class.java))
        )

        assertThat(
            albumInfo.getOrAwaitValue(),
            `is`(instanceOf(Resource.Success::class.java))
        )
        assertThat(
            (albumInfo.getOrAwaitValue() as Resource.Success).data,
            `is`(albumInfo1)
        )
    }

    @Test
    fun addToLibrary_CheckIfInLibrary_ReturnsTrue() = runBlocking {
        viewModel.addToLibrary()

        val isLibraryItem = viewModel.isLibraryItem(track1.idTrack)

        assertThat(
            isLibraryItem.getOrAwaitValue(),
            `is`(true)
        )
    }

    @Test
    fun addToAndDeleteFromLibrary_CheckIfInLibrary_ReturnsFalse() = runBlocking {
        viewModel.addToLibrary()
        viewModel.removeFromLibrary()

        val isLibraryItem = viewModel.isLibraryItem(track1.idTrack)

        assertThat(
            isLibraryItem.getOrAwaitValue(),
            `is`(false)
        )
    }

    @Test
    fun selectTrack_ChannelEmitsNavigateEventWithExpectedValue() = runBlocking {
        viewModel.onItemClick(trackInfo1)

        val event = viewModel.detailsEvent.take(2).toList()

        assertThat(
            event[0],
            `is`(instanceOf(StateEvent.Loading::class.java))
        )
        assertThat(
            event[1],
            `is`(instanceOf(StateEvent.NavigateToDetails::class.java))
        )
        assertThat(
            (event[1] as StateEvent.NavigateToDetails).track,
            `is`(track1.copy(lyrics = TRACK_1_LYRICS))
        )
    }

    @Test
    fun sendErrorEvent_ChannelEmitsErrorEvent() = runBlocking {
        viewModel.onErrorOccurred()

        val event = viewModel.detailsEvent.first()

        assertThat(
            event, `is`(instanceOf(StateEvent.Error::class.java))
        )
    }

    @Test
    fun clickOnMediaIcon_ChannelEmitsValidNavigateToMediaEvent() = runBlocking {
        viewModel.onMediaIconClick("")

        val event = viewModel.detailsEvent.first()

        assertThat(
            event, `is`(instanceOf(StateEvent.NavigateToMedia::class.java))
        )

        assertThat(
            (event as StateEvent.NavigateToMedia).url,
            `is`("")
        )
    }
}