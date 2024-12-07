package com.dentify.dentifycare.utils

import androidx.recyclerview.widget.DiffUtil
import com.dentify.dentifycare.data.local.News

class NewsDiffCallback(
    private val oldList: List<News>,
    private val newList: List<News>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}