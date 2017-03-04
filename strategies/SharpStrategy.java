/*
 * Agurk
 * Author: traillj
 */

package strategies;

import java.util.List;


// Assumes hand is sorted
// Attempt to play highest card at all times if possible
// Returns the index of the chosen card
public class SharpStrategy implements Strategy {

    @Override
    public int chooseIndex(List<Integer> hand, int highestPlay) {
        int chosenIndex;
        int highestCard = hand.get(hand.size() - 1);
        
        if (highestCard >= highestPlay) {
            chosenIndex = hand.size() - 1;
        } else {
            // must play lowest card
            chosenIndex = 0;
        }
        
        return chosenIndex;
    }
}
