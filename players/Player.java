/*
 * Agurk
 * Author: traillj
 */

package players;

import java.util.List;

import core.Deck;


public abstract class Player {

    private String name;
    protected List<Integer> hand;
    
    public Player(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
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
    
    public int playLowestCard() {
        return hand.remove(0);
    }
    
    public boolean removeCard(int value) {
        return false;
    }
}
