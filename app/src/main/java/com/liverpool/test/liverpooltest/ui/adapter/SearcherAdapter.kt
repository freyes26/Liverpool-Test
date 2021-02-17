package com.liverpool.test.liverpooltest.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.liverpool.test.liverpooltest.BaseApplication
import com.liverpool.test.liverpooltest.R
import com.liverpool.test.liverpooltest.databinding.ItemSearcherBinding
import com.liverpool.test.liverpooltest.repository.network.Connection
import com.liverpool.test.liverpooltest.repository.network.json.response.Records

class SearcherAdapter(val recordListener: ItemRecordListener) :
        ListAdapter<Records, SearcherAdapter.SearchViewHolder>(searcherComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val records = getItem(position)
        holder.bind(records, recordListener)
    }

    class SearchViewHolder private constructor(private val binding: ItemSearcherBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Records, recordListener: ItemRecordListener) {
            binding.item = item
            binding.thumbImage.setOnClickListener {
                recordListener.onClickListener(item,binding.thumbImage)
            }
            setImage(item.smImage)
            binding.executePendingBindings()
        }

        fun setImage(image : String){
            if(Connection.isConnected(BaseApplication.context)){
                Glide.with(BaseApplication.context).load(image).into(binding.thumbImage)
            }
            else{
                binding.thumbImage.setImageResource(R.drawable.ic_baseline_wifi_off_24)
            }
        }

        companion object {
            fun from(parent: ViewGroup): SearchViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSearcherBinding.inflate(layoutInflater, parent, false)
                return SearchViewHolder(binding)
            }
        }
    }


    object searcherComparator : DiffUtil.ItemCallback<Records>() {
        override fun areItemsTheSame(oldItem: Records, newItem: Records): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Records, newItem: Records): Boolean {
            return oldItem.productId == newItem.productId
        }

    }
}

class ItemRecordListener(val onClickListener: (Records, ImageView) -> Unit) {
    fun onClick(records: Records, imageView: ImageView) = onClickListener(records,imageView)
}