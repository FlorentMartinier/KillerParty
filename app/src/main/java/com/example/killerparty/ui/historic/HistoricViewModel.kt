package com.example.killerparty.ui.historic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoricViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "C'est ici que sera affiché l'historique des parties : date, vainqueur. Si une partie est en cours (le vainqueur n'est pas encore défini), alors l'état actuel de la partie est affiché : joueur en vie, et joueur killé. Quand un joueur est killé, alors il le signale au maitre du jeu, qui vient sur cet écran et killer le joueur en question. Un bouton 'killer' est affiché pour chaque joueur. Un écran de confirmation doit s'afficher au clic sur le bouton. Si un joueur est killé de cette manière, un SMS doit être envoyé au chasseur pour lui indiquer sa nouvelle cible et nouveau défi." +
                "Quand l'avant dernier joueur est killé, alors le dernier est déclaré vainqeur. Un SMS est envoyé à tous les joueurs pour l'annoncer."
    }
    val text: LiveData<String> = _text
}