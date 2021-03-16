package shvyn22.lyricsapplication.ui.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import shvyn22.lyricsapplication.LyricsApplication.Companion.BASE_COVER_URL
import shvyn22.lyricsapplication.R
import shvyn22.lyricsapplication.data.model.LibraryItem
import shvyn22.lyricsapplication.databinding.ItemTrackBinding

class LibraryAdapter(private val listener: OnItemClickListener)
    : ListAdapter<LibraryItem, LibraryAdapter.LibraryViewHolder>(ITEM_COMPARATOR){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        return LibraryViewHolder(
                ItemTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) holder.bind(currentItem)
    }

    inner class LibraryViewHolder(private val binding: ItemTrackBinding)
        : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) listener.onItemClick(item)
                }
            }
        }

        fun bind(item: LibraryItem) {
            binding.apply {
                Glide.with(itemView)
                        .load(BASE_COVER_URL + item.idAlbum.toString())
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(ivTrack)

                tvTitle.text = item.track

                tvArtist.text = item.artist
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: LibraryItem)
    }

    companion object {
        val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<LibraryItem>() {
            override fun areItemsTheSame(oldItem: LibraryItem, newItem: LibraryItem) =
                    oldItem.idTrack == newItem.idTrack

            override fun areContentsTheSame(oldItem: LibraryItem, newItem: LibraryItem) =
                    oldItem == newItem
        }
    }
}