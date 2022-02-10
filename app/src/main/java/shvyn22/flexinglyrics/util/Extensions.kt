package shvyn22.flexinglyrics.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.reactivex.rxjava3.core.Observable
import shvyn22.flexinglyrics.FlexingLyrics
import shvyn22.flexinglyrics.R
import shvyn22.flexinglyrics.di.component.SingletonComponent

fun RequestBuilder<Drawable>.defaultRequests(): RequestBuilder<Drawable> {
    return this
        .transition(DrawableTransitionOptions.withCrossFade())
        .centerCrop()
        .error(R.drawable.ic_error)
}

fun <T> Observable<T>.toLiveData(): LiveData<T> =
    MutableLiveData<T>().apply {
        this@toLiveData.subscribe { postValue(it) }
    }

val Context.singletonComponent: SingletonComponent
    get() = when (this) {
        is FlexingLyrics -> singletonComponent
        else -> applicationContext.singletonComponent
    }