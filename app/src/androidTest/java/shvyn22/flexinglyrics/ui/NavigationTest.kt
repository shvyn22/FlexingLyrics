package shvyn22.flexinglyrics.ui

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import shvyn22.flexinglyrics.R
import shvyn22.flexinglyrics.ui.search.SearchAdapter
import shvyn22.flexinglyrics.util.track3

@HiltAndroidTest
class NavigationTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()

        launchActivity<MainActivity>()
    }

    @Test
    fun openDrawer_clickOnHistory_HistoryFragmentIsOpened() {
        onView(withId(R.id.drawer_main))
            .perform(DrawerActions.open())

        onView(withText(R.string.nav_history))
            .perform(click())

        onView(withId(R.id.tv_history_hint))
            .check(matches(isDisplayed()))

        onView(withId(R.id.action_delete))
            .check(matches(isDisplayed()))

        onView(withId(R.id.action_search))
            .check(doesNotExist())
    }

    @Test
    fun openDrawer_clickOnLibrary_LibraryFragmentIsOpened() {
        onView(withId(R.id.drawer_main))
            .perform(DrawerActions.open())

        onView(withText(R.string.nav_library))
            .perform(click())

        onView(withId(R.id.tv_library_hint))
            .check(matches(isDisplayed()))

        onView(withId(R.id.action_delete))
            .check(matches(isDisplayed()))

        onView(withId(R.id.action_search))
            .check(matches(isDisplayed()))
    }

    @Test
    fun navigateToDetails_navigateToHistory_TrackIsInHistory() {
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

        pressBack()

        onView(withId(R.id.drawer_main))
            .perform(DrawerActions.open())

        onView(withText(R.string.nav_history))
            .perform(click())

        onView(withId(R.id.tv_history_hint))
            .check(matches(isDisplayed()))

        onView(withText(track3.track))
            .check(matches(isDisplayed()))
    }

    @Test
    fun navigateToDetails_addToLibrary_navigateToLibrary_TrackIsInLibrary() {
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

        onView(withText(R.string.tag_add))
            .perform(click())

        pressBack()

        onView(withId(R.id.drawer_main))
            .perform(DrawerActions.open())

        onView(withText(R.string.nav_library))
            .perform(click())

        onView(withId(R.id.tv_library_hint))
            .check(matches(isDisplayed()))

        onView(withText(track3.track))
            .check(matches(isDisplayed()))
    }
}