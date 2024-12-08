package com.dentify.dentifycare.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dentify.dentifycare.R
import com.dentify.dentifycare.data.local.PostCoAss
import com.dentify.dentifycare.databinding.ItemActivityBinding
import com.dentify.dentifycare.ui.activity.coass.DeleteActivity
import com.dentify.dentifycare.utils.PostActivityDiffCallback

class ActivityCoAssAdapter(private var post: List<PostCoAss>): RecyclerView.Adapter<ActivityCoAssAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemActivityBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val postCoAss = post[position]
        holder.binding.tvName.text = postCoAss.name
        holder.binding.tvHospitalTitle.text = postCoAss.hospital
        holder.binding.tvStatusActivity.text = postCoAss.status

        if (postCoAss.status == "Uncompleted") {
            holder.binding.imgStatus.backgroundTintList = holder.itemView.context.getColorStateList(R.color.dentifycare_button_red_color)
        }

        holder.itemView.setOnClickListener {
            val context = it.context
            val intent = Intent(context, DeleteActivity::class.java).apply {
                putExtra("EXTRA_NAME", postCoAss.name)
                putExtra("EXTRA_HOSPITAL", postCoAss.hospital)
                putExtra("EXTRA_SKILL", postCoAss.selectedSkills[0])
                putExtra("EXTRA_ADDITIONAL_INFORMATION", postCoAss.additionalInfo)
                putExtra("EXTRA_OPERATIONAL_DATE", postCoAss.currentDate)
                putExtra("EXTRA_OPERATIONAL_HOURS", postCoAss.selectedHours[0])
                putExtra("EXTRA_REMAINING_QUOTA", postCoAss.quota)
                putExtra("EXTRA_QUOTA", postCoAss.quota)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = post.size

    fun updateData(newList: List<PostCoAss>) {
        val diffCallback = PostActivityDiffCallback(post, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        post = newList
        diffResult.dispatchUpdatesTo(this)
    }
}