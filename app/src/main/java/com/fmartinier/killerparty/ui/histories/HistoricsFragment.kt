package com.fmartinier.killerparty.ui.histories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fmartinier.killerparty.databinding.FragmentHistoricsBinding
import com.fmartinier.killerparty.model.Party
import com.fmartinier.killerparty.services.PartyService
import com.fmartinier.killerparty.services.PlayerService

class HistoricsFragment : Fragment() {

    private lateinit var binding: FragmentHistoricsBinding
    private lateinit var partyService: PartyService
    private lateinit var playerService: PlayerService

    private val histories: MutableList<Party> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoricsBinding.inflate(inflater, container, false)
        partyService = PartyService(requireContext())
        playerService = PlayerService(requireContext())
        fillParties()

        binding.historics.apply {
            layoutManager = LinearLayoutManager(context)
            val adapter = HistoricViewAdapter(histories, context, partyService, playerService)
            this.adapter = adapter
        }
        return binding.root
    }

    private fun fillParties() {
        histories.clear()
        histories.addAll(partyService.findAllBegan())
        histories.sortByDescending { it.id }
    }
}