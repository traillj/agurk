/*
 * Agurk
 * Author: traillj
 */

package core;

import java.util.ArrayList;
import java.util.List;


public class TrickInfo {
    
    private List<Integer> cardsPlayed;
    public int highestPlay = 0;
    public int highestPlayPlayer = -1;
    
    public TrickInfo(int numAlive) {
        cardsPlayed = new ArrayList<Integer>();
        for (int i = 0; i < numAlive; i++) {
            cardsPlayed.add(0);
        }
    }
    
    public void updateTrickInfo(int card, int playerIndex) {
        cardsPlayed.set(playerIndex, card);
        
        // The last of equally high cards wins the trick,
        // the winner leads the next trick.
        if (card >= highestPlay) {
            highestPlay = card;
            highestPlayPlayer = playerIndex;
        }
    }
    
    // Cards played list was initialised to invalid values
    // Creates a new list to hold the valid cards
    public List<Integer> getCardsPlayed() {
        List<Integer> cards = new ArrayList<Integer>();
        for (Integer card : cardsPlayed) {
            if (card != 0) {
                cards.add(card);
            }
        }
        return cards;
    }
}
