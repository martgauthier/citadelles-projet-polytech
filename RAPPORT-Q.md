<h1 align="center">
  <br>
  <img src="https://gusandco.net/wp-content/uploads/2013/06/citadelles_edition3_front.png?w=234" 
  width="200">
  <br>
  Citadelles : Groupe Q
  <br>
</h1>

<h5 align="center">Citadelle, un jeu de société mêlant stratégie et bluff dans un décor médiéval fantastique. Les joueurs incarnent des personnages ambitieux construisant leur cité tout en contrant les plans de leurs adversaires.</h5>

<p align="center">
  <a href="#notre-avancement">Notre avancement</a> •
  <a href="#architecture-et-qualité">Architecture et qualité</a> •
  <a href="#notre-processus-de-développement">Notre processus de développement</a> 
</p>

# Rapport final


## 1. Notre avancement



### Avancement du jeu
#### Ce qui a été fait
La plupart des fonctionnalités ont été intégrées dans le jeu,
garantissant ainsi une **couverture globale de ses fonctionnalités**. De même pour ce qui est des différents **pouvoirs des**
**cartes** ou des **rôles**. 

#### Ce qui n'a pas été fait
Il est important de noter que ces différents points ne sont pas présent dans notre implémentation :

- Nous ne gérons pas la **limite de pièces** sur une partie, qui est fixée à un maximum de 30.
- Les bots n'ont **pas de mémoires** et donc ne peuvent pas retenir les rôles.
- Lorsqu'un joueur récupère des pièces à l'aide de la couleur de son rôle et de ses cités, il **ne peut pas choisir**
de les obtenir **avant d'effectuer son tour** ou **à la fin de celui-ci** (pour potentiellement en obtenir plus suite 
- à l'achat d'une carte pendant le tour)


#### Nos choix d'affichage
Nous avons opté pour une approche simple en affichant les informations uniquement sur l'entrée standard. Lors de 
l'exécution d'une partie différentes informations sont affichées. Tel que :
- Le **numéro de tour**
- Le **type de bot** qui joue
- Les différentes **actions** et **choix du bot**
- La **stratégie** du bot
- Tous les événements liés à l'utilisation des **pouvoirs**
- Les **pièces** et les **cartes** en main et les cartes posées d'un joueur
- L'annonce du **vainqueur**

*Voici l'exemple de l'affichage lors d'un tour :*
<div class="container">
<img src="assetsrapport/output.png" width="500" height=auto>
</div>


### Avancement des statistiques et du CSV
Nous avons réalisé deux csv.

- [gamestatsdetails](gamestatsdetails.csv) a pour objectif de stocker en détails différentes statistiques important sur la partie d'un bot. Tel que :
  - Nom du joueur
  - Prix moyen des citadelles achetées
  - Rôle préféré
  - ...

    Il permet donc d'analyser précisément le comportment de nos bots sur plusieurs parties pour améliorer ses 
stratégies.

- [gamestats](gamestats.csv) a pour objectif de présenter de manière claire les performances de nos bots. En affichant le pourcentage 
de victoire, de défaite et de match nul.

### Avancement du bot demandé : “RichardPlayer”

Pour le bot Richard nous nous sommes basés sur les conseils de [Richard](https://forum.trictrac.net/t/citadelles-charte-citadelles-de-base/509) en implémentant les comportements qu’il aime prendre
au cours de la partie comme par exemple sa **stratégie offensive** quand un adversaire est sur le point de poser son avant-dernier 
quartier. Nous lui avons donné également un **comportement par défaut** qui correspond à la description de **« l’OPTIMISTE »** selon le 
deuxième utilisateur du forum car c’est selon lui le meilleur comportement. Nous avons choisis arbitrairement ses choix entre piocher 
des cartes ou prendre des pièces (car cela n’était pas indiqué).

### Nos meilleurs bots: “Matt” et “Thomas”


## 3. Notre processus de développement
### A. Branching strategy: *Github Flow*
Nous avons décidé d'implémenter la branching strategy *Github flow*:

![Schema de la stratégie *Github flow*](assetsrapport/branching.jpeg)

Avec cette stratégie, pour ajouter ou modifier une fonctionnalité, on crée une seule branche,
dans laquelle on commitera nos modifications, ainsi que nos tests et vérifications. Une fois qu'on
estime le travail fini, on *pull-request*, on laisse l'équipe vérifier le travail (et le corriger si nécessaire),
et on valide la pull-request, qui modifie alors la branche `master`.

Nos branches se nomment:
* `feature/nomdelajout` (avec `nomdelajout` un nom représentatif de l'ajout désiré), pour représenter une branche ajoutant une fonctionnalité
* `fix/nomdufix`, pour représenter un réglage de bug ou de problème
* `doc/nomdeladoc`, pour un ajout de documentation

#### Avantages et inconvénients de cette stratégie
Cette stratégie est très pratique et facile à mettre en place, tout en produisant un `master` stable.
Elle ne demande pas beaucoup de vérification, et de changements de branche et de pull-requests.

En revanche, elle assure une moins bonne stabilité du `master`: le code a subi moins de vérifications avant d'être merge.

### B. Utilisation des outils Github: issues, milestones, commits, PR
Nous avons délivré environ 1 milestone par semaine, celles-ci étaient toujours stables, testées et relues par les autres.
Nos milestones regroupaient entre 5 et 15 issues par semaine, labelisées, et réparties à chaque membre au fil de la semaine
sur notre Discord.

Nous avons globalement respecté les règles des *conventional commits*, en préfixant nos commit et en référençant les issues
qu'ils impactaient.

Toutes nos branches ont été *merge* par des *pull-requests*. Nous nous sommes permis de les accepter nous-même si les fonctionnalités étaient petites,
ou que nous nous étions mis d'accord pour le faire.

### C. Répartition du travail
Tout le monde a participé aux fonctionnalités. Chaque membre a fait au moins un peu de tests, de codages de merveilles, de rôles...

Mathias et Romain, en plus du code, sont principalement responsables de la réflexion autour des stratégies: quelles stratégies implémenter pour gagner ?
Comment choisir les citadelles à acheter ? 


Gauthier, en plus du code, a mené la division du projet en issues et milestones, et a contribué à la réflexion autour des responsabilités
de chaque classe. Il a proposé la majorité de la structure et des classes à implémenter.
