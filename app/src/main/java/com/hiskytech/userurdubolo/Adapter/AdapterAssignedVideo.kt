package com.hiskytech.userurdubolo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.admin.Constants
import com.bumptech.glide.Glide
import com.hiskytech.userurdubolo.Model.ModelVideo
import com.hiskytech.userurdubolo.R
import com.hiskytech.userurdubolo.databinding.ItemVideoManagementBinding

class AdapterAssignedVideo(
    var from:String,
    var activity: Context,
    var data: List<ModelVideo>,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<AdapterAssignedVideo.ViewHolder>() {

    interface OnItemClickListener {
        fun onUnAssignClick(modelVideo: ModelVideo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemVideoManagementBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateList(newList: List<ModelVideo>) {
        data = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val itemBinding: ItemVideoManagementBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(modelVideo: ModelVideo) {
            if(from.equals("Assigned"))
            {
                itemBinding.btnAssign.text="UnAssign"
            }
            itemBinding.dramaName.text = modelVideo.dramaName
            itemBinding.episodeNumber.text = modelVideo.episodeno
            itemBinding.totalEpisode.text = modelVideo.totalepisodes
            Glide.with(activity).load(modelVideo.thumbnail).placeholder(R.drawable.ic_launcher_background).centerCrop().into(itemBinding.dramaImage)


            itemBinding.btnAssign.setOnClickListener {
                listener.onUnAssignClick(modelVideo)
            }
        }
    }
}
