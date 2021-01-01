package com.linh.doan.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.linh.doan.R

class StickerAdapter(private val itemList: List<Int>, val context: Context) :
    RecyclerView.Adapter<StickerAdapter.ItemViewHolder>() {

    lateinit var listener: OnItemClickListener

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageSticker: ImageView = itemView.findViewById(R.id.sticker_preview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.sticker_image_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.imageSticker.setImageResource(currentItem)
        holder.imageSticker.setOnClickListener {
            val name = it.resources.getResourceEntryName(currentItem)
            listener.onItemClick(name)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(nameSticker: String)
    }

    fun setOnItemClickListener(_listener: OnItemClickListener) {
        this.listener = _listener
    }
}