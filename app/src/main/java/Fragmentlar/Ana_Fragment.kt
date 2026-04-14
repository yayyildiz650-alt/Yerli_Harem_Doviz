package Fragmentlar

import Adapterler.Adapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yerliharem.R
import com.example.yerliharem.SharedModel
import com.example.yerliharem.databinding.FragmentAnaBinding
 
class Ana_Fragment : Fragment() {
    private var _binding: FragmentAnaBinding? = null
    private val binding get() = _binding!!

    // activityViewModels: Tüm fragmentlar arasında ortak bir veri merkezi (SharedModel) kullanır
    private val sharedViewModel: SharedModel by activityViewModels()
    private lateinit var adapter: Adapter

    // Görünüm oluşturulurken XML tasarımı bağlanır
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAnaBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Görünüm oluştuktan sonra verileri çekme ve listeleme işlemleri yapılır
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView için dikey sıralama düzeni ayarlanır
        binding.rvCommodities.layoutManager = LinearLayoutManager(requireContext())

        // Boş bir adapter ile başlatılır, veriler geldikçe güncellenir
        adapter = Adapter()
        binding.rvCommodities.adapter = adapter

        // ViewModel üzerinden internetten verileri çekmeye başlar
        sharedViewModel.vericek()

        // Gelen API verilerini gözlemler (observe) ve liste değiştikçe arayüzü günceller
        sharedViewModel.apiData.observe(viewLifecycleOwner) { emtialarListesi ->
            if (emtialarListesi != null) {
                // Adapter'daki listeyi yeniler
                adapter.listeguncelle(emtialarListesi)
            }
        }

        // Portföy (Cüzdan) butonuna basıldığında Varlık Fragment'ına geçer
        binding.fabPortfolio.setOnClickListener {
            findNavController().navigate(R.id.action_ana_Fragment_to_varlik_Fragment)
        }

        // Sayfayı aşağı çekince yenileme (Swipe Refresh) özelliği
        binding.swipeRefreshLayout.setOnRefreshListener {
            // Verileri tekrar internetten çeker
            sharedViewModel.vericek() 
            // Dönme animasyonunu durdurur
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    // Bellek sızıntısını önlemek için fragment yok olduğunda binding temizlenir
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
