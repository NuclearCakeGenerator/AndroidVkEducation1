package com.example.vkeducationkotlindz1

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class GiphyResponse(val data: List<GifObject>)
data class GifObject(val images: GifImages)
data class GifImages(val downsized: GifData)
data class GifData(val url: String)

interface GiphyApi {
    @GET("v1/gifs/trending")
    suspend fun loadTrending(
        @Query("api_key") key: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("rating") rating: String = "g"
    ): GiphyResponse
}

fun createApi(): GiphyApi = Retrofit.Builder()
    .baseUrl("https://api.giphy.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(GiphyApi::class.java)
