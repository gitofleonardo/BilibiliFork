package com.hhvvg.bilibilifork.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hhvvg.bilibilifork.adapter.AnimeItemWrapper
import com.hhvvg.bilibilifork.model.MainModel
import com.hhvvg.bilibilifork.network.resp.AnimeEntity
import com.hhvvg.bilibilifork.network.resp.UserAnimeSubscriptionsResult
import kotlinx.coroutines.launch

/**
 * @author hhvvg
 */
class MainViewModel : ViewModel() {
    private val model by lazy { MainModel() }

    var userUid: Long = -1
    var cookie: String = ""
    val loadAnimeResult = MutableLiveData<LoadAnimeResult>()
    val animeSubscribeResult = MutableLiveData<AnimeSubscribeResult>()

    fun loadAllSubscribedAnime() = viewModelScope.launch {
        try {
            var currentPage = 1
            val pageSize = 15
            val resultList = mutableListOf<AnimeEntity>()
            var resultUser: UserAnimeSubscriptionsResult
            do {
                resultUser =
                    model.getUserAnimeSubscriptionList(cookie, userUid, pageSize, currentPage)
                ++currentPage
                if (resultUser.code == 0 && resultUser.data != null) {
                    resultList.addAll(resultUser.data!!.list)
                } else {
                    loadAnimeResult.value = LoadAnimeResult.LoadFailure(resultUser.message)
                    break
                }
            } while (resultUser.code == 0 && resultList.size < resultUser.data?.total ?: 0)
            loadAnimeResult.value =
                LoadAnimeResult.LoadSuccess(resultList.map { AnimeItemWrapper(it, true) })
        } catch (e: Exception) {
            loadAnimeResult.value = LoadAnimeResult.LoadFailure(e.message)
        }
    }

    fun subscribeAnime(animeSeasons: List<String>) = viewModelScope.launch {
        if (animeSeasons.isEmpty()) {
            animeSubscribeResult.value = AnimeSubscribeResult.SubscribeSuccess
            return@launch
        }
        val total = animeSeasons.size
        var done = 0
        var errMessage: String? = null
        val failures = mutableListOf<String>()
        val successList = mutableListOf<String>()
        for (season in animeSeasons) {
            try {
                val result = model.subscribeAnime(cookie, season)
                if (result.code != 0) {
                    failures.add(season)
                    errMessage = result.message
                } else {
                    successList.add(season)
                }
            } catch (e: Exception) {
                failures.add(season)
                errMessage = e.message
            }
            ++done
            animeSubscribeResult.value = AnimeSubscribeResult.SubscribeProgress(done, total)
        }
        if (failures.isEmpty()) {
            animeSubscribeResult.value = AnimeSubscribeResult.SubscribeSuccess
            return@launch
        }
        if (failures.size == animeSeasons.size) {
            // All failed
            animeSubscribeResult.value = AnimeSubscribeResult.SubscribeFailure(errMessage)
            return@launch
        }
        animeSubscribeResult.value = AnimeSubscribeResult.SubscribePartSuccess(failures, successList)
    }
}

sealed class LoadAnimeResult {
    class LoadSuccess(val anime: List<AnimeItemWrapper>) : LoadAnimeResult()
    class LoadFailure(val message: String?) : LoadAnimeResult()
}

sealed class AnimeSubscribeResult {
    object SubscribeSuccess : AnimeSubscribeResult()
    class SubscribeProgress(val done: Int, val total: Int) : AnimeSubscribeResult()
    class SubscribePartSuccess(val failures: List<String>, val success: List<String>) : AnimeSubscribeResult()
    class SubscribeFailure(val message: String?) : AnimeSubscribeResult()
}
