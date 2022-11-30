package uz.nurlibaydev.transportschedule.presentation.main

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.nurlibaydev.transportschedule.R
import uz.nurlibaydev.transportschedule.data.models.TaxiData
import uz.nurlibaydev.transportschedule.databinding.ListItemMainBinding
import uz.nurlibaydev.transportschedule.utils.extenions.inflate

// Created by Jamshid Isoqov an 11/20/2022

val listItemTaxiCallback = object : DiffUtil.ItemCallback<TaxiData>() {
    override fun areItemsTheSame(oldItem: TaxiData, newItem: TaxiData): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TaxiData, newItem: TaxiData): Boolean =
        oldItem.taxiName == newItem.taxiName &&
                oldItem.startLan == newItem.startLan &&
                oldItem.startLng == newItem.startLng &&
                oldItem.endLan == newItem.endLan &&
                oldItem.endLng == newItem.endLng &&
                oldItem.address == newItem.address &&
                oldItem.phone == newItem.phone

}

class MainAdapter : ListAdapter<TaxiData, MainAdapter.ViewHolder>(listItemTaxiCallback) {

    private var itemClickListener: ((TaxiData) -> Unit)? = null

    fun setItemClickListener(block:(TaxiData)->Unit){
        itemClickListener = block
    }


    inner class ViewHolder(private val binding: ListItemMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                itemClickListener?.invoke(getItem(absoluteAdapterPosition))
            }
        }

        fun onBind() {
            val data = getItem(absoluteAdapterPosition)
            binding.apply {
                tvTaxiName.text = data.taxiName
                tvTaxiStartLocation.text = data.address[0]
                tvTaxiEndLocation.text = data.address[data.address.lastIndex]
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ListItemMainBinding.bind(parent.inflate(R.layout.list_item_main)))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.onBind()

}