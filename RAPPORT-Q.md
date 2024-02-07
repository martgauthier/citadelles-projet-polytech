# Rapport final

## 2) Architecture et qualité
### A) Architecture du code
Notre architecture est divisé en 3 parties majeures :
*  Le moteur de jeu:
    * Gestion de l’output (GameOutputManager), gestion du déroulement de la partie (GameLogicManager, RoundSummary) et les classes d’éléments du jeu (District, Color, Role, CardDeck).
*  Les bots/Player:
    * Une classe mère [Player](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/players/Player.java) avec des classes filles qui extend Player pour chaque type de bot.
* Les stratégies:
    * On a une interface stratégie qu’on se sert pour crée des classes stratégies qu’on utilise ensuite dans les bots
* La dernière partie de l’architecture est là uniquement pour les statistiques liées au CSV.


#### Les `Player` et les `Strategy`
Ce que nous appelons `Player` sont les classes représentant des bots, pas des joueurs humains !
Notre code est structuré ainsi:
* La classe mère [Player](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/players/Player.java) définit toutes les méthodes utilitaires attribuées à un bot:
récupérer ses cartes en main [getCardsInHand()](src/main/java/fr/cotedazur/univ/polytech/citadellesgroupeq/players/Player.java#L104)


### B) Documentation du code
Notre code est documenté via une [java doc](file:///C:/Users/mat7t/Documents/fr/cotedazur/univ/polytech/citadellesgroupeq/players/package-summary.html). Le code est dans sa globalité bien commenté et documenté.
### C) Qualité du code
#### a. Parties en confiance

#### b. Parties à améliorer

#### c. Analyse de SonnarQube



## Notre processus de développement
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
