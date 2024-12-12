package com.example.projectadvocata.ui.compliment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectadvocata.R
import com.example.projectadvocata.ui.lawyer.Lawyer

class ListLawyerAdapter(private val listLawyer: ArrayList<Lawyer>) :
    RecyclerView.Adapter<ListLawyerAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        var tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        var tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_lawyer, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val lawyer = listLawyer[position]

        // Set data pada item
        holder.imgPhoto.setImageResource(lawyer.photo)
        holder.tvName.text = lawyer.name
        holder.tvDescription.text = lawyer.description

    }

    override fun getItemCount(): Int {
        return listLawyer.size
    }
}
