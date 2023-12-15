package fr.cotedazur.univ.polytech.citadellesgroupeq;

import java.util.List;
import java.util.Random;

/**
 * Enum that represents roles. Each enum overrides the "power" method. You can use ".name()" and ".ordinal()" to get their number
 * and string representations.
 */
public enum Role {
    EMPTY_ROLE(Color.GRAY),
    ASSASSIN(Color.GRAY) {
        @Override
        public void power (GameManager g,Player assassin,RoundSummary summary) {
            Role assassinatedRole=assassin.selectAssassinatedRole(g.generateAvailableRoles(g.getPlayersList().size()));
            for(Player player : g.getPlayersList()){
                if(player.getRole().equals(assassinatedRole)){
                    player.Assassinate();
                    break;
                }
            }
        }
    },
    VOLEUR(Color.GRAY) {
        @Override
        public void power(GameManager g,Player assassin,RoundSummary summary) {
            //TODO
        }
    },
    MAGICIEN(Color.GRAY) {
        @Override
        public void power(GameManager g,Player assassin,RoundSummary summary) {
            //TODO
        }
    },
    ROI (Color.YELLOW) {
        @Override
        public void power(GameManager g,Player assassin,RoundSummary summary) {
            //TODO
        }
    },
    EVEQUE(Color.BLUE) {
        @Override
        public void power(GameManager g,Player assassin,RoundSummary summary) {
            //TODO
        }
    },
    MARCHAND (Color.GREEN) {
        @Override
        public void power(GameManager g,Player assassin,RoundSummary summary) {
            //TODO
        }
    },
    ARCHITECTE (Color.GRAY) {
        @Override
        public void power(GameManager g,Player assassin,RoundSummary summary) {
            //TODO
        }
    },
    CONDOTTIERE (Color.RED) {
        @Override
        public void power(GameManager g,Player assassin,RoundSummary summary) {
            //TODO
        }
    };


    /**
     * Méthode pour utiliser le pouvoir du rôle. PAS ENCORE IMPLEMENTE
     */
    public void power(GameManager g,Player assassin,RoundSummary summary) {
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
