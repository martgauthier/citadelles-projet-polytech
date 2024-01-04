package fr.cotedazur.univ.polytech.citadellesgroupeq;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
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
        public void power (GameLogicManager g, Player assassin, RoundSummary summary) {
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
        public void power(GameLogicManager g,Player voleur,RoundSummary summary) {
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
            } catch (Exception ignored) {}
        }
    },
    MAGICIEN(Color.GRAY) {
        @Override
        public void power(GameLogicManager g, Player magicien, RoundSummary summary) {
            super.power(g, magicien, summary);

            if(magicien.choosesToExchangeCardWithPlayer()) {
                Player selectedPlayerToExchangeWith = magicien.selectPlayerToExchangeCardsWithAsMagicien(g.getPlayersList());
                List<District> districtListCopy=selectedPlayerToExchangeWith.getCardsInHand();
                selectedPlayerToExchangeWith.setCardsInHand(magicien.getCardsInHand());
                magicien.setCardsInHand(districtListCopy);
                summary.setExchangedCardsPlayerId(selectedPlayerToExchangeWith.getId());
            }
            else {//wants to draw some cards from the pile
                int[] cardsToExchange=magicien.selectCardsToExchangeWithPileAsMagicien();

                DistrictsJSONReader districtsJSONReader = new DistrictsJSONReader();

                for(int cardIndex: cardsToExchange) {
                    magicien.setCardInHand(cardIndex, districtsJSONReader.getRandomDistrict());
                }

                summary.setHasExchangedCardsWithPileAsMagician(true);
                summary.setExchangedCardsWithPileIndex(cardsToExchange);
            }
        }
    },
    ROI (Color.YELLOW) {
        @Override
        public void power(GameLogicManager g, Player roi, RoundSummary summary) {
            //TODO
            super.power(g, roi, summary);
        }
    },
    EVEQUE(Color.BLUE) {
        @Override
        public void power(GameLogicManager g, Player eveque, RoundSummary summary) {
            //TODO
            super.power(g, eveque, summary);
        }
    },
    MARCHAND (Color.GREEN) {
        @Override
        public void power(GameLogicManager g, Player marchand, RoundSummary summary) {
            //TODO
            super.power(g, marchand, summary);
        }
    },
    ARCHITECTE (Color.GRAY) {
        @Override
        public void power(GameLogicManager g, Player architecte, RoundSummary summary) {
            //TODO
            super.power(g, architecte, summary);
        }
    },
    CONDOTTIERE (Color.RED) {
        @Override
        public void power(GameLogicManager g, Player condottiere, RoundSummary summary) {
            //TODO
            super.power(g, condottiere, summary);
        }
    };


    /**
     * Méthode pour utiliser le pouvoir du rôle. Toujours appeler "super.power", car c'est lui qui écrit dans le summary que le pouvoir a été utilisé
     */
    public void power(GameLogicManager g, Player player, RoundSummary summary) {
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
