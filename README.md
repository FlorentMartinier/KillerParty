# KillerParty
Application Android d'automatisation du jeu du killer. Disponible en téléchargement ici : https://killer-party.en.aptoide.com/app.

## Pour exécuter en local
* Ouvrir le projet sur Android studio (qui doit posséder un émulateur android)
* Lancer une synchronisation gradle
* Run la configuration créée par gradle

## Prochaines features souhaitées :
* Pouvoir transmettre un lien permettant d'intégrer une partie en cours à tous les joueurs (en cours de développement avec le projet https://github.com/FlorentMartinier/Killer-Party-Backend)
* Mettre en place un gestionnaire de BDD type flyway
* Rendre la l'invalidation d'un joueur plus ergonomique (empêcher de fermer la fenêtre, rajouter du rouge là où le problème est présent)
* Utiliser une lib de validation de numéro de téléphone (https://github.com/google/libphonenumber)
* Mettre de la publicité sur l'appli

## Prochains Fix :
* Les flèches indiquant sur quel bouton cliquer sont fixes. Le format tablette ne fonctionne plus. A rendre plus générique
* Le bouton + passe devant l'icone de suppression des défis. Cela rend très compliqué la suppression du dernier défi. A voir comment arranger la chose.
