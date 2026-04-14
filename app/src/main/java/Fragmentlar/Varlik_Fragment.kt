package Fragmentlar

import Adapterler.Adapter2
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yerliharem.SharedModel
import com.example.yerliharem.databinding.FragmentVarlikBinding
class Varlik_Fragment : Fragment() {
    private var _binding: FragmentVarlikBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedModel by activityViewModels()
    private lateinit var cuzdanadapter: Adapter2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVarlikBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPortfolio.layoutManager = LinearLayoutManager(requireContext())

        cuzdanadapter = Adapter2 { roomModel ->
            sharedViewModel.silbutonunaBasildi(roomModel)
        }
        binding.rvPortfolio.adapter = cuzdanadapter
        // Portföy Ekranı: Cüzdandaki varlıkların listelendiği sayfa
        class Varlik_Fragment : Fragment() {
            private var _binding: FragmentVarlikBinding? = null
            private val binding get() = _binding!!
            private val sharedViewModel: SharedModel by activityViewModels()
            private lateinit var cuzdanadapter: Adapter2

            override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View {
                _binding = FragmentVarlikBinding.inflate(inflater, container, false)
                return binding.root
            }

            override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)

                binding.rvPortfolio.layoutManager = LinearLayoutManager(requireContext())

                // Adapter başlatılır ve silme tıklaması dinlenir
                cuzdanadapter = Adapter2 {
                    sharedViewModel.silbutonunaBasildi(it) // ViewModel'den sildirir
                }
                binding.rvPortfolio.adapter = cuzdanadapter

                // Verileri getir
                sharedViewModel.roomVerileriniGetir()

                // Veritabanındaki değişimleri gözlemler
                sharedViewModel.roomData.observe(viewLifecycleOwner) { liste ->
                    if (liste != null) {
                        cuzdanadapter.listeguncelle(liste)
                        // Toplam portföy değerini hesaplar
                        val toplam = liste.sumOf { it.amount * it.buyingPrice }
                        binding.tvTotalPortfolioValue.text = String.format("%.2f ₺", toplam)
                    }
                }
            }

            override fun onDestroyView() {
                super.onDestroyView()
                _binding = null
            }
        }
        // Veritabanındaki verileri çekiyoruz
        sharedViewModel.roomVerileriniGetir()

        // Room verilerini gözlemliyoruz (Adapter2 List<RoomModel> beklediği için)
        sharedViewModel.roomData.observe(viewLifecycleOwner) { roomModelListesi ->
            if (roomModelListesi != null) {
                cuzdanadapter.listeguncelle(roomModelListesi)

                val toplamDeger = roomModelListesi.sumOf { it.amount * it.buyingPrice // sumOf listedeki her elemanı gezer.
                }
                binding.tvTotalPortfolioValue.text = String.format("%.2f ₺", toplamDeger)
            }
        }
    }
}
