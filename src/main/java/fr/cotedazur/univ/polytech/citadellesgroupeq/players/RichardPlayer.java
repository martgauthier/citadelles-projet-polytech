package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.*;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.*;

import java.util.*;
public class RichardPlayer extends Player{
    public RichardPlayer(int id,CardDeck pioche){
        super(id,pioche);
        setStrategy(new DefaultStrategy(this));
    }

    @Override
    public String getBotLogicName() {
        return "RichardPlayer";
    }

    @Override
    public void playTurn(RoundSummary summary, GameLogicManager game) {

    }

    @Override
    public boolean choosesToExchangeCardWithPlayer() {
        return false;
    }

    @Override
    public Player selectPlayerToExchangeCardsWithAsMagicien(List<Player> playerList) {
        return null;
    }

    @Override
    public Optional<District> getChoosenDistrictToBuy() {
        return Optional.empty();
    }
    @Override
    public Role selectRoleToKillAsAssassin(List<Role> availableRoles){
        if(availableRoles.contains(Role.ROI)){
            return Role.ROI;
        }else if(availableRoles.contains(Role.CONDOTTIERE)){
            return Role.CONDOTTIERE;
        } else if (availableRoles.contains(Role.EVEQUE)){
            return Role.EVEQUE;
        }else{
            return availableRoles.get(0);
        }
    }
    @Override
    public int selectAndSetRole(List<Role> availableRoles, List<Player> playerList) {
        if(getStrategy().getClass() == DefaultStrategy.class && this.getCity().size()<=4){
            if (availableRoles.contains(Role.VOLEUR)) {
                setRole(Role.VOLEUR);
                return availableRoles.indexOf(Role.VOLEUR);
            } else if (availableRoles.contains(Role.EVEQUE)) {
                setRole(Role.EVEQUE);
                return availableRoles.indexOf(Role.EVEQUE);
            } else if (availableRoles.contains(Role.CONDOTTIERE)) {
                setRole(Role.CONDOTTIERE);
                return availableRoles.indexOf(Role.CONDOTTIERE);
            } else {
                setRole(availableRoles.get(0));
                return 0;
            }
        }else if(getStrategy().getClass() == DefaultStrategy.class && this.getCity().size()>4){
            if (availableRoles.contains(Role.CONDOTTIERE)) {
                setRole(Role.CONDOTTIERE);
                return availableRoles.indexOf(Role.CONDOTTIERE);
            } else if (availableRoles.contains(Role.EVEQUE)) {
                setRole(Role.EVEQUE);
                return availableRoles.indexOf(Role.EVEQUE);
            } else {
                setRole(availableRoles.get(0));
                return 0;
            }
        }else if(getStrategy().getClass() == RichardMaliceStrategy.class){
            if (availableRoles.contains(Role.ROI)) {
                setRole(Role.ROI);
                return availableRoles.indexOf(Role.ROI);
            } else if (availableRoles.contains(Role.ASSASSIN)) {
                setRole(Role.ASSASSIN);
                return availableRoles.indexOf(Role.ASSASSIN);
            } else if (availableRoles.contains(Role.CONDOTTIERE)){
                setRole(Role.CONDOTTIERE);
                return availableRoles.indexOf(Role.CONDOTTIERE);
            } else if (availableRoles.contains(Role.EVEQUE)) {
                setRole(Role.EVEQUE);
                return availableRoles.indexOf(Role.EVEQUE);
            } else {
                setRole(availableRoles.get(0));
                return 0;
            }
        } else if (false){ //TODO rajouter la strat Luxe
            return 0;
        } else {
            setRole(availableRoles.get(0));
            return 0;
        }
        
    }
}
