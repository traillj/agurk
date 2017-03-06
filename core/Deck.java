/*
 * Agurk
 * Author: traillj
 */

package core;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * Rank-only standard deck of 52 cards.
 * Maintains a list of the cards dealt, and keeps them
 * separate from the main deck.
 */
public class Deck {

    /** Minimum card value. */
    private static final int MIN_VALUE = 2;
    /** Maximum card value. */
    private static final int MAX_VALUE = 14;
    /** Number of suits. */
    private static final int NUM_SUITS = 4;
    /** Total number of cards. */
    private static final int DECK_SIZE = 52;
    
    /** Cards in deck. */
    private List<Integer> cards;
    /** Cards removed from deck. */
    private List<Integer> removedCards;
    
    /**
     * Creates a new Deck object. Initialises deck.
     */
    public Deck() {
        cards = new LinkedList<Integer>();
        removedCards = new LinkedList<Integer>();
        
        int i, j;
        for (i = MIN_VALUE; i <= MAX_VALUE; i++) {
            for (j = 0; j <= NUM_SUITS; j++) {
                cards.add(i);
            }
        }
    }
    
    /**
     * Choose and remove the specified number of cards at random
     * from the deck. Returns the cards in sorted order.
     * @param numCards Number of cards to deal.
     * @return List of cards.
     */
    public List<Integer> dealCards(int numCards) {
        int[] hand = new int[numCards];
        Random rand = new Random();
        int index, chosenCard;
        
        for (int i = 0; i < numCards; i++) {
            index = rand.nextInt(cards.size());
            chosenCard = cards.remove(index);
            
            hand[i] = chosenCard;
            removedCards.add(chosenCard);
        }
        
        Arrays.sort(hand);
        return toList(hand);
    }
    
    /**
     * Converts an array to a list.
     * @param array Array to be converted.
     * @return List representation of the array.
     */
    private List<Integer> toList(int[] array) {
        List<Integer> list = new LinkedList<Integer>();
        for (int item : array) {
            list.add(item);
        }
        return list;
    }
    
    /**
     * Adds the removed cards back to the deck.
     */
    public void resetDeck() {
        cards.addAll(removedCards);
        removedCards.clear();
    }
    
    
    /**
     * Returns a comma delimited representation of the cards.
     * Converts the card values to symbols.
     * @param cards Cards to be included in the string
     * @return Representation of the cards.
     */
    public static String cardsToString(List<Integer> cards) {
        if (cards.size() == 0) {
            return "";
        }
        
        StringBuilder out = new StringBuilder();
        out.append(toSymbol(cards.get(0)));
        for (int i = 1; i < cards.size(); i++) {
            out.append("," + toSymbol(cards.get(i)));
        }
        
        return out.toString();
    }
    
    /**
     * Returns the corresponding symbol for the value.
     * @param value A card's value.
     * @return The card's symbol.
     */
    public static char toSymbol(int value) {
        char symbol;
        if (value < 10 && value > 1) {
            symbol = (char)(value + '0');
        } else if (value == 10) {
            symbol = 'T';
        } else if (value == 11) {
            symbol = 'J';
        } else if (value == 12) {
            symbol = 'Q';
        } else if (value == 13) {
            symbol = 'K';
        } else if (value == 14) {
            symbol = 'A';
        } else {
            System.err.println("Error: invalid value");
            symbol = 'X';
        }
        
        return symbol;
    }
    
    /**
     * Returns the corresponding value for the symbol.
     * Ace is assumed to have a value of 14, the highest value.
     * @param symbol A card's symbol.
     * @return The card's value.
     */
    public static int toValue(char symbol) {
        int value;
        if (Character.isDigit(symbol)) {
            value = Character.getNumericValue(symbol);
        } else if (symbol == 'T') {
            value = 10;
        } else if (symbol == 'J') {
            value = 11;
        } else if (symbol == 'Q') {
            value = 12;
        } else if (symbol == 'K') {
            value = 13;
        } else if (symbol == 'A') {
            value = 14;
        } else {
            System.err.println("Error: Invalid symbol");
            value = 0;
        }
        
        return value;
    }
    
    /**
     * Tests if the specified value is valid.
     * @param value The value to be tested.
     * @return True if the value is valid.
     */
    public static boolean isValidValue(int value) {
        return (value >= MIN_VALUE && value <= MAX_VALUE);
    }
    
    /**
     * Gets the total number of cards.
     * @return Deck size.
     */
    public static int getDeckSize() {
        return DECK_SIZE;
    }
}
