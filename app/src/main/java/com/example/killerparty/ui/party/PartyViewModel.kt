package com.example.killerparty.ui.party

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PartyViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "C'est ici que nous pourrons initialiser des parties. Il y aura une liste de joueurs (nom + numéro de tel). Il sera possible de noter manuellement le numéro, ou d'aller chercher dans le répertoire du tel. Un bouton 'Lancer la partie' sera présent en bas de l'écran. Quand ce bouton est cliqué, un SMS est envoyé à tous les participants avec la cible et le défi." +
                "Il existe une table en BDD qui répertorie les joueurs, leurs défis et leur statut (en vie, ou killé). Une fois la partie terminée, les joueurs sont supprimés de la bdd." +
                "Quand une partie est en cours, l'écran n'est plus accessible. Mais un bouton 'annuler la partie en cours' devrait être présent."
    }
    val text: LiveData<String> = _text
}