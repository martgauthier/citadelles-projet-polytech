package fr.cotedazur.univ.polytech.citadellesgroupeq;

import java.util.*;

/**
 * La classe GameManager gère le déroulement du jeu Citadelles. Elle initialise le jeu, fait choisir des rôles aux joueurs,
 * exécute les tours des joueurs, et vérifie si le jeu est terminé.
 */
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

    public static final int NUMBER_OF_CITADELS_TO_WIN=8;

    private Map<Player, Role> rolesSelectedMap;

    /**
     * True quand la partie est finie, False quand elle est en cours
     */
    private boolean isFinished;

    /**
     * Contient les joueurs dans leur ordre de passage (en fonction de leur rôle). Les {@link TreeSet} sont automatiquement triés dans l'ordre
     */
    private final SortedSet<Player> playerTreeSet;
  
    public static List<Player> DEFAULT_PLAYER_LIST= Arrays.asList(new Player(0), new Player(1),new Player(2),new Player(3));

    public GameManager() {
        this(DEFAULT_PLAYER_LIST);
        DEFAULT_PLAYER_LIST=Arrays.asList(new Player(0), new Player(1),new Player(2),new Player(3));//to prevent game from modifying default players values
    }

    public GameManager(List<Player> playersList) {
        randomGenerator=new Random();
        this.playersList=new ArrayList<>(DEFAULT_PLAYER_LIST);//pour ne pas modifier le tableau de base
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
        availableRoles=new ArrayList<>(availableRoles);//used to prevent original list to be modified
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

    /**
     * Exécute le tour d'un joueur, lui permettant de piocher des pièces et d'acheter des citadelles.
     * @param player Le joueur dont c'est le tour.
     * @return Un objet {@link RoundSummary}contenant les informations sur le tour du joueur.
     */
    public RoundSummary playPlayerTurn(Player player) {
        RoundSummary summary=new RoundSummary();
        if(player.isAssassinated()){
            summary.setHasKilledDuringTurn();
            return summary;
        }
        player.getRole().power(this,player,summary);
        summary.setUsePower();

        for(Citadel cartePosee: player.getCity()) {
            if(cartePosee.getColor() == player.getRole().getColor() && player.getRole().getColor()!=Color.GRAY) {
                player.addCoins(1);
                summary.addCoinsWonByColorCards(1);
            }
        }

        player.dealCardsOrCash(summary);

        Optional<Citadel> boughtCardOptional=player.getChoosenCitadelToBuy();
        if(boughtCardOptional.isPresent()) {
            summary.addBoughtCitadel(boughtCardOptional.get());
            player.removeCoins(boughtCardOptional.get().getCost());
            player.addCitadelToCity(boughtCardOptional.get());
            player.removeCardFromHand(boughtCardOptional.get());
        }

        if(player.getCity().size() == NUMBER_OF_CITADELS_TO_WIN) {
            summary.setHasWonDuringTurn(true);
            finishGame();
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
}

