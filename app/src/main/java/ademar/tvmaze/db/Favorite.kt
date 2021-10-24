package ademar.tvmaze.db

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long,
)

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorites")
    fun getAll(): Single<List<FavoriteEntity>>

    @Query("SELECT COUNT(id) FROM favorites where id = :showId")
    fun count(showId: Long): Single<Int>

    @Insert(onConflict = REPLACE)
    fun insert(favoriteEntity: FavoriteEntity): Completable

    @Query("DELETE FROM favorites WHERE id = :showId")
    fun delete(showId: Long): Completable

}
