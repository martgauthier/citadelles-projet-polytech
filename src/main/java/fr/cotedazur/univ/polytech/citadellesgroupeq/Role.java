package fr.cotedazur.univ.polytech.citadellesgroupeq;

/**
 * Enum that represents roles. Each enum overrides the "power" method. You can use ".name()" and ".ordinal()" to get their number
 * and string representations.
 */
public enum Role {
    EMPTY_ROLE(Color.GRAY),
    ASSASSIN(Color.GRAY) {
        @Override
        public void power (GameManager g,Player assassin,RoundSummary summary) {
            Role assassinatedRole=assassin.selectRoleToKillAsAssassin(g.generateAvailableRoles(g.getPlayersList().size()));
            for(Player player : g.getPlayersList()){
                if(player.getRole().equals(assassinatedRole)){
                    player.dieForThisTurn();
                    break;
                }
            }
        }
    },
    VOLEUR(Color.GRAY) {
        @Override
        public void power(GameManager g,Player voleur,RoundSummary summary) {
            //TODO
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
