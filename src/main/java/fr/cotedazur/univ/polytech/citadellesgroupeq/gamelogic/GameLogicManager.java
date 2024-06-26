package fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic;

import fr.cotedazur.univ.polytech.citadellesgroupeq.Color;
import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.CardDeck;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.*;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * La classe GameManager gère le déroulement du jeu Citadelles. Elle initialise le jeu, fait choisir des rôles aux joueurs,
 * exécute les tours des joueurs, et vérifie si le jeu est terminé.
 */
public class GameLogicManager {
    private final Random randomGenerator;

    /**
     * Index du joueur qui commence à tirer (le maitre du jeu, ou le roi au tour d'avant)
     */
    private int masterOfTheGameIndex;

    /**
     * La liste non triée des joueurs
     */
    private final List<Player> playersList;


    /**
     * Nombre de districts nécessaires pour gagner. ! A adapter en fonction du nombre de joueurs d'après les règles du jeu !
     */
    public static final int NUMBER_OF_DISTRICTS_TO_WIN =8;

    /**
     * True quand la partie est finie, False quand elle est en cours
     */
    private boolean isFinished;

    /**
     * Pioche de carte (implémentée en LIFO), commune au jeu et à chacun des joueurs.
     */
    private CardDeck cardDeck;

    /**
     * Contient les joueurs dans leur ordre de passage (en fonction de leur rôle). Les {@link TreeSet} sont automatiquement triés dans l'ordre
     */
    private final SortedSet<Player> playerTreeSet;

    /**
     * Contient les scores de fin de partie de chaque joueur. Uniquement remplie après le dernier tour du dernier joueur, à la toute fin de partie.
     */
    private Map<Player, Integer> scoreOfEnd =new HashMap<>();



    private boolean shouldWriteInCsv;


    //nécessaire pour régler l'issue #53 sur github : voir la doc de public GameManager()
    /**
     * Liste des joueurs à utiliser par défaut pour une partie. Contient les classes à instancier, et non pas des instances, pour ne pas avoir à effectuer des Deep copy. (voir issue #53)
     */
    protected static final List<Class<? extends Player>> DEFAULT_PLAYER_CLASS_LIST = Arrays.asList(ThomasPlayer.class, RichardPlayer.class, AlwaysSpendPlayer.class, MattPlayer.class);

    public GameLogicManager(boolean writeInCsv) {
        this(List.of(), writeInCsv);//liste de joueurs vide


        //nécessaire pour régler l'issue #53 sur github :
        // il est plus simple d'instancier les joueurs depuis une liste de classes, avec leur constructeur, qu'essayer de les copier en profondeur

        int ids=0;
        for(Class<? extends Player> playerStrategy: DEFAULT_PLAYER_CLASS_LIST) {
            try {
                Constructor<? extends Player> constructor = playerStrategy.getDeclaredConstructor(int.class, CardDeck.class);
                this.playersList.add(constructor.newInstance(ids++, cardDeck));
            }
            catch(Exception e) {
                throw new IllegalArgumentException("pas de constructeur prenant un int et un DistrictJSONReader en paramètre trouvé pour la classe " + playerStrategy.getName() + "!");
            }
        }
    }

    public GameLogicManager(List<Player> playersList, boolean writeInCsv) {
        randomGenerator=new Random();
        this.playersList=new ArrayList<>(playersList);//pour ne pas modifier le tableau de base
        this.masterOfTheGameIndex=0;
        playerTreeSet=new TreeSet<>();
        isFinished=false;
        cardDeck =new CardDeck();
        shouldWriteInCsv=writeInCsv;
    }

    public GameLogicManager() {//for backward compatibility
        this(false);
    }

    public GameLogicManager(List<Player> playersList) {//for backward compatibility
        this(playersList, false);
    }

    public List<Player> getPlayersList() {
        return playersList;
    }
    public Map<Player, Integer> getScoreOfEnd() {
        return scoreOfEnd;
    }

    /**
     * Voir {@link #makeAllPlayersSelectRole(List)}
     *
     */
    public List<Player> makeAllPlayersSelectRole() {
        int nombreDeJoueurs = getPlayersList().size();
        return makeAllPlayersSelectRole(generateAvailableRoles(nombreDeJoueurs));
    }



    public CardDeck getCardDeck() { return cardDeck; }

    public void setCardDeck(CardDeck cardDeck) {
        this.cardDeck = cardDeck;
    }

    /**
     * Fait choisir à chaque {@link Player} à partir du maitre du jeu un rôle dans 'availableRoles'. Remplit la variable 'rolesSelectedMap'
     * @param availableRoles les rôles que les joueurs peuvent choisir.
     * @return la liste des joueurs dans leur ordre de choix du rôle
     * @throws IllegalArgumentException Si certains rôles sont EMPTY ou qu'il y a moins de rôles que de joueurs.
     */
    @SuppressWarnings("java:S5413")//unsafe usage of 'List.remove' in a loop, not matching our case because it is safe.
    public List<Player> makeAllPlayersSelectRole(List<Role> availableRoles) throws IllegalArgumentException {
        availableRoles=new ArrayList<>(availableRoles);//used to prevent original list to be modified
        if(availableRoles.size() <= playersList.size()) {
            throw new IllegalArgumentException("availableRoles must be bigger than players number.");
        }

        List<Player> playersInRolePickingOrder=new ArrayList<>();

        playersList.forEach(player -> player.setRole(Role.EMPTY_ROLE));

        playerTreeSet.clear();
        //setRandomMasterOfGame();//pour débuter par un joueur aléatoire
        for(int i=0; i < playersList.size(); i++) {
            int selectedPlayerIndex=(i+masterOfTheGameIndex) % playersList.size();//permet de boucler toutes les valeurs dans l'ordre, à partir du masterOfGame
            Player selectedPlayer = playersList.get(selectedPlayerIndex);

            playersInRolePickingOrder.add(selectedPlayer);

            int selectedRoleIndex=selectedPlayer.getStrategy().selectAndSetRole(availableRoles, playersList);
            if(availableRoles.get(selectedRoleIndex) == Role.EMPTY_ROLE) {
                throw new IllegalArgumentException("Roles can't be EMPTY_ROLE.");
            }

            playerTreeSet.add(selectedPlayer);//on ajoute le joueur actuel à la liste, qui est automatiquement triée de par son type TreeSet
            availableRoles.remove(selectedRoleIndex);//pour que les prochains joueurs ne puissent pas prendre le même rôle
        }
        return playersInRolePickingOrder;
    }

    /**
     * Exécute le tour d'un joueur, lui permettant de piocher des pièces et d'acheter des districts.
     * @param player Le joueur dont c'est le tour.
     * @return Un objet {@link RoundSummary}contenant les informations sur le tour du joueur.
     */
    public RoundSummary playPlayerTurn(Player player) {
        RoundSummary summary=new RoundSummary();
        summary.setRole(player.getRole());
        if(player.isDeadForThisTurn()){
            summary.setHasBeenKilledDuringTurn();
        }
        else {
            player.getStrategy().playTurn(summary, this);
            if (player.getCity().size() >= NUMBER_OF_DISTRICTS_TO_WIN && !isFinished) {
                summary.setHasFinishDuringTurn(true);
                finishGame();
            }

            makeScoreOfPlayer(player, summary);//is read only at the end of game, but is useful for tests.
        }
        return summary;
    }

    /**
     *
     * @return la liste des joueurs dans leur ordre de passage dû à leur rôle (un Assassin joue avant un Condottiere, quel que soit son id de joueur).
     * @throws IllegalStateException si on accède au TreeSet avant que les joueurs aient choisi leur rôle
     */
    public SortedSet<Player> getPlayerTreeSet() throws IllegalStateException {
        if(playerTreeSet.isEmpty()) {
            throw new IllegalStateException("Trying to access player tree set before giving roles to them.");
        }
        else {
            return playerTreeSet;
        }
    }

    /**
     * Définit un master of the game aléatoire, dans l'intervalle du nombre de joueurs.
     */
    public void setRandomMasterOfGame() {
        setMasterOfTheGameIndex(randomGenerator.nextInt(playersList.size()));
    }

    public int getMasterOfTheGameIndex() {
        return masterOfTheGameIndex;
    }

    /**
     * "Finit le jeu" -> dit au {@link GameLogicManager} qu'il ne faudra pas relancer de round à la fin de celui en cours.
     * Cela ne coupe pas instantanément la partie: les joueurs n'ayant pas encore joué leur round à ce tour
     * vont encore jouer.
     */
    public void finishGame() {
        isFinished=true;
    }

    /**
     *
     * @return true si la partie est finie, false sinon. Voir {@link GameLogicManager#finishGame()}
     */
    public boolean isFinished() {
        return isFinished;
    }

    /**
     *
     * @param index l'index du joueur qui est le "maitre du jeu", et qui est donc le premier à piocher
     */
    public void setMasterOfTheGameIndex(int index) {
        if(0<=index && index<playersList.size()) {
            masterOfTheGameIndex=index;
        }
    }

    /**
     * Voir {@link #generateAvailableRoles(int, List)}
     */
    public List<Role> generateAvailableRoles(int nombreDeJoueurs) {
        ArrayList<Role> availableRoles = new ArrayList<>(Arrays.asList(Role.values()));
        availableRoles.remove(0);//removes 'EMPTY_ROLE'
        return generateAvailableRoles(nombreDeJoueurs,availableRoles);
    }

    /**
     * Défini à partir du nombre de joueurs n roles aléatoirement
     * @param nombreDeJoueur indique le nombre de joueurs
     * @param availableRoles tous les roles disponibles
     * @throws IllegalArgumentException Si certains rôles sont EMPTY ou qu'il y a moins de rôles que de joueurs.
     * @return Une liste de role que le joueur pourra choisir
     */
    public List<Role> generateAvailableRoles(int nombreDeJoueur, List<Role> availableRoles) throws IllegalArgumentException{
        if(nombreDeJoueur < 2 || nombreDeJoueur > 7){
            throw new IllegalArgumentException();
        }
        List<Role> newAvailableRoles = new ArrayList<>();
        availableRoles=new ArrayList<>(availableRoles);//to prevent original list to be modified
        for (int i = 0; i <= nombreDeJoueur; i++) {
            int randomIndex = randomGenerator.nextInt(availableRoles.size());
            Role randomRole = availableRoles.get(randomIndex);
            availableRoles.remove(randomRole);
            newAvailableRoles.add(randomRole);
        }
        return newAvailableRoles;
    }

    /**
     * Makes score of given player, and adds it to score list.
     * @param player joueur sur lequel appliquer la méthode
     * @param summary summary
     * @return player's score
     */
    @SuppressWarnings("java:S3655")//false positive for checking for Optional.isPresent()
    public int makeScoreOfPlayer(Player player, RoundSummary summary){
        String coursDesMiraclesName="Cour des miracles";
        int score=player.getTotalCityPrice();

        if(player.getDistrictInCity("Université").isPresent()) {
            score+=2;//the card originally costs 6, but its value is 8 for score counting (source: règle du jeu)
        }
        if(player.getDistrictInCity("Dracoport").isPresent()) {
            score+=2;//the card originally costs 6, but its value is 8 for score counting (source: règle du jeu)
        }



        if(!summary.hasFinishDuringTurn() && player.getCity().size() == NUMBER_OF_DISTRICTS_TO_WIN){
            score+=2;
        }
        if(summary.hasFinishDuringTurn()){
            score+=4;
        }

        if(player.hasAllColorsInCity()){
            score+=3;
        }
        else if(player.getNumberOfColorsInCity() == 4 && player.getDistrictInCity(coursDesMiraclesName).isPresent() && !summary.containsCourDesMiracles()) {//il lui en manque une
            player.removeDistrictFromCity(player.getDistrictInCity(coursDesMiraclesName).get());//remove it from colorMap, to check if there is another purple card.
            Map<Color, Boolean> colorMap=player.getColorsContainedInCityMap();
            if(Boolean.TRUE.equals(colorMap.get(Color.PURPLE))) {//s'il y a une autre carte violette que la cour des miracles, cela veut dire qu'on peut
                // remplacer la couleur manquante grâce au pouvoir de la cour des miracles
                score+=3;
            }

            player.addDistrictToCity(new District(coursDesMiraclesName, 2, Color.PURPLE));
        }


        scoreOfEnd.put(player,score);
        return score;
    }

    /**
     *
     * @return empty si il y a égalité, le joueur gagnant sinon
     */
    public Optional<Player> whoIsTheWinner(){
        int higherScore=0;
        boolean onePlayerHasTheSameScore=false;
        Player winner=playerTreeSet.last();//en cas d'égalité, le joueur qui gagne est celui qui a le plus haut rôle au dernier tour
        for (Map.Entry<Player, Integer> entry : scoreOfEnd.entrySet()) {
            if(higherScore<entry.getValue()){
                higherScore=entry.getValue();
                winner=entry.getKey();
                onePlayerHasTheSameScore=false;
            }
            else if (higherScore==entry.getValue()) {
                winner=entry.getKey();
                onePlayerHasTheSameScore=true;
            }
        }
        return (onePlayerHasTheSameScore) ? Optional.empty() : Optional.of(winner);
    }

    /**
     * Rend tous les joueurs vivants (qu'ils aient été tué précédemment ou non).
     */
    public void resuscitateAllPlayers() {
        for(Player player: playersList) {
            player.resuscitate();
        }
    }

    public void setScoreOfEnd(Map<Player, Integer> scores) {
        scoreOfEnd=scores;
    }



    public boolean shouldWriteInCsv() { return shouldWriteInCsv; }

    public void setShouldWriteInCsv(boolean shouldWriteInCsv) {
        this.shouldWriteInCsv = shouldWriteInCsv;
    }
}

