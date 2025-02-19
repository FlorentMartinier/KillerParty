# KillerParty
## Pour exécuter en local
* Ouvrir le projet sur Android studio (qui doit posséder un émulateur android)
* Lancer une synchronisation gradle
* Run la configuration créée par gradle

## Prochaines features souhaitées :
* La fonctionnalité d'envois de SMS pose problème pour le déploiement sur google play. Etudier la possibilité d'envoyer des messages whattsap plutôt que des SMS.
* Pouvoir importer tous les contacts depuis un groupe whattsap, messenger, etc. Etudier la possibilité de transmettre un lien d'invitation.
* Mettre de la publicité sur l'appli
* Mettre en place un gestionnaire de BDD type flyway
* Rendre la l'invalidation d'un joueur plus ergonomique (empêcher de fermer la fenêtre, rajouter du rouge là où le problème est présent)
* Utiliser une lib de validation de numéro de téléphone (https://github.com/google/libphonenumber)

## Prochains Fix :
* Les flèches indiquant sur quel bouton cliquer sont fixes. Le format tablette ne fonctionne plus. A rendre plus générique
* Le bouton + passe devant l'icone de suppression des défis. Cela rend très compliqué la suppression du dernier défi. A voir comment arranger la chose.
