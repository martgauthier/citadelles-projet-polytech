package fr.cotedazur.univ.polytech.citadellesgroupeq;

import java.io.IOException;
import java.util.*;

public class GameManager {
    private final Random randomGenerator;

    /**
     * index du joueur qui commence à tirer (le maitre du jeu, ou le roi au tour d'avant)
     */
    private int masterOfTheGameIndex;

    /**
     * La liste non triée des joueurs
     */
    private final List<Player> playersList;

    private Map<Player, Role> rolesSelectedMap;

    /**
     * True quand la partie est finie, False quand elle est en cours
     */
    private boolean isFinished;

    /**
     * Contient les joueurs dans leur ordre de passage (en fonction de leur rôle). Les {@link TreeSet} sont automatiquement triés dans l'ordre
     */

    private final SortedSet<Player> playerTreeSet;
    private static final List<Player> DEFAULT_PLAYER_LIST= Arrays.asList(new Player(0), new Player(1));

    public GameManager() {
        this(DEFAULT_PLAYER_LIST);
    }

    public GameManager(List<Player> playersList) {
        randomGenerator=new Random();
        this.playersList=playersList;
        this.masterOfTheGameIndex=0;
        playerTreeSet=new TreeSet<>();
        isFinished=false;
        rolesSelectedMap=new HashMap<>();
    }

    public List<Player> getPlayersList() {
        return playersList;
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
        if(availableRoles.size() <= playersList.size()) {
            throw new IllegalArgumentException("availableRoles must be bigger than players number.");
        }

        List<Player> playersInRolePickingOrder=new ArrayList<>();

        playerTreeSet.clear();
        rolesSelectedMap.clear();
        setRandomMasterOfGame();//pour débuter par un joueur aléatoire
        for(int i=0; i < playersList.size(); i++) {
            int selectedPlayerIndex=(i+masterOfTheGameIndex) % playersList.size();//permet de boucler toutes les valeurs dans l'ordre, à partir du masterOfGame
            Player selectedPlayer = playersList.get(selectedPlayerIndex);

            playersInRolePickingOrder.add(selectedPlayer);

            int selectedRoleIndex=selectedPlayer.selectRole(availableRoles);
            if(availableRoles.get(selectedRoleIndex) == Role.EMPTY_ROLE) {
                throw new IllegalArgumentException("Roles can't be EMPTY_ROLE.");
            }

            rolesSelectedMap.put(selectedPlayer, availableRoles.get(selectedRoleIndex));
            playerTreeSet.add(selectedPlayer);//on ajoute le joueur actuel à la liste, qui est automatiquement triée de par son type TreeSet
            availableRoles.remove(selectedRoleIndex);//pour que les prochains joueurs ne puissent pas prendre le même rôle
        }
        return playersInRolePickingOrder;
    }

    public RoundSummary playPlayerTurn(Player player) {
        RoundSummary summary=new RoundSummary();

        player.draw2Coins();
        summary.addCoins(2);

        List<Citadel> buyableCards=player.getBuyableCards();
        if(!buyableCards.isEmpty()) {//le joueur gagne car il a assez pour acheter une de ses citadelles
            summary.setBoughtCitadels(buyableCards);
            finishGame();
        }
        return summary;
    }

    public String playPlayerTurnV2(Player player) {
        StringBuilder output = new StringBuilder("Joueur " + player.getId() + " joue: ");
        output.append(player.toString());

        player.dealCardsOrCash();
        output.append("Il possède maintenant ").append(player.getCash()).append(" pièces.\n");
        output.append("Et ").append(player.getDescriptionOfCards());

        List<Citadel> buyableCards=player.getBuyableCards();
        if(buyableCards.isEmpty()) {
            output.append("Il n'a que ").append(player.getCash()).append(" pièces, il ne peut donc rien acheter.\n");
        }
        else {//le joueur gagne car il a assez pour acheter une de ses citadelles
            output.append("Avec ses pièces, il a assez pour acheter: \n");
            for(Citadel card : buyableCards) {
                output.append("\t").append(card.getName()).append(" : ").append(card.getCost()).append("\n");
            }
            output.append("GAGNANT : JOUEUR ").append(player.getId());
            finishGame();
        }
        return output.toString();
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
        for (int i = 0; i <= nombreDeJoueur; i++) {
            int randomIndex = randomGenerator.nextInt(availableRoles.size());
            Role randomRole = availableRoles.get(randomIndex);
            availableRoles.remove(randomRole);
            newAvailableRoles.add(randomRole);
        }
        return newAvailableRoles;
    }
}

