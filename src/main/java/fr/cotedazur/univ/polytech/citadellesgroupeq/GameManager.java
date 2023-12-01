package fr.cotedazur.univ.polytech.citadellesgroupeq;

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
    }

    public List<Player> getPlayersList() {
        return playersList;
    }

    /**
     * Voir {@link #makeAllPlayersSelectRole(List)}
     * @return
     */
    public String makeAllPlayersSelectRole() {
        ArrayList<Role> availableRoles = new ArrayList<>(Arrays.asList(Role.values()));
        availableRoles.remove(0);//removes EMPTY_ROLE
        return makeAllPlayersSelectRole(availableRoles);
    }

    /**
     * Fait choisir à chaque {@link Player} à partir du maitre du jeu un rôle dans `availableRoles`
     * @param availableRoles les rôles que les joueurs peuvent choisir
     * @return le texte de sortie à afficher
     * @throws IllegalArgumentException Si certains rôles sont EMPTY ou qu'il y a moins de rôles que de joueurs.
     */
    public String makeAllPlayersSelectRole(List<Role> availableRoles) throws IllegalArgumentException {
        playerTreeSet.clear();
        if(availableRoles.size() <= playersList.size()) {
            throw new IllegalArgumentException("availableRoles must be bigger than players number.");
        }
        StringBuilder output=new StringBuilder("Liste des rôles disponibles: \n");
        for(Role role: availableRoles) {
            output.append("-").append(role.name()).append("   ");
        }

        output.append("\nSélection des rôles pour chaque joueur: \n");

        setRandomMasterOfGame();//pour débuter par un joueur aléatoire
        for(int i=0; i < playersList.size(); i++) {
            int selectedPlayerIndex=(i+masterOfTheGameIndex) % playersList.size();//permet de boucler toutes les valeurs dans l'ordre, à partir du masterOfGame
            Player selectedPlayer = playersList.get(selectedPlayerIndex);
            output.append("Joueur ").append(selectedPlayer.getId()).append(" sélectionne son rôle: ");

            int selectedRoleIndex=selectedPlayer.selectRole(availableRoles);
            output.append(availableRoles.get(selectedRoleIndex).name()).append('\n');
            if(availableRoles.get(selectedRoleIndex) == Role.EMPTY_ROLE) {
                throw new IllegalArgumentException("Roles can't be EMPTY_ROLE.");
            }
            playerTreeSet.add(selectedPlayer);//on ajoute le joueur actuel à la liste, qui est automatiquement triée de par son type TreeSet
            availableRoles.remove(selectedRoleIndex);//pour que les prochains joueurs ne puissent pas prendre le même rôle
        }

        return output.toString();
    }

    public String playPlayerTurn(Player player) {
        StringBuilder output = new StringBuilder("Joueur " + player.getId() + " joue: ");
        output.append(player.toString());

        player.draw2Coins();
        output.append("Il tire 2 pièces, ce qui l'amène à: ").append(player.getCash()).append(" pièces.\n");

        List<Citadel> buyableCards=player.getBuyableCards();
        if(buyableCards.isEmpty()) {
            output.append("Il n'a que ").append(player.getCash()).append("pièces, il ne peut donc rien acheter.\n");
        }
        else {//le joueur gagne car il a assez pour acheter une de ses citadelles
            output.append("Avec ses pièces, il a assez pour acheter: \n");
            for(Citadel card : buyableCards) {
                output.append("\t").append(card.getName()).append(" : ").append(card.getCost()).append("\n");
            }
            output.append("GAGNANT : JOUEUR ").append(player.getId()).append("\n");
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
}
