/*
 * Agurk
 * Author: traillj
 */

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Deck {

    private List<Integer> cards;
    private List<Integer> removedCards;
    
    private static final int MIN_RANK = 2;
    private static final int MAX_RANK = 14;
    private static final int NUM_SUITS = 4;
    
    private static final int HAND_SIZE = 7;
    
    public Deck() {
        cards = new LinkedList<Integer>();
        removedCards = new LinkedList<Integer>();
        
        int i, j;
        for (i = MIN_RANK; i <= MAX_RANK; i++) {
            for (j = 0; j <= NUM_SUITS; j++) {
                cards.add(i);
            }
        }
    }
    
    // Choose and remove 7 cards at random from the deck
    public int[] dealHand() {
        int[] hand = new int[HAND_SIZE];
        Random rand = new Random();
        int index, chosenCard;
        
        for (int i = 0; i < HAND_SIZE; i++) {
            index = rand.nextInt(cards.size());
            chosenCard = cards.remove(index);
            
            hand[i] = chosenCard;
            removedCards.add(chosenCard);
        }
        
        return hand;
    }
}
