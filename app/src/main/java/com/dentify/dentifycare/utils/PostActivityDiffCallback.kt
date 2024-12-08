package com.dentify.dentifycare.utils

import androidx.recyclerview.widget.DiffUtil
import com.dentify.dentifycare.data.local.PostCoAss

class PostActivityDiffCallback(
    private val oldList: List<PostCoAss>,
    private val newList: List<PostCoAss>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].postId == newList[newItemPosition].postId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}