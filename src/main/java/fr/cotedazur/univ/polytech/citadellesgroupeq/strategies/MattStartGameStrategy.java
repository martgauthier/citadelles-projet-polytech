package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;

import fr.cotedazur.univ.polytech.citadellesgroupeq.PowerManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.List;

public class MattStartGameStrategy extends DefaultStrategy {
    public MattStartGameStrategy(Player player) {
        super(player);
    }

    /**
     * Choisit par ordre de priorité:
     * 1. Marchand 2. Architecte 3. Assassin 4. Autre rôle disponible
     * @param availableRoles les rôles disponibles
     * @param playerList
     * @return
     */
    @Override
    public int selectAndSetRole(List<Role> availableRoles, List<Player> playerList) {
        if(availableRoles.contains(Role.MARCHAND)) {
            player.setRole(Role.MARCHAND);
            return availableRoles.indexOf(Role.MARCHAND);
        }
        else if(availableRoles.contains(Role.ARCHITECTE)) {
            player.setRole(Role.ARCHITECTE);
            return availableRoles.indexOf(Role.ARCHITECTE);
        }
        else if(availableRoles.contains(Role.ASSASSIN)) {
            player.setRole(Role.ASSASSIN);
            return availableRoles.indexOf(Role.ASSASSIN);
        }
        else {
            player.setRole(availableRoles.get(0));
            return 0;
        }
    }

    @Override
    public void playTurn(RoundSummary summary, GameLogicManager game) {
        player.getCoinsFromColorCards(summary);

        player.getRole().power(game, player, summary);//it is no duplicate, as another Player logic could decide not to use its power

        if(player.getCardsInHand().size() < 4) {
            if(!player.haveObservatoryInCity()){
                player.pickCard(summary);
            }
        }
        else {
            player.draw2Coins(summary);
        }


        PowerManager powerManager = new PowerManager(game);
        powerManager.applyCityPowers(player, summary);

        player.buyDistrictsDuringTurn(summary);
    }
}
