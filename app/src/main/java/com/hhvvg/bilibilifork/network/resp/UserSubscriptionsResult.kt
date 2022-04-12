package com.hhvvg.bilibilifork.network.resp

/**
 * @author hhvvg
 */
data class UserAnimeSubscriptionsResult(val data: AnimeData?) : BaseResult()

data class AnimeData(
    val list: List<AnimeEntity>,
    val pn: Int,
    val ps: Int,
    val total: Int,
)

data class AnimeEntity(
    val season_id: Int,
    val media_id: Int,
    val title: String,
    val cover: String,
    val evaluate: String,
)