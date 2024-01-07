package com.hiskytech.userurdubolo.Adapter

import android.content.Context
import android.opengl.GLDebugHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hiskytech.userurdubolo.R
import java.io.File

class VideoAdapter(
    var context: Context,
    private val videoFiles: List<String>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val container: LinearLayout = itemView.findViewById(R.id.containerDrama)
        val thumnail: ImageView = itemView.findViewById(R.id.video_thumanil)
    }

    interface OnItemClickListener {
        fun onItemClick(videoFile: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val videoFile = videoFiles[position]
        val fileName = File(videoFile).nameWithoutExtension
        val modifiedFileName = if (fileName.startsWith("K")) {
            fileName.substring(1) // Remove the 'C' from the beginning of the filename
        } else {
            fileName
        }


        holder.container.setOnClickListener { listener.onItemClick(videoFile) }
    }


    override fun getItemCount(): Int {
        return videoFiles.size
    }
    fun generateURL(uniqueIdentifier: String): String {
        val baseURL = "https://firebasestorage.googleapis.com/v0/b/urdubolotv-c88cd.appspot.com/o/thumbnails%2F"
        val constantPart = "?alt=media&token=72d816a0-bc45-4f47-b650-e3c87203a946"

        return "$baseURL$uniqueIdentifier$constantPart"
    }
}
