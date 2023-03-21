package com.bitlogger.onair.di

import `in`.bitlogger.kikstart.network.apiInterface.APInterface
import `in`.bitlogger.studentsolutions.utils.PreferenceManager
import android.content.Context
import com.bitlogger.onair.network.apiInterface.LiveInterface
import com.bitlogger.onair.network.apiInterface.LiveKey
import com.bitlogger.onair.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providePreferenceManager(
        @ApplicationContext context: Context
    ) = PreferenceManager(context)

    @Provides
    @Singleton
    fun apiClient(): APInterface {
        val auth = Retrofit.Builder()
            .baseUrl("https://pink-proud-swallow.cyclic.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(interceptor())
            .build()
        return auth.create(APInterface::class.java)
    }

    @Provides
    @Singleton
    fun liveClient(): LiveInterface {
        val auth = Retrofit.Builder()
            .baseUrl("https://api.mux.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(interceptor())
            .build()
        return auth.create(LiveInterface::class.java)
    }

    private fun interceptor(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(interceptor).build()
    }

    @Provides
    @Singleton
    fun liveClientKey(): LiveKey {
        val auth = Retrofit.Builder()
            .baseUrl("https://api.mux.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(interceptorKey())
            .build()
            return auth.create(LiveKey::class.java)
    }

    private fun interceptorKey(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(networkInterceptor())
            .addInterceptor(interceptor).build()
    }

    private fun networkInterceptor(): Interceptor {
        val credentials: String = Credentials.basic(Constants.MUX_KEY, Constants.MUX_SECRET)

        return Interceptor { chain ->
            val request = chain.request().newBuilder().run {
                addHeader("Authorization", credentials)
            }.build()
            return@Interceptor chain.proceed(request)
        }
    }
}