package shvyn22.flexinglyrics.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import shvyn22.flexinglyrics.R
import java.io.File
import java.io.FileOutputStream

fun createBitmap(
    context: Context,
    text: String
): Bitmap {
    val charSize = 16
    val lineSize = 20

    val lines = text.split("\n")

    val width = lines.maxOf { it.length } * charSize
    val height = lines.size * lineSize

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val paintBg = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.colorPrimary)
    }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBg)

    val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = charSize.toFloat()
        textAlign = Paint.Align.CENTER
        color = ContextCompat.getColor(context, R.color.colorAccent)
    }
    val offsetX = canvas.width / 2f
    var offsetY = lineSize * 2f
    lines.forEach { line ->
        canvas.drawText(line, offsetX, offsetY, paintText)
        offsetY += paintText.descent() - paintText.ascent()
    }

    return bitmap
}

fun downloadBitmap(
    context: Context,
    bitmap: Bitmap,
    title: String
) {
    val fileFormat = "jpeg"
    val fileName = "$title.$fileFormat"

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.MIME_TYPE, "image/$fileFormat")
    }

    if (Build.VERSION.SDK_INT >= 29) {
        contentValues.apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                "Pictures/" + context.getString(R.string.app_name)
            )
            put(MediaStore.MediaColumns.IS_PENDING, true)
        }

        val imageUri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )

        imageUri
            ?.let { context.contentResolver.openOutputStream(it) }
            ?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, false)
                context.contentResolver.update(imageUri, contentValues, null, null)
            }
    } else {
        val dir = File(
            Environment.getExternalStorageDirectory().toString()
                + "/" + context.getString(R.string.app_name)
        )
        if (!dir.exists()) dir.mkdirs()

        val file = File(dir, fileName)

        FileOutputStream(file)
            .use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }

        contentValues.put(MediaStore.Images.Media.DATA, file.absolutePath)
        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }
}