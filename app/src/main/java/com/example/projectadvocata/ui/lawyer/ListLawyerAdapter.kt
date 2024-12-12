package com.example.projectadvocata.ui.compliment

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectadvocata.databinding.ItemLawyerBinding
import com.example.projectadvocata.ui.lawyer.Lawyer
import com.example.projectadvocata.ui.lawyer.LawyerChatActivity
class ListLawyerAdapter(private val listLawyer: List<Lawyer>) :
    RecyclerView.Adapter<ListLawyerAdapter.LawyerViewHolder>() {

    inner class LawyerViewHolder(val binding: ItemLawyerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(lawyer: Lawyer) {
            binding.tvItemName.text = lawyer.name
            binding.tvItemDescription.text = lawyer.description
            binding.imgItemPhoto.setImageResource(lawyer.photo)

            binding.btnConsultNow.setOnClickListener {
                val context = it.context
                val intent = Intent(context, LawyerChatActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawyerViewHolder {
        val binding = ItemLawyerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LawyerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LawyerViewHolder, position: Int) {
        holder.bind(listLawyer[position])
    }

    override fun getItemCount(): Int {
        return listLawyer.size
    }
}
