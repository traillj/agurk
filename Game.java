/*
 * Agurk
 * Author: traillj
 */


public class Game {

    private Deck deck;
    private Player[] players;
    
    public Game(int numPlayers) {
        deck = new Deck();
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            players[i] = new Player();
            players[i].setHand(deck.dealHand());
        }
        
        System.out.print(showHands());
    }
    
    private StringBuilder showHands() {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < players.length; i++) {
            out.append("Player " + i + ": " + players[i].showHand() + '\n');
        }
        return out;
    }
    
}
