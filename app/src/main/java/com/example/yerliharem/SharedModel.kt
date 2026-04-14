package com.example.yerliharem

import Database.AppDatabase
import Database.RoomModel
import Retrofit.RetrofitClient
import Retrofit.Emtialar
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// SharedModel: Uygulamanın verilerini (API ve DB) yöneten merkezi sınıf
class SharedModel(application: Application) : AndroidViewModel(application) {

    // 1. RETROFIT: İnternetten (API) gelen canlı verileri tutan liste
    private val _apiData = MutableLiveData<List<Emtialar>>()
    val apiData: LiveData<List<Emtialar>> get() = _apiData

    // API'den verileri çeken fonksiyon
    fun vericek() {
        viewModelScope.launch { // Coroutine: Arka planda çalışmayı sağlar
            try { 
                val benimAnahtarim = "apikey 67VUYbvAIq38J8ysDDynrz:03shLmrgVxIZ633XV7hR3T"
                val response = RetrofitClient.apiService.getCommodities(benimAnahtarim)

                if (response.isSuccessful) {
                    // API'den gelen listeyi LiveData'ya aktarır
                    _apiData.value = response.body()?.result 
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 2. ROOM: Cüzdan (yerel veritabanı) verilerini tutan liste
    private val _roomData = MutableLiveData<List<RoomModel>>()
    val roomData: LiveData<List<RoomModel>> get() = _roomData

    // Veritabanındaki tüm varlıkları çekme fonksiyonu
    fun roomVerileriniGetir() {
        viewModelScope.launch(Dispatchers.IO) { // Veritabanı işlemleri IO thread'inde yapılır
            try {
                val liste = AppDatabase.getDatabase(getApplication()).userDao().getAll()
                _roomData.postValue(liste) // Veriyi arayüze (Fragment'a) gönderir
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 3. PAYLAŞILAN VERİ: Sayfalar arası metin (emtia ismi) taşımak için
    private val _sharedData = MutableLiveData<String>()
    val sharedData: LiveData<String> get() = _sharedData

    fun setSharedData(commodityName: String) {
        _sharedData.value = commodityName
    }

    // SİLME İŞLEMİ: Cüzdandan bir öğeyi siler
    fun silbutonunaBasildi(silinecekModel: RoomModel){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Veritabanından siler
                AppDatabase.getDatabase(getApplication()).userDao().delete(silinecekModel)
                // Sildikten sonra listeyi otomatik yeniler
                roomVerileriniGetir()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // KAYDETME İŞLEMİ: Cüzdana yeni bir varlık ekler
    fun kaydetbutonunaBasildi (name: String, amount: Double, price: Double){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val roomModel = RoomModel(
                    id = 0,
                    commadityName = name,
                    amount = amount,
                    buyingPrice = price
                )
                // Veritabanına ekler
                AppDatabase.getDatabase(getApplication()).userDao().insert(roomModel)
                // Eklendikten sonra listeyi otomatik yeniler
                roomVerileriniGetir()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
