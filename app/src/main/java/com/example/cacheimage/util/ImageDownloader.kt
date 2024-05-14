package com.example.cacheimage.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.LifecycleCoroutineScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageDownloader @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun downloadImage(
        url: String?,
        scope: CoroutineScope,
        onDownloaded: CoroutineScope.(Uri) -> Unit,
    ) = scope?.launch(Dispatchers.IO) {
        var imageUri: Uri? = null

        try {
            // Create a URL object from the imageUrl string
            val url = URL(url)

            // Open a connection to the URL
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()

            // Get the input stream from the connection
            val inputStream = connection.inputStream

            // Decode the input stream into a Bitmap
            val bitmap = BitmapFactory.decodeStream(inputStream)

            // Close the input stream
            inputStream.close()

            // Create a temporary file to store the downloaded image
            val fileName = "${System.currentTimeMillis()}.JPG"
            val file = File(context.cacheDir, fileName)

            // Create a FileOutputStream to write the Bitmap to the file
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

            // Flush and close the FileOutputStream
            outputStream.flush()
            outputStream.close()

            // Get the Uri for the temporary file
            imageUri = Uri.fromFile(file)
            onDownloaded(scope, imageUri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        scope.cancel()
    }


}