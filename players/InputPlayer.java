/*
 * Agurk
 * Author: traillj
 */

package players;


// Player that requires external input
public class InputPlayer extends Player {

    public boolean removeCard(int value) {
        
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i) == value) {
                hand.remove(i);
                return true;
            }
        }
        
        return false;
    }
}
