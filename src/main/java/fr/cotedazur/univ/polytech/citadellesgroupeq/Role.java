package fr.cotedazur.univ.polytech.citadellesgroupeq;

/**
 * Enum that represents roles. Each enum overrides the "power" method. You can use ".name()" and ".ordinal()" to get their number
 * and string representations.
 */
public enum Role {
    EMPTY_ROLE(Color.GRAY),
    ASSASSIN(Color.GRAY) {
        @Override
        public void power () {
            //TODO create power
        }
    },
    VOLEUR(Color.GRAY) {
        @Override
        public void power() {
            //TODO
        }
    },
    MAGICIEN(Color.GRAY) {
        @Override
        public void power() {
            //TODO
        }
    },
    ROI (Color.YELLOW) {
        @Override
        public void power() {
            //TODO
        }
    },
    EVEQUE(Color.BLUE) {
        @Override
        public void power() {
            //TODO
        }
    },
    MARCHAND (Color.GREEN) {
        @Override
        public void power() {
            //TODO
        }
    },
    ARCHITECTE (Color.GRAY) {
        @Override
        public void power() {
            //TODO
        }
    },
    CONDOTTIERE (Color.RED) {
        @Override
        public void power() {
            //TODO
        }
    };


    /**
     * Méthode pour utiliser le pouvoir du rôle. PAS ENCORE IMPLEMENTE
     */
    public void power() {
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
