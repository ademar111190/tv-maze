package ademar.tvmaze.di

import ademar.tvmaze.db.AppDatabase
import ademar.tvmaze.db.AppDatabaseCreator
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
object DbModule {

    @[Provides Singleton]
    fun providesDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "tvmaze-database",
    ).addCallback(AppDatabaseCreator()).build()

}
