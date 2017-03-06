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


/**
 * Contains playing components and game information.
 * The player indexed at 0 is the non-AI player.
 */
public class Game {

    /** Name of the non-AI player. */
    private static final String NON_AI_NAME = "0";
    
    /** Rank-only standard deck of 52 cards. */
    private Deck deck;
    
    /** Players of the game. */
    private List<Player> players;
    
    /** Game information up to the previous round. */
    private GameInfo gameInfo;
    
    /** Number of cards held by each player at the start of a trick. */
    private int handSize;
    
    /** True if the game has ended. */
    private boolean gameOver = false;
    /** True if the non-AI player has lost. */
    private boolean lose = false;
    
    /**
     * Creates a new Game object.
     * @param numPlayers Number of players.
     * @param handSize Number of cards held by each player
     *        at the start of a trick.
     * @param initialLives Starting number of lives.
     * @param deathPoints Minimum points necessary to lose a life.
     */
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
    
    /**
     * Starts the trick up to the non-AI player's turn.
     * @param leadPlayer The player to lead the trick.
     * @return Information about the trick.
     */
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
    
    /**
     * Finishes the trick after the non-AI player's turn.
     * @param trickInfo Information that includes the highest play and
     *        corresponding player of the trick so far.
     * @param leadPlayer The player that led the trick.
     * @return New information about the trick only.
     */
    public TrickInfo finishNonLastTrick(TrickInfo trickInfo, int leadPlayer) {
        TrickInfo newTrickInfo = new TrickInfo(players.size());
        newTrickInfo.setHighestPlay(trickInfo.getHighestPlay());
        newTrickInfo.setHighestPlayPlayer(trickInfo.getHighestPlayPlayer());
        
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
    
    /**
     * Gets a card from an AI player and updates the trick information.
     * @param trickInfo Information about the trick.
     * @param playerIndex The AI player's index.
     */
    private void aiTurn(TrickInfo trickInfo, int playerIndex) {
        int chosenCard = 0;
        Player player = players.get(playerIndex);
        try {
            chosenCard = player.chooseCard(trickInfo.getHighestPlay());
        } catch (NoStrategyException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        
        trickInfo.updateTrickInfo(chosenCard, playerIndex);
    }
    
    /**
     * Plays the last trick.
     * @param leadPlayer The player to lead the trick.
     * @return Complete information about the last trick.
     */
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
    
    /**
     * Deals a new set of hands.
     */
    private void dealNewHands() {
        deck.resetDeck();
        for (Player player : players) {
            player.setHand(deck.dealCards(handSize));
        }
    }
    
    /**
     * Shows the non-AI player's hand.
     * @return Representation of the non-AI player's cards.
     */
    public String showNonAIHand() {
        return players.get(0).showHand();
    }
    
    /**
     * Removes the first card with the specified value.
     * @param value Rank of the card to remove.
     */
    public void removeNonAICard(int value) {
        players.get(0).removeCard(value);
    }
    
    /**
     * Gets the names of the players.
     * @return The player names.
     */
    public List<String> getPlayerNames() {
        List<String> names = new ArrayList<String>();
        for (Player player : players) {
            names.add(player.getName());
        }
        return names;
    }
    
    /**
     * Removes dead players from the game information.
     * @return The number of remaining players.
     */
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
    
    /**
     * Tests if the game is over.
     * @return True if the game has ended.
     */
    public boolean isGameOver() {
        return gameOver;
    }
    
    /**
     * Tests if the game has been lost.
     * @return True if the non-AI player has lost.
     */
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
