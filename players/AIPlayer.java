/*
 * Agurk
 * Author: traillj
 */

package players;

import strategies.Strategy;


public class AIPlayer extends Player {

    private Strategy strategy;
    
    public AIPlayer(Strategy strategy) {
        this.strategy = strategy;
    }

    public int chooseCard(int highestPlay) {
        int chosenIndex = strategy.chooseIndex(hand, highestPlay);
        return hand.remove(chosenIndex);
    }
}
