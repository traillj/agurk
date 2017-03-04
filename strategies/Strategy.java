/*
 * Agurk
 * Author: traillj
 */

package strategies;

import java.util.List;


// Use to pick a card in a non-last trick for AI
public interface Strategy {
    
    public int chooseIndex(List<Integer> hand, int highestPlay);
}
