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
) {

    fun bind(show: Show) {
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

    private fun applyGenre(genres: List<Genre>, genre: ImageView, index: Int) {
        if (genres.size > index) {
            genre.setImageResource(genres[index].icon)
            genre.visibility = View.VISIBLE
        } else {
            genre.visibility = View.INVISIBLE
        }
    }

}
