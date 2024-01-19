package fr.cotedazur.univ.polytech.citadellesgroupeq.strategies;

import fr.cotedazur.univ.polytech.citadellesgroupeq.District;
import fr.cotedazur.univ.polytech.citadellesgroupeq.Role;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.List;
import java.util.Optional;

/**
 * This strategy:
 * -Tries to pick role Voleur if available
 * -pick coins at start of round (no cards)
 * -Doesn't buy anything
 */
public class AimForMoneyStrategy extends DefaultStrategy {
    public AimForMoneyStrategy(Player player) {
        super(player);
        strategyName="[AimForMoney Strategy]";
    }

    @Override
    public int selectAndSetRole(List<Role> availableRoles, List<Player> playerList) {
        int voleurIndex=availableRoles.indexOf(Role.VOLEUR);
        if(voleurIndex!=-1) {
            player.setRole(availableRoles.get(voleurIndex));
            return voleurIndex;
        }
        else {
            player.setRole(availableRoles.get(0));
            return 0;//first role available otherwise
        }
    }

    /**
     * Tries to steal architecte, first role stealable if architecte is not present
     * @param availableRoles
     * @param unstealableRoles
     * @return
     */
    @Override
    public Optional<Role> selectRoleToSteal(List<Role> availableRoles, List<Role> unstealableRoles) {
        int architecteIndex= availableRoles.indexOf(Role.ARCHITECTE);
        if(architecteIndex!=-1) return Optional.of(availableRoles.get(architecteIndex));
        else {
            return (!availableRoles.isEmpty()) ? Optional.of(availableRoles.get(0)) : Optional.empty();
        }
    }

    @Override
    public Optional<District> getChoosenDistrictToBuy() {
        return Optional.empty();//to save money
    }

    @Override
    public void playTurn(RoundSummary summary, GameLogicManager game) {
        if(player.getCash() < 6) {
            player.getCoinsFromColorCards(summary);
            player.getRole().power(game, player, summary);

            player.draw2Coins(summary);

            player.buyDistrictsDuringTurn(summary);
        }
        else {
            player.setStrategy(new DefaultStrategy(player));
            player.playTurn(summary, game);
        }
    }
}
