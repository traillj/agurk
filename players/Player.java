/*
 * Agurk
 * Author: traillj
 */

package players;

import java.util.List;


public abstract class Player {

    protected List<Integer> hand;
    
    // Keep hand sorted always
    public void setHand(List<Integer> hand) {
        this.hand = hand;
        
    }
    
    public StringBuilder showHand() {
        StringBuilder out = new StringBuilder();
        if (hand.size() == 0) {
            return out;
        }
        
        
        out.append(toSymbol(hand.get(0)));
        for (int i = 1; i < hand.size(); i++) {
            out.append("," + toSymbol(hand.get(i)));
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
    
    public abstract int chooseCard(int highestPlay)
    throws NoStrategyException;
}
