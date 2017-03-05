/*
 * Agurk
 * Author: traillj
 */

package core;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


// Rank-only standard deck of 52 cards.
// Maintains a list of the cards dealt, and keeps them
// separate from the main deck.
public class Deck {

    private List<Integer> cards;
    private List<Integer> removedCards;
    
    private static final int MIN_VALUE = 2;
    private static final int MAX_VALUE = 14;
    private static final int NUM_SUITS = 4;
    
    private static final int HAND_SIZE = 7;
    
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
    
    // Choose and remove 7 cards at random from the deck
    // Returns the cards in sorted order
    public List<Integer> dealHand() {
        int[] hand = new int[HAND_SIZE];
        Random rand = new Random();
        int index, chosenCard;
        
        for (int i = 0; i < HAND_SIZE; i++) {
            index = rand.nextInt(cards.size());
            chosenCard = cards.remove(index);
            
            hand[i] = chosenCard;
            removedCards.add(chosenCard);
        }
        
        Arrays.sort(hand);
        return toList(hand);
    }
    
    private List<Integer> toList(int[] array) {
        List<Integer> list = new LinkedList<Integer>();
        for (int item : array) {
            list.add(item);
        }
        return list;
    }
    
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
    
    private static char toSymbol(int value) {
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
    
    public static boolean isValidValue(int value) {
        return (value >= MIN_VALUE && value <= MAX_VALUE);
    }
}
