/*
 * Agurk
 * Author: traillj
 */

package core;

import java.util.LinkedList;
import java.util.List;

import players.AIPlayer;
import players.NoStrategyException;
import players.Player;

import strategies.SharpStrategy;


public class Game {

    private Deck deck;
    private Player[] players;
    
    private int leadPlayer = 0;
    
    public Game(int numPlayers) {
        deck = new Deck();
        players = new Player[numPlayers];
        
        for (int i = 0; i < numPlayers; i++) {
            players[i] = new AIPlayer(new SharpStrategy());
            players[i].setHand(makePlayerHand());
        }
    }
    
    private List<Integer> makePlayerHand() {
        int[] arrayHand = deck.dealHand();
        List<Integer> listHand = new LinkedList<Integer>();
        
        for (int card : arrayHand) {
            listHand.add(card);
        }
        return listHand;
    }
    
    public int[] playNonLastTrick() throws NoStrategyException {
        int highestPlay = 0;
        int highestPlayPlayer = -1;
        
        int playerTurn, chosenCard;
        int[] cardsPlayed = {0,0,0,0};
        
        for (int i = 0; i < players.length; i++) {
            playerTurn = (leadPlayer + i) % players.length;
            chosenCard = players[playerTurn].chooseCard(highestPlay);
            cardsPlayed[playerTurn] = chosenCard;
            
            if (chosenCard >= highestPlay) {
                highestPlay = chosenCard;
                highestPlayPlayer = playerTurn;
            }
            //System.out.println(showHands());
        }
        
        leadPlayer = highestPlayPlayer;
        return cardsPlayed;
    }
    
    public StringBuilder showHands() {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < players.length; i++) {
            out.append("Player " + i + ": " + players[i].showHand() + '\n');
        }
        return out;
    }
    
}
