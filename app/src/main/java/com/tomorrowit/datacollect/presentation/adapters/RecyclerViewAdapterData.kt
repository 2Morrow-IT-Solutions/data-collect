package com.tomorrowit.datacollect.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tomorrowit.datacollect.databinding.ItemDataBinding
import com.tomorrowit.datacollect.domain.models.DataModel

class RecyclerViewAdapterData(
    private val mContext: Context,
    private val mData: List<DataModel>
) : RecyclerView.Adapter<RecyclerViewAdapterData.DataViewHolder>() {

    private lateinit var innerRecyclerViewAdapter: RecyclerViewAdapterInsideData

    private fun setupInnerRecyclerView(
        recyclerView: RecyclerView,
        faqModel: DataModel
    ) {
        innerRecyclerViewAdapter =
            RecyclerViewAdapterInsideData(mContext, faqModel.insideData)
        recyclerView.adapter = innerRecyclerViewAdapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        with(holder) {
            with(mData[position]) {
                binding.itemDataTitle.text = title
                setupInnerRecyclerView(binding.dataRv, this)
            }
        }
    }

    inner class DataViewHolder(
        val binding: ItemDataBinding
    ) : RecyclerView.ViewHolder(binding.root)
}
