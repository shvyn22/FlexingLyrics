package shvyn22.flexinglyrics.ui.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import shvyn22.flexinglyrics.data.local.model.LibraryItem
import shvyn22.flexinglyrics.databinding.ItemTrackBinding
import shvyn22.flexinglyrics.util.BASE_COVER_URL
import shvyn22.flexinglyrics.util.defaultRequests

class LibraryAdapter(
    private val onClick: (LibraryItem) -> Unit
) : ListAdapter<LibraryItem, LibraryAdapter.LibraryViewHolder>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        return LibraryViewHolder(
            ItemTrackBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        val currentItem = getItem(position)

        currentItem?.let { holder.bind(currentItem) }
    }

    inner class LibraryViewHolder(
        private val binding: ItemTrackBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    item?.let { onClick(item) }
                }
            }
        }

        fun bind(item: LibraryItem) {
            binding.apply {
                Glide.with(itemView)
                    .load(BASE_COVER_URL + item.idAlbum.toString())
                    .defaultRequests()
                    .into(ivTrack)

                tvTitle.text = item.track
                tvArtist.text = item.artist
            }
        }
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