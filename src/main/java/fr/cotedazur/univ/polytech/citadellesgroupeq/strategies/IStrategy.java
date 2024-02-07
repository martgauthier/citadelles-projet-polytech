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
     * L'Optional est empty si le condottiere ne veut/ne peut rien détruire.
     * @param players liste des joueurs.
     * @return un optional rempli avec un seul couple idjoueur/district. Optional est empty si le condottiere ne veut/ne peut rien détruire
     */
    Optional<AbstractMap.SimpleEntry<Integer, District>> selectDistrictToDestroyAsCondottiere(List<Player> players);

    /**
     *
     * @return un tableau avec les index dans la main du joueur des cartes qu'il veut échanger
     */
    int[] selectCardsToExchangeWithPileAsMagicien();
    void playTurn(RoundSummary summary, GameLogicManager game);

    /**
     *
     * @return true s'il échange avec un joueur, false s'il veut échanger certaines de ses cartes avec des cartes de la pile (d'après règle du jeu)
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
     * @param playerList liste des joueurs
     * @return l'id dans la liste fournie du rôle sélectionné
     */
    int selectAndSetRole(List<Role> availableRoles, List<Player> playerList);

    /**
     *
     * @param availableRoles rôles disponibles à voler
     * @param unstealableRoles rôles involables (assassin et lui-même)
     * @return un rôle choisi dans la liste availableRoles, rien si il ne veut pas voler
     */
    Optional<Role> selectRoleToSteal(List<Role> availableRoles, List<Role> unstealableRoles);

    /**
     * Choisi un rôle à Assasiner
     * @param availableRoles les rôles disponibles
     * @return un rôle
     */
    Role selectRoleToKillAsAssassin(List<Role> availableRoles);

    Player getPlayer();

    String getStrategyName();

    /**
     *
     * @return true si le joueur veut utiliser le pouvoir de la merveille "Manufacture"
     */
    boolean wantsToUseManufacturePower();

    /**
     * Choix de racheter ou non quand on a le pouvoir du cimetière
     */
    Optional<District> chooseToUseCimetierePower(District destroyedDistrict);
}
