package fr.cotedazur.univ.polytech.citadellesgroupeq.players;

import fr.cotedazur.univ.polytech.citadellesgroupeq.*;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.GameLogicManager;
import fr.cotedazur.univ.polytech.citadellesgroupeq.gamelogic.RoundSummary;
import fr.cotedazur.univ.polytech.citadellesgroupeq.strategies.*;

import java.util.*;

/**
 * Classe implémentant les stratégies du forum.
 */
public class RichardPlayer extends Player{
    private boolean isInGameBeginning;//utile pour vérifier que "Richard ne veut plus prendre le voleur après le début de partie"

    public RichardPlayer(int id,CardDeck pioche){
        super(id,pioche);
        setStrategy(new DefaultStrategy(this));
        isInGameBeginning=true;
    }

    @Override
    public String getBotLogicName() {
        return "RichardPlayer";
    }

    @Override
    public void playTurn(RoundSummary summary, GameLogicManager game) {
        setStrategy(new DefaultStrategy(this));
        boolean actMalice=false;
        for (Player joueur:game.getPlayersList()){
            if(joueur!=this && joueur.getCity().size()==6){
                actMalice=true;
                break;
            }
        }


        if(game.isFinished()) {//par ordre de priorité, défini en testant à la main
            setStrategy(new SecurePointsForEndGame(this));
        }
        else if(getCash() >= 6) {
            setStrategy(new RichardRichStrategy(this));
        }
        else if(actMalice){
            setStrategy(new RichardMaliceStrategy(this));
        }

        getCoinsFromColorCards(summary);

        getRole().power(game, this, summary);

        if(getCash() < 8) draw2Coins(summary);//8 est un nombre déterminé manuellement par nos tests, qui produit le meilleur taux de réussite
        else if(!haveObservatoryInCity()) pickCard(summary);

        buyDistrictsDuringTurn(summary);

        PowerManager powerManager = new PowerManager(game);
        powerManager.applyCityPowers(this, summary);
    }

    @Override
    public boolean choosesToExchangeCardWithPlayer() {
        return getCardsInHand().isEmpty();
    }

    @Override
    public Player selectPlayerToExchangeCardsWithAsMagicien(List<Player> playerList) {
        if(playerList.get(0)!=this){
            Player joueurMainPleine=playerList.get(0);
            for(Player joueur: playerList){
                if(joueur.getCardsInHand().size()>joueurMainPleine.getCardsInHand().size() && joueur!=this){
                    joueurMainPleine=joueur;
                }
            }
            return joueurMainPleine;
        }else {
            Player joueurMainPleine=playerList.get(1);
            for(Player joueur :playerList){
                if(joueur.getCardsInHand().size()>joueurMainPleine.getCardsInHand().size() && joueur!=this){
                    joueurMainPleine=joueur;
                }
            }
            return joueurMainPleine;
        }
    }

    /**
     * Les préférences d'achats n'étant pas précisé, j'ai décidé arbitrairement qu'il
     * Choisit le district selon ces règles :
     * -Achetable
     * -De la couleur Bleu (eveque) ou Rouge (condottiere) si possible
     * La carte dont le prix est le plus proche de la valeur 3
     */
    @Override
    public Optional<District> getChoosenDistrictToBuy() {
        Optional<District> boughtCard=Optional.empty();
        for(District card: getBuyableCards()) {
            if(boughtCard.isEmpty()) {
                boughtCard=Optional.of(card);
            }
            else if(card.getColor()==Color.BLUE || card.getColor()==Color.RED) {
                if(boughtCard.get().getColor()!=Color.BLUE || boughtCard.get().getColor()!=Color.RED || Math.abs(3-boughtCard.get().getCost()) >= Math.abs(3-card.getCost())) {//le prix le plus proche de 3
                    boughtCard=Optional.of(card);
                }
            }
            else if(boughtCard.get().getColor()!=Color.BLUE && boughtCard.get().getColor()!=Color.RED && (Math.abs(3-boughtCard.get().getCost()) >= Math.abs(3-card.getCost()))) {
                boughtCard=Optional.of(card);
            }
        }
        return boughtCard;
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

    /**
     * par défaut il préfère au début prendre voleur et ensuite il va préféré condottiere et eveque
     * @param availableRoles les rôles disponibles
     * @param playerList liste des joueurs
     * @return
     */
    @Override
    public int selectAndSetRole(List<Role> availableRoles, List<Player> playerList) {
        if(getCardsInHand().size()<=3 && availableRoles.contains(Role.MAGICIEN)) {//Richard aime prendre magicien quand il a plus de carte en main
            setRole(Role.MAGICIEN);
            return availableRoles.indexOf(Role.MAGICIEN);
        }
        if(isInGameBeginning && this.getCity().size()<=4){
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
        }
        else {
            isInGameBeginning=false;//on sort du début de partie
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
        }
    }
}
