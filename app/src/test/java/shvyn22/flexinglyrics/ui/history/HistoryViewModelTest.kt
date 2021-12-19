package shvyn22.flexinglyrics.ui.history

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
import shvyn22.flexinglyrics.repository.FakeHistoryRepositoryImpl
import shvyn22.flexinglyrics.repository.FakeRemoteRepositoryImpl
import shvyn22.flexinglyrics.repository.local.HistoryRepository
import shvyn22.flexinglyrics.util.*

@ExperimentalCoroutinesApi
class HistoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var historyRepository: HistoryRepository
    private lateinit var viewModel: HistoryViewModel

    @Before
    fun setup() {
        historyRepository = FakeHistoryRepositoryImpl()
        viewModel = HistoryViewModel(
            FakeRemoteRepositoryImpl(),
            historyRepository
        )
    }

    @Test
    fun populateHistoryWith1Item_ReturnsThisItem() = runBlocking {
        historyRepository.add(track1)

        val items = viewModel.items

        assertThat(
            items.getOrAwaitValue().map { it.idTrack },
            `is`(listOf(track1.idTrack))
        )
    }

    @Test
    fun populateHistoryWith2Items_ReturnsTheseItemsInReversedOrder() = runBlocking {
        historyRepository.add(track1)
        historyRepository.add(track2)

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
        historyRepository.add(track1)
        historyRepository.add(track2)
        historyRepository.add(track3)

        val items = viewModel.items

        viewModel.deleteTrack(track2.idTrack)

        assertThat(
            items.getOrAwaitValue().map { it.idTrack },
            `is`(listOf(track3.idTrack, track1.idTrack))
        )
    }

    @Test
    fun populateHistoryWithItems_DeleteAllItems_ReturnsEmptyList() = runBlocking {
        historyRepository.add(track1)
        historyRepository.add(track2)
        historyRepository.add(track3)

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