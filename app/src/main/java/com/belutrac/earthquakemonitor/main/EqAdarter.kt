package com.belutrac.earthquakemonitor.main

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.belutrac.earthquakemonitor.Earthquake
import com.belutrac.earthquakemonitor.R
import com.belutrac.earthquakemonitor.databinding.EqListItemBinding

private val TAG = EqAdarter::class.java.simpleName

class EqAdarter(private val context: Context) : ListAdapter<Earthquake, EqAdarter.EqViewHolder> (
    DiffCallback
){

    companion object DiffCallback : DiffUtil.ItemCallback<Earthquake>()
    {
        override fun areItemsTheSame(oldItem: Earthquake, newItem: Earthquake): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Earthquake, newItem: Earthquake): Boolean {
           return oldItem == newItem
        }
    }

    lateinit var onItemClickListener : (Earthquake) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EqViewHolder {
        val binding = EqListItemBinding.inflate(LayoutInflater.from(parent.context))
        return EqViewHolder(binding) //Devuelvo un objeto viewHolder con los elementos del item
    }

    override fun onBindViewHolder(holder: EqViewHolder, position: Int) { //Seteo los valores del item, para cada uno se ejecuta
        val earthquake : Earthquake = getItem(position)
        holder.bind(earthquake)
    }

    inner class EqViewHolder(private val binding: EqListItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(earthquake: Earthquake){
            Log.d("LLALALLAL", earthquake.longitude.toString())
            binding.eqMagnitude.text = context.getString(R.string.magnitude_format, earthquake.magnitude)
            binding.eqPlace.text = earthquake.place
            binding.executePendingBindings()
            binding.root.setOnClickListener{
                if(::onItemClickListener.isInitialized) //No entiendo que hace esto
                { onItemClickListener(earthquake) }
                else{
                    Log.d(TAG, "OnItemClickListener not initialized")
                }
            }
        }
    }


}