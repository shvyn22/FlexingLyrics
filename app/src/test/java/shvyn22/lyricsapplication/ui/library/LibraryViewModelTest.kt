package shvyn22.lyricsapplication.ui.library

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import shvyn22.lyricsapplication.repository.FakeRepositoryImpl
import shvyn22.lyricsapplication.repository.Repository
import shvyn22.lyricsapplication.util.*

@ExperimentalCoroutinesApi
class LibraryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: Repository
    private lateinit var viewModel: LibraryViewModel

    @Before
    fun setup() {
        repository = FakeRepositoryImpl()
        viewModel = LibraryViewModel(repository)
    }

    @Test
    fun search2TracksThatExist_Returns2TracksWithTheSameName() = runBlocking {
        repository.addToLibrary(track1)
        repository.addToLibrary(track2)

        viewModel.searchTracks("Radioactive")
        val items = viewModel.items

        assertThat(
            items.getOrAwaitValue().map { it.idTrack },
            `is`(listOf(track1.idTrack, track2.idTrack))
        )
    }

    @Test
    fun searchTrackThatExist_Returns1Track() = runBlocking {
        repository.addToLibrary(track3)

        viewModel.searchTracks("Believer")
        val items = viewModel.items

        assertThat(
            items.getOrAwaitValue().map { it.idTrack },
            `is`(listOf(track3.idTrack))
        )
    }

    @Test
    fun searchTracksThatDoNotExist_ReturnsEmptyList() = runBlocking {
        viewModel.searchTracks("Underdog")
        val items = viewModel.items

        assertThat(
            items.getOrAwaitValue(),
            `is`(emptyList())
        )
    }

    @Test
    fun populateHistoryWithItems_DeleteItem_ReturnsValidList() = runBlocking {
        repository.addToLibrary(track1)
        repository.addToLibrary(track2)
        repository.addToLibrary(track3)

        val items = viewModel.items

        viewModel.deleteTrack(track2.idTrack)

        assertThat(
            items.getOrAwaitValue().map { it.idTrack },
            `is`(listOf(track1.idTrack, track3.idTrack))
        )
    }

    @Test
    fun populateHistoryWithItems_DeleteAllItems_ReturnsEmptyList() = runBlocking {
        repository.addToLibrary(track1)
        repository.addToLibrary(track2)
        repository.addToLibrary(track3)

        val items = viewModel.items

        viewModel.deleteAll()

        assertThat(
            items.getOrAwaitValue(),
            `is`(emptyList())
        )
    }

    @Test
    fun selectTrack_ChannelEmitsNavigateEventWithExpectedValue() = runBlocking {
        viewModel.onTrackSelected(fromTrackToLibraryItem(track1))

        val event = viewModel.libraryEvent.take(2).toList()

        assertThat(
            event[0], `is`(CoreMatchers.instanceOf(StateEvent.Loading::class.java))
        )
        assertThat(
            event[1],
            `is`(CoreMatchers.instanceOf(StateEvent.NavigateToDetails::class.java))
        )
        assertThat(
            (event[1] as StateEvent.NavigateToDetails).track,
            `is`(track1.copy(lyrics = TRACK_1_LYRICS))
        )
    }

    @Test
    fun sendErrorEvent_ChannelEmitsErrorEvent() = runBlocking {
        viewModel.onErrorOccurred()

        val event = viewModel.libraryEvent.first()

        assertThat(
            event, `is`(CoreMatchers.instanceOf(StateEvent.Error::class.java))
        )
    }
}