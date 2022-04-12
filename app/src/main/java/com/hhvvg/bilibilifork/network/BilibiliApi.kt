package com.hhvvg.bilibilifork.network

import com.hhvvg.bilibilifork.network.resp.AnimeSubscriptionResult
import com.hhvvg.bilibilifork.network.resp.UserAnimeSubscriptionsResult
import retrofit2.http.*

const val TYPE_ANIME = 1
const val TYPE_DRAMA = 2

/**
 * @author hhvvg
 */
interface BilibiliApi {
    @GET("x/space/bangumi/follow/list")
    suspend fun getAnimeSubscribeList(
        @Header("Cookie") cookie: String,
        @Query("type") type: Int,
        @Query("follow_status") followStatus: Int,
        @Query("pn") pageNumber: Int,
        @Query("ps") pageSize: Int,
        @Query("vmid") vmid: Long,
        @Query("ts") timeStamp: Long,
    ): UserAnimeSubscriptionsResult

    @POST("pgc/web/follow/add")
    @FormUrlEncoded
    suspend fun subscribeAnime(
        @Header("Cookie") cookie: String,
        @Header("Referer") referer: String,
        @Field("season_id") seasonId: String,
        @Field("csrf") csrf: String
    ): AnimeSubscriptionResult
}
