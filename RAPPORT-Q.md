# Rapport final

## 2) Architecture et qualité
### A) Architecture du code
Notre architecture est divisé en 3 parties majeure :
*  Le moteur de jeu:
    * gestion de l’output (GameOutputManager), gestion du déroulement de la partie (GameLogicManager, RoundSummary) et les classes d’éléments du jeu (District, Color, Role, CardDeck).
*  Les bots/Player:
    * Une classe mère Player avec des classes filles qui extend Player pour chaque type de bot.
* Les Stratégies:
    * On a une interface stratégie qu’on se sert pour crée des classes stratégies qu’on utilise ensuite dans les bots
* La dernière partie de l’architecture est là uniquement pour les statistiques liées au CSV.
### B) Documentation du code
Notre code est documenté via une [java doc](file:///C:/Users/mat7t/Documents/fr/cotedazur/univ/polytech/citadellesgroupeq/players/package-summary.html). Le code est dans sa globalité bien commenté et documenté.
### C) Qualité du code
#### a. Parties en confiance

#### b. Parties à améliorer

#### c. Analyse de SonnarQube



## Notre processus de développement
### A. Branching strategy: *Github Flow*
Nous avons décidé d'implémenter la branching strategy *Github flow*:
![Schema de la stratégie *Github flow*](assetsrapport/branching.jpeg)
Ouverture de la branche "rapport final."