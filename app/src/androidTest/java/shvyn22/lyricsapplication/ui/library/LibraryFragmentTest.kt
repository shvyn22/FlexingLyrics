package shvyn22.lyricsapplication.ui.library

import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
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
import shvyn22.lyricsapplication.R
import shvyn22.lyricsapplication.repository.Repository
import shvyn22.lyricsapplication.util.*
import shvyn22.lyricsapplication.util.RecyclerViewItemCountAssertion.Companion.withItemCount
import javax.inject.Inject

@HiltAndroidTest
class LibraryFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: Repository

    @Before
    fun init() {
        hiltRule.inject()
        runBlocking {
            repository.addToLibrary(track1)
            repository.addToLibrary(track2)
            repository.addToLibrary(track3)
        }
    }

    @Test
    fun search2TracksThatExist_Returns2TracksWithTheSameName() {
        launchFragmentInHiltContainer<LibraryFragment>()

        onView(withId(R.id.action_search))
            .perform(click())
        onView(withId(R.id.search_src_text))
            .perform(
                typeText("Radioactive"),
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
                typeText("Believer"),
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
                typeText("Underdog"),
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
                typeText("Believer"),
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

        onView(withText("Radioactive"))
            .check(matches(isDisplayed()))

        onView(withText("Believer"))
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