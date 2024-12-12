package com.example.projectadvocata.ui.compliment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectadvocata.R

class ListNewsAdapter(private val listNews: ArrayList<News>) :
    RecyclerView.Adapter<ListNewsAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        var tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        var tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val news = listNews[position]

        holder.imgPhoto.setImageResource(news.photo)
        holder.tvName.text = news.name
        holder.tvDescription.text = news.description

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailNews::class.java)
            intent.putExtra("NEWS_PHOTO", news.photo)
            intent.putExtra("NEWS_NAME", news.name)
            intent.putExtra("NEWS_DESCRIPTION", news.description)

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listNews.size
    }
}
