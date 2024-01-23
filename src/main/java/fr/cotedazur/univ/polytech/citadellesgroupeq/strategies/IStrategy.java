package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;

import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;

public interface IStrategy {

    /**
     * Le choix du district à détruire si le rôle est condottiere. Ne contient qu'UN couple IdPlayer/District au maximum.
     * Le district choisi doit coûter au maximum cashdujoueur+1.
     * L'Optional est empty si le condottiere ne veut/peut rien détruire.
     * @param players liste des joueurs.
     * @return un optional rempli avec un seul couple idjoueur/district. Optional est empty si le condottiere ne veut/peut rien détruire
     */
    Optional<AbstractMap.SimpleEntry<Integer, District>> selectDistrictToDestroyAsCondottiere(List<Player> players);
    int[] selectCardsToExchangeWithPileAsMagicien();
    void playTurn(RoundSummary summary, GameLogicManager game);

    /**
     *
     * @return true si il échange avec un joueur, false si il veut échanger certaines de ses cartes avec des cartes de la pile (d'après règle du jeu)
     */
    boolean choosesToExchangeCardWithPlayer();

    Player selectPlayerToExchangeCardsWithAsMagicien(List<Player> playerList);

    /**
     *
     * @return la citadelle que le joueur a choisi d'acheter (par défaut, la moins chère). Si le joueur n'est pas en mesure d'acheter une citadelle, l'Optional est empty
     */
    Optional<District> getChoosenDistrictToBuy();

    /**
     * Sélectionne un rôle aléatoirement dans la liste availableRoles pour le joueur
     *
     * @param availableRoles les rôles disponibles
     * @param playerList
     * @return l'id dans la liste fournie du rôle sélectionné
     */
    int selectAndSetRole(List<Role> availableRoles, List<Player> playerList);

    Optional<Role> selectRoleToSteal(List<Role> availableRoles, List<Role> unstealableRoles);

    /**
     * Choisi un rôle à Assasiner
     * @param availableRoles les rôles disponible
     * @return un rôle
     */
    Role selectRoleToKillAsAssassin(List<Role> availableRoles);

    Player getPlayer();

    String getStrategyName();

    boolean wantsToUseManufacturePower();
}
