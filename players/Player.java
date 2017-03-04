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
    
    public String showHand() {
        return Deck.cardsToString(hand);
    }
    
    public int chooseCard(int highestPlay) throws NoStrategyException {
        throw new NoStrategyException();
    }
    
    public boolean removeCard(int value) {
        return false;
    }
}
