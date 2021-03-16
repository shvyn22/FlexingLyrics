package shvyn22.lyricsapplication.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import shvyn22.lyricsapplication.data.model.AlbumInfo
import shvyn22.lyricsapplication.databinding.ItemAlbumBinding

class AlbumAdapter(private val listener : OnItemClickListener) :
    ListAdapter<AlbumInfo.TrackInfo, AlbumAdapter.AlbumViewHolder>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return  AlbumViewHolder(
                ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) holder.bind(currentItem)
    }

    inner class AlbumViewHolder(private val binding: ItemAlbumBinding) :
    RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) listener.onItemClick(item)
                }
            }
        }

        fun bind(item: AlbumInfo.TrackInfo) {
            binding.apply {
                btnAlbum.text = item.track
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: AlbumInfo.TrackInfo)
    }

    companion object {
        val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<AlbumInfo.TrackInfo>() {
            override fun areItemsTheSame(oldItem: AlbumInfo.TrackInfo, newItem: AlbumInfo.TrackInfo) =
                    oldItem.idTrack == newItem.idTrack

            override fun areContentsTheSame(oldItem: AlbumInfo.TrackInfo, newItem: AlbumInfo.TrackInfo) =
                    oldItem == newItem
        }
    }
}