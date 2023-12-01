package fr.cotedazur.univ.polytech.citadellesgroupeq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameManager {
    private final Random randomGenerator;
    private int masterOfTheGameIndex;
    private final List<Player> playersList;
    private static final List<Player> DEFAULT_PLAYER_LIST= Arrays.asList(new Player(), new Player());

    public GameManager() {
        this(DEFAULT_PLAYER_LIST);
    }

    public GameManager(List<Player> playersList) {
        randomGenerator=new Random();
        this.playersList=playersList;
        this.masterOfTheGameIndex=0;
    }

    public List<Player> getPlayersList() {
        return playersList;
    }

    public String makeAllPlayersSelectRole() {
        ArrayList<Role> availableRoles = new ArrayList<>(Arrays.asList(Role.values()));
        availableRoles.remove(0);//removes EMPTY_ROLE
        return makeAllPlayersSelectRole(availableRoles);
    }

    public String makeAllPlayersSelectRole(List<Role> availableRoles) throws IllegalArgumentException {
        if(availableRoles.size() <= playersList.size()) {
            throw new IllegalArgumentException("availableRoles must be bigger than players number.");
        }
        StringBuilder output=new StringBuilder("Sélection des rôles pour chaque joueur: \n");

        setRandomMasterOfGame();//pour débuter par un joueur aléatoire
        for(int i=0; i < playersList.size(); i++) {
            int selectedPlayerIndex=(i+masterOfTheGameIndex) % playersList.size();//permet de boucler toutes les valeurs dans l'ordre, à partir du masterOfGame
            Player selectedPlayer = playersList.get(selectedPlayerIndex);
            output.append("Joueur ").append(selectedPlayerIndex + 1).append(" sélectionne son rôle: ");

            int selectedRoleIndex=selectedPlayer.selectRole(availableRoles);
            output.append(availableRoles.get(selectedRoleIndex).name()).append('\n');
            if(availableRoles.get(selectedRoleIndex) == Role.EMPTY_ROLE) {
                throw new IllegalArgumentException("Roles can't be EMPTY_ROLE.");
            }
            availableRoles.remove(selectedRoleIndex);//pour que les prochains joueurs ne puissent pas prendre le même rôle
        }

        return output.toString();
    }

    public void setRandomMasterOfGame() {
        setMasterOfTheGameIndex(randomGenerator.nextInt(playersList.size()));
    }

    public int getMasterOfTheGameIndex() {
        return masterOfTheGameIndex;
    }

    public void setMasterOfTheGameIndex(int index) {
        if(0<=index && index<playersList.size()) {
            masterOfTheGameIndex=index;
        }
    }
}
