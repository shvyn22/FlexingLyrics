package shvyn22.flexinglyrics.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import shvyn22.flexinglyrics.FlexingLyrics.Companion.BASE_COVER_URL
import shvyn22.flexinglyrics.data.local.model.HistoryItem
import shvyn22.flexinglyrics.databinding.ItemTrackBinding
import shvyn22.flexinglyrics.util.defaultRequests

class HistoryAdapter(
    private val onClick: (HistoryItem) -> Unit
) : ListAdapter<HistoryItem, HistoryAdapter.HistoryViewHolder>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            ItemTrackBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = getItem(position)

        currentItem?.let { holder.bind(currentItem) }
    }

    inner class HistoryViewHolder(
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

        fun bind(item: HistoryItem) {
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
        val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<HistoryItem>() {
            override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem) =
                oldItem.idTrack == newItem.idTrack

            override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem) =
                oldItem == newItem
        }
    }
}