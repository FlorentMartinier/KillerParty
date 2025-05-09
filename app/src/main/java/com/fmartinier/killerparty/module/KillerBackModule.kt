package com.fmartinier.killerparty.module

import com.fmartinier.killerparty.client.KillerBackClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.fmartinier.killerparty.R

val killerBackModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(androidContext().getString(R.string.killer_back_api_base_url))
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    single<KillerBackClient> {
        get<Retrofit>().create(KillerBackClient::class.java)
    }
}