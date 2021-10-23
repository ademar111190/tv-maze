package ademar.tvmaze.db

import ademar.tvmaze.data.Genre
import ademar.tvmaze.data.Language
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Entity(tableName = "shows")
data class ShowEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "language") val language: Language,
    @ColumnInfo(name = "rating") val rating: Double,
)

@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0L,
    @ColumnInfo(name = "showId") val showId: Long,
    @ColumnInfo(name = "genre") val genre: Genre,
)

data class ShowWithGenre(
    @Embedded val show: ShowEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "showId",
    )
    val genres: List<GenreEntity>,
)

@Dao
interface ShowDao {

    @Transaction
    @Query("SELECT * FROM shows")
    fun getAll(): Single<List<ShowWithGenre>>

    @Transaction
    @Query("SELECT * FROM shows WHERE name LIKE '%' || :query || '%'")
    fun search(query: String): Single<List<ShowWithGenre>>

    @Insert(onConflict = REPLACE)
    fun insert(show: ShowEntity): Completable

    @Insert(onConflict = REPLACE)
    fun insert(genre: GenreEntity): Completable

    @Query("DELETE FROM genres WHERE showId = :showId")
    fun clearGenres(showId: Long): Completable

}
