package com.tomorrowit.datacollect.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tomorrowit.datacollect.databinding.ItemDataCollectBinding
import com.tomorrowit.datacollect.domain.listeners.ItemClicked
import com.tomorrowit.datacollect.domain.models.CollectedDataModel

class RecyclerViewAdapterCollectedData(
    private val mContext: Context,
    private val mData: List<CollectedDataModel>,
    private val itemClicked: ItemClicked
) : RecyclerView.Adapter<RecyclerViewAdapterCollectedData.DataCollectedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataCollectedViewHolder {
        val binding =
            ItemDataCollectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataCollectedViewHolder(binding, itemClicked)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: DataCollectedViewHolder, position: Int) {
        with(holder) {
            with(mData[position]) {
                binding.dataCollectedIcon.setImageResource(icon)
                binding.dataCollectedTitle.text = title
                binding.dataCollectedDescription.text = description
            }
        }
    }

    inner class DataCollectedViewHolder(
        val binding: ItemDataCollectBinding,
        private val itemClicked: ItemClicked
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            this.binding.root.setOnClickListener {
                itemClicked.onItemClicked(adapterPosition)
            }
        }
    }
}