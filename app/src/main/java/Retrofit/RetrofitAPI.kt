package Retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface RetrofitAPI { // API İsteği bu sınıfta yapılır...
    @GET("economy/goldPrice") //Verilerin alınacağı site
    suspend fun getCommodities( // Veri çekmeyi arka planda yapıyoruz(suspend fun )
        @Header("authorization") apiKey: String
    ): Response<RetrofitModel> // Gelen veri Listeliyoruz.
}
