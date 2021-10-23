package ademar.tvmaze.network.api

import ademar.tvmaze.network.payload.Search
import ademar.tvmaze.network.payload.ShowItem
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TvMazeService {

    @GET("shows")
    fun shows(
        @Query("page") page: Int? = null,
    ): Single<List<ShowItem>>

    @GET("search/shows")
    fun search(
        @Query("q") query: String,
    ): Single<List<Search>>

}
