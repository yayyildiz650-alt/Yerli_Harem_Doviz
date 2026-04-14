package Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://api.collectapi.com/" //Ana web adresi burada tanımlanır.

    val apiService: RetrofitAPI by lazy {
        Retrofit.Builder() //Retrofit nesnesi oluşturulur ve başlatır.
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitAPI::class.java) //RetrofitAPI arayüzünü oluşturur.

    }
}
