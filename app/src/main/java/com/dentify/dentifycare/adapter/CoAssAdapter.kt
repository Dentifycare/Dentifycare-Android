package com.dentify.dentifycare.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dentify.dentifycare.data.local.CoAssBooked
import com.dentify.dentifycare.databinding.ItemCoAssBinding
import com.dentify.dentifycare.ui.home.booked.DetailBookedActivity
import com.dentify.dentifycare.utils.CoAssDiffCallback

class CoAssAdapter(private var post: List<CoAssBooked>): RecyclerView.Adapter<CoAssAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemCoAssBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemCoAssBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val postCoAss = post[position]
        holder.binding.tvName.text = postCoAss.name
        holder.binding.tvHospitalTitle.text = postCoAss.hospital
        holder.binding.tvHours.text = postCoAss.selectedHours[0]
        holder.binding.tvDated.text = postCoAss.currentDate

        holder.itemView.setOnClickListener {
            val context = it.context
            val intent = Intent(context, DetailBookedActivity::class.java).apply {
                putExtra("EXTRA_NAME", postCoAss.name)
                putExtra("EXTRA_HOSPITAL", postCoAss.hospital)
                putExtra("EXTRA_SKILL", postCoAss.selectedSkills[0])
                putExtra("EXTRA_ADDITIONAL_INFORMATION", postCoAss.additionalInfo)
                putExtra("EXTRA_OPERATIONAL_DATE", postCoAss.currentDate)
                putExtra("EXTRA_OPERATIONAL_HOURS", postCoAss.selectedHours[0])
                putExtra("EXTRA_PHONE_Co_Ass", postCoAss.phone)
                Log.d("CEKPHONE", postCoAss.phone)
                putExtra("EXTRA_REMAINING_QUOTA", postCoAss.quota)
                putExtra("EXTRA_QUOTA", postCoAss.quota)
                putExtra("EXTRA_PHONE", postCoAss.postId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = post.size

    fun updateData(newList: List<CoAssBooked>) {
        val diffCallback = CoAssDiffCallback(post, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        post = newList
        diffResult.dispatchUpdatesTo(this)
    }
}