package com.dentify.dentifycare.utils

import androidx.recyclerview.widget.DiffUtil
import com.dentify.dentifycare.data.local.HistoryPatient

class HistoryPatientDiffCallback(
    private val oldList: List<HistoryPatient>,
    private val newList: List<HistoryPatient>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].historyID == newList[newItemPosition].historyID
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}