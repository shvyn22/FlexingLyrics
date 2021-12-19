package shvyn22.flexinglyrics.ui.search

import androidx.navigation.NavController
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import shvyn22.flexinglyrics.R
import shvyn22.flexinglyrics.ui.MainActivity
import shvyn22.flexinglyrics.util.RecyclerViewItemCountAssertion.Companion.withItemCount
import shvyn22.flexinglyrics.util.launchFragmentInHiltContainer
import shvyn22.flexinglyrics.util.track1
import shvyn22.flexinglyrics.util.track3
import shvyn22.flexinglyrics.util.trackInfo2

@HiltAndroidTest
class SearchFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun launchApplication_SearchFragmentIsInView() {
        launchActivity<MainActivity>()

        onView(withId(R.id.tv_search))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.et_search))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.rv_search))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun searchTracksThatExist_Returns2TracksWithTheSameName() {
        launchFragmentInHiltContainer<SearchFragment>()

        onView(withId(R.id.et_search))
            .perform(click())
        onView(withId(R.id.et_search))
            .perform(
                typeText(track1.track),
                pressImeActionButton()
            )
        onView(withId(R.id.tv_search))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(R.id.rv_search))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.rv_search))
            .check(withItemCount(2))
    }

    @Test
    fun searchTracksThatExist_Returns1Track() {
        launchFragmentInHiltContainer<SearchFragment>()

        onView(withId(R.id.et_search))
            .perform(click())
        onView(withId(R.id.et_search))
            .perform(
                typeText(track3.track),
                pressImeActionButton()
            )
        onView(withId(R.id.tv_search))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))


        onView(withId(R.id.rv_search))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.rv_search))
            .check(withItemCount(1))
        onView(withText(track3.artist))
            .check(matches(isDisplayed()))
    }

    @Test
    fun searchTracksThatDoNotExist_ReturnsEmptyList() {
        launchFragmentInHiltContainer<SearchFragment>()

        onView(withId(R.id.et_search))
            .perform(click())
        onView(withId(R.id.et_search))
            .perform(
                typeText(trackInfo2.track),
                pressImeActionButton()
            )
        onView(withId(R.id.tv_search))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))


        onView(withId(R.id.rv_search))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.rv_search))
            .check(withItemCount(0))
    }

    @Test
    fun typeText_ThenEraseText_ReturnsToInitialState() {
        launchFragmentInHiltContainer<SearchFragment>()

        onView(withId(R.id.et_search))
            .perform(click())
        onView(withId(R.id.et_search))
            .perform(typeText(trackInfo2.track))
        onView(withId(R.id.tv_search))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(R.id.et_search))
            .perform(replaceText(""))
        onView(withId(R.id.tv_search))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun selectTrack_NavigateToDetailsFragment() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<SearchFragment>(
            navController = navController
        )

        onView(withId(R.id.et_search))
            .perform(click())
        onView(withId(R.id.et_search))
            .perform(
                typeText(track3.track),
                pressImeActionButton()
            )

        onView(withId(R.id.rv_search))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.rv_search))
            .perform(
                actionOnItemAtPosition<SearchAdapter.SearchViewHolder>(
                    0,
                    click()
                )
            )

        verify(navController).navigate(
            SearchFragmentDirections.actionSearchFragmentToDetailsFragment(
                track3
            )
        )
    }
}