package ademar.tvmaze.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        GenreEntity::class,
        ShowEntity::class,
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun showDao(): ShowDao

}

class AppDatabaseCreator : RoomDatabase.Callback()
