package shvyn22.lyricsapplication.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.core.Is.`is`

class RecyclerViewItemCountAssertion(
    private val expectedCount: Int
) : ViewAssertion {

    companion object {
        fun withItemCount(expectedCount: Int): ViewAssertion {
            return RecyclerViewItemCountAssertion(expectedCount)
        }
    }

    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) throw noViewFoundException

        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter

        assertThat(adapter?.itemCount, `is`(expectedCount))
    }
}