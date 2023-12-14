package fr.cotedazur.univ.polytech.citadellesgroupeq;

import java.util.List;
import java.util.Optional;

public class alwaysSpendPlayer extends Player{

    public alwaysSpendPlayer(int id) {
        super(id);
    }

    public alwaysSpendPlayer(int id, int cash, List<Citadel> cards) {
        super(id, cash, cards);
    }

    @Override
    public void playerTurn(RoundSummary summary) {
        if (!getCardsInHand().isEmpty()) {
            draw2Coins(summary);
            Optional<Citadel> choosenCitadel = getChoosenCitadelToBuy();
            if (choosenCitadel.isPresent()) {
                Citadel citadel = choosenCitadel.get();
                addCitadelToCity(citadel);
                summary.addBoughtCitadel(citadel);
                removeCardFromHand(citadel);
                removeCoins(citadel.getCost());
            }
        }
        else{
            pickCard(summary);
        }
    }


}
