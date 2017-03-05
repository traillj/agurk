/*
 * Agurk
 * Author: traillj
 */

package core;

import java.util.ArrayList;
import java.util.List;

import players.InputPlayer;
import players.NoStrategyException;
import players.Player;
import players.StrategyPlayer;

import strategies.SharpStrategy;


// Methods for playing components of the game
// Player 0 is non-AI
public class Game {

    private Deck deck;
    private List<Player> players;
    
    // Game information up to the previous deal
    private GameInfo gameInfo;
    
    private int leadPlayer = 2;
    
    // Number of players: 2-7
    public Game(int numPlayers) {
        deck = new Deck();
        players = new ArrayList<Player>();
        
        Player player = new InputPlayer("0");
        player.setHand(deck.dealHand());
        players.add(player);
        
        for (Integer i = 1; i < numPlayers; i++) {
            player = new StrategyPlayer(i.toString(), new SharpStrategy());
            player.setHand(deck.dealHand());
            players.add(player);
        }
        
        gameInfo = new GameInfo(numPlayers);
    }
    
    public TrickInfo startNonLastTrick() {
        TrickInfo trickInfo = new TrickInfo();
        
        if (leadPlayer == 0) {
            // non-AI player requires external input
            return trickInfo;
        }
        
        for (int i = leadPlayer; i < players.size(); i++) {
            aiTurn(trickInfo, i);
        }
        return trickInfo;
    }
    
    public TrickInfo finishNonLastTrick(TrickInfo trickInfo) {
        TrickInfo newTrickInfo = new TrickInfo();
        newTrickInfo.highestPlay = trickInfo.highestPlay;
        newTrickInfo.highestPlayPlayer = trickInfo.highestPlayPlayer;
        
        for (int i = 1; i < leadPlayer; i++) {
            aiTurn(newTrickInfo, i);
        }
        
        leadPlayer = newTrickInfo.highestPlayPlayer;
        return newTrickInfo;
    }
    
    private void aiTurn(TrickInfo trickInfo, int playerIndex) {
        int chosenCard = 0;
        Player player = players.get(playerIndex);
        try {
            chosenCard = player.chooseCard(trickInfo.highestPlay);
        } catch (NoStrategyException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        
        trickInfo.updateTrickInfo(chosenCard, playerIndex);
    }
    
    public GameInfo playlastTrick() {
        TrickInfo trickInfo = new TrickInfo();
        
        for (int i = 0; i < players.size(); i++) {
            int turn = (i + leadPlayer) % players.size();
            if (turn == 0) {
                int chosenCard = players.get(0).playLowestCard();
                trickInfo.updateTrickInfo(chosenCard, 0);
            } else {
                aiTurn(trickInfo, turn);
            }
        }
        
        List<Integer> prevScores = gameInfo.getScores();
        List<Integer> prevLives = gameInfo.getLives();
        gameInfo = new GameInfo(leadPlayer, trickInfo, prevScores, prevLives);
        
        return gameInfo;
    }
    
    public String showNonAIHand() {
        return players.get(0).showHand();
    }
    
    public boolean removeNonAICard(int value) {
        return players.get(0).removeCard(value);
    }
    
    // for debugging
    public String showHands() {
        StringBuilder out = new StringBuilder();
        
        for (int i = 0; i < players.size(); i++) {
            out.append("Player " + i + ": "
                    + players.get(i).showHand() + '\n');
        }

        return out.toString();
    }
}
