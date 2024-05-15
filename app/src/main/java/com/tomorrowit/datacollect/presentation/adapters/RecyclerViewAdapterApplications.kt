package com.tomorrowit.datacollect.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tomorrowit.datacollect.R
import com.tomorrowit.datacollect.databinding.ItemApplicaitonBinding
import com.tomorrowit.datacollect.domain.models.ApplicationInfoModel

class RecyclerViewAdapterApplications(
    private val mContext: Context,
    private val mData: List<ApplicationInfoModel>
) : RecyclerView.Adapter<RecyclerViewAdapterApplications.ApplicationsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationsViewHolder {
        val binding =
            ItemApplicaitonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApplicationsViewHolder(binding)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ApplicationsViewHolder, position: Int) {
        with(holder) {
            with(mData[position]) {
                binding.itemApplicationIcon.setImageDrawable(icon)
                binding.itemApplicationName.text = String.format("%s (%s)", appName, versionCode)

                binding.itemApplicationPackage.text = packageName

                binding.itemApplicationSize.text = String.format(
                    mContext.getString(R.string.application_size),
                    size
                )

                binding.itemApplicationVersionName.text = String.format(
                    mContext.getString(R.string.application_version_name),
                    versionName
                )

                binding.itemApplicationFirstInstall.text = String.format(
                    mContext.getString(R.string.application_first_install),
                    firstInstall
                )

                binding.itemApplicationLastUpdate.text = String.format(
                    mContext.getString(R.string.application_last_update),
                    lastUpdate
                )
            }
        }
    }

    inner class ApplicationsViewHolder(
        val binding: ItemApplicaitonBinding
    ) : RecyclerView.ViewHolder(binding.root)

}