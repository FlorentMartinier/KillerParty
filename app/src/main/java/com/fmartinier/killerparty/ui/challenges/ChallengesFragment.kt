package com.fmartinier.killerparty.ui.challenges

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fmartinier.killerparty.databinding.FragmentChallengesBinding
import com.fmartinier.killerparty.model.Challenge
import com.fmartinier.killerparty.services.ChallengeService
import com.fmartinier.killerparty.ui.dialogs.AddChallengeDialogFragment
import com.fmartinier.killerparty.utils.CHALLENGE_DESCRIPTION


class ChallengesFragment : Fragment() {

    private lateinit var binding: FragmentChallengesBinding
    private lateinit var challengeService: ChallengeService

    private val challenges: MutableList<Challenge> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChallengesBinding.inflate(inflater, container, false)
        challengeService = ChallengeService(requireContext())
        fillAllChallenges()

        binding.challenges.apply {
            layoutManager = LinearLayoutManager(context)
            val adapter = ChallengeViewAdapter(challenges, context)
            adapter.onChallengeRemoved = {
                challengeService.deleteById(it.id)
                challenges.remove(it)
                adapter.notifyDataSetChanged()
            }
            this.adapter = adapter
        }

        binding.floatingActionButton.setOnClickListener {
            val dialog = AddChallengeDialogFragment()
            requireActivity().supportFragmentManager.setFragmentResultListener(
                CHALLENGE_DESCRIPTION,
                viewLifecycleOwner
            ) { requestKey, bundle ->
                if (requestKey == CHALLENGE_DESCRIPTION && !bundle.getString(CHALLENGE_DESCRIPTION)
                        .isNullOrEmpty()
                ) {
                    challengeService.insert(bundle.getString(CHALLENGE_DESCRIPTION)!!)
                    fillAllChallenges()
                }
            }
            dialog.show(activity?.supportFragmentManager!!, "")
        }
        return binding.root
    }

    private fun fillAllChallenges() {
        challenges.clear()
        challenges.addAll(challengeService.findAll())
    }

}