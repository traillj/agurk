/*
 * Agurk
 * Author: traillj
 */

package core;

import java.util.ArrayList;
import java.util.List;


/**
 * Stores information of the last trick and overall scores.
 * A new GameInfo should be created after each round is
 * completed.
 */
public class GameInfo {
    
    /** Stores information about the last trick. */
    private TrickInfo trickInfo;
    /** Points awarded for the last trick. */
    private List<Integer> trickPoints;
    /** Points for the overall game. */
    private List<Integer> gamePoints;
    /** Number of lives of each player. */
    private List<Integer> lives;
    
    /** Number of remaining players. */
    private int numAlive;
    /** Minimum points necessary to lose a life. */
    private int deathPoints;
    
    /**
     * Creates a new TrickInfo object with the
     * initial game information, no tricks played.
     * Initialises game points and lives.
     * @param numPlayers Number of players.
     * @param initialLives Starting number of lives.
     * @param deathPoints Minimum points necessary to lose a life.
     */
    public GameInfo(int numPlayers, int initialLives, int deathPoints) {
        this.numAlive = numPlayers;
        this.deathPoints = deathPoints;
        
        gamePoints = new ArrayList<Integer>();
        for (int i = 0; i < numPlayers; i++) {
            gamePoints.add(0);
        }
        
        lives = new ArrayList<Integer>();
        for (int i = 0; i < numPlayers; i++) {
            lives.add(initialLives);
        }
    }
    
    /**
     * Creates a new TrickInfo object with information of the last
     * trick and information of the game up to the previous round.
     * @param leadPlayer The player that led the last trick.
     * @param trickInfo Information of the last trick.
     * @param prevGameInfo Information of the game up to the previous round.
     */
    public GameInfo(int leadPlayer, TrickInfo trickInfo,
            GameInfo prevGameInfo) {
        this.trickInfo = trickInfo;
        numAlive = prevGameInfo.getNumAlive();
        deathPoints = prevGameInfo.getDeathPoints();
        List<Integer> prevGamePoints = prevGameInfo.getGamePoints();
        List<Integer> prevLives = prevGameInfo.getLives();
        
        computeTrickPoints(leadPlayer, numAlive);
        
        boolean lifeLost = updateGamePoints(prevGamePoints, prevLives);
        updateLives(prevLives, lifeLost);
    }
    
    /**
     * Compute the points awarded for the last trick.
     * @param leadPlayer The player that led the last trick.
     * @param numAlive Number of remaining players.
     */
    private void computeTrickPoints(int leadPlayer, int numAlive) {
        trickPoints = new ArrayList<Integer>();
        for (int i = 0; i < numAlive; i++) {
            trickPoints.add(0);
        }
        
        List<Integer> cardsPlayed = trickInfo.getCardsPlayed();
        for (int i = 0; i < numAlive; i++) {
            int turn = (i + leadPlayer) % numAlive;
            if (turn == trickInfo.getHighestPlayPlayer()) {
                // Winner gets points equal to their card
                trickPoints.set(turn, trickInfo.getHighestPlay());
                
            } else if (cardsPlayed.get(turn) == trickInfo.getHighestPlay()) {
                // Deduct points from players who played same rank as winner
                trickPoints.set(turn, -trickInfo.getHighestPlay());
            }
        }
    }
    
    /**
     * Updates the points for the overall game.
     * Does not reset the points to the next highest score if
     * the player is dead.
     * @param prevGamePoints Points for the overall game up to
     *        the previous round.
     * @param prevLives Number of lives of each player up to
     *        the previous round.
     * @return True if the trick winner lost a life.
     */
    private boolean updateGamePoints(List<Integer> prevGamePoints,
            List<Integer> prevLives) {
        gamePoints = new ArrayList<Integer>();
        for (int i = 0; i < numAlive; i++) {
            gamePoints.add(prevGamePoints.get(i));
        }
        
        int winnerIndex = trickInfo.getHighestPlayPlayer();
        int winnerPrevPoints = prevGamePoints.get(winnerIndex);
        gamePoints.set(winnerIndex,
                winnerPrevPoints + trickInfo.getHighestPlay());
        
        boolean lifeLost = false;
        if (gamePoints.get(winnerIndex) >= deathPoints) {
            lifeLost = true;
            
            if (prevLives.get(winnerIndex) > 1) {
                // Player not dead, reset to the next highest points
                gamePoints.set(winnerIndex, 0);
                gamePoints.set(winnerIndex, max(gamePoints));
            }
        }
        
        deductGamePoints(prevGamePoints);
        return lifeLost;
    }
    
    /**
     * Deducts points from players who played the equal highest
     * card but did not win the trick. Call this after game points
     * have been updated for the winner.
     * @param prevGamePoints Points for the overall game up to
     *        the previous round.
     */
    private void deductGamePoints(List<Integer> prevGamePoints) {
        for (int i = 0; i < numAlive; i++) {
            int playerTrickPoints = trickPoints.get(i);
            if (playerTrickPoints < 0) {
                int prevPoints = prevGamePoints.get(i);
                int newPoints = prevPoints + playerTrickPoints;
                
                if (newPoints < 0) {
                    // Cannot have negative points
                    gamePoints.set(i, 0);
                } else {
                    gamePoints.set(i, newPoints);
                }
            }
        }
    }
    
    /**
     * Updates the number of lives of each player.
     * @param prevLives Number of lives of each player up to
     *        the previous round.
     * @param lifeLost True if the trick winner lost a life.
     */
    private void updateLives(List<Integer> prevLives, boolean lifeLost) {
        lives = new ArrayList<Integer>();
        for (int i = 0; i < numAlive; i++) {
            lives.add(prevLives.get(i));
        }
        
        if (lifeLost) {
            int winnerIndex = trickInfo.getHighestPlayPlayer();
            int winnerPrevLives = lives.get(winnerIndex);
            lives.set(winnerIndex, winnerPrevLives - 1);
            if (winnerPrevLives == 1) {
                numAlive--;
            }
        }
    }
    
    /**
     * Returns the maximum item in the list.
     * @param list A non-empty list.
     * @return The maximum item.
     */
    private int max(List<Integer> list) {
        int max = list.get(0);
        for (Integer item : list) {
            if (item > max) {
                max = item;
            }
        }
        return max;
    }
    
    /**
     * Removes all game information of the player.
     * @param index Index of the player.
     */
    public void removePlayer(int index) {
        trickPoints.remove(index);
        gamePoints.remove(index);
        lives.remove(index);
    }

    /**
     * Gets information about the last trick.
     * @return The trick information.
     */
    public TrickInfo getTrickInfo() {
        return trickInfo;
    }

    /**
     * Gets the points awarded for the last trick.
     * @return The trick points.
     */
    public List<Integer> getTrickPoints() {
        return trickPoints;
    }

    /**
     * Gets the points for the overall game.
     * @return The game points.
     */
    public List<Integer> getGamePoints() {
        return gamePoints;
    }

    /**
     * Gets the number of lives of each player.
     * @return The lives.
     */
    public List<Integer> getLives() {
        return lives;
    }
    
    /**
     * Gets the number of remaining players.
     * @return The number of alive players.
     */
    public int getNumAlive() {
        return numAlive;
    }
    
    /**
     * Gets the minimum points necessary to lose a life.
     * @return The death points.
     */
    public int getDeathPoints() {
        return deathPoints;
    }
}
