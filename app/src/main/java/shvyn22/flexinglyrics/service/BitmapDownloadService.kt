package shvyn22.flexinglyrics.service

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.os.IBinder
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import shvyn22.flexinglyrics.R
import shvyn22.flexinglyrics.util.*
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class BitmapDownloadService : Service() {

    @Inject
    @Named(SERVICE_SCOPE_NAME)
    lateinit var scope: CoroutineScope

    private var _job: Job? = null
    private val job: Job get() = _job!!

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        _job = scope.launch {
            val text = intent?.extras?.getString(WORK_TEXT_KEY)
            val title = intent?.extras?.getString(WORK_TITLE_KEY)

            val bitmapResource = createBitmap(text)

            if (bitmapResource is Resource.Success) {
                val downloadResource = downloadBitmap(title, bitmapResource.data)
                if (downloadResource is Resource.Success) {
                    Toast
                        .makeText(
                            applicationContext,
                            getString(R.string.text_success_loading),
                            Toast.LENGTH_LONG
                        )
                        .show()
                }
            }
        }
        return START_STICKY
    }

    private suspend fun createBitmap(text: String?): Resource<Bitmap> {
        return withContext(Dispatchers.Default) {
            try {
                if (text.isNullOrEmpty())
                    Resource.Error("")
                else {
                    val bitmap = createBitmap(applicationContext, text)
                    Resource.Success(bitmap)
                }
            } catch (e: Exception) {
                Resource.Error("")
            }
        }
    }

    private suspend fun downloadBitmap(title: String?, bitmap: Bitmap): Resource<Nothing?> {
        return withContext(Dispatchers.IO) {
            try {
                if (title.isNullOrEmpty())
                    Resource.Error("")
                else {
                    downloadBitmap(applicationContext, bitmap, title)
                    Resource.Success(null)
                }
            } catch (e: Exception) {
                Resource.Success(null)
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}