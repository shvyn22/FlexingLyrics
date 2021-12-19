package shvyn22.flexinglyrics.ui.library

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import shvyn22.flexinglyrics.repository.FakeLibraryRepositoryImpl
import shvyn22.flexinglyrics.repository.FakeRemoteRepositoryImpl
import shvyn22.flexinglyrics.repository.local.LibraryRepository
import shvyn22.flexinglyrics.util.*

@ExperimentalCoroutinesApi
class LibraryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var libraryRepository: LibraryRepository
    private lateinit var viewModel: LibraryViewModel

    @Before
    fun setup() {
        libraryRepository = FakeLibraryRepositoryImpl()
        viewModel = LibraryViewModel(
            FakeRemoteRepositoryImpl(),
            libraryRepository
        )
    }

    @Test
    fun search2TracksThatExist_Returns2TracksWithTheSameName() = runBlocking {
        libraryRepository.add(track1)
        libraryRepository.add(track2)
        libraryRepository.add(track3)

        viewModel.searchTracks(track1.track)
        val items = viewModel.items

        assertThat(
            items.getOrAwaitValue().map { it.idTrack },
            `is`(listOf(track1.idTrack, track2.idTrack))
        )
    }

    @Test
    fun searchTrackThatExist_Returns1Track() = runBlocking {
        libraryRepository.add(track3)

        viewModel.searchTracks(track3.track)
        val items = viewModel.items

        assertThat(
            items.getOrAwaitValue().map { it.idTrack },
            `is`(listOf(track3.idTrack))
        )
    }

    @Test
    fun searchTracksThatDoNotExist_ReturnsEmptyList() = runBlocking {
        viewModel.searchTracks(trackInfo2.track)
        val items = viewModel.items

        assertThat(
            items.getOrAwaitValue(),
            `is`(emptyList())
        )
    }

    @Test
    fun populateHistoryWithItems_DeleteItem_ReturnsValidList() = runBlocking {
        libraryRepository.add(track1)
        libraryRepository.add(track2)
        libraryRepository.add(track3)

        val items = viewModel.items

        viewModel.deleteTrack(track2.idTrack)

        assertThat(
            items.getOrAwaitValue().map { it.idTrack },
            `is`(listOf(track1.idTrack, track3.idTrack))
        )
    }

    @Test
    fun populateHistoryWithItems_DeleteAllItems_ReturnsEmptyList() = runBlocking {
        libraryRepository.add(track1)
        libraryRepository.add(track2)
        libraryRepository.add(track3)

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
}