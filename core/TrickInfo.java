/*
 * Agurk
 * Author: traillj
 */

package core;

import java.util.ArrayList;
import java.util.List;


/**
 * Stores information about a trick.
 * Provides methods to update the information when a new
 * card is played. To maintain the order that cards were
 * played when printing, it is necessary to keep multiple
 * TrickInfo classes where each added card is made by the
 * player of the succeeding index.
 * The highest play and corresponding player may need to
 * be updated when there a multiple TrickInfo classes.
 */
public class TrickInfo {
    
    /** Cards played in the trick. */
    private List<Integer> cardsPlayed;
    
    /** Highest card played in the trick. */
    private int highestPlay = 0;
    /** Index of player who played the highest card in the trick. */
    private int highestPlayPlayer = -1;
    
    /**
     * Creates a new TrickInfo object.
     * @param numAlive Number of remaining players.
     */
    public TrickInfo(int numAlive) {
        cardsPlayed = new ArrayList<Integer>();
        for (int i = 0; i < numAlive; i++) {
            cardsPlayed.add(0);
        }
    }
    
    /**
     * Updates the trick information.
     * @param card Card played in the trick.
     * @param playerIndex Index of the player who played the card.
     */
    public void updateTrickInfo(int card, int playerIndex) {
        cardsPlayed.set(playerIndex, card);
        
        // The last of equally high cards wins the trick
        if (card >= highestPlay) {
            highestPlay = card;
            highestPlayPlayer = playerIndex;
        }
    }
    
    /**
     * Gets a new list holding the cards played.
     * @return The cards played.
     */
    public List<Integer> getCardsPlayed() {
        List<Integer> cards = new ArrayList<Integer>();
        for (Integer card : cardsPlayed) {
            // Cards played list was initialised to invalid values
            if (card != 0) {
                cards.add(card);
            }
        }
        return cards;
    }

    /**
     * Gets the highest card played in the trick.
     * @return The highest play.
     */
    public int getHighestPlay() {
        return highestPlay;
    }

    /**
     * Sets the highest play.
     * @param highestPlay The highest card played in the trick.
     */
    public void setHighestPlay(int highestPlay) {
        this.highestPlay = highestPlay;
    }

    /**
     * Gets the index of player who played
     * the highest card in the trick.
     * @return The highest play player.
     */
    public int getHighestPlayPlayer() {
        return highestPlayPlayer;
    }

    /**
     * Sets highest play player.
     * @param highestPlayPlayer The index of player who
     *        played the highest card in the trick.
     */
    public void setHighestPlayPlayer(int highestPlayPlayer) {
        this.highestPlayPlayer = highestPlayPlayer;
    }
}
