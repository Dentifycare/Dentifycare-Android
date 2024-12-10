package com.dentify.dentifycare.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dentify.dentifycare.R
import com.dentify.dentifycare.data.local.HistoryPatient
import com.dentify.dentifycare.databinding.ItemActivityBinding
import com.dentify.dentifycare.ui.activity.patient.FeedbackPageActivity
import com.dentify.dentifycare.utils.HistoryPatientDiffCallback

class HistoryPatientAdapter(private var post: List<HistoryPatient>): RecyclerView.Adapter<HistoryPatientAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemActivityBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val historyPatient = post[position]
        holder.binding.tvName.text = historyPatient.nameCoAss
        holder.binding.tvHospitalTitle.text = historyPatient.hospital
        holder.binding.tvStatusActivity.text = historyPatient.status

        if (historyPatient.status == "Uncompleted") {
            holder.binding.imgStatus.backgroundTintList = holder.itemView.context.getColorStateList(
                R.color.dentifycare_button_red_color)

            holder.itemView.setOnClickListener {
                val context = it.context
                val intent = Intent(context, FeedbackPageActivity::class.java).apply {
                    putExtra("EXTRA_NAME_Co_Ass", historyPatient.nameCoAss)
                    putExtra("EXTRA_HISTORY_ID", historyPatient.historyID)
                    Log.d("HistoryPatientAdapter", "History ID: ${historyPatient.historyID}")
                    putExtra("EXTRA_HOSPITAL", historyPatient.hospital)
                }
                context.startActivity(intent)
            }
        } else {
            holder.itemView.setOnClickListener {
                Toast.makeText(holder.itemView.context, "Status: Completed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = post.size

    fun updateData(newList: List<HistoryPatient>) {
        val diffCallback = HistoryPatientDiffCallback(post, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        post = newList
        diffResult.dispatchUpdatesTo(this)
    }
}