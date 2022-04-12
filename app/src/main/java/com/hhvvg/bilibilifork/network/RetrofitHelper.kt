package com.hhvvg.bilibilifork.network

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author hhvvg
 */
class RetrofitHelper private constructor(){
    companion object {
        private const val BILI_BASE_URL = "https://api.bilibili.com/"

        @JvmStatic
        val retrofit: Retrofit by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            val ret = Retrofit.Builder()
                .baseUrl(BILI_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .build()
            ret
        }
    }
}