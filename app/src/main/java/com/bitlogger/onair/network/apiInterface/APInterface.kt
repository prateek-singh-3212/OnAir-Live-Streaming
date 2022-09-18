package `in`.bitlogger.kikstart.network.apiInterface

import com.bitlogger.onair.model.StreamModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APInterface {

    @GET("/streams")
    suspend fun getAllStreams(): Response<Array<StreamModel>>

    @GET("/streams/{id}")
    suspend fun getStreams(@Path("id") id: Int): Response<StreamModel>
}