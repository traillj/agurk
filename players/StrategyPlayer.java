/*
 * Agurk
 * Author: traillj
 */

package players;

import strategies.Strategy;


// Computer controlled player
public class StrategyPlayer extends Player {

    private Strategy strategy;
    
    public StrategyPlayer(String name, Strategy strategy) {
        super(name);
        this.strategy = strategy;
    }

    public int chooseCard(int highestPlay) {
        int chosenIndex = strategy.chooseIndex(hand, highestPlay);
        return hand.remove(chosenIndex);
    }
}
