package com.rikkei.tranning.chatapp.views.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rikkei.tranning.chatapp.R
import java.io.File


class ImageAdapter(private val itemList: List<String>, val context: Context) :
    RecyclerView.Adapter<ImageAdapter.ItemViewHolder>() {
    private var listener: OnItemClickListener? = null

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageSticker: ImageView = itemView.findViewById(R.id.image_preview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        Glide.with(context).load(currentItem).into(holder.imageSticker)
        holder.imageSticker.setOnClickListener {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener!!.onItemClick(Uri.fromFile(File(currentItem)))
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(uri: Uri)
    }

    fun setOnItemClickListener(_listener: OnItemClickListener) {
        listener = _listener
    }
}