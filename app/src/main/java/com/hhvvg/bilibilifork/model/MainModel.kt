package com.hhvvg.bilibilifork.model

import com.hhvvg.bilibilifork.network.BilibiliApi
import com.hhvvg.bilibilifork.network.RetrofitHelper
import com.hhvvg.bilibilifork.network.TYPE_ANIME
import com.hhvvg.bilibilifork.network.parseCookies
import com.hhvvg.bilibilifork.network.resp.AnimeSubscriptionResult
import com.hhvvg.bilibilifork.network.resp.UserAnimeSubscriptionsResult
import okhttp3.Cookie
import retrofit2.Retrofit
import java.net.CookieManager
import java.net.HttpCookie

/**
 * @author hhvvg
 */
class MainModel {
    private val retrofit: Retrofit by lazy { RetrofitHelper.retrofit }
    private val api: BilibiliApi by lazy { retrofit.create(BilibiliApi::class.java) }

    suspend fun getUserAnimeSubscriptionList(
        cookie: String,
        userUid: Long,
        pageSize: Int,
        pageNum: Int
    ): UserAnimeSubscriptionsResult {
        val timeStamp = System.currentTimeMillis()
        return api.getAnimeSubscribeList(
            cookie = cookie,
            type = TYPE_ANIME,
            followStatus = 0,
            pageSize = pageSize,
            pageNumber = pageNum,
            vmid = userUid,
            timeStamp = timeStamp,
        )
    }

    suspend fun subscribeAnime(cookie: String, seasonId: String): AnimeSubscriptionResult {
        val cookies = parseCookies(cookie)
        val csrf = cookies["bili_jct"] ?: ""
        val referer = "https://www.bilibili.com"
        return api.subscribeAnime(cookie, referer, seasonId, csrf)
    }
}
