package com.example.projectadvocata.ui.lawyer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectadvocata.R
import com.example.projectadvocata.databinding.FragmentLawyerBinding
import com.example.projectadvocata.ui.compliment.ListLawyerAdapter

class LawyerFragment : Fragment() {
    private var _binding: FragmentLawyerBinding? = null
    private val binding get() = _binding!!

    private val listLawyer = ArrayList<Lawyer>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLawyerBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
    }
    private fun setupRecyclerViews() {
        binding.recyclerViewLawyer.layoutManager = LinearLayoutManager(context)
        if (listLawyer.isEmpty()) {
            listLawyer.addAll(getListLawyer())
        }
        val adapter = ListLawyerAdapter(listLawyer)
        binding.recyclerViewLawyer.adapter = adapter
    }
    private fun getListLawyer(): List<Lawyer> {
        val LawyerList = ArrayList<Lawyer>()
        LawyerList.add(
            Lawyer("Rico Pahlevi Siregaar S.H., M.H.", "Rp.100.000", R.drawable.lawyer_rico)
        )
        LawyerList.add(
            Lawyer("Faris Fadhil Dhiaulhaq S.H., M.H.", "Rp.100.000", R.drawable.lawyer_faris)
        )
        return LawyerList
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
