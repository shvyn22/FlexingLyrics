package shvyn22.lyricsapplication.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import shvyn22.lyricsapplication.LyricsApplication.Companion.BASE_COVER_URL
import shvyn22.lyricsapplication.data.remote.Track
import shvyn22.lyricsapplication.databinding.ItemTrackBinding
import shvyn22.lyricsapplication.util.defaultRequests

class SearchAdapter(
    private val onClick: (Track) -> Unit
) : ListAdapter<Track, SearchAdapter.SearchViewHolder>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemTrackBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val currentItem = getItem(position)

        currentItem?.let { holder.bind(currentItem) }
    }

    inner class SearchViewHolder(
        private val binding: ItemTrackBinding
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

        fun bind(item: Track) {
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
        val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Track>() {
            override fun areItemsTheSame(oldItem: Track, newItem: Track) =
                oldItem.idTrack == newItem.idTrack

            override fun areContentsTheSame(oldItem: Track, newItem: Track) =
                oldItem == newItem
        }
    }
}