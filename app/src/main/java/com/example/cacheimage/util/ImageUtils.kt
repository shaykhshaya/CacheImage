package com.example.cacheimage.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


suspend fun getResizeBitmap(context: Context, uri: Uri, reqHeight: Int, reqWidth: Int) =
    suspendCancellableCoroutine<Bitmap?> {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height

        if (originalWidth > reqWidth || originalHeight > reqHeight) {

            val height: Int = reqHeight
            val width: Int = reqWidth

            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)

            it.resume(resizedBitmap)
        } else {
            it.resume(bitmap)
        }
    }