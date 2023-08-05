package com.example.killerparty.ui.histories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.killerparty.databinding.FragmentHistoricsBinding
import com.example.killerparty.model.Party
import com.example.killerparty.services.PartyService

class HistoricsFragment : Fragment() {

    private lateinit var binding: FragmentHistoricsBinding
    private lateinit var partyService: PartyService

    private val histories: MutableList<Party> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoricsBinding.inflate(inflater, container, false)
        partyService = PartyService(requireContext())
        histories.addAll(partyService.findAll())

        binding.historics.apply {
            layoutManager = LinearLayoutManager(context)
            val adapter = HistoricViewAdapter(histories, context, partyService)
            this.adapter = adapter
        }
        return binding.root
    }
}