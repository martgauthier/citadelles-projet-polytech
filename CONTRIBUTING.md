# Comment contribuer au projet
Nous avons choisi la stratégie "*Github Flow*".

Pour chacune des slices (pas des issues, pas des milestones, bien des *slices*) qu'on a découpé
sur notre Discord, on la code dans la branche:
`feature/nom_representant_la_slice`

Une fois qu'elle est validée et testée, on propose une pull request de la branche.
Si elle est validée, cela va alors merge la branche, avec la branche `master`.


**Notre `master` contient donc uniquement des commits "stables"**


## Comment créer une branche
Une fois positionné sur un commit propre de la branche `master`, pour créer une nouvelle branche `feature/...`:

`git switch -c feature/nom_representant_la_slice`