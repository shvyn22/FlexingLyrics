package shvyn22.flexinglyrics.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import shvyn22.flexinglyrics.FlexingLyrics
import shvyn22.flexinglyrics.R
import shvyn22.flexinglyrics.di.component.SingletonComponent

fun RequestBuilder<Drawable>.defaultRequests(): RequestBuilder<Drawable> {
    return this
        .transition(DrawableTransitionOptions.withCrossFade())
        .centerCrop()
        .error(R.drawable.ic_error)
}

fun <T> Flow<T>.collectOnLifecycle(
    lifecycleOwner: LifecycleOwner,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend (value: T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.lifecycle.repeatOnLifecycle(state) {
            collect(block)
        }
    }
}

val Context.singletonComponent : SingletonComponent
    get() = when (this) {
        is FlexingLyrics -> singletonComponent
        else -> applicationContext.singletonComponent
    }