package com.ixap2i.proto.adapters

import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.ixap2i.proto.viewmodels.CookingRecord
import com.ixap2i.proto.R
import com.ixap2i.proto.databinding.AlbumRowBindingImpl

class AlbumListAdapter(val messageList: List<CookingRecord>): RecyclerView.Adapter<AlbumListAdapter.CookingRecordViewHolder>() {


    override fun onBindViewHolder(holder: CookingRecordViewHolder, position: Int) {
        holder.apply {
            bind(messageList, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CookingRecordViewHolder {
        return AlbumListAdapter.CookingRecordViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.album_row,parent, false))
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class CookingRecordViewHolder(private val binding: AlbumRowBindingImpl):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(messages: List<CookingRecord>, position: Int) {

            binding.apply {
                Picasso.get().load(messages[position].image_url).into(binding.imageView)
                binding.textView.text = messages[position].recipeType
                binding.textView2.text = messages[position].comment
                binding.textView3.text = messages[position].recorded_at
            }
        }
    }
}
