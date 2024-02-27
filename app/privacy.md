L'application demande à l'utilisateur l'autorisation d'accéder à ses contacts, ainsi que
l'autorisation d'envoyer des SMS. Cela fait partie de la fonctionnalité de base de l'application. En
effet, le principe du jeu du killer est de donner des défis aux joueurs. Et les joueurs reçoivent
leurs défis par SMS, envoyés automatiquement par l'application. L'accès à la liste de contacts de l'
utilisateur permet d'ajouter des joueurs issus de son répertoire dans une partie.

Au moment d'envoyer des SMS aux différents joueurs, l'application demande avertis l'utilisateur du
nombre de SMS qui vont être envoyés, et lui demande une confirmation. Si la confirmation est
refusée, alors aucun SMS n'est envoyé.

Les données collectées par l'application sont stockées dans une base de données SQLite, interne au téléphone.
L'application ne possède aucune fonctionnalité réseau. Les données restent donc 100% en interne dans
le téléphone de l'utilisateur. De plus, les données collectées ne sont que des noms et numéros de
téléphone déjà issus du répertoire de l'utilisateur. Donc les données stockées sont déjà existantes
localement dans le téléphone du l'utilisateur.

La désinstallation de l'application permet la suppression intégrale de toutes les données stockées par celle-ci.