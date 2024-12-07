package com.dentify.dentifycare.ui.home.news

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dentify.dentifycare.data.local.News
import com.dentify.dentifycare.databinding.ItemNewsBinding
import com.dentify.dentifycare.utils.NewsDiffCallback

class NewsAdapter(private var news: List<News>): RecyclerView.Adapter<NewsAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemNewsBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val news = news[position]
        holder.binding.tvDesc.text = news.desc
        holder.binding.tvName.text = news.title
        Glide.with(holder.itemView.context)
            .load(news.image)
            .into(holder.binding.imgNews)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, NewsDetailActivity::class.java).apply {
                putExtra("NEWS_URL", news.url)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = news.size

    fun updateData(newList: List<News>) {
        val diffCallback = NewsDiffCallback(news, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        news = newList
        diffResult.dispatchUpdatesTo(this)
    }
}