/*
 * Agurk
 * Author: traillj
 */

package players;

import strategies.Strategy;


/**
 * Computer controlled player that uses a strategy
 * based only on the highest card played in the trick.
 */
public class StrategyPlayer extends Player {

    /** Strategy for choosing a card. */
    private Strategy strategy;
    
    /**
     * Creates a new StrategyPlayer object.
     * @param name Player's ID.
     * @param strategy Strategy for choosing a card.
     */
    public StrategyPlayer(String name, Strategy strategy) {
        super(name);
        this.strategy = strategy;
    }
    
    @Override
    public int chooseCard(int highestPlay) {
        int chosenIndex = strategy.chooseIndex(hand, highestPlay);
        return hand.remove(chosenIndex);
    }
}
