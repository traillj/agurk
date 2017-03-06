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

    private static final String NON_AI_NAME = "0";
    
    private Deck deck;
    private List<Player> players;
    
    // Game information up to the previous round
    private GameInfo gameInfo;
    
    private int handSize;
    
    private boolean gameOver = false;
    private boolean lose = false;
    
    // Number of players: 2-7
    public Game(int numPlayers, int handSize, int initialLives,
            int deathPoints) {
        this.handSize = handSize;
        deck = new Deck();
        players = new ArrayList<Player>();
        
        Player player = new InputPlayer(NON_AI_NAME);
        player.setHand(deck.dealCards(handSize));
        players.add(player);
        
        for (Integer i = 1; i < numPlayers; i++) {
            player = new StrategyPlayer(i.toString(), new SharpStrategy());
            player.setHand(deck.dealCards(handSize));
            players.add(player);
        }
        
        gameInfo = new GameInfo(numPlayers, initialLives, deathPoints);
    }
    
    public TrickInfo startNonLastTrick(int leadPlayer) {
        TrickInfo trickInfo = new TrickInfo(players.size());
        
        if (leadPlayer == 0) {
            // non-AI player requires external input
            return trickInfo;
        }
        
        for (int i = leadPlayer; i < players.size(); i++) {
            aiTurn(trickInfo, i);
        }
        return trickInfo;
    }
    
    public TrickInfo finishNonLastTrick(TrickInfo trickInfo, int leadPlayer) {
        TrickInfo newTrickInfo = new TrickInfo(players.size());
        newTrickInfo.highestPlay = trickInfo.highestPlay;
        newTrickInfo.highestPlayPlayer = trickInfo.highestPlayPlayer;
        
        int lastPlayer = leadPlayer - 1;
        if (lastPlayer < 0) {
            // Player 0 led the trick
            lastPlayer = players.size() - 1;
        }
        for (int i = 1; i <= lastPlayer; i++) {
            aiTurn(newTrickInfo, i);
        }
        
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
    
    public GameInfo playLastTrick(int leadPlayer) {
        TrickInfo trickInfo = new TrickInfo(players.size());
        
        for (int i = 0; i < players.size(); i++) {
            int turn = (i + leadPlayer) % players.size();
            int chosenCard = players.get(turn).playLowestCard();
            trickInfo.updateTrickInfo(chosenCard, turn);
        }
        
        gameInfo = new GameInfo(leadPlayer, trickInfo, gameInfo);
        dealNewHands();
        return gameInfo;
    }
    
    public void dealNewHands() {
        deck.resetDeck();
        for (Player player : players) {
            player.setHand(deck.dealCards(handSize));
        }
    }
    
    public String showNonAIHand() {
        return players.get(0).showHand();
    }
    
    public boolean removeNonAICard(int value) {
        return players.get(0).removeCard(value);
    }
    
    public List<String> getPlayerNames() {
        List<String> names = new ArrayList<String>();
        for (Player player : players) {
            names.add(player.getName());
        }
        return names;
    }
    
    public int removeDeadPlayers() {
        int index = 0;
        List<Integer> playersLives =
                new ArrayList<Integer>(gameInfo.getLives());
        
        for (Integer playerLives : playersLives) {
            if (playerLives == 0) {
                players.remove(index);
                gameInfo.removePlayer(index);
                if (index == 0) {
                    gameOver = true;
                    lose = true;
                }
            } else {
                index++;
            }
        }
        
        if (players.size() == 1) {
            gameOver = true;
        }
        return players.size();
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public boolean hasLost() {
        return lose;
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
