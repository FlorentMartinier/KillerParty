package com.example.killerparty.ui.challenges

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.killerparty.CHALLENGE_DESCRIPTION
import com.example.killerparty.databinding.FragmentChallengesBinding
import com.example.killerparty.db.MyDatabaseHelper
import com.example.killerparty.model.Challenge
import com.example.killerparty.ui.dialogs.AddChallengeDialogFragment


class ChallengesFragment : Fragment() {

    private lateinit var binding: FragmentChallengesBinding
    private val challenges: MutableList<Challenge> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChallengesBinding.inflate(inflater, container, false)

        val db = MyDatabaseHelper(requireContext())
        fillAllChallenges(db)
        binding.challenges.apply {
            layoutManager = LinearLayoutManager(context)
            val adapter = ChallengeViewAdapter(challenges)
            adapter.onChallengeRemoved = {
                db.deleteChallengeById(it.id)
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
                if (requestKey == CHALLENGE_DESCRIPTION && bundle.getString(CHALLENGE_DESCRIPTION) != null) {
                    db.addChallenge(bundle.getString(CHALLENGE_DESCRIPTION)!!)
                    fillAllChallenges(db)
                }
            }
            dialog.show(activity?.supportFragmentManager!!, "")
        }
        return binding.root
    }

    private fun fillAllChallenges(db: MyDatabaseHelper) {
        challenges.clear()
        challenges.addAll(db.getAllChallenges())
    }

}