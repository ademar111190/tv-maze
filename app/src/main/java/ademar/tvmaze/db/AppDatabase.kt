package ademar.tvmaze.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        EpisodeEntity::class,
        GenreEntity::class,
        ScheduleDayEntity::class,
        SeasonEntity::class,
        ShowEntity::class,
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun showDao(): ShowDao

    abstract fun seasonDao(): SeasonDao

    abstract fun episodeDao(): EpisodeDao

}

class AppDatabaseCreator : RoomDatabase.Callback()
