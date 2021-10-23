package ademar.tvmaze.di

import ademar.tvmaze.BuildConfig
import ademar.tvmaze.network.api.TvMazeService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
object NetworkModule {

    @[Provides Singleton]
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .setLevel(if (BuildConfig.DEBUG) BODY else NONE)

    @[Provides Singleton]
    fun providesOkHttpClient(
        logging: HttpLoggingInterceptor,
    ) = OkHttpClient.Builder().addInterceptor(logging).build()

    @[Provides Singleton]
    fun providesMoshi(): Moshi = Moshi.Builder().build()

    @[Provides Singleton]
    fun providesRxJava3CallAdapterFactory(): RxJava3CallAdapterFactory = RxJava3CallAdapterFactory.create()

    @[Provides Singleton]
    fun providesMoshiConverterFactory(
        moshi: Moshi,
    ): MoshiConverterFactory = MoshiConverterFactory.create(moshi).asLenient()

    @[Provides Singleton]
    fun providesRetrofit(
        client: OkHttpClient,
        rxJava3CallAdapterFactory: RxJava3CallAdapterFactory,
        moshiConverterFactory: MoshiConverterFactory,
    ): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BuildConfig.TV_MAZE_ROOT_URL)
        .addCallAdapterFactory(rxJava3CallAdapterFactory)
        .addConverterFactory(moshiConverterFactory)
        .build()

    @[Provides Singleton]
    fun providesAlphaVantageService(
        retrofit: Retrofit,
    ): TvMazeService = retrofit.create(TvMazeService::class.java)

}
