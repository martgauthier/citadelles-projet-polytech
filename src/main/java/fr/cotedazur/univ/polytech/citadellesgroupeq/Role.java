package fr.cotedazur.univ.polytech.citadellesgroupeq;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Enum that represents roles. Each enum overrides the "power" method. You can use ".name()" and ".ordinal()" to get their number
 * and string representations.
 */
public enum Role {
    EMPTY_ROLE(Color.GRAY),
    ASSASSIN(Color.GRAY) {
        @Override
        public void power (GameManager g, Player assassin, RoundSummary summary) {
            Role assassinatedRole=assassin.selectRoleToKillAsAssassin(g.generateAvailableRoles(g.getPlayersList().size()));
            for(Player player : g.getPlayersList()){
                if(player.getRole().equals(assassinatedRole)){
                    player.dieForThisTurn();
                }
            }
            summary.setHasUsedPower();
        }
    },
    VOLEUR(Color.GRAY) {
        @Override
        public void power(GameManager g,Player voleur,RoundSummary summary) {
            List<Role> unstealableRoles = new ArrayList<>(Arrays.asList(ASSASSIN, VOLEUR));
            //On récupère le joueur assassiné ce tour
            for(Player player : g.getPlayersList()){
                if(player.isDeadForThisTurn()){
                    Role assassinatedRole  = player.getRole();
                    unstealableRoles.add(assassinatedRole);
                }
            }
            List<Role> availableRoles = g.generateAvailableRoles(g.getPlayersList().size());

            try {
                Role stealedRole = voleur.selectRoleToSteal(availableRoles, unstealableRoles);

                for (Player player : g.getPlayersList()) {
                    if (player.getRole().equals(stealedRole)) {
                        int totalCash = player.getCash();
                        player.removeCoins(totalCash);
                        voleur.addCoins(totalCash);
                        summary.setDrawnCoins(totalCash);
                        summary.setStealedRole(stealedRole);
                        summary.setHasUsedPower();
                    }
                }
            } catch (Exception e) {}
        }
    },
    MAGICIEN(Color.GRAY) {
        @Override
        public void power(GameManager g,Player magicien,RoundSummary summary) {
            //TODO
        }
    },
    ROI (Color.YELLOW) {
        @Override
        public void power(GameManager g,Player roi,RoundSummary summary) {
            //TODO
        }
    },
    EVEQUE(Color.BLUE) {
        @Override
        public void power(GameManager g,Player eveque,RoundSummary summary) {
            //TODO
        }
    },
    MARCHAND (Color.GREEN) {
        @Override
        public void power(GameManager g,Player marchand,RoundSummary summary) {
            //TODO
        }
    },
    ARCHITECTE (Color.GRAY) {
        @Override
        public void power(GameManager g,Player architecte,RoundSummary summary) {
            //TODO
        }
    },
    CONDOTTIERE (Color.RED) {
        @Override
        public void power(GameManager g,Player condottiere,RoundSummary summary) {
            //TODO
        }
    };


    /**
     * Méthode pour utiliser le pouvoir du rôle. PAS ENCORE IMPLEMENTE
     */
    public void power(GameManager g,Player player,RoundSummary summary) {
        throw new UnsupportedOperationException();
    }

    private Color color;

    private Role(Color color) {
        this.color=color;
    }
    public Color getColor() {
        return this.color;
    }

}
