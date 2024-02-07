package fr.cotedazur.univ.polytech.citadellesgroupeq;

import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.players.Player;

import java.util.*;

/**
 * Enum that represents roles. Each enum overrides the 'power' method. You can use '.name()' and '.ordinal()' to get their number
 * and string representations.
 */
public enum Role {
    EMPTY_ROLE(Color.GRAY),
    ASSASSIN(Color.GRAY) {
        @Override
        public void power (GameLogicManager g, Player assassin, RoundSummary summary) {
            summary.setHasUsedRolePower();
            Role assassinatedRole=assassin.getStrategy().selectRoleToKillAsAssassin(g.generateAvailableRoles(g.getPlayersList().size()));
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

            Optional<Role> stealedRole = voleur.getStrategy().selectRoleToSteal(availableRoles, unstealableRoles);

            if(stealedRole.isPresent()) {
                for (Player player : g.getPlayersList()) {
                    if (player.getRole() == stealedRole.get()) {
                        int totalCash = player.getCash();
                        player.removeCoins(totalCash);
                        voleur.addCoins(totalCash);
                        summary.setDrawnCoins(totalCash);
                        summary.setStealedRole(stealedRole.get());
                        summary.setHasUsedRolePower();
                    }
                }
            }
        }
    },
    MAGICIEN(Color.GRAY) {
        @Override
        public void power(GameLogicManager g, Player magicien, RoundSummary summary) {
            summary.setHasUsedRolePower();

            if(magicien.choosesToExchangeCardWithPlayer()) {
                Player selectedPlayerToExchangeWith = magicien.getStrategy().selectPlayerToExchangeCardsWithAsMagicien(g.getPlayersList());
                List<District> districtListCopy=selectedPlayerToExchangeWith.getCardsInHand();
                selectedPlayerToExchangeWith.setCardsInHand(magicien.getCardsInHand());
                magicien.setCardsInHand(districtListCopy);
                summary.setExchangedCardsPlayerId(selectedPlayerToExchangeWith.getId());
            }
            else {//wants to draw some cards from the pile
                int[] cardsToExchange=magicien.getStrategy().selectCardsToExchangeWithPileAsMagicien();

                CardDeck cardDeck = new CardDeck();

                for(int cardIndex: cardsToExchange) {
                    District oldCard=magicien.getCardsInHand().get(cardIndex);
                    cardDeck.addDistrictUnderCardsPile(oldCard);
                    magicien.setCardInHand(cardIndex, cardDeck.pickTopCard());
                }

                summary.setHasExchangedCardsWithPileAsMagician(true);
                summary.setExchangedCardsWithPileIndex(cardsToExchange);
            }
        }
    },
    ROI (Color.YELLOW) {
        @Override
        public void power(GameLogicManager g, Player roi, RoundSummary summary) {
            g.setMasterOfTheGameIndex(roi.getId());
            summary.setHasUsedRolePower();
        }
    },
    EVEQUE(Color.BLUE) {
        @Override
        public void power(GameLogicManager g, Player eveque, RoundSummary summary) {
            summary.setHasUsedRolePower();
        }
    },
    MARCHAND (Color.GREEN) {
        @Override
        public void power(GameLogicManager g, Player marchand, RoundSummary summary) {
            marchand.addCoins(1);
            summary.setHasUsedRolePower();
        }
    },
    ARCHITECTE (Color.GRAY) {
        @Override
        public void power(GameLogicManager g, Player architecte, RoundSummary summary) {
            summary.setHasUsedRolePower();
            architecte.pickCard(summary);
            architecte.pickCard(summary);//"l'architecte pioche d'office 2 cartes de plus"

            architecte.buyDistrictsDuringTurn(summary);
            architecte.buyDistrictsDuringTurn(summary);//il peut acheter jusqu'à 3 cartes
        }
    },
    CONDOTTIERE (Color.RED) {
        @Override
        public void power(GameLogicManager g, Player condottiere, RoundSummary summary) {
            if (!g.isFinished()) {
                Optional<AbstractMap.SimpleEntry<Integer, District>> optionalPlayerChoice = condottiere.selectDistrictToDestroyAsCondottiere(g.getPlayersList());
                if (optionalPlayerChoice.isPresent()) {
                    AbstractMap.SimpleEntry<Integer, District> playerChoice = optionalPlayerChoice.get();

                    if (playerChoice.getKey() < 0 || playerChoice.getKey() >= g.getPlayersList().size()) {
                        throw new IllegalArgumentException("Player index is out of bounds.");
                    }


                    Player selectedPlayer = g.getPlayersList().get(playerChoice.getKey());

                    if (!selectedPlayer.getCity().contains(playerChoice.getValue())) {
                        throw new IllegalArgumentException("Player doesn't own this district.");
                    }

                    if (playerChoice.getValue().getCost() - 1 > condottiere.getCash()) {
                        throw new IllegalArgumentException("Condottiere doesn't have enough cash to destroy this city.");
                    }

                    if (selectedPlayer.getRole() == Role.EVEQUE && !selectedPlayer.isDeadForThisTurn()) {
                        throw new IllegalArgumentException("Can't destroy District of an alive Eveque.");
                    }

                    if (playerChoice.getValue().getName().equalsIgnoreCase("donjon") && playerChoice.getValue().getColor() == Color.PURPLE) {
                        throw new IllegalArgumentException("Can't destroy Donjon merveille.");
                    }


                    condottiere.removeCoins(playerChoice.getValue().getCost() - 1);
                    selectedPlayer.removeDistrictFromCity(playerChoice.getValue());

                    g.getCardDeck().addDistrictUnderCardsPile(playerChoice.getValue());

                    summary.setDestroyedDistrict(playerChoice);

                    summary.setHasUsedRolePower();
                }
            }
        }
    };


    /**
     * Pouvoir associé au rôle
     * @param g
     * @param player
     * @param summary
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
