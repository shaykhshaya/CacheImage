package com.example.cacheimage.presentation.image_listing.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.cacheimage.databinding.RowItemImageBinding
import com.example.cacheimage.domain.model.Image
import com.example.cacheimage.domain.model.ImageLinks
import com.example.cacheimage.util.ImageDownloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageListAdapter(
    private var mList: ArrayList<ImageLinks>,
    private val imageDownloader: ImageDownloader,
    private val scope: LifecycleOwner
) : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: RowItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ImageLinks) {
            binding.apply {
                //val imageUrl = makeUrl(item)

                //ivImage.loadImageByUrl(imageUrl)
              /*  if (item.uri == null) {

                    val itemScope = binding.ivImage.findViewTreeLifecycleOwner()?.lifecycle?.currentState
                   println("Sompe State------>>>>>"+itemScope.toString())
                    //Image not present
                  *//*  imageDownloader.downloadImage(
                        url = item.url,
                        onDownloaded = {
                            //Inset to DB URI
                            scope.lifecycleScope.launch(Dispatchers.Main) {
                                ivImage.setImageURI(it)
                            }


                        },
                        scope =scope
                    )*//*
                } else {
                    ivImage.setImageURI(item.uri)
                }*/

            }
        }


        fun startImageDownload(url: String, onDownloaded: (Uri)-> Unit){
            if(mList[adapterPosition].uri.isNullOrEmpty()){
                imageDownloader.downloadImage(
                    url = url,
                    onDownloaded = {
                        //Inset to DB URI
                        scope.lifecycleScope.launch(Dispatchers.Main) {
                            binding.ivImage.setImageURI(it)
                            onDownloaded(it)
                        }
                    },
                    scope =scope
                )
            }else{
                binding.ivImage.setImageURI(mList[adapterPosition].uri?.toUri())
            }


        }








    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowItemImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    private fun makeUrl(item: Image): String {
        return item.domain + "/" + item.basePath + "/0/" + item.key
    }
}