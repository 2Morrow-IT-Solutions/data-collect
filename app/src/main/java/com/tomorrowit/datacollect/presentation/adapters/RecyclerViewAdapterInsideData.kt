package com.tomorrowit.datacollect.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tomorrowit.datacollect.databinding.SimpleListItemBinding
import com.tomorrowit.datacollect.domain.models.BasicDataModel

class RecyclerViewAdapterInsideData(
    private val mContext: Context,
    private val mData: List<BasicDataModel>
) : RecyclerView.Adapter<RecyclerViewAdapterInsideData.InsideDataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsideDataViewHolder {
        val binding =
            SimpleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InsideDataViewHolder(binding)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: InsideDataViewHolder, position: Int) {
        with(holder) {
            with(mData[position]) {
                binding.itemDataText.text = title
                binding.itemDataDescription.text = description
            }
        }
    }

    inner class InsideDataViewHolder(
        val binding: SimpleListItemBinding
    ) : RecyclerView.ViewHolder(binding.root)
}
