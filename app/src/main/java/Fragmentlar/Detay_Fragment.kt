package Fragmentlar

import Adapterler.Adapter2
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yerliharem.R
import com.example.yerliharem.SharedModel
import com.example.yerliharem.databinding.FragmentDetayBinding
import com.example.yerliharem.databinding.FragmentVarlikBinding

class Detay_Fragment : Fragment() {
    lateinit var binding: FragmentDetayBinding
    private val sharedViewModel: SharedModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetayBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Detay Sayfası: Seçilen varlığı cüzdana ekleme ekranı
    class Detay_Fragment : Fragment() {
        lateinit var binding: FragmentDetayBinding
        private val sharedViewModel: SharedModel by activityViewModels()

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentDetayBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            // Cüzdana Ekle butonu
            binding.btnAddPortfolio.setOnClickListener {
                if (binding.miktar.text!!.isNotEmpty() && binding.fiyati.text!!.isNotEmpty()) {
                    val miktar = binding.miktar.text.toString().toDouble()
                    val fiyat = binding.fiyati.text.toString().toDouble()
                    val isim = sharedViewModel.sharedData.value ?: "Yeni Varlık"

                    // ViewModel üzerinden veritabanına kaydet
                    sharedViewModel.kaydetbutonunaBasildi(isim, miktar, fiyat)

                    Toast.makeText(requireContext(), "Eklendi!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_detay_Fragment_to_ana_Fragment)
                }
            }

            // Hesaplama butonu
            binding.button.setOnClickListener {
                if (binding.miktar.text!!.isNotEmpty() && binding.fiyati.text!!.isNotEmpty()) {
                    val miktar = binding.miktar.text.toString().toDouble()
                    val fiyat = binding.fiyati.text.toString().toDouble()
                    binding.toplammaliyet.text = (miktar * fiyat).toString()
                }
            }
        }
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddPortfolio.setOnClickListener {

            if (binding.miktar.text!!.isNotEmpty() && binding.fiyati.text!!.isNotEmpty()) { // Boş değilse

                val miktar = binding.miktar.text.toString()
                val fiyat = binding.fiyati.text.toString()

                // Seçilen veriyi ortak beyine alır.
                val emtiaadi = sharedViewModel.sharedData.value ?: "Yeni Varlık"

                sharedViewModel.kaydetbutonunaBasildi(emtiaadi, miktar.toDouble(), fiyat.toDouble())

                Toast.makeText(requireContext(), "Portföyüme Eklendi", Toast.LENGTH_SHORT).show()

                findNavController().navigate(R.id.action_detay_Fragment_to_ana_Fragment)
            }
        }
        binding.button.setOnClickListener {
            if (binding.miktar.text!!.isNotEmpty() && binding.fiyati.text!!.isNotEmpty()) { // Boşluk kontrolü

                val miktar = binding.miktar.text.toString()
                val fiyat = binding.fiyati.text.toString()
                val toplam = miktar.toInt() * fiyat.toInt()

                binding.toplammaliyet.text = toplam.toString()
            }
            else {
                Toast.makeText(requireContext(), "Lütfen boşlukları doldurunuz", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}