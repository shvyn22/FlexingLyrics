package shvyn22.flexinglyrics.util

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
import shvyn22.flexinglyrics.R

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