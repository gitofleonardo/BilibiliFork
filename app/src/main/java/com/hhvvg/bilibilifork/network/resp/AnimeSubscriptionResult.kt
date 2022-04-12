package com.hhvvg.bilibilifork.network.resp

/**
 * @author hhvvg
 */
data class AnimeSubscriptionResult(val result: ResultEntity) : BaseResult()

data class ResultEntity(
    val fmid: Int,
    val relation: Boolean,
    val status: Int,
    val toast: String?
)
