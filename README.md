# Imposteur Game
## Workflow

Au lancement de l'écran hôte, un QR Code est affiché afin que les joueurs puisse rejoindre la partie en scannant 
avec leur téléphone le code.

Côté serveur, cela correspond à une salle de jeu créée dans une phase de préparation où on attend que tous les joueurs
rejoignent la partie.

Scanner ce code en tant que joueur ouvre une page web dédiée au joueur sur son téléphone.
Cela consiste à l'ouverture d'une session STOMP dédiée au joueur pour y recevoir le mot.


L'écran hôte dispose d'un bouton Démarrer qui démarre la partie avec les joueurs connectés.
Au lancement de la partie, on entre dans la phase de tour de jeu.

### Tour de jeu
Résumé (TBD) :

Un joueur au hasard est désigné comme l'imposteur.

Chaque joueur reçoit un mot sur son téléphone.

L'ordre de parole des joueurs est défini au hasard par tour de jeu et est affiché sur l'écran hôte.

Combien de tours de parole ? TBD

Combien de temps de parole ? TBD

Une fois les X tours de parole terminés, on passe en phase de vote.

### Vote
Discussion sur qui est l'imposteur et désignation d'un joueur.

### Révélation
Affichage du vrai imposteur

### Rejouer
Bouton pour relancer la partie (comportement similaire au bouton Démarrer)


# Robustesse

## Gestion des reconnections
- Téléphone qui se met en veille
- Joueur qui quitte la page web