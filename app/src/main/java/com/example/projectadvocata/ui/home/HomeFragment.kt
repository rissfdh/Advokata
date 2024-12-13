package com.example.projectadvocata.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectadvocata.R
import com.example.projectadvocata.databinding.FragmentHomeBinding
import com.example.projectadvocata.ui.ViewModelFactory
import com.example.projectadvocata.ui.compliment.ListNewsAdapter
import com.example.projectadvocata.ui.compliment.News
import com.example.projectadvocata.ui.compliment.PERDA
import com.example.projectadvocata.ui.compliment.PERPU
import com.example.projectadvocata.ui.compliment.UUD
import com.example.projectadvocata.ui.compliment.UUKUHP
import kotlin.getValue

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getAuthInstance(
            requireActivity()
        )
    }
    private val listNews = ArrayList<News>()

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()

        binding.buttonCariPengacara.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_home_to_lawyer)
        }

        binding.buttonKUHP.setOnClickListener {
            val intent = Intent(requireContext(), UUKUHP::class.java)
            startActivity(intent)
        }
        binding.buttonUUD.setOnClickListener {
            val intent = Intent(requireContext(), UUD::class.java)
            startActivity(intent)
        }
        binding.buttonPERPU.setOnClickListener {
            val intent = Intent(requireContext(), PERPU::class.java)
            startActivity(intent)
        }
        binding.buttonPERDA.setOnClickListener {
            val intent = Intent(requireContext(), PERDA::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerViews() {


        // Set RecyclerView layout manager and adapter
        binding.recyclerViewNews.layoutManager = LinearLayoutManager(context)
        // Add sample data for News
        if (listNews.isEmpty()) {
            listNews.addAll(getListNews())
        }

        val adapter = ListNewsAdapter(listNews)
        binding.recyclerViewNews.adapter = adapter
    }

    private fun getListNews(): List<News> {
        val newsList = ArrayList<News>()

        newsList.add(
            News(
                "Agus Buntung Penyandang Disabilitas Viral",
                "     Kasus pelecehan seksual yang melibatkan I Wayan Agus Suartama, atau yang dikenal sebagai Agus Buntung, hingga kini terus menjadi sorotan publik,Terbaru, penyandang disabilitas asal NTB ini resmi ditetapkan sebagai tersangka oleh Polda NTB setelah menerima laporan dari sejumlah korban.\n" + "\n" + "    Meskipun Agus sempat membantah tuduhan tersebut dan mengklaim dirinya difitnah, fakta dan kesaksian dari berbagai pihak, termasuk penjaga homestay, mengungkap sisi lain dari kasus ini.",
                R.drawable.news1
            )
        )
        newsList.add(
            News(
                " Viral Video Gus Miftah Emosi dengan Penjual Es Teh",
                "Kumpulan video lawas Gus Miftah bercanda dan memaki menggunakan kata-kata kasar mendadak jadi sorotan, setelah kasusnya menghina penjual es teh goblok. \n" + "\n",
                R.drawable.news2
            )
        )
        newsList.add(
            News(
                "Agus \"Sedih Banget\" Sampai Sesegukan Dibully Netizen\n",
                "Kisruh soal uang donasi antara Agus Salim, Noviyanthi Pratiwi dan Denny Sumargo masih terus berlanjut makain panas. Diketahui, Teh Novi dan Denny Sumargo merupakan orang yang pertama kali membuka donasi untuk Agus. Namun kasus ini semakin rumit lantaran Agus disebut tidak memanfaatkan duit itu untuk keperluan pengobatan. Apalagi kini Agus dibantu oleh pengacara Farhat Abas dan Alvin Lim yang dianggap semakin memperkeruh keadaan.\n",
                R.drawable.news3
            )
        )
        return newsList
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
