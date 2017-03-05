/*
 * Agurk
 * Author: traillj
 */

package core;

import java.util.ArrayList;
import java.util.List;


// Information for the last trick, and for overall game points and lives.
public class GameInfo {
    
    // Minimum points necessary to lose a life
    private static final int DEATH_POINTS = 21;
    private static final int INITIAL_LIVES = 2;
    
    private TrickInfo trickInfo;
    private List<Integer> trickPoints;
    private List<Integer> gamePoints;
    private List<Integer> lives;
    
    // Initial game information, no tricks played.
    // Initialises game points and lives.
    public GameInfo(int numPlayers) {
        gamePoints = new ArrayList<Integer>();
        for (int i = 0; i < numPlayers; i++) {
            gamePoints.add(0);
        }
        
        lives = new ArrayList<Integer>();
        for (int i = 0; i < numPlayers; i++) {
            lives.add(INITIAL_LIVES);
        }
    }
    
    // Call after the trick is complete
    public GameInfo(int leadPlayer, TrickInfo trickInfo,
            List<Integer> prevGamePoints, List<Integer> prevLives) {
        
        this.trickInfo = trickInfo;
        int numPlayers = prevLives.size();
        computeTrickPoints(leadPlayer, numPlayers);
        
        boolean lifeLost = updateGamePoints(prevGamePoints);
        updateLives(prevLives, lifeLost);
    }
    
    private void computeTrickPoints(int leadPlayer, int numPlayers) {
        trickPoints = new ArrayList<Integer>();
        for (int i = 0; i < numPlayers; i++) {
            trickPoints.add(0);
        }
        
        List<Integer> cardsPlayed = trickInfo.getCardsPlayed();
        for (int i = 0; i < numPlayers; i++) {
            int turn = (i + leadPlayer) % numPlayers;
            if (turn == trickInfo.highestPlayPlayer) {
                // Winner gets points equal to their card
                trickPoints.set(turn, trickInfo.highestPlay);
                
            } else if (cardsPlayed.get(turn) == trickInfo.highestPlay) {
                // deduct points from players who played same rank as winner
                trickPoints.set(turn, -trickInfo.highestPlay);
            }
        }
    }
    
    // Returns true if the trick winner lost a life
    private boolean updateGamePoints(List<Integer> prevGamePoints) {
        int numPlayers = prevGamePoints.size();
        gamePoints = new ArrayList<Integer>();
        for (int i = 0; i < numPlayers; i++) {
            gamePoints.add(prevGamePoints.get(i));
        }
        
        int winnerIndex = trickInfo.highestPlayPlayer;
        int winnerPrevPoints = prevGamePoints.get(winnerIndex);
        gamePoints.set(winnerIndex, winnerPrevPoints + trickInfo.highestPlay);
        
        boolean lifeLost = false;
        if (gamePoints.get(winnerIndex) >= DEATH_POINTS) {
            lifeLost = true;
            // Reset to the next highest points
            gamePoints.set(winnerIndex, 0);
            gamePoints.set(winnerIndex, max(gamePoints));
        }
        
        deductGamePoints(prevGamePoints);
        return lifeLost;
    }
    
    // Deduct points after the winner's points has been completely updated
    private void deductGamePoints(List<Integer> prevGamePoints) {
        int numPlayers = prevGamePoints.size();
        for (int i = 0; i < numPlayers; i++) {
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
    
    private void updateLives(List<Integer> prevLives, boolean lifeLost) {
        int numPlayers = prevLives.size();
        lives = new ArrayList<Integer>();
        for (int i = 0; i < numPlayers; i++) {
            lives.add(prevLives.get(i));
        }
        
        if (lifeLost) {
            int winnerIndex = trickInfo.highestPlayPlayer;
            int winnerLives = lives.get(winnerIndex);
            lives.set(winnerIndex, winnerLives - 1);
        }
    }
    
    // Assumes list is not empty
    private int max(List<Integer> list) {
        int max = list.get(0);
        for (Integer item : list) {
            if (item > max) {
                max = item;
            }
        }
        return max;
    }

    public TrickInfo getTrickInfo() {
        return trickInfo;
    }

    public List<Integer> getTrickPoints() {
        return trickPoints;
    }

    public List<Integer> getGamePoints() {
        return gamePoints;
    }

    public List<Integer> getLives() {
        return lives;
    }
}
