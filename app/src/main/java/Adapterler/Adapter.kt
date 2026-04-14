package Adapterler

import Retrofit.Emtialar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.yerliharem.R
import com.example.yerliharem.SharedModel
import com.google.android.material.imageview.ShapeableImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider

// Ana sayfadaki emtia listesini (altın, döviz vb.) yöneten adapter
class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>() {
    private var altinListesi: List<Emtialar> = emptyList()

    // Görünüm tutucu: Satır tasarımındaki bileşenleri tanımlar
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val isimText: TextView = itemView.findViewById(R.id.tvItemName)
        val fiyatText: TextView = itemView.findViewById(R.id.tvItemBuyingPrice)
        val satisText: TextView = itemView.findViewById(R.id.tvItemSellingPrice)
        val ivItemIcon: ShapeableImageView = itemView.findViewById(R.id.ivItemIcon)
    }

    // Listeyi dışarıdan güncellemek için kullanılan fonksiyon
    fun listeguncelle(yeniListe: List<Emtialar>){ 
        altinListesi = yeniListe
        notifyDataSetChanged() // Arayüzü yeniler
    }

    // Satır tasarımı oluşturulur
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row, parent, false)
        return ViewHolder(view)
    }

    // Veriler satırlara bağlanır
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val oAnkiAltin = altinListesi[position]

        holder.isimText.text = oAnkiAltin.name
        holder.fiyatText.text = oAnkiAltin.alis.toString()
        holder.satisText.text = oAnkiAltin.satis.toString()

        // Satıra tıklandığında detay sayfasına gider
        holder.itemView.setOnClickListener {
            val activity = it.context as? FragmentActivity
            if (activity != null) {
                // Tıklanan öğenin ismini ViewModel'e kaydeder (Detay sayfasında kullanmak için)
                val viewModel = ViewModelProvider(activity).get(SharedModel::class.java)
                viewModel.setSharedData(oAnkiAltin.name)
            }
            it.findNavController().navigate(R.id.action_ana_Fragment_to_detay_Fragment)
        }
    }

    override fun getItemCount(): Int {
        return altinListesi.size
    }
}
