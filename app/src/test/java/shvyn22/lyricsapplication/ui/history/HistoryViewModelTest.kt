package shvyn22.lyricsapplication.ui.history

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
import shvyn22.lyricsapplication.repository.FakeRepositoryImpl
import shvyn22.lyricsapplication.repository.Repository
import shvyn22.lyricsapplication.util.*

@ExperimentalCoroutinesApi
class HistoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: Repository
    private lateinit var viewModel: HistoryViewModel

    @Before
    fun setup() {
        repository = FakeRepositoryImpl()
        viewModel = HistoryViewModel(repository)
    }

    @Test
    fun populateHistoryWith1Item_ReturnsThisItem() = runBlocking {
        repository.addToHistory(track1)

        val items = viewModel.items

        assertThat(
            items.getOrAwaitValue().map { it.idTrack },
            `is`(listOf(track1.idTrack))
        )
    }

    @Test
    fun populateHistoryWith2Items_ReturnsTheseItemsInReversedOrder() = runBlocking {
        repository.addToHistory(track1)
        repository.addToHistory(track2)

        val items = viewModel.items

        assertThat(
            items.getOrAwaitValue().map { it.idTrack },
            `is`(listOf(track2.idTrack, track1.idTrack))
        )
    }

    @Test
    fun populateHistoryWithNoItems_ReturnsEmptyList() = runBlocking {
        val items = viewModel.items

        assertThat(
            items.getOrAwaitValue(),
            `is`(emptyList())
        )
    }

    @Test
    fun populateHistoryWithItems_DeleteItem_ReturnsValidListInReversedOrder() = runBlocking {
        repository.addToHistory(track1)
        repository.addToHistory(track2)
        repository.addToHistory(track3)

        val items = viewModel.items

        viewModel.deleteTrack(track2.idTrack)

        assertThat(
            items.getOrAwaitValue().map { it.idTrack },
            `is`(listOf(track3.idTrack, track1.idTrack))
        )
    }

    @Test
    fun populateHistoryWithItems_DeleteAllItems_ReturnsEmptyList() = runBlocking {
        repository.addToHistory(track1)
        repository.addToHistory(track2)
        repository.addToHistory(track3)

        val items = viewModel.items

        viewModel.deleteAll()

        assertThat(
            items.getOrAwaitValue(),
            `is`(emptyList())
        )
    }

    @Test
    fun selectTrack_ChannelEmitsNavigateEventWithExpectedValue() = runBlocking {
        viewModel.onTrackSelected(fromTrackToHistoryItem(track1))

        val event = viewModel.historyEvent.take(2).toList()

        assertThat(
            event[0], `is`(instanceOf(StateEvent.Loading::class.java))
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

        val event = viewModel.historyEvent.first()

        assertThat(
            event, `is`(instanceOf(StateEvent.Error::class.java))
        )
    }
}