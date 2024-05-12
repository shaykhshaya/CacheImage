package com.example.cacheimage.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.DiscretePathEffect
import android.net.Uri
import androidx.core.net.toUri
import com.example.cacheimage.data.local.ImageDatabase
import com.example.cacheimage.data.local.ImageEntity
import com.example.cacheimage.data.local.ImageLinkDatabase
import com.example.cacheimage.data.local.ImageLinkEntity
import com.example.cacheimage.data.mappers.toImage
import com.example.cacheimage.data.mappers.toImageEntity
import com.example.cacheimage.data.remote.ImageDto
import com.example.cacheimage.data.remote.client.MainServices
import com.example.cacheimage.domain.model.Image
import com.example.cacheimage.domain.repository.ImageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val mainServices: MainServices,
    private val imageDb: ImageDatabase,
    private val imageLinkDb: ImageLinkDatabase
) : ImageRepository {

    override suspend fun getList(dispatcher: CoroutineDispatcher): Flow<List<Image>> =
        imageDb.dao.getAll().map {
            if (it.isEmpty()){
                val images = mainServices.getRemoteImageList().body()
                val imageEntityList = images?.map { imageDto ->
                    imageDto.toImageEntity()
                }
                if (imageEntityList != null) {
                    imageDb.dao.insertImages(imageEntityList)
                }
            }
            it.map { it.toImage() }
        }

    override suspend fun insertUri(dispatcher: CoroutineDispatcher, imageLinkEntity: ImageLinkEntity) {

        withContext(Dispatchers.IO){
            imageLinkDb.dao.insertImagesLink(imageLinkEntity)
        }
    }

    override suspend fun getUriList(dispatcher: CoroutineDispatcher): Flow<List<ImageLinkEntity>> =
        imageLinkDb.dao.getAllImagesLink()




    /* override suspend fun fetchImageListAndSaveToDatabase(dispatcher: CoroutineDispatcher) {
         withContext(dispatcher) {

             try {
                 val images = mainServices.getRemoteImageList().body()
                 val imageEntityList = images?.map { imageDto ->
                     imageDto.toImageEntity()
                 }

                 if (imageEntityList != null) {
                     imageDb.dao.insertImages(imageEntityList)
                 }
             } catch (e: Exception) {
                 println("error message is----->>>>" + e.message)
             }
         }
     }*/
    /*override suspend fun getImageListLocally(dispatcher: CoroutineDispatcher): Flow<List<ImageEntity>> {
        return withContext(dispatcher) {
            imageDb.dao.getAll()
        }
    }*/

    override suspend fun downloadImageAndGetUri(
        dispatcher: CoroutineDispatcher,
        context: Context,
        imageUrl: String
    ): Uri? {


        return withContext(dispatcher) {

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
                val file = File(context.cacheDir, imageUrl)

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
        }
    }


}

