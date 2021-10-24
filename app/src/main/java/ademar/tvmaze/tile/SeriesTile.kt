package ademar.tvmaze.tile

import ademar.tvmaze.R
import ademar.tvmaze.data.Genre
import ademar.tvmaze.data.Show
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class SeriesTile(
    private val itemView: View,
    private val callback: SeriesTileCallback,
) {

    private var currentId: Long? = null

    init {
        itemView.findViewById<View>(R.id.root).setOnClickListener {
            currentId?.let(callback::onItemClick)
        }
    }

    fun bind(show: Show) {
        currentId = show.id

        val name = itemView.findViewById<TextView>(R.id.name)
        val ratingValue = itemView.findViewById<TextView>(R.id.rating_value)
        val banner = itemView.findViewById<ImageView>(R.id.banner)
        val language = itemView.findViewById<ImageView>(R.id.language)
        val genre1 = itemView.findViewById<ImageView>(R.id.genre_1)
        val genre2 = itemView.findViewById<ImageView>(R.id.genre_2)
        val genre3 = itemView.findViewById<ImageView>(R.id.genre_3)

        name.text = show.name
        ratingValue.text = show.rating.toString()
        language.setImageResource(show.language.icon)
        language.contentDescription = itemView.context.getString(show.language.title)

        applyGenre(show.genres, genre3, 2)
        applyGenre(show.genres, genre2, 1)
        applyGenre(show.genres, genre1, 0)

        Glide.with(itemView)
            .load(show.image)
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher_background)
            .transition(DrawableTransitionOptions.withCrossFade(200))
            .into(banner)
    }

    private fun applyGenre(genres: Set<Genre>, view: ImageView, index: Int) {
        if (genres.size > index) {
            val genre = genres.toList()[index]
            view.setImageResource(genre.icon)
            view.contentDescription = itemView.context.getString(genre.title)
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.INVISIBLE
        }
    }

}

interface SeriesTileCallback {

    fun onItemClick(id: Long)

}
