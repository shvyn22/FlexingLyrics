package shvyn22.flexinglyrics.ui.library

import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import shvyn22.flexinglyrics.R
import shvyn22.flexinglyrics.repository.local.LibraryRepository
import shvyn22.flexinglyrics.util.*
import shvyn22.flexinglyrics.util.RecyclerViewItemCountAssertion.Companion.withItemCount
import javax.inject.Inject

@HiltAndroidTest
class LibraryFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var libraryRepository: LibraryRepository

    @Before
    fun init() {
        hiltRule.inject()
        runBlocking {
            libraryRepository.add(track1)
            libraryRepository.add(track2)
            libraryRepository.add(track3)
        }
    }

    @Test
    fun search2TracksThatExist_Returns2TracksWithTheSameName() {
        launchFragmentInHiltContainer<LibraryFragment>()

        onView(withId(R.id.action_search))
            .perform(click())
        onView(withId(R.id.search_src_text))
            .perform(
                typeText(track1.track),
                pressImeActionButton()
            )

        onView(withId(R.id.rv_library))
            .check(matches(isDisplayed()))

        onView(withId(R.id.rv_library))
            .check(withItemCount(2))
    }

    @Test
    fun searchTrackThatExist_Returns1Track() {
        launchFragmentInHiltContainer<LibraryFragment>()

        onView(withId(R.id.action_search))
            .perform(click())
        onView(withId(R.id.search_src_text))
            .perform(
                typeText(track3.track),
                pressImeActionButton()
            )

        onView(withId(R.id.rv_library))
            .check(matches(isDisplayed()))

        onView(withId(R.id.rv_library))
            .check(withItemCount(1))
    }

    @Test
    fun searchTracksThatDoNotExist_ReturnsEmptyList() {
        launchFragmentInHiltContainer<LibraryFragment>()

        onView(withId(R.id.action_search))
            .perform(click())
        onView(withId(R.id.search_src_text))
            .perform(
                typeText(trackInfo2.track),
                pressImeActionButton()
            )

        onView(withId(R.id.rv_library))
            .check(matches(isDisplayed()))

        onView(withId(R.id.rv_library))
            .check(withItemCount(0))
    }

    @Test
    fun typeText_ThenEraseText_ReturnsToInitialState() {
        launchFragmentInHiltContainer<LibraryFragment>()

        onView(withId(R.id.action_search))
            .perform(click())
        onView(withId(R.id.search_src_text))
            .perform(
                typeText(track3.track),
                pressImeActionButton()
            )

        onView(withId(R.id.rv_library))
            .check(withItemCount(1))
        onView(withId(R.id.search_src_text))
            .perform(
                replaceText(""),
                pressImeActionButton()
            )
        onView(withId(R.id.rv_library))
            .check(withItemCount(3))
    }

    @Test
    fun deleteItem_ReturnsValidList() {
        launchFragmentInHiltContainer<LibraryFragment>()

        onView(withId(R.id.rv_library))
            .check(matches(isDisplayed()))

        onView(withId(R.id.rv_library))
            .check(withItemCount(3))

        onView(withId(R.id.rv_library))
            .perform(
                actionOnItemAtPosition<LibraryAdapter.LibraryViewHolder>(
                    1,
                    swipeRight()
                )
            )

        onView(withId(R.id.rv_library))
            .check(withItemCount(2))

        runBlocking { delay(100) }

        onView(withText(track3.track))
            .check(matches(isDisplayed()))

        onView(withText(track2.track))
            .check(matches(isDisplayed()))
    }

    @Test
    fun deleteAllItems_ReturnsEmptyList() {
        launchFragmentInHiltContainer<LibraryFragment>()

        onView(withId(R.id.action_delete))
            .perform(click())

        onView(withId(R.id.rv_library))
            .check(withItemCount(0))
    }

    @Test
    fun selectTrack_NavigateToDetails() {
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<LibraryFragment>(
            navController = navController
        )

        onView(withId(R.id.rv_library))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.rv_library))
            .perform(
                actionOnItemAtPosition<LibraryAdapter.LibraryViewHolder>(
                    1,
                    click()
                )
            )

        Mockito.verify(navController).navigate(
            LibraryFragmentDirections.actionLibraryFragmentToDetailsFragment(
                track1.copy(lyrics = TRACK_1_LYRICS)
            )
        )
    }
}