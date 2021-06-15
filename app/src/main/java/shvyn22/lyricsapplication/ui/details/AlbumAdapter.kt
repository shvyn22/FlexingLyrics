package shvyn22.lyricsapplication.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import shvyn22.lyricsapplication.data.remote.AlbumInfo
import shvyn22.lyricsapplication.databinding.ItemAlbumBinding

class AlbumAdapter(
    private val onClick: (AlbumInfo.TrackInfo) -> Unit
) : ListAdapter<AlbumInfo.TrackInfo, AlbumAdapter.AlbumViewHolder>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            ItemAlbumBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val currentItem = getItem(position)

        currentItem?.let { holder.bind(currentItem) }
    }

    inner class AlbumViewHolder(
        private val binding: ItemAlbumBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    item?.let { onClick(item) }
                }
            }
        }

        fun bind(item: AlbumInfo.TrackInfo) {
            binding.apply {
                btnAlbum.text = item.track
            }
        }
    }

    companion object {
        val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<AlbumInfo.TrackInfo>() {
            override fun areItemsTheSame(
                oldItem: AlbumInfo.TrackInfo,
                newItem: AlbumInfo.TrackInfo
            ) = oldItem.idTrack == newItem.idTrack

            override fun areContentsTheSame(
                oldItem: AlbumInfo.TrackInfo,
                newItem: AlbumInfo.TrackInfo
            ) = oldItem == newItem
        }
    }
}