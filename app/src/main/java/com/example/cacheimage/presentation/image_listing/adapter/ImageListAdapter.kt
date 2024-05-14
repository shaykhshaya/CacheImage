package com.example.cacheimage.presentation.image_listing.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cacheimage.databinding.RowItemImageBinding
import com.example.cacheimage.domain.model.Image
import com.example.cacheimage.util.CustomImageBuilder

class ImageListAdapter(
    private var mList: ArrayList<Image>,
    private val customImageBuilder: CustomImageBuilder,
) : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: RowItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Image) {
            binding.apply {
                //val imageUrl = makeUrl(item)

               // ivImage.loadImageByUrl(item.url)
                binding.progressCircular.visibility = View.VISIBLE
                customImageBuilder.with(ivImage.context)
                    .url(item.url)
                    .callback(object : CustomImageBuilder.Callback {
                        override fun onLoaded() {
                            binding.progressCircular.visibility = View.GONE
                        }
                    })
                    .into(ivImage)
                    .build()
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
         /*   if(mList[adapterPosition].uri.isNullOrEmpty()){
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
            }*/


        }








    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : RowItemImageBinding = RowItemImageBinding.inflate(
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