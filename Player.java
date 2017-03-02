/*
 * Agurk
 * Author: traillj
 */

import java.util.Arrays;


public class Player {

    private int[] hand;
    
    public void setHand(int[] hand) {
        this.hand = hand;
    }
    
    public StringBuilder showHand() {
        StringBuilder out = new StringBuilder();
        if (hand.length == 0) {
            return out;
        }
        
        Arrays.sort(hand);
        
        out.append(toSymbol(hand[0]));
        for (int i = 1; i < hand.length; i++) {
            out.append(", " + toSymbol(hand[i]));
        }
        
        return out;
    }
    
    private char toSymbol(int rank) {
        char symbol;
        if (rank < 10 && rank > 1) {
            symbol = (char)(rank + '0');
        } else if (rank == 10) {
            symbol = 'T';
        } else if (rank == 11) {
            symbol = 'J';
        } else if (rank == 12) {
            symbol = 'Q';
        } else if (rank == 13) {
            symbol = 'K';
        } else if (rank == 14) {
            symbol = 'A';
        } else {
            System.err.println("Error: invalid rank");
            symbol = 'X';
        }
        
        return symbol;
    }
}
