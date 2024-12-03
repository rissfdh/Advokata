@file:Suppress("unused", "RedundantSuppression")

package com.example.projectadvocata.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.projectadvocata.R
import com.example.projectadvocata.data.local.entity.EventEntity
import com.example.projectadvocata.databinding.ItemRowBinding

class EventAdapter(
    private val onItemClick: (EventEntity) -> Unit
) : ListAdapter<EventEntity, EventAdapter.EventViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)

    }

    class EventViewHolder(
        private val binding: ItemRowBinding,
        private val onItemClick: (EventEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: EventEntity) {
            binding.tvItemName.text = event.name

            Glide.with(itemView.context)
                .load(event.mediaCover)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .fitCenter()
                )
                .into(binding.imgItemPhoto)

            itemView.setOnClickListener {
                onItemClick(event)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<EventEntity> =
            object : DiffUtil.ItemCallback<EventEntity>() {
                override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                    return oldItem.id == newItem.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: EventEntity,
                    newItem: EventEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
