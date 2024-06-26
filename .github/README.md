<h1 align="center">
  <br>
  <img src="https://gusandco.net/wp-content/uploads/2013/06/citadelles_edition3_front.png?w=234" 
  width="200">
  <br>
  Citadelles : Groupe Q
  <br>
</h1>

<h5 align="center">Citadelles, un jeu de société mêlant stratégie et bluff dans un décor médiéval fantastique. Les joueurs incarnent des personnages ambitieux construisant leur cité tout en contrant les plans de leurs adversaires.</h5>

<p align="center">
  <a href="#notre-avancement">Notre avancement</a> •
  <a href="#architecture-et-qualité">Architecture et qualité</a> •
  <a href="#notre-processus-de-développement">Notre processus de développement</a> 
</p>

# Rapport final


## 1. Notre avancement



### A. Avancement du jeu
#### a. Ce qui a été fait
La plupart des fonctionnalités ont été intégrées dans le jeu,
garantissant ainsi une **couverture globale de ses fonctionnalités**. De même pour ce qui est des différents **pouvoirs des**
**cartes** ou des **rôles**. 

#### b. Ce qui n'a pas été fait
Il est important de noter que ces différents points ne sont pas présent dans notre implémentation :

- Nous ne gérons pas la **limite de pièces** sur une partie, qui est fixée à un maximum de 30.
- Les bots n'ont **pas de mémoires** et donc ne peuvent pas retenir les rôles.
- Lorsqu'un joueur récupère des pièces à l'aide de la couleur de son rôle et de ses cités, il **ne peut pas choisir**
de les obtenir **avant d'effectuer son tour** ou **à la fin de celui-ci** (pour potentiellement en obtenir plus suite à l'achat d'une carte pendant le tour)


#### c. Nos choix d'affichage
Nous avons opté pour une approche simple en affichant les informations uniquement sur l'entrée standard. Lors de 
l'exécution d'une partie différentes informations sont affichées. Tels que :
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


### B. Avancement des statistiques et du CSV
Nous avons réalisé deux csv.

- [gamestatsdetails](gamestatsdetails.csv) a pour objectif de stocker en détails différentes statistiques important sur la partie d'un bot. Tels que :
  - Nom du joueur
  - Prix moyen des citadelles achetées
  - Rôle préféré
  - ...

    Il permet donc d'analyser précisément le comportment de nos bots sur plusieurs parties pour améliorer ses 
stratégies.

- [gamestats](gamestats.csv) a pour objectif de présenter de manière claire les performances de nos bots. En affichant le pourcentage 
de victoire, de défaite et de match nul.

### C. Avancement du bot demandé : `RichardPlayer`

<div align="center">
<img src="assetsrapport/richardplayer.png" width="100" height=auto>
</div>

Pour le bot Richard, nous nous sommes basés sur les conseils de [Richard](https://forum.trictrac.net/t/citadelles-charte-citadelles-de-base/509) en implémentant les comportements qu’il aime prendre
au cours de la partie comme par exemple sa **stratégie offensive** quand un adversaire est sur le point de poser son avant-dernier 
quartier. Nous lui avons donné également un **comportement par défaut** qui correspond à la description de **« l’OPTIMISTE »** selon le 
deuxième utilisateur du forum, car c’est selon lui le meilleur comportement. Nous avons choisi arbitrairement ses choix entre piocher 
des cartes ou prendre des pièces (car cela n’était pas indiqué).

### D. Nos meilleurs bots : `MattPlayer` et `ThomasPlayer`

#### Le bot `MattPlayer`
<div align="center">
<img src="assetsrapport/mattplayer.png" width="100" height=auto>
</div>

[Mattplayer](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/players/MattPlayer.java) adopte une stratégie de base [MattStartGameStrategy](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/strategies/MattStartGameStrategy.java)
pendant le début de partie. Puis, il modifie son comportement en utilisant [MattMoreThan5CitiesStrategy](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/strategies/MattMoreThan5CitiesStrategy.java). Son comportement se base sur les points suivants :
- Pendant le **début de partie** pour son choix de **rôle** :
  - Son objectif est de prendre le **marchand** dès qu'il le peut et de piocher avec celui-ci
  - Sinon, il prend l'**architecte** et pioche des pièces
  - Sinon l'**assassin** (pour ne pas se faire assassiner)
- Pendant le **début de partie** pour son choix entre piocher des pièces ou des cartes :
  - S'il a moins de 4 pièces il en pioche 
  - Sinon, il pioche des cartes
- Pendant le **début de partie** pour son choix d'**achat** :
  - Il essaye de toujours acheter des cartes **vertes** et prend **la plus proche de 3 pièces**  
- Quand il a **bientôt fini** la partie **(Plus de 4 cartes et plus de 5 pièces)** :
    - Il change de stratégie et utilise [MattMoreThan5CitiesStrategy](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/strategies/MattMoreThan5CitiesStrategy.java)
    - Il essaye d'acheter la carte la **moins chère** parmis les **couleurs manquantes** à sa cité
    - S'il a déjà toutes les couleurs, il prend la **moins chère**
    - Sinon il prend la **plus proche de trois** et de sa **couleur** s'il le peut 
 - Si un joueur est **proche de la victoire** ([closeToWin](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/players/Player.java#L479)) 
et qu'il possède peu de cartes :
   - Il prend le **Magicien** et échange ces cartes avec les siennes 
- Si un joueur pose sa **8ᵉ carte** :
  - Il utilise la stratégie [SecurePointsForEndGame](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/strategies/SecurePointsForEndGame.java)
qui lui permet de faire le **choix optimal**

#### Le bot `ThomasPlayer`

<div align="center">
<img src="assetsrapport/thomasplayer.png" width="100" height=auto>
</div>

[ThomasPlayer](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/players/ThomasPlayer.java) adopte une stratégie basée sur les points suivants :
* Il préfère les rôles **Architecte/Marchand/Condottiere.**
* Il choisit de prendre des pièces tant qu'il a des cartes en main.
* Il achète à chaque fois sa citadelle en main **la moins chère**.
* Quand il est **Condottiere**, il **cible** en priorité les joueurs **proche de gagner**.
* Quand il est **voleur,** il **cible** en priorité l'**architecte** et le **marchand**.
* Lorsque la partie arrive à son **dernier tour**, il active la stratégie [SecurePointForEndGame](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/strategies/SecurePointsForEndGame.java).

## 2. Architecture et qualité
### A. Architecture du code
*(Nous parlerons parfois de "joueur" dans cette partie, mais nous voulons bien dire par cela "logique de robot".)*

Notre architecture est divisée en plusieurs parties :
*  Le moteur de jeu :
    * Gestion de l’output ([GameOutputManager](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/GameOutputManager.java)), gestion du déroulement de la partie ([GameLogicManager](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/gamelogic/GameLogicManager.java), [RoundSummary](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/gamelogic/RoundSummary.java))
      et les classes d’éléments du jeu ([District](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/District.java), [Color](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/Color.java),
      [Role](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/Role.java), [CardDeck](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/CardDeck.java)).
*  Les bots/Player:
    * Une classe mère [Player](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/players/Player.java) avec des classes filles qui extend Player
      pour chaque type de bot ([ThomasPlayer](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/players/ThomasPlayer.java), [MattPlayer](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/players/MattPlayer.java)...).
* Les stratégies :
    * On a une interface `IStrategy` dont on se sert pour créer des classes stratégies qu’on utilise ensuite dans les bots.
* La dernière partie de l’architecture est là uniquement pour les calculs de statistiques ([BestScoreCalculator](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/playerevaluator/BestScoreCalculator.java), [GameStatsCsv](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/playerevaluator/GameStatsCsv.java), [StatsManager](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/playerevaluator/StatsManager.java)).

#### Le moteur du jeu :
* Notre classe [GameLogicManager](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/gamelogic/GameLogicManager.java)
  fonctionne dans cet ordre :
1. Elle génère une liste de joueurs, ou en prend une en paramètre, et crée une pioche partagée ([CardDeck](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/CardDeck.java)).
2. À chaque tour, elle génère une liste de rôles aléatoires, fait choisir à chaque joueur (à partir du maître du jeu) son rôle.
3. Elle fait jouer le tour de chaque joueur, par ordre de rôle.
4. Elle arrête la partie lorsqu'un joueur a atteint 8 citadelles.
* [RoundSummary](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/gamelogic/RoundSummary.java) est une classe qui va stocker et enregistrer tous les événements durant le tour du joueur.
* Cette classe est utilisée dans la classe [GameOutputManager](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/GameOutputManager.java). Grâce à un `RoundSummary` la méthode [describePlayerRound()](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/GameOutputManager.java#L109) de `GameOutputManager` va pouvoir vérifier les évènements qui se sont déroulés et activer les bons logs.
  `GameOutputManager` contient d'autres méthodes de description comme une description de la cité d'un joueur ou encore la description des roles disponibles au début du tour. La principale methode [startMainOutputLoop()](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/GameOutputManager.java#L33) déroule l'affichage de la partie du début à la fin.

#### Les `District` et les `Role`
Les `District` sont modélisés dans une classe. Ils possèdent un nom, un prix, une couleur, et optionnellement un nom de pouvoir (si ce sont des merveilles).
Nos rôles sont stockés dans un `Enum`, et possèdent une couleur ([Color](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/Color.java)), un nom, et un [pouvoir de rôle](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/Role.java#L167).

#### Les `Player` et les `Strategy`

Ce que nous appelons `Player` sont les classes représentant des bots.
Notre code est structuré ainsi :
* La classe abstraite mère [Player](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/players/Player.java) définit toutes les méthodes utilitaires attribuées à un bot :
  récupérer ses cartes en main [(getCardsInHand())](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/players/Player.java#L104), connaitre sa fortune [(getCash())](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/players/Player.java#L100), etc...
  Le `Player` possède un rôle, un accès à la pioche (classe [CardDeck](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/CardDeck.java)), son argent (`Player.Cash`), des cartes en main (`Player.cardsInHand`), et une cité (`Player.city`).
  Elle possède également une instanciation de stratégie. Détaillons :
* L'interface [IStrategy](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/strategies/IStrategy.java) liste les méthodes à Override pour changer le comportement d'un joueur. Entre autres :
    * [getChoosenCitadelToBuy()](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/strategies/IStrategy.java#L43) renvoie un Optional, contenant si désiré le district à acheter.
    * [selectAndSetRole()](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/strategies/IStrategy.java#L52), qui choisit parmi la liste des rôles disponibles le rôle que le bot choisira.
    * [selectDistrictToDestroyAsCondottiere()](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/strategies/IStrategy.java#L22), qui choisit le district à détruire si le bot est Condottière.

Le `Player` possède un attribut de type `IStrategy`, et peut le changer en milieu de partie. Nous avons donc plusieurs implémentations de stratégie, pouvant être modifiées en plein milieu de partie.
C'est un début modeste de *Strategy Pattern* (https://en.wikipedia.org/wiki/Strategy_pattern).

Précisons le fonctionnement de ce *Strategy Pattern*:

La classe `Player` (et donc toutes ses classes filles) implémentent l'interface `IStrategy`. Un `Player` possède ainsi une implémentation par défaut de `getChoosenCitadelToBuy()`, `selectRoleToSteal()`...

Mais les `Player` possèdent aussi un attribut privé `strategy` de type `IStrategy`. Nous avons fait en sorte que toutes les instances de `IStrategy` appellent par défaut les comportements codés dans la classe `Player`,
**sauf si leur comportement est redéfini dans cette stratégie** !

Nos joueurs possèdent donc :
* Une stratégie prioritaire, définissant les comportements à adopter, échangeable en pleine partie, grâce à l'attribut `strategy`
* Une stratégie par défaut si la stratégie prioritaire ne redéfinit pas tout, codée dans l'implémentation du `Player`

Exemple :
La stratégie [AimForMoneyStrategy](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/strategies/AimForMoneyStrategy.java) fait juste en sorte de ne rien acheter, et de choisir en priorité le rôle Voleur.
Elle n'implémente donc pas tous les comportements à prévoir (qui voler en tant que voleur ? qui tuer en tant qu'assassin ?).

Si [RandomPlayer](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/players/RandomPlayer.java) utilise une instance d'`AimForMoneyStrategy` en tant que `strategy`, il va utiliser en priorité les comportements décrits par cette stratégie (donc essayer de choisir le rôle voleur, et ne rien acheter),
mais il va utiliser son comportement codé par défaut pour le reste (par exemple, voler un joueur aléatoire en tant que voleur).

### B. Documentation du code
Notre code est documenté via une [java doc](javadoc/index.html). Le code est dans sa globalité bien commenté et documenté.


### C. Qualité du code
#### a. Parties en confiance
* Nous sommes plutôt en confiance sur notre logique de jeu notamment avec `GameLogicManager` et `RoundSummary`. Elle fait son travail sans erreur.
* Les méthodes de `Player` fonctionnent et sont testées.
* Quant aux `Roles` ils ne peuvent pas contenir d'erreur notamment avec les pouvoirs, car ils contiennent des exceptions en cas d'action impossible (et nous avons testé l'appel de ces exceptions).
* Les `Strategy` sont bien implémentées et chaque méthode est bien testée.

#### b. Parties à améliorer
* La partie sur le `CSV` est encore trop brouillon et il manque beaucoup d'exceptions. Même si elle est testée, le code pourrait être plus propre et modulaire.
* À certains endroits du code la complexité pourrait être diminuée, car inutilement haute. (notamment dans des méthodes qui contiennent pas mal de condition avec des _if_ et des _else if_)
* On peut relever aussi certaines duplications de code.
* On aurait aussi dû implémenter les stratégies par défaut des `Player` dans des classes `Strategy`, au lieu de les implémenter dans les classes des `Player` afin de les alléger.
#### c. Analyse de SonarQube

<div align="center">
<img src="assetsrapport/sonar.png" width="700" height=auto>
</div>

Le **coverage** est plutôt bon, avec 83.2% sur la globalité du projet. Nous n'avons pas de *bugs*, de *code smells*, de *vulnerabilities*, ni
de *security hotspots*. Nous avons cependant quelques *duplications*.

Notez que nous avons passé le ***Quality Gate*** !
## Notre processus de développement

## 3. Notre processus de développement
### A. Branching strategy: *GitHub Flow*
Nous avons décidé d'implémenter la branching strategy *Github flow*:

![Schema de la stratégie *Github flow*](assetsrapport/branching.jpeg)

Avec cette stratégie, pour ajouter ou modifier une fonctionnalité, on crée une seule branche,
dans laquelle on commitera nos modifications, ainsi que nos tests et vérifications. Une fois qu'on
estime le travail fini, on *pull-request*, on laisse l'équipe vérifier le travail (et le corriger si nécessaire),
et on valide la pull-request, qui modifie alors la branche `master`.

Nos branches se nomment :
* `feature/nomdelajout` (avec `nomdelajout` un nom représentatif de l'ajout désiré), pour représenter une branche ajoutant une fonctionnalité
* `fix/nomdufix`, pour représenter un réglage de bug ou de problème
* `doc/nomdeladoc`, pour un ajout de documentation

#### Avantages et inconvénients de cette stratégie
Cette stratégie est très pratique et facile à mettre en place, tout en produisant un `master` stable.
Elle ne demande pas beaucoup de vérification, et de changements de branche et de pull-requests.

En revanche, elle assure une moins bonne stabilité du `master`: le code a subi moins de vérifications avant d'être merge.

### B. Utilisation des outils Github : issues, milestones, commits, PR
Nous avons délivré environ 1 milestone par semaine, celles-ci étaient toujours stables, testées et relues par les autres.
Nos milestones regroupaient entre 5 et 15 issues par semaine, labellisées, et réparties à chaque membre au fil de la semaine
sur notre Discord.

Nous avons globalement respecté les règles des *conventional commits*, en préfixant nos commits et en référençant les issues
qu'ils impactaient.

Toutes nos branches ont été *merge* par des *pull requests*. Nous nous sommes permis de les accepter nous-même si les fonctionnalités étaient petites,
ou que nous nous étions mis d'accord pour le faire.

### C. Répartition du travail
<div align="center" style="display: flex; justify-content: space-evenly;">
<img style="margin: 20px; border-radius: 20px" src="assetsrapport/mathias.png" width="100" height=auto>
<img style="margin: 20px; border-radius: 20px" src="assetsrapport/gauthier.png" width="100" height=auto>
<img style="margin: 20px; border-radius: 20px" src="assetsrapport/romain.jpg" width="100" height=auto>
</div>

Tout le monde a participé aux fonctionnalités. Chaque membre a fait au moins un peu de tests, de codages de merveilles, de rôles...

Mathias et Romain, en plus du code, sont principalement responsables de la réflexion autour des stratégies : quelles stratégies implémenter pour gagner ?
Comment choisir les citadelles à acheter ? 


Gauthier, en plus du code, a mené la division du projet en issues et milestones, et a contribué à la réflexion autour des responsabilités
de chaque classe. Il a proposé la majorité de la structure et des classes à implémenter.
