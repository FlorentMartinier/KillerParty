package com.fmartinier.killerparty.ui.histories

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fmartinier.killerparty.R
import com.fmartinier.killerparty.databinding.FragmentHistoryListBinding
import com.fmartinier.killerparty.extensions.toDP
import com.fmartinier.killerparty.model.Party
import com.fmartinier.killerparty.services.PartyService
import com.fmartinier.killerparty.services.PlayerService


class HistoriesFragment : Fragment() {

    private lateinit var binding: FragmentHistoryListBinding
    private lateinit var partyService: PartyService
    private lateinit var playerService: PlayerService

    private val histories: MutableList<Party> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryListBinding.inflate(inflater, container, false)

        manageHelperConfiguration()

        partyService = PartyService(requireContext())
        playerService = PlayerService(requireContext())
        fillParties()

        binding.histories.apply {
            layoutManager = LinearLayoutManager(context)
            val adapter = HistoriesViewAdapter(histories, context, partyService, playerService, activity)
            adapter.onPlayerKilled = {
                adapter.notifyDataSetChanged()
            }
            this.adapter = adapter
        }
        return binding.root
    }

    private fun fillParties() {
        histories.clear()
        histories.addAll(partyService.findAllBegan())
        histories.sortByDescending { it.id }
        manageHelperVisibility()
    }

    private fun manageHelperVisibility() {
        if (histories.isEmpty()) {
            binding.noHistoryHelperMessage.editText.visibility = View.VISIBLE
            binding.noHistoryHelperMessage.imageView.visibility = View.VISIBLE
        } else {
            binding.noHistoryHelperMessage.editText.visibility = View.INVISIBLE
            binding.noHistoryHelperMessage.imageView.visibility = View.INVISIBLE
        }
    }

    private fun manageHelperConfiguration() {
        binding.noHistoryHelperMessage.editText.text = context?.getString(R.string.no_history)
        binding.noHistoryHelperMessage.imageView.rotationY = 180F
        val params = LinearLayout.LayoutParams(
            binding.noHistoryHelperMessage.imageView.layoutParams
        ).apply {
            weight = 1.0f
            gravity = Gravity.START
            marginStart = 55F.toDP(context)
        }
        binding.noHistoryHelperMessage.imageView.layoutParams = params
    }
}