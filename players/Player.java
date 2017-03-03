/*
 * Agurk
 * Author: traillj
 */

package players;

import java.util.List;

import core.Deck;


public abstract class Player {

    protected List<Integer> hand;
    
    // Keep hand sorted always
    public void setHand(List<Integer> hand) {
        this.hand = hand;
        
    }
    
    public StringBuilder showHand(Deck deck) {
        return deck.cardsToString(hand);
    }
    
    public abstract int chooseCard(int highestPlay)
    throws NoStrategyException;
}
