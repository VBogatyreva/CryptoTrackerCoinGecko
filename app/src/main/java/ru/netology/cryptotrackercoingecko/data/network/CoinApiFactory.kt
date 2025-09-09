package ru.netology.cryptotrackercoingecko.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.netology.cryptotrackercoingecko.BuildConfig
import ru.netology.cryptotrackercoingecko.BuildConfig.BASE_URL

object CoinApiFactory {

    private const val API_KEY = BuildConfig.COINGECKO_API_KEY

        private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Accept", "application/json")
                .header("x-cg-demo-api-key", API_KEY)
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val coinGeckoApiService: CoinApiService = retrofit.create(CoinApiService::class.java)

    fun printConfig() {
        println("API Key: $API_KEY")
        println("Base URL: ${BuildConfig.BASE_URL}")
    }
}