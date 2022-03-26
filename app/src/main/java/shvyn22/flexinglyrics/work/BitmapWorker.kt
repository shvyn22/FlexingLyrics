package shvyn22.flexinglyrics.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import shvyn22.flexinglyrics.util.WORKER_BITMAP_KEY
import shvyn22.flexinglyrics.util.WORKER_TEXT_KEY
import shvyn22.flexinglyrics.util.createBitmap
import shvyn22.flexinglyrics.util.serializeBitmap

class BitmapWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.Default) {
            try {
                val text = inputData.getString(WORKER_TEXT_KEY)

                if (text.isNullOrEmpty())
                    Result.failure()
                else {
                    val bitmap = createBitmap(applicationContext, text)
                    Result.success(
                        workDataOf(WORKER_BITMAP_KEY to serializeBitmap(bitmap))
                    )
                }
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }
}