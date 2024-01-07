package com.hiskytech.userurdubolo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class BannerItemFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home_banner_item, container, false)
        val imageViewBannerItem: ImageView = root.findViewById(R.id.imageViewBannerItem)

        // Retrieve the image resource ID passed from the adapter
        val imageResource = requireArguments().getInt(ARG_IMAGE_RESOURCE)
        imageViewBannerItem.setImageResource(imageResource)

        return root
    }

    companion object {
        const val ARG_IMAGE_RESOURCE = "image_resource"
    }
}
