package com.example.paryavarankavalu.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ImageUtils(private val context: Context) {

    fun compressImage(uri: Uri, maxSizeKb: Int = 500): String? {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val outputStream = ByteArrayOutputStream()
        var quality = 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

        while (outputStream.toByteArray().size / 1024 > maxSizeKb && quality > 10) {
            outputStream.reset()
            quality -= 10
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        }

        val compressedFile = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
        FileOutputStream(compressedFile).use { fos ->
            fos.write(outputStream.toByteArray())
        }

        return compressedFile.absolutePath
    }
}