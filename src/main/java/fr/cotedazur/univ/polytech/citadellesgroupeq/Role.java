package fr.cotedazur.univ.polytech.citadellesgroupeq;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameManager;
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
        public void power (GameManager g, Player assassin, RoundSummary summary) {
            super.power(g, assassin, summary);
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
        public void power(GameManager g,Player voleur,RoundSummary summary) {
            //TODO
            super.power(g, voleur, summary);
        }
    },
    MAGICIEN(Color.GRAY) {
        @Override
        public void power(GameManager g,Player magicien,RoundSummary summary) {
            //TODO
            super.power(g, magicien, summary);
        }
    },
    ROI (Color.YELLOW) {
        @Override
        public void power(GameManager g,Player roi,RoundSummary summary) {
            //TODO
            super.power(g, roi, summary);
        }
    },
    EVEQUE(Color.BLUE) {
        @Override
        public void power(GameManager g,Player eveque,RoundSummary summary) {
            //TODO
            super.power(g, eveque, summary);
        }
    },
    MARCHAND (Color.GREEN) {
        @Override
        public void power(GameManager g,Player marchand,RoundSummary summary) {
            //TODO
            super.power(g, marchand, summary);
        }
    },
    ARCHITECTE (Color.GRAY) {
        @Override
        public void power(GameManager g,Player architecte,RoundSummary summary) {
            //TODO
            super.power(g, architecte, summary);
        }
    },
    CONDOTTIERE (Color.RED) {
        @Override
        public void power(GameManager g,Player condottiere,RoundSummary summary) {
            //TODO
            super.power(g, condottiere, summary);
        }
    };


    /**
     * Méthode pour utiliser le pouvoir du rôle. PAS ENCORE IMPLEMENTE
     */
    public void power(GameManager g,Player player,RoundSummary summary) {
        summary.setHasUsedPower();
    }

    private Color color;

    private Role(Color color) {
        this.color=color;
    }
    public Color getColor() {
        return this.color;
    }

}
