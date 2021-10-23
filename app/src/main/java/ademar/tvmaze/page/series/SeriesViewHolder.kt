package ademar.tvmaze.page.series

import ademar.tvmaze.data.Show
import ademar.tvmaze.tile.SeriesTile
import ademar.tvmaze.tile.SeriesTileCallback
import android.view.View
import androidx.recyclerview.widget.RecyclerView

sealed class SeriesViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    class LoadViewHolder(
        itemView: View,
    ) : SeriesViewHolder(itemView)

    class SeriesItemViewHolder(
        itemView: View,
        callback: SeriesTileCallback,
    ) : SeriesViewHolder(itemView) {

        private val seriesTile = SeriesTile(itemView, callback)

        fun bind(show: Show) {
            seriesTile.bind(show)
        }

    }

}
