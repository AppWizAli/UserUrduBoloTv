package com.hiskytech.userurdubolo.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.admin.Constants
import com.bumptech.glide.Glide
import com.hiskytech.userurdubolo.Model.ModelVideo
import com.hiskytech.userurdubolo.databinding.ItemAddVideoBinding
import java.text.SimpleDateFormat
import java.util.Locale


class AdapteraddVideo (var context: Context, val data: List<ModelVideo>, val listener: OnItemClickListener) : RecyclerView.Adapter<AdapteraddVideo.ViewHolder>(){


    var constant= Constants()

    interface OnItemClickListener {
        fun onItemClick(modelDrama: ModelVideo)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemAddVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) { holder.bind(data[position]) }
    override fun getItemCount(): Int { return data.size }
    inner class ViewHolder(val itemBinding: ItemAddVideoBinding) : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(modelDrama: ModelVideo) {

/*itemBinding.episodeNumber.text=modelDrama.episodeno*/
            Glide.with(context).load(modelDrama.thumbnail).centerCrop()
                .into(itemBinding.videoThumanil)
itemBinding.tvEpidoeNumber.text=modelDrama.episodeno
            itemBinding.containerDrama.setOnClickListener{ listener.onItemClick(modelDrama)}

        }

    }

}