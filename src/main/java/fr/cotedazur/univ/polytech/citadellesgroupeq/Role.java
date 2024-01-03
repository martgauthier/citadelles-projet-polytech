package fr.cotedazur.univ.polytech.citadellesgroupeq;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

/**
 * Enum that represents roles. Each enum overrides the "power" method. You can use ".name()" and ".ordinal()" to get their number
 * and string representations.
 */
public enum Role {
    EMPTY_ROLE(Color.GRAY),
    ASSASSIN(Color.GRAY) {
        @Override
        public void power (GameLogicManager g, Player assassin, RoundSummary summary) {
            Role assassinatedRole=assassin.selectRoleToKillAsAssassin(g.generateAvailableRoles(g.getPlayersList().size()));
            for(Player player : g.getPlayersList()){
                if(player.getRole().equals(assassinatedRole)){
                    player.dieForThisTurn();
                }
            }
        }
    },
    VOLEUR(Color.GRAY) {
        @Override
        public void power(GameLogicManager g, Player voleur, RoundSummary summary) {
            //TODO
        }
    },
    MAGICIEN(Color.GRAY) {
        @Override
        public void power(GameLogicManager g, Player magicien, RoundSummary summary) {
            //TODO
        }
    },
    ROI (Color.YELLOW) {
        @Override
        public void power(GameLogicManager g, Player roi, RoundSummary summary) {
            //TODO
        }
    },
    EVEQUE(Color.BLUE) {
        @Override
        public void power(GameLogicManager g, Player eveque, RoundSummary summary) {
            //TODO
        }
    },
    MARCHAND (Color.GREEN) {
        @Override
        public void power(GameLogicManager g, Player marchand, RoundSummary summary) {
            //TODO
        }
    },
    ARCHITECTE (Color.GRAY) {
        @Override
        public void power(GameLogicManager g, Player architecte, RoundSummary summary) {
            //TODO
        }
    },
    CONDOTTIERE (Color.RED) {
        @Override
        public void power(GameLogicManager g, Player condottiere, RoundSummary summary) {
            //TODO
        }
    };


    /**
     * Méthode pour utiliser le pouvoir du rôle. PAS ENCORE IMPLEMENTE
     */
    public void power(GameLogicManager g, Player player, RoundSummary summary) {
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
