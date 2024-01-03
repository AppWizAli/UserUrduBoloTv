package com.hiskytech.userurdubolo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.admin.Constants
import com.bumptech.glide.Glide
import com.hiskytech.userurdubolo.Model.ModelSeason
import com.hiskytech.userurdubolo.R
import com.hiskytech.userurdubolo.databinding.ItemSeasonBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AdapterSeason (var activity: Context, val data: List<ModelSeason>, val listener: OnItemClickListener) : RecyclerView.Adapter<AdapterSeason.ViewHolder>(){


    var constant= Constants()

    interface OnItemClickListener {
        fun onItemClick(modelSeason: ModelSeason)
        fun onDeleteClick(modelSeason: ModelSeason)
        fun onEditClick(modelSeason: ModelSeason)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSeasonBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) { holder.bind(data[position]) }
    override fun getItemCount(): Int { return data.size }
    inner class ViewHolder(val itemBinding: ItemSeasonBinding) : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(modelSeason: ModelSeason) {


            Glide.with(activity).load(modelSeason.thumbnail).centerCrop().placeholder(R.drawable.ic_launcher_background).into(itemBinding.dramaImage)

            itemBinding.containerDrama.setOnClickListener{ listener.onItemClick(modelSeason)}


        }

    }

}