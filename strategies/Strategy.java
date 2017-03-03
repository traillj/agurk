/*
 * Agurk
 * Author: traillj
 */

package strategies;

import java.util.List;


public interface Strategy {
    
    public int chooseIndex(List<Integer> hand, int highestPlay);
}
