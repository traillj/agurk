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
    
    private int leadPlayer = 2;
    
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
    
    // Assume Player 0 is the non-AI player
    public TrickInfo startNonLastTrick() throws NoStrategyException {
        TrickInfo trickInfo = new TrickInfo();
        trickInfo.cardsPlayed = new LinkedList<Integer>();
        
        if (leadPlayer == 0) {
            // non-AI player requires external input
            return trickInfo;
        }
        
        for (int i = leadPlayer; i < players.length; i++) {
            aiPlayerTurn(trickInfo, i);
        }
        return trickInfo;
    }
    
    public TrickInfo finishNonLastTrick(TrickInfo trickInfo)
            throws NoStrategyException {
        TrickInfo newTrickInfo = new TrickInfo();
        newTrickInfo.cardsPlayed = new LinkedList<Integer>();
        newTrickInfo.highestPlay = trickInfo.highestPlay;
        newTrickInfo.highestPlayPlayer = trickInfo.highestPlayPlayer;
        
        for (int i = 1; i < leadPlayer; i++) {
            aiPlayerTurn(newTrickInfo, i);
        }
        
        leadPlayer = newTrickInfo.highestPlayPlayer;
        return newTrickInfo;
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
            out.append("Player " + i + ": " + players[i].showHand() + '\n');
        }
        return out;
    }
    
}
