package shvyn22.flexinglyrics.ui.history

import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import shvyn22.flexinglyrics.R
import shvyn22.flexinglyrics.repository.local.HistoryRepository
import shvyn22.flexinglyrics.ui.search.SearchAdapter
import shvyn22.flexinglyrics.util.RecyclerViewItemCountAssertion.Companion.withItemCount
import shvyn22.flexinglyrics.util.TRACK_1_LYRICS
import shvyn22.flexinglyrics.util.launchFragmentInHiltContainer
import shvyn22.flexinglyrics.util.track1
import shvyn22.flexinglyrics.util.track3
import javax.inject.Inject

@HiltAndroidTest
class HistoryFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var historyRepository: HistoryRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun populateHistoryWith1Item_ReturnsThisItem() {
        runBlocking {
            historyRepository.add(track1)
        }

        launchFragmentInHiltContainer<HistoryFragment>()

        onView(withId(R.id.rv_history))
            .check(matches(isDisplayed()))
        onView(withId(R.id.rv_history))
            .check(withItemCount(1))

        onView(withText(track1.track))
            .check(matches(isDisplayed()))
    }

    @Test
    fun populateHistoryWith2Items_ReturnsTheseItems() {
        runBlocking {
            historyRepository.add(track1)
            historyRepository.add(track3)
        }

        launchFragmentInHiltContainer<HistoryFragment>()

        onView(withId(R.id.rv_history))
            .check(matches(isDisplayed()))
        onView(withId(R.id.rv_history))
            .check(withItemCount(2))

        onView(withText(track1.track))
            .check(matches(isDisplayed()))
        onView(withText(track3.track))
            .check(matches(isDisplayed()))
    }

    @Test
    fun populateHistoryWithNoItems_ReturnsEmptyList() {
        launchFragmentInHiltContainer<HistoryFragment>()

        onView(withId(R.id.rv_history))
            .check(matches(isDisplayed()))
        onView(withId(R.id.rv_history))
            .check(withItemCount(0))
    }

    @Test
    fun populateHistoryWithItems_SwipeToDelete1Item_ReturnsValidList() {
        runBlocking {
            historyRepository.add(track1)
            historyRepository.add(track3)
        }

        launchFragmentInHiltContainer<HistoryFragment>()

        onView(withId(R.id.rv_history))
            .check(matches(isDisplayed()))
        onView(withId(R.id.rv_history))
            .check(withItemCount(2))

        onView(withId(R.id.rv_history))
            .perform(
                actionOnItemAtPosition<HistoryAdapter.HistoryViewHolder>(
                    1,
                    swipeRight()
                )
            )

        onView(withId(R.id.rv_history))
            .check(withItemCount(1))
        onView(withText(track3.track))
            .check(matches(isDisplayed()))
    }

    @Test
    fun populateHistoryWithItems_DeleteAllItems_ReturnsEmptyList() {
        runBlocking {
            historyRepository.add(track1)
            historyRepository.add(track3)
        }

        launchFragmentInHiltContainer<HistoryFragment>()

        onView(withId(R.id.rv_history))
            .check(matches(isDisplayed()))
        onView(withId(R.id.rv_history))
            .check(withItemCount(2))

        onView(withId(R.id.action_delete))
            .perform(click())
        onView(withId(R.id.rv_history))
            .check(withItemCount(0))
    }

    @Test
    fun selectTrack_NavigateToDetailsFragment() {
        runBlocking {
            historyRepository.add(track1)
        }

        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<HistoryFragment>(
            navController = navController
        )

        onView(withId(R.id.rv_history))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.rv_history))
            .perform(
                actionOnItemAtPosition<SearchAdapter.SearchViewHolder>(
                    0,
                    click()
                )
            )

        Mockito.verify(navController).navigate(
            HistoryFragmentDirections.actionHistoryFragmentToDetailsFragment(
                track1.copy(lyrics = TRACK_1_LYRICS)
            )
        )
    }
}