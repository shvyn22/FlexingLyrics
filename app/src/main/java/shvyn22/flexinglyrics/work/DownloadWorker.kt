package shvyn22.flexinglyrics.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import shvyn22.flexinglyrics.util.WORKER_BITMAP_KEY
import shvyn22.flexinglyrics.util.WORKER_TITLE_KEY
import shvyn22.flexinglyrics.util.deserializeBitmap
import shvyn22.flexinglyrics.util.downloadBitmap

class DownloadWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val title = inputData.getString(WORKER_TITLE_KEY)
                val jsonString = inputData.getString(WORKER_BITMAP_KEY)

                if (jsonString.isNullOrEmpty() || title.isNullOrEmpty())
                    Result.failure()
                else {
                    downloadBitmap(applicationContext, deserializeBitmap(jsonString), title)
                    Result.success()
                }
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }
}