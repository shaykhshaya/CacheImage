package com.example.cacheimage.presentation.image_listing

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cacheimage.data.local.ImageEntity
import com.example.cacheimage.data.local.ImageLinkEntity
import com.example.cacheimage.data.mappers.toImageLinks
import com.example.cacheimage.data.remote.ImageDto
import com.example.cacheimage.domain.model.Image
import com.example.cacheimage.domain.model.ImageLinks
import com.example.cacheimage.domain.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageListViewModel @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {

  /*  init {
        viewModelScope.launch {
            repository.fetchImageListAndSaveToDatabase(Dispatchers.IO)
        }
    }*/



    private var _mImageListStateFlow = MutableStateFlow<List<Image>>(emptyList())
    val mImageListStateFlow: StateFlow<List<Image>> = _mImageListStateFlow


    /*fun getImageListLocally(){
        viewModelScope.launch {
            repository.getImageListLocally(Dispatchers.IO).collect(){
                _mImageListStateFlow.value = it
            }
        }
    }*/
    private var _mImageUriListStateFlow = MutableStateFlow<List<ImageLinks>>(emptyList())
    val mImageUriListStateFlow: StateFlow<List<ImageLinks>> = _mImageUriListStateFlow
    fun getUriLocally(){
        viewModelScope.launch {
            repository.getUriList(Dispatchers.IO).collect(){
                _mImageUriListStateFlow.value = it.map { it.toImageLinks() }

            }
        }
    }

    fun getImages(){
        viewModelScope.launch {
            repository.getList(Dispatchers.IO).collect {
                _mImageListStateFlow.value = it
            }
        }
    }

    fun insertUri(url: String, uri: Uri){
        viewModelScope.launch {
            repository.insertUri(Dispatchers.IO, ImageLinkEntity(url, uri.toString()) )
        }
    }

  /*  fun downloadImageAndGetUri(context: Context, imageUrl: String): Uri?{
        var uri: Uri? = null
        viewModelScope.launch {
          uri =  repository.downloadImageAndGetUri(
                dispatcher = Dispatchers.IO,
                context = context,
                imageUrl = imageUrl
            )
        }
        return uri
    }*/


}