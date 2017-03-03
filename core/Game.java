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
    
    public TrickInfo startNonLastTrick() {
        TrickInfo trickInfo = new TrickInfo();
        trickInfo.cardsPlayed = new LinkedList<Integer>();
        int playerNum;
        
        for (int i = 0; i < players.length; i++) {
            playerNum = (leadPlayer + i) % players.length;
            try {
                aiPlayerTurn(trickInfo, playerNum);
            } catch (NoStrategyException e) {
                break;
            }
        }
        
        // temporary
        leadPlayer = trickInfo.highestPlayPlayer;
        
        return trickInfo;
    }
    
    private void aiPlayerTurn(TrickInfo trickInfo, int playerNum)
            throws NoStrategyException {
        
        int chosenCard = players[playerNum].chooseCard(trickInfo.highestPlay);
        trickInfo.cardsPlayed.add(chosenCard);
        
        // The last of equally high cards wins the trick,
        // the winner leads the next trick.
        if (chosenCard >= trickInfo.highestPlay) {
            trickInfo.highestPlay = chosenCard;
            trickInfo.highestPlayPlayer = playerNum;
        }
    }
    
    // for debugging
    public StringBuilder showHands() {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < players.length; i++) {
            out.append("Player " + i + ": "
                    + players[i].showHand(deck) + '\n');
        }
        return out;
    }
    
}
