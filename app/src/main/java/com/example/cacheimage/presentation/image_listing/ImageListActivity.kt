package com.example.cacheimage.presentation.image_listing

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cacheimage.databinding.ActivityImageListBinding
import com.example.cacheimage.domain.model.Image
import com.example.cacheimage.domain.model.ImageLinks
import com.example.cacheimage.presentation.image_listing.adapter.ImageListAdapter
import com.example.cacheimage.util.CustomImageBuilder
import com.example.cacheimage.util.ImageDownloader
import com.example.cacheimage.util.makeUrl
import com.example.movieslistapp.util.GridItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ImageListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageListBinding
    private var mImageListAdapter: ImageListAdapter? = null
    private var mImageList = arrayListOf<Image>()
    //private var mUriList = arrayListOf<Pair<String, String>>()
   // private var mImageLinkLists = arrayListOf<ImageLinks>()
    private val viewModel: ImageListViewModel by viewModels()

    @Inject
    lateinit var customImageBuilder: CustomImageBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.makeStretchedFullScreen()
        binding = ActivityImageListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeImageList()
        //observeUriList()


        initializeImageListAdapter()
        viewModel.getImages()
       // viewModel.getUriLocally()

       /* binding.rvImageList.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Get the first and last visible item positions
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                println("start and end position------->>>"+firstVisibleItemPosition+"and"+lastVisibleItemPosition)

                // Loop through the visible items
                for (i in firstVisibleItemPosition..lastVisibleItemPosition) {
                    // Get the ViewHolder for the current visible item
                    val viewHolder = recyclerView.findViewHolderForAdapterPosition(i)
                    if (viewHolder is ImageListAdapter.ViewHolder) {
                        // Start the download for the visible item
                        val url = mImageLinkLists[i].url
                        url?.let { url ->
                            viewHolder.startImageDownload(url){ uri ->
                                viewModel.insertUri(url, uri)
                            }
                        }
                    }
                }
            }

        })*/



    }

    private fun observeImageList() {

            lifecycleScope.launch {
                viewModel.mImageListStateFlow.collect { imageList ->
                    showProgressBar(false)
                    mImageList.clear()
                    mImageList.addAll(imageList)
                    /*  mUriList.addAll(
                        imageList.map { Pair(it.makeUrl(), "") }
                    )*/

                    // mImageLinkLists.addAll(imageList.map { ImageLinks(url = it.url, uri = null)  } )
                    mImageListAdapter?.notifyDataSetChanged()

                    //  downloadImageUri(mImageList)
                    println("--------->>>>>>>mImageList"+mImageList.size)
                }
            }


    }

   /* private fun observeUriList() {

        lifecycleScope.launch {
            viewModel.mImageUriListStateFlow.collect { imageList ->
                if (imageList != null) {
                    mImageLinkLists.addAll(imageList.map { ImageLinks(url = it.url, uri = it.uri)  } )
                    mImageListAdapter?.notifyDataSetChanged()

                    //  downloadImageUri(mImageList)
                    println("--------->>>>>>>mImageList"+mImageList.size)
                } else {
                    Toast.makeText(this@ImageListActivity, "data is null", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }*/

/*    private fun downloadImageUri(imageList: List<Image>) {
        mImageList.forEach{
            val imageUrl = it.makeUrl()
            println("--------->>>>>>>mUriList url "+ imageUrl)
            val imageUri =  viewModel.downloadImageAndGetUri(this@ImageListActivity, imageUrl)
            if (imageUri != null) {
                mUriList.add(imageUri)
                println("--------->>>>>>>mUriList"+mUriList.size)
                mImageListAdapter?.notifyDataSetChanged()

            }
        }


           *//* val imageUrl = imageList.get(0).makeUrl()
            val imageUri =  viewModel.downloadImageAndGetUri(this@ImageListActivity, imageUrl)
            if (imageUri != null) {
                mUriList.add(imageUri)
                println("--------->>>>>>>mUriList"+mUriList.size)
                mImageListAdapter?.notifyDataSetChanged()

            }*//*





    }*/

    private fun initializeImageListAdapter() {
        mImageListAdapter = ImageListAdapter(
            mImageList,
            customImageBuilder
        )
        binding.rvImageList.apply {
            adapter = mImageListAdapter
            layoutManager = GridLayoutManager(this@ImageListActivity, 3)
            addItemDecoration(GridItemDecorator(3, 5, false))
        }
    }

    private fun Window.makeStretchedFullScreen() {
        this.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

    }

    private fun showProgressBar(isShow: Boolean){
        binding.clProgressbar.visibility = if(isShow) View.VISIBLE else View.GONE
    }
}