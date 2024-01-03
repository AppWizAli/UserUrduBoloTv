package com.hiskytech.userurdubolo.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.admin.Constants
import com.bumptech.glide.Glide
import com.hiskytech.userurdubolo.Model.ModelDrama
import com.hiskytech.userurdubolo.R
import com.hiskytech.userurdubolo.databinding.ItemDramaBinding
import java.text.SimpleDateFormat
import java.util.Locale


class AdapterDrama (var context: Context, val data: List<ModelDrama>, val listener: OnItemClickListener) : RecyclerView.Adapter<AdapterDrama.ViewHolder>(){


    var constant= Constants()

    interface OnItemClickListener {
        fun onItemClick(modelDrama: ModelDrama)
        fun onDeleteClick(modelDrama: ModelDrama)
        fun onEditClick(modelDrama: ModelDrama)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDramaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) { holder.bind(data[position]) }
    override fun getItemCount(): Int { return data.size }
    inner class ViewHolder(val itemBinding: ItemDramaBinding) : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(modelDrama: ModelDrama) {


            Glide.with(context).load(modelDrama.thumbnail).centerCrop().placeholder(R.drawable.ic_launcher_background)
                .into(itemBinding.dramaImage)

            itemBinding.containerDrama.setOnClickListener{ listener.onItemClick(modelDrama)}


        }

    }

}