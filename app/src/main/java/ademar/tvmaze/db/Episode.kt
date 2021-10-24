package ademar.tvmaze.db

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Entity(tableName = "episodes")
data class EpisodeEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "season_id") val seasonId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "summary") val summary: String,
    @ColumnInfo(name = "season") val season: Int,
    @ColumnInfo(name = "number") val number: Int,
    @ColumnInfo(name = "image") val image: String,
)

@Dao
interface EpisodeDao {

    @Query("SELECT * FROM episodes")
    fun getAll(): Single<List<EpisodeEntity>>

    @Insert(onConflict = REPLACE)
    fun insert(episodeEntity: EpisodeEntity): Completable

    @Query("DELETE FROM episodes WHERE season_id = :seasonId")
    fun clearEpisodes(seasonId: Long): Completable

}
