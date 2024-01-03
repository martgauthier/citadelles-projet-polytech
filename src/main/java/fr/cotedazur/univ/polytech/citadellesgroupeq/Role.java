package fr.cotedazur.univ.polytech.citadellesgroupeq;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;
import org.json.simple.parser.ParseException;

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
            super.power(g, magicien, summary);

            if(magicien.choosesToExchangeCardWithPlayer()) {
                Player selectedPlayerToExchangeWith = magicien.selectPlayerToExchangeCardsWithAsMagicien(g.getPlayersList());
                List<District> districtListCopy=selectedPlayerToExchangeWith.getCardsInHand();
                selectedPlayerToExchangeWith.setCardsInHand(magicien.getCardsInHand());
                magicien.setCardsInHand(districtListCopy);
                //TODO: note that in summary
            }
            else {//wants to draw some cards from the pile
                int[] cardsToExchange=magicien.selectCardsToExchangeWithPileAsMagicien();
                try {
                    DistrictsJSONReader districtsJSONReader = new DistrictsJSONReader();

                    for(int cardIndex: cardsToExchange) {
                        magicien.getCardsInHand().set(cardIndex, districtsJSONReader.getRandomDistrict());
                    }

                    //TODO: note that in summary
                }
                catch(ParseException e) {
                    throw new RuntimeException("Error while parsing district JSON file.");
                }
            }
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
     * Méthode pour utiliser le pouvoir du rôle. Toujours appeler "super.power", car c'est lui qui écrit dans le summary que le pouvoir a été utilisé
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
