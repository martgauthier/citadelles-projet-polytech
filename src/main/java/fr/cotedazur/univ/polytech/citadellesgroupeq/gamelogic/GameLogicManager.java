package fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic;

import fr.cotedazur.univ.polytech.citadellesgroupeq.players.RandomPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.AlwaysSpendPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.ColorPlayer;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.RealEstatePlayer;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * La classe GameManager gère le déroulement du jeu Citadelles. Elle initialise le jeu, fait choisir des rôles aux joueurs,
 * exécute les tours des joueurs, et vérifie si le jeu est terminé.
 */
public class GameLogicManager {
    private final Random randomGenerator;

    /**
     * index du joueur qui commence à tirer (le maitre du jeu, ou le roi au tour d'avant)
     */
    private int masterOfTheGameIndex;

    /**
     * La liste non triée des joueurs
     */
    private final List<Player> playersList;

    public static final int NUMBER_OF_DISTRICTS_TO_WIN =8;

    /**
     * True quand la partie est finie, False quand elle est en cours
     */
    private boolean isFinished;

    /**
     * Contient les joueurs dans leur ordre de passage (en fonction de leur rôle). Les {@link TreeSet} sont automatiquement triés dans l'ordre
     */
    private final SortedSet<Player> playerTreeSet;
    private Map<Player, Integer> ScoreOfEnd=new HashMap<>();


    //nécessaire pour régler l'issue #53 sur github: voir la doc de public GameManager()
    public static final List<Class<? extends Player>> DEFAULT_PLAYER_CLASS_LIST = Arrays.asList(ColorPlayer.class, RealEstatePlayer.class, AlwaysSpendPlayer.class, RandomPlayer.class);

    public GameLogicManager() {
        this(List.of());//liste de joueurs vide


        //nécessaire pour régler l'issue #53 sur github:
        // il est plus simple d'instancier les joueurs depuis une liste de classes, avec leur constructeur, qu'essayer de les copier en profondeur

        int ids=0;
        for(Class<? extends Player> playerStrategy: DEFAULT_PLAYER_CLASS_LIST) {
            try {
                Constructor<? extends Player> constructor = playerStrategy.getDeclaredConstructor(int.class);
                this.playersList.add(constructor.newInstance(ids++));
            }
            catch(Exception e) {
                throw new RuntimeException("pas de constructeur prenant un int en paramètre trouvé pour la classe " + playerStrategy.getName() + "! Erreur de code");
            }
        }
    }

    public GameLogicManager(List<Player> playersList) {
        randomGenerator=new Random();
        this.playersList=new ArrayList<>(playersList);//pour ne pas modifier le tableau de base
        this.masterOfTheGameIndex=0;
        playerTreeSet=new TreeSet<>();
        isFinished=false;
    }

    public List<Player> getPlayersList() {
        return playersList;
    }
    public Map<Player, Integer> getScoreOfEnd() {
        return ScoreOfEnd;
    }

    /**
     * Voir {@link #makeAllPlayersSelectRole(List)}
     *
     */
    public List<Player> makeAllPlayersSelectRole() {
        int nombreDeJoueurs = getPlayersList().size();
        return makeAllPlayersSelectRole(generateAvailableRoles(nombreDeJoueurs));
    }

    /**
     * Fait choisir à chaque {@link Player} à partir du maitre du jeu un rôle dans `availableRoles`. Remplit la variable 'rolesSelectedMap'
     * @param availableRoles les rôles que les joueurs peuvent choisir.
     * @return la liste des joueurs dans leur ordre de choix du rôle
     * @throws IllegalArgumentException Si certains rôles sont EMPTY ou qu'il y a moins de rôles que de joueurs.
     */
    public List<Player> makeAllPlayersSelectRole(List<Role> availableRoles) throws IllegalArgumentException {
        availableRoles=new ArrayList<>(availableRoles);//used to prevent original list to be modified
        if(availableRoles.size() <= playersList.size()) {
            throw new IllegalArgumentException("availableRoles must be bigger than players number.");
        }

        List<Player> playersInRolePickingOrder=new ArrayList<>();

        playerTreeSet.clear();
        //setRandomMasterOfGame();//pour débuter par un joueur aléatoire
        for(int i=0; i < playersList.size(); i++) {
            int selectedPlayerIndex=(i+masterOfTheGameIndex) % playersList.size();//permet de boucler toutes les valeurs dans l'ordre, à partir du masterOfGame
            Player selectedPlayer = playersList.get(selectedPlayerIndex);

            playersInRolePickingOrder.add(selectedPlayer);

            int selectedRoleIndex=selectedPlayer.getStrategy().selectRole(availableRoles);
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
        if(player.isDeadForThisTurn()){
            summary.setHasBeenKilledDuringTurn();
        }
        else {
            player.getStrategy().playPlayerTurn(summary, this);
            if (player.getCity().size() == NUMBER_OF_DISTRICTS_TO_WIN && !isFinished) {
                summary.setHasFinishDuringTurn(true);
                finishGame();
            }

            makeScoreofPlayer(player, summary);//is read only at end of game, but is useful for tests
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

    public void setRandomMasterOfGame() {
        setMasterOfTheGameIndex(randomGenerator.nextInt(playersList.size()));
    }

    public int getMasterOfTheGameIndex() {
        return masterOfTheGameIndex;
    }

    public void finishGame() {
        isFinished=true;
    }

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
     * @return
     */
    public List<Role> generateAvailableRoles(int nombreDeJoueurs) {
        ArrayList<Role> availableRoles = new ArrayList<>(Arrays.asList(Role.values()));
        availableRoles.remove(0);//removes EMPTY_ROLE
        return generateAvailableRoles(nombreDeJoueurs,availableRoles);
    }

    /**
     * Défini à partir du nombre de joueur n roles aléatoirement
     * @param nombreDeJoueur
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
    public void makeScoreofPlayer(Player player,RoundSummary summary){
        int score=player.getTotalCityPrice();
        if(!summary.hasFinishDuringTurn() && player.getCity().size() == NUMBER_OF_DISTRICTS_TO_WIN){
            score+=2;
        }
        if(summary.hasFinishDuringTurn()){
            score+=4;
        }
        if(player.hasAllColorsInCity()){
            score+=3;
        }
        ScoreOfEnd.put(player,score);
    }
    public Player whoIsTheWinner(){
        int higherScore=0;
        Player winner=playerTreeSet.last();//en cas d'égalité, le joueur qui gagne est celui qui a le plus haut rôle au dernier tour
        for (Map.Entry<Player, Integer> entry : ScoreOfEnd.entrySet()) {
            if(higherScore<entry.getValue()){
                higherScore=entry.getValue();
                winner=entry.getKey();
            }
        }
        return winner;
    }

    public void resuscitateAllPlayers() {
        for(Player player: playersList) {
            player.rescucitate();
        }
    }
}

