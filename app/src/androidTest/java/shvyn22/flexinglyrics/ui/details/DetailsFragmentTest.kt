package shvyn22.flexinglyrics.ui.details

import android.os.Bundle
import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import shvyn22.flexinglyrics.R
import shvyn22.flexinglyrics.repository.local.LibraryRepository
import shvyn22.flexinglyrics.ui.details.adapter.AlbumAdapter
import shvyn22.flexinglyrics.util.*
import shvyn22.flexinglyrics.util.RecyclerViewItemCountAssertion.Companion.withItemCount
import javax.inject.Inject

@HiltAndroidTest
class DetailsFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var libraryRepository: LibraryRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun trackWithLyrics_launchFragment_ValidTrackDisplayed() {
        launchFragmentInHiltContainer<DetailsFragment>(
            fragmentArgs = Bundle().apply {
                putParcelable("track", track1.copy(lyrics = TRACK_1_LYRICS))
            }
        )

        onView(withId(R.id.tv_details_title))
            .check(matches(withText(track1.track)))

        onView(withId(R.id.tv_details_artist))
            .check(matches(withText(track1.artist)))

        onView(withId(R.id.tv_details_album))
            .check(matches(withText(track1.album)))
    }

    @Test
    fun trackWithLyrics_openLyricsTab_ValidLyricsDisplayed() {
        launchFragmentInHiltContainer<DetailsFragment>(
            fragmentArgs = Bundle().apply {
                putParcelable("track", track1.copy(lyrics = TRACK_1_LYRICS))
            }
        )

        onView(withId(R.id.tv_lyrics))
            .check(matches(withText(TRACK_1_LYRICS)))
    }

    @Test
    fun trackWithLyrics_openArtistInfoTab_ValidArtistInfoDisplayed() {
        launchFragmentInHiltContainer<DetailsFragment>(
            fragmentArgs = Bundle().apply {
                putParcelable("track", track1.copy(lyrics = TRACK_1_LYRICS))
            }
        )

        onView(withText(R.string.tab_artist)).perform(click())

        onView(withId(R.id.tv_artist))
            .check(matches(withText(track1.artist)))

        onView(withId(R.id.tv_artist_gender))
            .check(matches(withText("Gender: ${artistInfo1.gender}")))

        onView(withId(R.id.tv_artist_country))
            .check(matches(withText("Country: ${artistInfo1.country}")))

        onView(withId(R.id.iv_facebook)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_instagram)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_twitter)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_youtube)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_web)).check(matches(isDisplayed()))
    }

    @Test
    fun trackWithLyrics_openAlbumInfoTab_ValidAlbumInfoDisplayed() {
        launchFragmentInHiltContainer<DetailsFragment>(
            fragmentArgs = Bundle().apply {
                putParcelable("track", track1.copy(lyrics = TRACK_1_LYRICS))
            }
        )

        onView(withText(R.string.tab_album)).perform(click())

        onView(withId(R.id.tv_album))
            .check(matches(withText(albumInfo1.album)))

        onView(withId(R.id.rv_album))
            .check(withItemCount(albumInfo1.tracks.size))
    }

    @Test
    fun trackWithoutLyrics_launchFragment_ValidDataDisplayed() {
        launchFragmentInHiltContainer<DetailsFragment>(
            fragmentArgs = Bundle().apply {
                putParcelable("track", track1.copy(hasLyrics = false))
            }
        )

        onView(withId(R.id.tv_details_title))
            .check(matches(withText(track1.track)))

        onView(withId(R.id.tv_details_artist))
            .check(matches(withText(track1.artist)))

        onView(withId(R.id.tv_details_album))
            .check(matches(withText(track1.album)))

        onView(withId(R.id.tv_lyrics))
            .check(matches(withText(R.string.text_no_lyrics)))
    }

    @Test
    fun toggleLibraryButton_ValidStateOfLibrary() {
        launchFragmentInHiltContainer<DetailsFragment>(
            fragmentArgs = Bundle().apply {
                putParcelable("track", track1.copy(lyrics = TRACK_1_LYRICS))
            }
        )

        onView(withText(R.string.tag_add))
            .check(matches(isDisplayed()))
        onView(withText(R.string.tag_add))
            .perform(click())

        var isLibraryItem = runBlocking {
            libraryRepository.isLibraryItem(track1.idTrack).first()
        }
        assertThat(isLibraryItem, `is`(true))

        onView(withText(R.string.tag_remove))
            .check(matches(isDisplayed()))
        onView(withText(R.string.tag_remove))
            .perform(click())

        isLibraryItem = runBlocking {
            libraryRepository.isLibraryItem(track1.idTrack).first()
        }
        assertThat(isLibraryItem, `is`(false))

        onView(withText(R.string.tag_add))
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickOnAlbumTrack_NavigateToDetails() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<DetailsFragment>(
            fragmentArgs = Bundle().apply {
                putParcelable("track", track1.copy(lyrics = TRACK_1_LYRICS))
            },
            navController = navController
        )

        onView(withText(R.string.tab_album)).perform(click())

        onView(withId(R.id.rv_album))
            .perform(
                actionOnItemAtPosition<AlbumAdapter.AlbumViewHolder>(
                    1,
                    click()
                )
            )

        verify(navController).navigate(
            DetailsFragmentDirections.actionDetailsFragmentSelf(
                track4
            )
        )
    }
}