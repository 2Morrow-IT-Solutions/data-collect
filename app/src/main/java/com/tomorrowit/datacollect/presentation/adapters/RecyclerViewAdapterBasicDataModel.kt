package com.tomorrowit.datacollect.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tomorrowit.datacollect.databinding.SimpleListItemBinding
import com.tomorrowit.datacollect.domain.models.BasicDataModel

class RecyclerViewAdapterBasicDataModel(
    private val mContext: Context,
    private val mData: List<BasicDataModel>
) : RecyclerView.Adapter<RecyclerViewAdapterBasicDataModel.StringMapViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringMapViewHolder {
        val binding =
            SimpleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StringMapViewHolder(binding)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: StringMapViewHolder, position: Int) {
        with(holder) {
            with(mData[position]) {
                binding.itemDataText.text = title
                binding.itemDataDescription.text = description
            }
        }
    }

    inner class StringMapViewHolder(
        val binding: SimpleListItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

}