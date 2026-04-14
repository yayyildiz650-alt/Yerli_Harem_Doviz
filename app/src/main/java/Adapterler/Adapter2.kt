package Adapterler

import Database.RoomModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.yerliharem.databinding.RecyclerRow2Binding

// Cüzdan (Portföy) listesini yöneten ve ekrana basan sınıf
class Adapter2(private val onSilTiklandi: (RoomModel) -> Unit) : RecyclerView.Adapter<Adapter2.ViewHolder>() {

    // Ekranda gösterilecek varlıkların listesi
    private var varlikListesi: List<RoomModel> = emptyList()

    // Görünüm tutucu: Her bir satırın içindeki görsel bileşenlere erişir
    class ViewHolder(val binding: RecyclerRow2Binding) : RecyclerView.ViewHolder(binding.root)

    // Satır tasarımı (XML) oluşturulduğamasında çağrılır
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerRow2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Her satırın verisini (isim, miktar vb.) bağlayan fonksiyon
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val oAnkiVarlik = varlikListesi[position]

        // Verileri ekrandaki metin kutularına yazar
        holder.binding.txtVarlikMiktar.text = oAnkiVarlik.amount.toString()
        holder.binding.txtVarlikAdi.text = oAnkiVarlik.commadityName

        // Toplam değeri hesaplar ve TL formatında yazdırır
        val toplamDeger = oAnkiVarlik.amount * oAnkiVarlik.buyingPrice
        holder.binding.txtVarlikToplam.text = String.format("%.2f TL", toplamDeger)

        // Üç nokta menüsüne tıklandığında silme opsiyonu çıkarır
        holder.binding.imgMenu.setOnClickListener { view ->
            val popup = PopupMenu(view.context, view)
            popup.menu.add("Sil")

            popup.setOnMenuItemClickListener { menuItem ->
                if (menuItem.title == "Sil") {
                    // Silme butonuna basıldığını Fragment'a haber verir
                    onSilTiklandi(oAnkiVarlik)
                    true 
                } else {
                    false
                }
            }
            popup.show()
        }
    }

    // Listedeki eleman sayısını döndürür
    override fun getItemCount(): Int = varlikListesi.size

    // Veritabanından yeni liste geldiğinde arayüzü yeniler
    fun listeguncelle(yeniListe: List<RoomModel>) {
        varlikListesi = yeniListe
        notifyDataSetChanged() // RecyclerView'ı tazeler
    }
}
