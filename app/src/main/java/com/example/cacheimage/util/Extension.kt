package com.example.cacheimage.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.cacheimage.R
import com.example.cacheimage.domain.model.Image
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

fun ImageView.loadImageByUrl(url: String) {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 10f
    circularProgressDrawable.centerRadius = 50f
    circularProgressDrawable.setColorSchemeColors(
        ContextCompat.getColor(
            this.context,
            R.color.blue
        )
    )
    circularProgressDrawable.start()
    Glide
        .with(this.context)
        .load(url)
        .centerCrop()
        .placeholder(circularProgressDrawable)
        .into(this)
}

fun LifecycleOwner.afterOnStart(action: suspend (CoroutineScope) -> Unit) : Job {
    return lifecycleScope.launch {
      /*  repeatOnLifecycle(Lifecycle.State.CREATED) {*/
            action.invoke(this)
        /*}*/
    }
}

/*fun String.downloadBitmap(): Bitmap?{

    return try {
        val connection = URL(this@downloadBitmap).openConnection()
        connection.connect()
        val inputStream = connection.getInputStream()
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        bitmap

    }catch (e: Exception){
        println("Exception-------->>>"+e)
        null
    }
}*/


/*suspend fun downloadImageAndGetUri(context: Context, imageUrl: String): Uri? = withContext(
    Dispatchers.IO) {

    var imageUri: Uri? = null

    try {
        // Create a URL object from the imageUrl string
        val url = URL(imageUrl)

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
        val file = File(context.cacheDir, "temp_image.jpg")

        // Create a FileOutputStream to write the Bitmap to the file
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

        // Flush and close the FileOutputStream
        outputStream.flush()
        outputStream.close()

        // Get the Uri for the temporary file
        imageUri = Uri.fromFile(file)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    // Return the Uri
    return@withContext imageUri
}*/


fun Image.makeUrl(): String {
    return "$domain/$basePath/0/$key"
}