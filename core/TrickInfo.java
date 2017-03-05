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
    
    public TrickInfo() {
        cardsPlayed = new ArrayList<Integer>();
    }
    
    public void updateTrickInfo(int card, int playerIndex) {
        cardsPlayed.add(card);
        
        // The last of equally high cards wins the trick,
        // the winner leads the next trick.
        if (card >= highestPlay) {
            highestPlay = card;
            highestPlayPlayer = playerIndex;
        }
    }

    public List<Integer> getCardsPlayed() {
        return cardsPlayed;
    }
}
