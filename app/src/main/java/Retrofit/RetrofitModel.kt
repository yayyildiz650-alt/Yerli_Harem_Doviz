package Retrofit

import com.google.gson.annotations.SerializedName

data class RetrofitModel ( //Uygulamadan gelen  verileri kotlin nesnesine dönüştürdük.
    val success: Boolean,
    val result: List<Emtialar> //Apı den gelen emtiaları listeliyoruz.
)

data class Emtialar(
    // API'den "name" gelirse, onu "name" değişkenine ata
    @SerializedName("name")
    val name: String,

    // API'den "selling" gelirse, onu "satis" değişkenine ata
    @SerializedName("selling")
    val satis: Double,

    // API'den "buying" gelirse, onu "alis" değişkenine ata
    @SerializedName("buying")
    val alis: Double
)