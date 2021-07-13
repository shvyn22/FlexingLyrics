package shvyn22.lyricsapplication.ui.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import shvyn22.lyricsapplication.repository.FakeRepositoryImpl
import shvyn22.lyricsapplication.repository.Repository
import shvyn22.lyricsapplication.util.*

@ExperimentalCoroutinesApi
class DetailsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: Repository
    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setup() {
        repository = FakeRepositoryImpl()
        viewModel = DetailsViewModel(repository)
        viewModel.init(track1)
    }

    @Test
    fun initViewModel_ArtistInfoReturnsValidValue() {
        val artistInfo = viewModel.artistInfo

        assertThat(
            artistInfo.getOrAwaitValue(),
            `is`(instanceOf(Resource.Success::class.java))
        )
        assertThat(
            artistInfo.getOrAwaitValue().data,
            `is`(artistInfo1)
        )
    }

    @Test
    fun initViewModel_AlbumInfoReturnsValidValue() {
        val albumInfo = viewModel.albumInfo

        assertThat(
            albumInfo.getOrAwaitValue(),
            `is`(instanceOf(Resource.Success::class.java))
        )
        assertThat(
            albumInfo.getOrAwaitValue().data,
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

        val event = viewModel.detailsEvent.first()

        assertThat(
            event,
            `is`(instanceOf(StateEvent.NavigateToDetails::class.java))
        )
        assertThat(
            (event as StateEvent.NavigateToDetails).track,
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