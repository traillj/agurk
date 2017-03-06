/*
 * Agurk
 * Author: traillj
 */

package players;

import java.util.List;

import core.Deck;


/**
 * Represents a player in a card game.
 * Extend with a class that implements
 * a strategy for choosing cards.
 */
public abstract class Player {

    /** Player's ID. */
    private String name;
    
    /** Player's cards. */
    protected List<Integer> hand;
    
    /**
     * Creates a new Player object.
     * @param name Player's ID.
     */
    public Player(String name) {
        this.name = name;
    }
    
    /**
     * Gets the player's name.
     * @return Player's ID.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the player's hand.
     * @param hand Player's cards.
     */
    public void setHand(List<Integer> hand) {
        this.hand = hand;
    }
    
    /**
     * Shows the player's hand.
     * @return Representation of the player's cards.
     */
    public String showHand() {
        return Deck.cardsToString(hand);
    }
    
    /**
     * Returns and removes a card from the player's hand,
     * given the highest card played.
     * @param highestPlay The highest rank played.
     * @return A card from the player's hand.
     * @throws NoStrategyException Thrown if this method
     *         has not been overridden.
     */
    public int chooseCard(int highestPlay) throws NoStrategyException {
        throw new NoStrategyException();
    }
    
    /**
     * Returns and removes the card of lowest
     * value from the player's hand.
     * @return Lowest card in hand.
     */
    public int playLowestCard() {
        return hand.remove(0);
    }
    
    /**
     * Removes the first card with the specified value.
     * @param value Rank of the card to remove.
     * @return False if the value is not in hand, or the method
     *         has not been overridden. Otherwise returns true.
     */
    public boolean removeCard(int value) {
        return false;
    }
}
