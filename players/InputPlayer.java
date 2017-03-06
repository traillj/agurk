/*
 * Agurk
 * Author: traillj
 */

package players;


/**
 * Player that requires external input.
 */
public class InputPlayer extends Player {
    
    /**
     * Creates a new InputPlayer object.
     * @param name Player's ID.
     */
    public InputPlayer(String name) {
        super(name);
    }
    
    @Override
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
