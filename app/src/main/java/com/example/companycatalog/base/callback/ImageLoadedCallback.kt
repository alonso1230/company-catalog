package com.example.companycatalog.base.callback

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.example.companycatalog.R
import com.squareup.picasso.Callback

class ImageLoadedCallback(private var imageView: ImageView, private var progressBar: ProgressBar) :
    Callback {

    override fun onSuccess() {
        progressBar.visibility = View.GONE
        imageView.visibility = View.VISIBLE
    }

    override fun onError(e: Exception?) {
        progressBar.visibility = View.GONE
        imageView.setImageResource(R.drawable.ic_error)
        imageView.visibility = View.VISIBLE
    }
}