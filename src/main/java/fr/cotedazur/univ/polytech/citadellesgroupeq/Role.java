package fr.cotedazur.univ.polytech.citadellesgroupeq;

/**
 * Enum that represents roles. Each enum overrides the "power" method. You can use ".name()" and ".ordinal()" to get their number
 * and string representations.
 */
public enum Role {
    EMPTY_ROLE,
    ASSASSIN {
        @Override
        public void power () {
            //TODO create power
        }
    },
    VOLEUR {
        @Override
        public void power() {
            //TODO
        }
    },
    MAGICIEN {
        @Override
        public void power() {
            //TODO
        }
    },
    ROI {
        @Override
        public void power() {
            //TODO
        }
    },
    EVEQUE {
        @Override
        public void power() {
            //TODO
        }
    },
    MARCHAND {
        @Override
        public void power() {
            //TODO
        }
    },
    ARCHITECTE {
        @Override
        public void power() {
            //TODO
        }
    },
    CONDOTTIERE {
        @Override
        public void power() {
            //TODO
        }
    };


    /**
     * Méthode pour utiliser le pouvoir du rôle. PAS ENCORE IMPLEMENTE
     */
    public void power() { }
}
