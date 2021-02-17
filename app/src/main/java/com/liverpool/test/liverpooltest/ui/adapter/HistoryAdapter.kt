package com.liverpool.test.liverpooltest.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.liverpool.test.liverpooltest.databinding.ItemHistoryBinding
import com.liverpool.test.liverpooltest.repository.database.model.Search

class HistoryAdapter(private val historyListener: ItemHistoryListener) :
        ListAdapter<Search, HistoryAdapter.HistoryViewHolder>(HistoryComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val search = getItem(position)
        holder.bind(search, historyListener)
    }

    class HistoryViewHolder private constructor(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Search, historyListener: ItemHistoryListener) {
            binding.item = item
            binding.historyListener = historyListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): HistoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemHistoryBinding.inflate(layoutInflater, parent, false)
                return HistoryViewHolder(binding)
            }
        }
    }

    class HistoryComparator : DiffUtil.ItemCallback<Search>() {
        override fun areItemsTheSame(oldItem: Search, newItem: Search): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Search, newItem: Search): Boolean {
            return oldItem.id == newItem.id
        }

    }

}

class ItemHistoryListener(val onClickListener: (Search) -> Unit) {
    fun onClick(search: Search) = onClickListener(search)
}