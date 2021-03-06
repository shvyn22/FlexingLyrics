package shvyn22.flexinglyrics.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import shvyn22.flexinglyrics.repository.FakeRemoteRepositoryImpl
import shvyn22.flexinglyrics.util.*

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        viewModel = SearchViewModel(FakeRemoteRepositoryImpl())
    }

    @Test
    fun searchTracksThatExist_Returns2TracksWithTheSameName() {
        viewModel.searchTracks(track1.track)

        val tracks = viewModel.tracks

        assertThat(
            tracks.getOrAwaitValue(),
            `is`(instanceOf(Resource.Loading::class.java))
        )

        assertThat(
            tracks.getOrAwaitValue(),
            `is`(instanceOf(Resource.Success::class.java))
        )
        assertThat(
            (tracks.getOrAwaitValue() as Resource.Success).data,
            `is`(listOf(track1, track2))
        )
    }

    @Test
    fun searchTracksThatExist_Returns1Track() {
        viewModel.searchTracks(track3.track)

        val tracks = viewModel.tracks

        assertThat(
            tracks.getOrAwaitValue(),
            `is`(instanceOf(Resource.Loading::class.java))
        )

        assertThat(
            tracks.getOrAwaitValue(),
            `is`(instanceOf(Resource.Success::class.java))
        )
        assertThat(
            (tracks.getOrAwaitValue() as Resource.Success).data,
            `is`(listOf(track3))
        )
    }

    @Test
    fun searchTracksThatDoNotExist_ReturnsError() {
        viewModel.searchTracks(trackInfo2.track)

        val tracks = viewModel.tracks

        assertThat(
            tracks.getOrAwaitValue(),
            `is`(instanceOf(Resource.Loading::class.java))
        )
        assertThat(
            tracks.getOrAwaitValue(),
            `is`(instanceOf(Resource.Error::class.java))
        )
    }

    @Test
    fun searchNoTracks_ReturnsIdle() {
        viewModel.searchTracks(null)

        val tracks = viewModel.tracks

        assertThat(
            tracks.getOrAwaitValue(),
            `is`(instanceOf(Resource.Loading::class.java))
        )
        assertThat(
            tracks.getOrAwaitValue(),
            `is`(instanceOf(Resource.Idle::class.java))
        )
    }

    @Test
    fun selectTrack_ChannelEmitsNavigateEventWithExpectedValue() = runBlocking {
        viewModel.onTrackSelected(track1)

        val event = viewModel.searchEvent.take(2).toList()

        assertThat(
            event[0], `is`(instanceOf(StateEvent.Loading::class.java))
        )
        assertThat(
            event[1], `is`(instanceOf(StateEvent.NavigateToDetails::class.java))
        )
        assertThat(
            (event[1] as StateEvent.NavigateToDetails).track,
            `is`(track1.copy(lyrics = TRACK_1_LYRICS))
        )
    }
}