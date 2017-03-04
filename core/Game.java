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


// Methods for playing components of the game
// Player 0 is non-AI
public class Game {

    private Deck deck;
    private Player[] players;
    
    private int leadPlayer = 2;
    
    // Number of players: 2-7
    public Game(int numPlayers) {
        deck = new Deck();
        players = new Player[numPlayers];
        
        players[0] = new Player();
        players[0].setHand(makePlayerHand());
        
        for (int i = 1; i < numPlayers; i++) {
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
        
        if (leadPlayer == 0) {
            // non-AI player requires external input
            return trickInfo;
        }
        
        for (int i = leadPlayer; i < players.length; i++) {
            aiPlayerTurn(trickInfo, i);
        }
        return trickInfo;
    }
    
    public TrickInfo finishNonLastTrick(TrickInfo trickInfo) {
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
    
    private void aiPlayerTurn(TrickInfo trickInfo, int playerNum) {
        
        int chosenCard = 0;
        try {
            chosenCard = players[playerNum].chooseCard(trickInfo.highestPlay);
        } catch (NoStrategyException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        trickInfo.cardsPlayed.add(chosenCard);
        
        // The last of equally high cards wins the trick,
        // the winner leads the next trick.
        if (chosenCard >= trickInfo.highestPlay) {
            trickInfo.highestPlay = chosenCard;
            trickInfo.highestPlayPlayer = playerNum;
        }
    }
    
    public String showNonAIHand() {
        return players[0].showHand();
    }
    
    // for debugging
    public String showHands() {
        StringBuilder out = new StringBuilder();
        
        for (int i = 0; i < players.length; i++) {
            out.append("Player " + i + ": "
                    + players[i].showHand() + '\n');
        }

        return out.toString();
    }
    
}
